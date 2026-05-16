package datacoders.vista.gui;

import datacoders.controlador.Controlador;
import datacoders.modelo.Articulo;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ArticulosPanel extends JPanel {

    private final Controlador controlador;

    private JTextField txtCodigo;
    private JTextField txtDescripcion;
    private JTextField txtPrecio;
    private JTextField txtGastosEnvio;
    private JTextField txtTiempoPreparacion;

    private JButton btnAgregar;
    private JButton btnMostrar;

    private JTextArea areaResultado;

    public ArticulosPanel(Controlador controlador) {
        this.controlador = controlador;
        inicializarComponentes();
        construirPanel();
        registrarEventos();
    }

    private void inicializarComponentes() {
        txtCodigo = new JTextField(15);
        txtDescripcion = new JTextField(20);
        txtPrecio = new JTextField(10);
        txtGastosEnvio = new JTextField(10);
        txtTiempoPreparacion = new JTextField(10);

        btnAgregar = new JButton("Añadir artículo");
        btnMostrar = new JButton("Mostrar artículos");

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
        panelFormulario.add(new JLabel("Código:"), gbc);

        gbc.gridx = 1;
        panelFormulario.add(txtCodigo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panelFormulario.add(new JLabel("Descripción:"), gbc);

        gbc.gridx = 1;
        panelFormulario.add(txtDescripcion, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panelFormulario.add(new JLabel("Precio:"), gbc);

        gbc.gridx = 1;
        panelFormulario.add(txtPrecio, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panelFormulario.add(new JLabel("Gastos de envío:"), gbc);

        gbc.gridx = 1;
        panelFormulario.add(txtGastosEnvio, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panelFormulario.add(new JLabel("Tiempo preparación (min):"), gbc);

        gbc.gridx = 1;
        panelFormulario.add(txtTiempoPreparacion, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBotones.add(btnAgregar);
        panelBotones.add(btnMostrar);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelFormulario, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        JScrollPane scroll = new JScrollPane(areaResultado);

        add(panelSuperior, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }

    private void registrarEventos() {
        btnAgregar.addActionListener(e -> agregarArticulo());
        btnMostrar.addActionListener(e -> mostrarArticulos());
    }

    private void agregarArticulo() {
        try {
            String codigo = txtCodigo.getText().trim();
            String descripcion = txtDescripcion.getText().trim();
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            double gastosEnvio = Double.parseDouble(txtGastosEnvio.getText().trim());
            int tiempoPreparacion = Integer.parseInt(txtTiempoPreparacion.getText().trim());

            controlador.addArticulo(codigo, descripcion, precio, gastosEnvio, tiempoPreparacion);

            JOptionPane.showMessageDialog(
                    this,
                    "Artículo añadido correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limpiarFormulario();
            mostrarArticulos();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Precio, gastos de envío y tiempo de preparación deben tener un formato numérico válido.",
                    "Error de formato",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error al añadir artículo",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void mostrarArticulos() {
        try {
            List<Articulo> articulos = controlador.getArticulos();
            areaResultado.setText("");

            if (articulos.isEmpty()) {
                areaResultado.setText("No hay artículos registrados.");
                return;
            }

            StringBuilder sb = new StringBuilder();
            for (Articulo articulo : articulos) {
                sb.append(articulo).append("\n");
            }

            areaResultado.setText(sb.toString());

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error al mostrar artículos",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void limpiarFormulario() {
        txtCodigo.setText("");
        txtDescripcion.setText("");
        txtPrecio.setText("");
        txtGastosEnvio.setText("");
        txtTiempoPreparacion.setText("");
    }
}