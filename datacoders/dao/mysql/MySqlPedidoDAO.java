package datacoders.dao.mysql;

import datacoders.dao.PedidoDao;
import datacoders.modelo.Articulo;
import datacoders.modelo.Cliente;
import datacoders.modelo.ClienteEstandar;
import datacoders.modelo.ClientePremium;
import datacoders.modelo.Pedido;
import datacoders.modelo.excepciones.ArticuloNoEncontradoException;
import datacoders.modelo.excepciones.DuplicadoException;
import datacoders.modelo.excepciones.PedidoNoCancelableException;
import datacoders.modelo.excepciones.PedidoNoEncontradoException;
import datacoders.modelo.util.BDConnection;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MySqlPedidoDAO implements PedidoDao {

    @Override
    public Pedido crearPedido(String emailCliente, String datosCliente, String codigoArticulo,
                              int cantidad, LocalDateTime ahora)
            throws ArticuloNoEncontradoException, DuplicadoException {

        asegurarClienteExiste(emailCliente, datosCliente);
        asegurarArticuloExiste(codigoArticulo);

        String call = "{CALL sp_crear_pedido(?, ?, ?)}";

        try (Connection con = BDConnection.getConnection();
             CallableStatement cs = con.prepareCall(call)) {

            cs.setString(1, emailCliente);
            cs.setString(2, codigoArticulo);
            cs.setInt(3, cantidad);

            cs.execute();

            int ultimoId = getLastInsertId(con);
            return buscarPedidoPorNumero(con, ultimoId);

        } catch (SQLException e) {
            throw new RuntimeException("Error al crear pedido: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean eliminarPedido(int numPedido, LocalDateTime ahora)
            throws PedidoNoEncontradoException, PedidoNoCancelableException {

        comprobarPedidoExiste(numPedido);

        String call = "{CALL sp_eliminar_pedido(?)}";

        try (Connection con = BDConnection.getConnection();
             CallableStatement cs = con.prepareCall(call)) {

            cs.setInt(1, numPedido);
            cs.execute();
            return true;

        } catch (SQLException e) {
            String msg = e.getMessage() == null ? "" : e.getMessage().toLowerCase();
            if (msg.contains("no se puede cancelar")) {
                throw new PedidoNoCancelableException(
                        "El pedido no se puede cancelar: ya está en preparación o enviado."
                );
            }
            throw new RuntimeException("Error al eliminar pedido: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Pedido> findPendientes(String emailCliente) {
        // CORREGIDO: 'articulo' en singular y 'tiempo_preparacion' sin el '_min'
        StringBuilder sql = new StringBuilder("""
                SELECT
                    p.num_pedido, p.cantidad, p.fecha_hora,
                    c.nombre, c.domicilio, c.nif, c.email, c.tipo,
                    a.codigo, a.descripcion, a.precio_venta, a.gastos_envio, a.tiempo_preparacion
                FROM pedidos p
                JOIN clientes c ON p.id_cliente = c.id_cliente
                JOIN articulo a ON p.id_articulo = a.id_articulo
                WHERE TIMESTAMPDIFF(MINUTE, p.fecha_hora, NOW()) < a.tiempo_preparacion
                """);

        boolean filtrar = emailCliente != null && !emailCliente.isBlank();
        if (filtrar) {
            sql.append(" AND c.email = ?");
        }
        sql.append(" ORDER BY p.num_pedido");

        return ejecutarListadoPedidos(sql.toString(), filtrar ? emailCliente : null);
    }

    @Override
    public List<Pedido> findEnviados(String emailCliente) {
        // CORREGIDO: 'articulo' en singular y 'tiempo_preparacion'
        StringBuilder sql = new StringBuilder("""
                SELECT
                    p.num_pedido, p.cantidad, p.fecha_hora,
                    c.nombre, c.domicilio, c.nif, c.email, c.tipo,
                    a.codigo, a.descripcion, a.precio_venta, a.gastos_envio, a.tiempo_preparacion
                FROM pedidos p
                JOIN clientes c ON p.id_cliente = c.id_cliente
                JOIN articulo a ON p.id_articulo = a.id_articulo
                WHERE TIMESTAMPDIFF(MINUTE, p.fecha_hora, NOW()) >= a.tiempo_preparacion
                """);

        boolean filtrar = emailCliente != null && !emailCliente.isBlank();
        if (filtrar) {
            sql.append(" AND c.email = ?");
        }
        sql.append(" ORDER BY p.num_pedido");

        return ejecutarListadoPedidos(sql.toString(), filtrar ? emailCliente : null);
    }

    private List<Pedido> ejecutarListadoPedidos(String sql, String email) {
        List<Pedido> pedidos = new ArrayList<>();
        try (Connection con = BDConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (email != null) {
                ps.setString(1, email);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    pedidos.add(mapPedido(rs));
                }
            }
            return pedidos;
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar pedidos: " + e.getMessage(), e);
        }
    }

    private Pedido mapPedido(ResultSet rs) throws SQLException {
        Cliente cliente;
        String tipo = rs.getString("tipo");

        if ("Premium".equalsIgnoreCase(tipo)) {
            cliente = new ClientePremium(rs.getString("nombre"), rs.getString("domicilio"), rs.getString("nif"), rs.getString("email"));
        } else {
            cliente = new ClienteEstandar(rs.getString("nombre"), rs.getString("domicilio"), rs.getString("nif"), rs.getString("email"));
        }

        // CORREGIDO: 'tiempo_preparacion'
        Articulo articulo = new Articulo(
                rs.getString("codigo"),
                rs.getString("descripcion"),
                rs.getDouble("precio_venta"),
                rs.getDouble("gastos_envio"),
                rs.getInt("tiempo_preparacion")
        );

        Timestamp ts = rs.getTimestamp("fecha_hora");
        LocalDateTime fechaHora = (ts != null) ? ts.toLocalDateTime() : LocalDateTime.now();

        return new Pedido(rs.getInt("num_pedido"), cliente, articulo, rs.getInt("cantidad"), fechaHora);
    }

    private void asegurarArticuloExiste(String codigoArticulo) throws ArticuloNoEncontradoException {
        // CORREGIDO: 'articulo' en singular
        String sql = "SELECT id_articulo FROM articulo WHERE codigo = ?";

        try (Connection con = BDConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, codigoArticulo);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new ArticuloNoEncontradoException("No existe artículo con código: " + codigoArticulo);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error comprobando artículo: " + e.getMessage(), e);
        }
    }

    private void asegurarClienteExiste(String emailCliente, String datosCliente) throws DuplicadoException {
        String checkSql = "SELECT id_cliente FROM clientes WHERE email = ?";

        try (Connection con = BDConnection.getConnection();
             PreparedStatement check = con.prepareStatement(checkSql)) {

            check.setString(1, emailCliente);

            try (ResultSet rs = check.executeQuery()) {
                if (rs.next()) return;
            }

            String[] partes = parseDatosCliente(datosCliente);
            MySqlClienteDAO clienteDAO = new MySqlClienteDAO();
            if ("premium".equalsIgnoreCase(partes[3])) {
                clienteDAO.insertPremium(partes[0], partes[1], partes[2], emailCliente);
            } else {
                clienteDAO.insertEstandar(partes[0], partes[1], partes[2], emailCliente);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error comprobando/creando cliente: " + e.getMessage(), e);
        }
    }

    private String[] parseDatosCliente(String datosCliente) {
        String nombre = "N/D", domicilio = "N/D", nif = "N/D", tipo = "Estandar";
        if (datosCliente != null && !datosCliente.isBlank()) {
            String[] parts = datosCliente.split("\\|");
            if (parts.length > 0) nombre = parts[0].trim();
            if (parts.length > 1) domicilio = parts[1].trim();
            if (parts.length > 2) nif = parts[2].trim();
            if (parts.length > 3) tipo = parts[3].trim();
        }
        return new String[]{nombre, domicilio, nif, tipo};
    }

    private int getLastInsertId(Connection con) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("SELECT LAST_INSERT_ID()");
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
            throw new SQLException("No se pudo recuperar LAST_INSERT_ID()");
        }
    }

    private Pedido buscarPedidoPorNumero(Connection con, int numPedido) throws SQLException {
        // CORREGIDO: 'articulo' en singular y 'tiempo_preparacion'
        String sql = """
                SELECT
                    p.num_pedido, p.cantidad, p.fecha_hora,
                    c.nombre, c.domicilio, c.nif, c.email, c.tipo,
                    a.codigo, a.descripcion, a.precio_venta, a.gastos_envio, a.tiempo_preparacion
                FROM pedidos p
                JOIN clientes c ON p.id_cliente = c.id_cliente
                JOIN articulo a ON p.id_articulo = a.id_articulo
                WHERE p.num_pedido = ?
                """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, numPedido);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new SQLException("No se pudo recuperar el pedido insertado");
                return mapPedido(rs);
            }
        }
    }

    private void comprobarPedidoExiste(int numPedido) throws PedidoNoEncontradoException {
        String sql = "SELECT num_pedido FROM pedidos WHERE num_pedido = ?";
        try (Connection con = BDConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, numPedido);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new PedidoNoEncontradoException("No existe pedido con número: " + numPedido);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error comprobando pedido: " + e.getMessage(), e);
        }
    }
}