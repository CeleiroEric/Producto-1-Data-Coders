package datacoders.modelo;

import datacoders.dao.ClienteDAO;
import datacoders.dao.ArticuloDAO;
import datacoders.dao.PedidoDAO;
import datacoders.factory.DAOFactory;
import datacoders.modelo.excepciones.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Datos {

    private final ClienteDAO clienteDAO;
    private final ArticuloDAO articuloDAO;
    private final PedidoDAO pedidoDAO;

    /**
     * Constructor principal usado por la aplicación.
     * MOD (Persona 3): Datos obtiene los DAO desde la fábrica,
     * manteniendo la Vista y el Controlador sin cambios.
     */
    public Datos() {
        DAOFactory factory = DAOFactory.getFactory(DAOFactory.MYSQL);

        // MOD (Persona 3):
        // Se usa Objects.requireNonNull para detectar antes y con mensaje claro
        // si la fábrica aún no devuelve implementaciones reales.
        this.clienteDAO = Objects.requireNonNull(factory.getClienteDAO(),
                "ClienteDAO no inicializado en MySqlDAOFactory");
        this.articuloDAO = Objects.requireNonNull(factory.getArticuloDAO(),
                "ArticuloDAO no inicializado en MySqlDAOFactory");
        this.pedidoDAO = Objects.requireNonNull(factory.getPedidoDAO(),
                "PedidoDAO no inicializado en MySqlDAOFactory");
    }

    /**
     * MOD (Persona 3):
     * Constructor alternativo para pruebas o integración manual.
     * Permite inyectar DAO concretos sin depender de la fábrica.
     * No afecta al funcionamiento normal del programa.
     */
    public Datos(ClienteDAO clienteDAO, ArticuloDAO articuloDAO, PedidoDAO pedidoDAO) {
        this.clienteDAO = Objects.requireNonNull(clienteDAO, "clienteDAO no puede ser null");
        this.articuloDAO = Objects.requireNonNull(articuloDAO, "articuloDAO no puede ser null");
        this.pedidoDAO = Objects.requireNonNull(pedidoDAO, "pedidoDAO no puede ser null");
    }

    // =========================
    // CLIENTES
    // =========================
    public boolean addClienteEstandar(String nombre, String domicilio, String nif, String email)
            throws DuplicadoException {
        return clienteDAO.insertEstandar(nombre, domicilio, nif, email);
    }

    public boolean addClientePremium(String nombre, String domicilio, String nif, String email)
            throws DuplicadoException {
        return clienteDAO.insertPremium(nombre, domicilio, nif, email);
    }

    public Cliente buscarClientePorEmail(String email) throws ClienteNoEncontradoException {
        return clienteDAO.findByEmail(email);
    }

    public List<Cliente> getClientes() {
        return clienteDAO.findAll();
    }

    public List<Cliente> getClientesEstandar() {
        return clienteDAO.findAllEstandar();
    }

    public List<Cliente> getClientesPremium() {
        return clienteDAO.findAllPremium();
    }

    // =========================
    // ARTÍCULOS
    // =========================
    public boolean addArticulo(Articulo a) throws DuplicadoException {
        return articuloDAO.insert(a);
    }

    public Articulo buscarArticuloPorCodigo(String codigo) throws ArticuloNoEncontradoException {
        return articuloDAO.findByCodigo(codigo);
    }

    public List<Articulo> getArticulos() {
        return articuloDAO.findAll();
    }

    // =========================
    // PEDIDOS
    // =========================

    /**
     * MOD (Persona 3):
     * Este método ya está adaptado a persistencia.
     * La implementación real de negocio queda delegada en PedidoDAO.
     *
     * NOTA PARA EL GRUPO:
     * En Producto 3, PedidoDAO debería implementar esta operación usando
     * JDBC y, preferiblemente, un procedimiento almacenado para crear pedido.
     */
    public Pedido addPedido(String emailCliente, String datosCliente, String codigoArticulo,
                            int cantidad, LocalDateTime ahora)
            throws ArticuloNoEncontradoException, DuplicadoException {
        return pedidoDAO.crearPedido(emailCliente, datosCliente, codigoArticulo, cantidad, ahora);
    }

    /**
     * MOD (Persona 3):
     * La lógica de cancelación debe resolverse en PedidoDAO
     * usando persistencia real y, si corresponde, transacción/procedimiento almacenado.
     */
    public boolean eliminarPedido(int numPedido, LocalDateTime ahora)
            throws PedidoNoEncontradoException, PedidoNoCancelableException {
        return pedidoDAO.eliminarPedido(numPedido, ahora);
    }

    public List<Pedido> getPedidosPendientes(String emailCliente) {
        return pedidoDAO.findPendientes(emailCliente);
    }

    public List<Pedido> getPedidosEnviados(String emailCliente) {
        return pedidoDAO.findEnviados(emailCliente);
    }
}