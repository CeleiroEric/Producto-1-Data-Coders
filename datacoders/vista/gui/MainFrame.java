package datacoders.vista.gui;

import datacoders.controlador.Controlador;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;

public class MainFrame extends JFrame {

    private final Controlador controlador;
    private final JTabbedPane pestañas;

    public MainFrame() {
        this.controlador = new Controlador();
        this.pestañas = new JTabbedPane();

        setTitle("Data Coders - Gestión Tienda Online");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        inicializarPestañas();

        add(pestañas, BorderLayout.CENTER);
    }

    private void inicializarPestañas() {
        pestañas.addTab("Artículos", new ArticulosPanel(controlador));
        pestañas.addTab("Clientes", new ClientesPanel(controlador));
        pestañas.addTab("Pedidos", new PedidosPanel(controlador));
    }
}