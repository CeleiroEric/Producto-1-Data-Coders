package datacoders.vista;
import datacoders.modelo.*;
import datacoders.controlador.*;

import java.util.Scanner;

public class LeerDatos {
    private static Scanner sc = new Scanner(System.in);


    public static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return sc.nextLine();

    }



    public static int leerEntero(String mensaje) {
        System.out.print(mensaje);
        while (!sc.hasNextInt()) {

            System.out.println("Error: Introduce un número entero.");
            sc.next();
        }

        int n = sc.nextInt();
        sc.nextLine(); // Limpiar el buffer
        return n;
    }



    public static double leerDouble(String mensaje) {
        System.out.print(mensaje);

        while (!sc.hasNextDouble()) {
            System.out.println("Error: Introduce un número decimal (usa coma o punto según tu PC).");
            sc.next();

        }

        double d = sc.nextDouble();
        sc.nextLine(); // Limpiar el buffer
        return d;
    }
}
