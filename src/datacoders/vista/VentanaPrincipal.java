package datacoders.vista;

import javafx.scene.layout.BorderPane;
import javafx.scene.control.*;
import datacoders.controlador.Controlador;

public class VentanaPrincipal extends BorderPane {
    private Controlador controlador;

    public VentanaPrincipal(Controlador controlador) {
        this.controlador = controlador;

        TabPane tabPane = new TabPane();

        // --- LAS 3 PESTAÑAS ---
        Tab tabArticulos = new Tab("Artículos", new SeccionArticulos(controlador));

        // AQUÍ ESTABA EL ERROR: Cambiamos FormularioCliente_VIEJO por SeccionClientes
        Tab tabClientes = new Tab("Clientes", new SeccionClientes(controlador));

        Tab tabPedidos = new Tab("Pedidos", new SeccionPedidos(controlador));

        tabPane.getTabs().addAll(tabArticulos, tabClientes, tabPedidos);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        this.setCenter(tabPane);
    }
}