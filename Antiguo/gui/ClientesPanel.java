package datacoders.vista.gui;

import datacoders.controlador.Controlador;
import datacoders.modelo.Cliente;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ClientesPanel extends JPanel {

    private final Controlador controlador;

    private JTextField txtNombre;
    private JTextField txtDomicilio;
    private JTextField txtNif;
    private JTextField txtEmail;

    private JComboBox<String> comboTipo;

    private JButton btnAgregar;
    private JButton btnMostrarTodos;
    private JButton btnMostrarEstandar;
    private JButton btnMostrarPremium;

    private JTextArea areaResultado;

    public ClientesPanel(Controlador controlador) {
        this.controlador = controlador;
        inicializarComponentes();
        construirPanel();
        registrarEventos();
    }

    private void inicializarComponentes() {
        txtNombre = new JTextField(20);
        txtDomicilio = new JTextField(20);
        txtNif = new JTextField(15);
        txtEmail = new JTextField(20);

        comboTipo = new JComboBox<>(new String[]{"Estandar", "Premium"});

        btnAgregar = new JButton("Añadir cliente");
        btnMostrarTodos = new JButton("Mostrar todos");
        btnMostrarEstandar = new JButton("Mostrar estándar");
        btnMostrarPremium = new JButton("Mostrar premium");

        areaResultado = new JTextArea(18, 50);
        areaResultado.setEditable(false);
        areaResultado.setLineWrap(true);
        areaResultado.setWrapStyleWord(true);
    }

    private void construirPanel() {
        setLayout(new BorderLayout(10, 10));

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panelFormulario.add(new JLabel("Nombre:"), gbc);

        gbc.gridx = 1;
        panelFormulario.add(txtNombre, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panelFormulario.add(new JLabel("Domicilio:"), gbc);

        gbc.gridx = 1;
        panelFormulario.add(txtDomicilio, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panelFormulario.add(new JLabel("NIF:"), gbc);

        gbc.gridx = 1;
        panelFormulario.add(txtNif, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panelFormulario.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        panelFormulario.add(txtEmail, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panelFormulario.add(new JLabel("Tipo:"), gbc);

        gbc.gridx = 1;
        panelFormulario.add(comboTipo, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBotones.add(btnAgregar);
        panelBotones.add(btnMostrarTodos);
        panelBotones.add(btnMostrarEstandar);
        panelBotones.add(btnMostrarPremium);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelFormulario, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        JScrollPane scroll = new JScrollPane(areaResultado);

        add(panelSuperior, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }

    private void registrarEventos() {
        btnAgregar.addActionListener(e -> agregarCliente());
        btnMostrarTodos.addActionListener(e -> mostrarTodos());
        btnMostrarEstandar.addActionListener(e -> mostrarEstandar());
        btnMostrarPremium.addActionListener(e -> mostrarPremium());
    }

    private void agregarCliente() {
        try {
            String nombre = txtNombre.getText().trim();
            String domicilio = txtDomicilio.getText().trim();
            String nif = txtNif.getText().trim();
            String email = txtEmail.getText().trim();
            String tipo = (String) comboTipo.getSelectedItem();

            if (nombre.isEmpty() || domicilio.isEmpty() || nif.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Todos los campos son obligatorios.",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            if ("Premium".equalsIgnoreCase(tipo)) {
                controlador.addClientePremium(nombre, domicilio, nif, email);
            } else {
                controlador.addClienteEstandar(nombre, domicilio, nif, email);
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Cliente añadido correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limpiarFormulario();
            mostrarTodos();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error al añadir cliente",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void mostrarTodos() {
        try {
            List<Cliente> clientes = controlador.getClientes();
            mostrarLista(clientes, "No hay clientes registrados.");
        } catch (Exception ex) {
            mostrarError(ex, "Error al mostrar clientes");
        }
    }

    private void mostrarEstandar() {
        try {
            List<Cliente> clientes = controlador.getClientesEstandar();
            mostrarLista(clientes, "No hay clientes estándar registrados.");
        } catch (Exception ex) {
            mostrarError(ex, "Error al mostrar clientes estándar");
        }
    }

    private void mostrarPremium() {
        try {
            List<Cliente> clientes = controlador.getClientesPremium();
            mostrarLista(clientes, "No hay clientes premium registrados.");
        } catch (Exception ex) {
            mostrarError(ex, "Error al mostrar clientes premium");
        }
    }

    private void mostrarLista(List<Cliente> clientes, String mensajeVacio) {
        areaResultado.setText("");

        if (clientes == null || clientes.isEmpty()) {
            areaResultado.setText(mensajeVacio);
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (Cliente cliente : clientes) {
            sb.append(cliente).append("\n");
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

    private void limpiarFormulario() {
        txtNombre.setText("");
        txtDomicilio.setText("");
        txtNif.setText("");
        txtEmail.setText("");
        comboTipo.setSelectedIndex(0);
    }
}