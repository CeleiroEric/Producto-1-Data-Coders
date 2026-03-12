
package datacoders.vista;

import datacoders.modelo.*;
import datacoders.controlador.*;

public class GestionStore {

    private Controlador controlador;


    public GestionStore () {
        this.controlador = new Controlador();
    }


    public void inicio() {
        boolean salir = false;
        do {
            System.out.println("\n===== GESTIÓN TIENDA ONLINE =====");
            System.out.println("1. Gestión de Artículos");
            System.out.println("2. Gestión de Clientes");
            System.out.println("3. Gestión de Pedidos");
            System.out.println("0. Salir");



            int opcion = LeerDatos.leerEntero("Selecciona una opción: ");

            switch (opcion) {
                case 1 -> menuArticulos();
                case 2 -> menuClientes();
                case 3 -> menuPedidos();
                case 0 -> salir = true;
                default -> System.out.println("Opción no válida.");
            }
        } while (!salir);
    }



    // --- SUBMENÚ ARTÍCULOS ---
    private void menuArticulos() {
        System.out.println("\n-- ARTÍCULOS --");
        System.out.println("1. Añadir Artículo");
        System.out.println("2. Mostrar Artículos");
        int opt = LeerDatos.leerEntero("Selecciona: ");



        if (opt == 1) {
            String cod = LeerDatos.leerTexto("Código: ");
            String desc = LeerDatos.leerTexto("Descripción: ");
            double precio = LeerDatos.leerDouble("Precio: ");
            double envio = LeerDatos.leerDouble("Gastos Envío: ");
            int tiempo = LeerDatos.leerEntero("Tiempo Prep (min): ");

            try {
                controlador.addArticulo(cod, desc, precio, envio, tiempo);
                System.out.println("Artículo añadido correctamente.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            // Usamos getArticulos() que es el nombre real en tu controlador
            System.out.println(controlador.getArticulos());
        }
    }



    // --- SUBMENÚ CLIENTES ---

    private void menuClientes() {
        System.out.println("\n-- CLIENTES --");
        System.out.println("1. Añadir Cliente");
        System.out.println("2. Mostrar Todos los Clientes");
        System.out.println("3. Mostrar Clientes Estándar");
        System.out.println("4. Mostrar Clientes Premium");

        // Solo una vez cada variable
        int opt = LeerDatos.leerEntero("Selecciona: ");

        if (opt == 1) {
            String email = LeerDatos.leerTexto("Email: ");
            String nombre = LeerDatos.leerTexto("Nombre: ");
            // CAMBIO: Usa el nombre 'domicilio' para que coincida con lo de abajo
            String domicilio = LeerDatos.leerTexto("Domicilio: ");
            String nif = LeerDatos.leerTexto("NIF: ");
            String tipo = LeerDatos.leerTexto("Tipo (Estandar/Premium): ");

            // Ahora 'domicilio' ya existe y no dará error
            try {
                if (tipo.equalsIgnoreCase("Estandar")) {
                    controlador.addClienteEstandar(nombre, domicilio, nif, email);
                    System.out.println("Cliente Estándar añadido!");
                } else {
                    controlador.addClientePremium(nombre, domicilio, nif, email);
                    System.out.println("Cliente Premium añadido!");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }


        } else if (opt == 2) {
            System.out.println("--- Lista de todos los Clientes ---");
            System.out.println(controlador.getClientes());

        } else if (opt == 3) {
            System.out.println("--- Clientes Estándar ---");
            System.out.println(controlador.getClientesEstandar());

        } else if (opt == 4) {
            System.out.println("--- Clientes Premium ---");
            System.out.println(controlador.getClientesPremium());
        }
    }

    // --- SUBMENÚ PEDIDOS ---

private void menuPedidos() {
    System.out.println("\n-- PEDIDOS --");
    System.out.println("1. Añadir Pedido");
    System.out.println("2. Eliminar Pedido");
    System.out.println("3. Mostrar Pedidos Pendientes");
    System.out.println("4. Mostrar Pedidos Enviados");
    int opt = LeerDatos.leerEntero("Selecciona: ");

        try {
            switch (opt) {
                case 1 -> {
                    String email = LeerDatos.leerTexto("Email Cliente: ");
                    String cod = LeerDatos.leerTexto("Código Artículo: ");
                    int cant = LeerDatos.leerEntero("Cantidad: ");
                    // Añadimos la fecha actual como 5º parámetro
                    controlador.addPedido(email, "", cod, cant, java.time.LocalDateTime.now());
                    System.out.println("Pedido añadido con éxito.");
                }
                case 2 -> {
                    int num = LeerDatos.leerEntero("Número de pedido a cancelar: ");
                    // Añadimos la fecha actual como 2º parámetro
                    controlador.eliminarPedido(num, java.time.LocalDateTime.now());
                    System.out.println("Pedido cancelado (si cumplía las condiciones).");
                }
                case 3 -> {
                    String email = LeerDatos.leerTexto("Email del cliente (o dejar vacío): ");
                    System.out.println(controlador.getPedidosPendientes(email));
                }
                case 4 -> {
                    String email = LeerDatos.leerTexto("Email del cliente (o dejar vacío): ");
                    System.out.println(controlador.getPedidosEnviados(email));
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
} // <--- ESTA ES LA ÚLTIMA LLAVE QUE CIERRA LA CLASE