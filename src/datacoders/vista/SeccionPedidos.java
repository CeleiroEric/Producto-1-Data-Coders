package datacoders.vista;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.beans.property.SimpleStringProperty;
import datacoders.controlador.Controlador;
import datacoders.modelo.Pedido;
import java.time.LocalDateTime;

public class SeccionPedidos extends VBox {
    private TableView<Pedido> tablaPedidos;
    private Controlador controlador;

    private TextField txtEmail, txtArticulo, txtCantidad, txtNomCli, txtDomCli, txtNifCli;
    private ComboBox<String> cbTipoCli;
    private TextField txtEmailFiltro;
    private TextField txtNumPedidoEliminar;

    public SeccionPedidos(Controlador controlador) {
        this.controlador = controlador;
        this.setPadding(new Insets(15));
        this.setSpacing(10);

        TitledPane paneCrear = new TitledPane();
        paneCrear.setText("Crear Nuevo Pedido");
        paneCrear.setExpanded(false);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        txtEmail = new TextField();
        txtEmail.setPromptText("Email cliente");

        txtArticulo = new TextField();
        txtArticulo.setPromptText("Código artículo");

        txtCantidad = new TextField();
        txtCantidad.setPromptText("Cantidad");

        txtNomCli = new TextField();
        txtNomCli.setPromptText("Nombre cliente");

        txtDomCli = new TextField();
        txtDomCli.setPromptText("Domicilio cliente");

        txtNifCli = new TextField();
        txtNifCli.setPromptText("NIF cliente");

        cbTipoCli = new ComboBox<>();
        cbTipoCli.getItems().addAll("Estandar", "Premium");
        cbTipoCli.setValue("Estandar");

        grid.add(new Label("Email cliente:"), 0, 0);
        grid.add(txtEmail, 1, 0);

        grid.add(new Label("Código artículo:"), 0, 1);
        grid.add(txtArticulo, 1, 1);

        grid.add(new Label("Cantidad:"), 0, 2);
        grid.add(txtCantidad, 1, 2);

        grid.add(new Label("Nombre cliente:"), 0, 3);
        grid.add(txtNomCli, 1, 3);

        grid.add(new Label("Domicilio cliente:"), 0, 4);
        grid.add(txtDomCli, 1, 4);

        grid.add(new Label("NIF cliente:"), 0, 5);
        grid.add(txtNifCli, 1, 5);

        grid.add(new Label("Tipo cliente:"), 0, 6);
        grid.add(cbTipoCli, 1, 6);

        Button btnCrear = new Button("Crear pedido");
        btnCrear.setMaxWidth(Double.MAX_VALUE);
        grid.add(btnCrear, 0, 7, 2, 1);

        paneCrear.setContent(grid);

        tablaPedidos = new TableView<>();
        tablaPedidos.setPlaceholder(new Label("Usa los botones para cargar datos"));

        TableColumn<Pedido, String> colNum = new TableColumn<>("Nº Pedido");
        colNum.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getNumPedido())));

        TableColumn<Pedido, String> colCli = new TableColumn<>("Cliente");
        colCli.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCliente().getNombre()));

        TableColumn<Pedido, String> colArt = new TableColumn<>("Artículo");
        colArt.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getArticulo().getDescripcion()));

        TableColumn<Pedido, String> colCant = new TableColumn<>("Cant.");
        colCant.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getCantidad())));

        TableColumn<Pedido, String> colTotal = new TableColumn<>("Precio Total");
        colTotal.setCellValueFactory(c -> new SimpleStringProperty(String.format("%.2f€", c.getValue().calcularTotal())));

        tablaPedidos.getColumns().addAll(colNum, colCli, colArt, colCant, colTotal);
        tablaPedidos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        txtEmailFiltro = new TextField();
        txtEmailFiltro.setPromptText("Email filtro...");

        Button btnPendientes = new Button("Mostrar pendientes");
        Button btnEnviados = new Button("Mostrar enviados");
        Button btnTodos = new Button("Mostrar todos");

        HBox hbFiltros = new HBox(10, new Label("Filtro:"), txtEmailFiltro, btnPendientes, btnEnviados, btnTodos);

        txtNumPedidoEliminar = new TextField();
        txtNumPedidoEliminar.setPromptText("Nº pedido");

        Button btnEliminar = new Button("Eliminar pedido");
        HBox hbEliminar = new HBox(10, new Label("Eliminar:"), txtNumPedidoEliminar, btnEliminar);

        btnCrear.setOnAction(e -> {
            try {
                String datosCliente = txtNomCli.getText() + "|" + txtDomCli.getText() + "|" + txtNifCli.getText() + "|" + cbTipoCli.getValue();

                controlador.addPedido(
                        txtEmail.getText(),
                        datosCliente,
                        txtArticulo.getText(),
                        Integer.parseInt(txtCantidad.getText()),
                        LocalDateTime.now()
                );

                limpiarCampos();
                btnTodos.fire();
                new Alert(Alert.AlertType.INFORMATION, "Pedido creado correctamente").show();

            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, "Error: " + ex.getMessage()).show();
            }
        });

        btnPendientes.setOnAction(e ->
                tablaPedidos.getItems().setAll(controlador.getPedidosPendientes(txtEmailFiltro.getText()))
        );

        btnEnviados.setOnAction(e ->
                tablaPedidos.getItems().setAll(controlador.getPedidosEnviados(txtEmailFiltro.getText()))
        );

        btnTodos.setOnAction(e -> {
            tablaPedidos.getItems().clear();
            tablaPedidos.getItems().addAll(controlador.getPedidosPendientes(""));
            tablaPedidos.getItems().addAll(controlador.getPedidosEnviados(""));
        });

        btnEliminar.setOnAction(e -> {
            try {
                int numPedido = Integer.parseInt(txtNumPedidoEliminar.getText());
                controlador.eliminarPedido(numPedido, LocalDateTime.now());
                txtNumPedidoEliminar.clear();
                btnTodos.fire();
                new Alert(Alert.AlertType.INFORMATION, "Pedido eliminado correctamente").show();
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, "Error: " + ex.getMessage()).show();
            }
        });

        this.getChildren().addAll(
                new Label("GESTIÓN DE PEDIDOS"),
                paneCrear,
                new Separator(),
                hbFiltros,
                hbEliminar,
                tablaPedidos
        );

        VBox.setVgrow(tablaPedidos, Priority.ALWAYS);
    }

    private void limpiarCampos() {
        txtEmail.clear();
        txtArticulo.clear();
        txtCantidad.clear();
        txtNomCli.clear();
        txtDomCli.clear();
        txtNifCli.clear();
        cbTipoCli.setValue("Estandar");
    }
}