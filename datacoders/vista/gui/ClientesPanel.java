package datacoders.vista.gui;

import datacoders.controlador.Controlador;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;

public class ClientesPanel extends JPanel {

    private final Controlador controlador;

    public ClientesPanel(Controlador controlador) {
        this.controlador = controlador;
        setLayout(new BorderLayout());
        add(new JLabel("Panel de clientes"), BorderLayout.NORTH);
    }
}