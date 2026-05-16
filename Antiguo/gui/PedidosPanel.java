package datacoders.vista.gui;

import datacoders.controlador.Controlador;
import datacoders.modelo.Pedido;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

public class PedidosPanel extends JPanel {

    private final Controlador controlador;

    private JTextField txtEmailCliente;
    private JTextField txtCodigoArticulo;
    private JTextField txtCantidad;

    private JTextField txtNombreCliente;
    private JTextField txtDomicilioCliente;
    private JTextField txtNifCliente;
    private JComboBox<String> comboTipoCliente;

    private JTextField txtNumPedidoEliminar;
    private JTextField txtEmailFiltro;

    private JButton btnCrearPedido;
    private JButton btnEliminarPedido;
    private JButton btnMostrarPendientes;
    private JButton btnMostrarEnviados;

    private JTextArea areaResultado;

    public PedidosPanel(Controlador controlador) {
        this.controlador = controlador;
        inicializarComponentes();
        construirPanel();
        registrarEventos();
    }

    private void inicializarComponentes() {
        txtEmailCliente = new JTextField(20);
        txtCodigoArticulo = new JTextField(15);
        txtCantidad = new JTextField(10);

        txtNombreCliente = new JTextField(20);
        txtDomicilioCliente = new JTextField(20);
        txtNifCliente = new JTextField(15);
        comboTipoCliente = new JComboBox<>(new String[]{"Estandar", "Premium"});

        txtNumPedidoEliminar = new JTextField(10);
        txtEmailFiltro = new JTextField(20);

        btnCrearPedido = new JButton("Crear pedido");
        btnEliminarPedido = new JButton("Eliminar pedido");
        btnMostrarPendientes = new JButton("Mostrar pendientes");
        btnMostrarEnviados = new JButton("Mostrar enviados");

        areaResultado = new JTextArea(18, 50);
        areaResultado.setEditable(false);
        areaResultado.setLineWrap(true);
        areaResultado.setWrapStyleWord(true);
    }

    private void construirPanel() {
        setLayout(new BorderLayout(10, 10));

        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));

        panelSuperior.add(crearPanelCrearPedido());
        panelSuperior.add(Box.createVerticalStrut(10));
        panelSuperior.add(crearPanelEliminarPedido());
        panelSuperior.add(Box.createVerticalStrut(10));
        panelSuperior.add(crearPanelListado());

        JScrollPane scroll = new JScrollPane(areaResultado);

        add(panelSuperior, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }

    private JPanel crearPanelCrearPedido() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Crear pedido"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Email cliente:"), gbc);
        gbc.gridx = 1;
        panel.add(txtEmailCliente, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Código artículo:"), gbc);
        gbc.gridx = 1;
        panel.add(txtCodigoArticulo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Cantidad:"), gbc);
        gbc.gridx = 1;
        panel.add(txtCantidad, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Nombre cliente:"), gbc);
        gbc.gridx = 1;
        panel.add(txtNombreCliente, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Domicilio cliente:"), gbc);
        gbc.gridx = 1;
        panel.add(txtDomicilioCliente, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("NIF cliente:"), gbc);
        gbc.gridx = 1;
        panel.add(txtNifCliente, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(new JLabel("Tipo cliente:"), gbc);
        gbc.gridx = 1;
        panel.add(comboTipoCliente, gbc);

        gbc.gridx = 1;
        gbc.gridy = 7;
        panel.add(btnCrearPedido, gbc);

        return panel;
    }

    private JPanel crearPanelEliminarPedido() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Eliminar pedido"));

        panel.add(new JLabel("Número de pedido:"));
        panel.add(txtNumPedidoEliminar);
        panel.add(btnEliminarPedido);

        return panel;
    }

    private JPanel crearPanelListado() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Listados"));

        panel.add(new JLabel("Email filtro (opcional):"));
        panel.add(txtEmailFiltro);
        panel.add(btnMostrarPendientes);
        panel.add(btnMostrarEnviados);

        return panel;
    }

    private void registrarEventos() {
        btnCrearPedido.addActionListener(e -> crearPedido());
        btnEliminarPedido.addActionListener(e -> eliminarPedido());
        btnMostrarPendientes.addActionListener(e -> mostrarPendientes());
        btnMostrarEnviados.addActionListener(e -> mostrarEnviados());
    }

    private void crearPedido() {
        try {
            String email = txtEmailCliente.getText().trim();
            String codigoArticulo = txtCodigoArticulo.getText().trim();
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());

            String nombre = txtNombreCliente.getText().trim();
            String domicilio = txtDomicilioCliente.getText().trim();
            String nif = txtNifCliente.getText().trim();
            String tipo = (String) comboTipoCliente.getSelectedItem();

            if (email.isEmpty() || codigoArticulo.isEmpty() || txtCantidad.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Email, código de artículo y cantidad son obligatorios.",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            String datosCliente = nombre + "|" + domicilio + "|" + nif + "|" + tipo;

            controlador.addPedido(email, datosCliente, codigoArticulo, cantidad, LocalDateTime.now());

            JOptionPane.showMessageDialog(
                    this,
                    "Pedido creado correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limpiarFormularioPedido();
            mostrarPendientes();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "La cantidad y el número de pedido deben tener formato numérico válido.",
                    "Error de formato",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error al crear pedido",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void eliminarPedido() {
        try {
            int numPedido = Integer.parseInt(txtNumPedidoEliminar.getText().trim());

            controlador.eliminarPedido(numPedido, LocalDateTime.now());

            JOptionPane.showMessageDialog(
                    this,
                    "Pedido eliminado correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

            txtNumPedidoEliminar.setText("");
            mostrarPendientes();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "El número de pedido debe ser un valor numérico válido.",
                    "Error de formato",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error al eliminar pedido",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void mostrarPendientes() {
        try {
            String emailFiltro = txtEmailFiltro.getText().trim();
            if (emailFiltro.isEmpty()) {
                emailFiltro = null;
            }

            List<Pedido> pedidos = controlador.getPedidosPendientes(emailFiltro);
            mostrarLista(pedidos, "No hay pedidos pendientes.");
        } catch (Exception ex) {
            mostrarError(ex, "Error al mostrar pedidos pendientes");
        }
    }

    private void mostrarEnviados() {
        try {
            String emailFiltro = txtEmailFiltro.getText().trim();
            if (emailFiltro.isEmpty()) {
                emailFiltro = null;
            }

            List<Pedido> pedidos = controlador.getPedidosEnviados(emailFiltro);
            mostrarLista(pedidos, "No hay pedidos enviados.");
        } catch (Exception ex) {
            mostrarError(ex, "Error al mostrar pedidos enviados");
        }
    }

    private void mostrarLista(List<Pedido> pedidos, String mensajeVacio) {
        areaResultado.setText("");

        if (pedidos == null || pedidos.isEmpty()) {
            areaResultado.setText(mensajeVacio);
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (Pedido pedido : pedidos) {
            sb.append(pedido).append("\n");
        }

        areaResultado.setText(sb.toString());
    }

    private void mostrarError(Exception ex, String titulo) {
        JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                titulo,
                JOptionPane.ERROR_MESSAGE
        );
    }

    private void limpiarFormularioPedido() {
        txtEmailCliente.setText("");
        txtCodigoArticulo.setText("");
        txtCantidad.setText("");
        txtNombreCliente.setText("");
        txtDomicilioCliente.setText("");
        txtNifCliente.setText("");
        comboTipoCliente.setSelectedIndex(0);
    }
}