package datacoders.vista;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import datacoders.controlador.Controlador;

public class FormularioArticulo extends VBox {
    private TextField txtCodigo, txtDescripcion, txtPrecio, txtGastos, txtTiempo;
    private Controlador controlador;
    private SeccionArticulos seccion;

    public FormularioArticulo(Controlador controlador, SeccionArticulos seccion) {
        this.controlador = controlador;
        this.seccion = seccion;
        this.setSpacing(10);
        this.setMinWidth(200);

        txtCodigo = new TextField(); txtCodigo.setPromptText("Código");
        txtDescripcion = new TextField(); txtDescripcion.setPromptText("Descripción");
        txtPrecio = new TextField(); txtPrecio.setPromptText("Precio Venta");
        txtGastos = new TextField(); txtGastos.setPromptText("Gastos Envío");
        txtTiempo = new TextField(); txtTiempo.setPromptText("Tiempo Preparación (min)");

        Button btnAdd = new Button("Guardar Artículo");

        btnAdd.setOnAction(e -> {
            try {
                // Llamamos al controlador con los 5 parámetros
                controlador.addArticulo(
                        txtCodigo.getText(),
                        txtDescripcion.getText(),
                        Double.parseDouble(txtPrecio.getText()),
                        Double.parseDouble(txtGastos.getText()),
                        Integer.parseInt(txtTiempo.getText())
                );
                seccion.refrescarTabla();
                limpiar();
            } catch (Exception ex) {
                System.out.println("Error en datos: " + ex.getMessage());
            }
        });

        this.getChildren().addAll(
                new Label("DATOS ARTÍCULO"),
                txtCodigo, txtDescripcion, txtPrecio, txtGastos, txtTiempo,
                btnAdd
        );
    }

    private void limpiar() {
        txtCodigo.clear(); txtDescripcion.clear(); txtPrecio.clear();
        txtGastos.clear(); txtTiempo.clear();
    }
}