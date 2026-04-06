package datacoders.factory;

import datacoders.dao.ArticuloDAO;
import datacoders.dao.ClienteDAO;
import datacoders.dao.PedidoDAO;

public abstract class DAOFactory {

    public static final int MYSQL = 1;

    public static DAOFactory getFactory(int tipo) {
        if (tipo == MYSQL) {
            return new MySqlDAOFactory();
        }
        throw new IllegalArgumentException("Tipo de factoría no soportado: " + tipo);
    }

    public abstract ClienteDAO getClienteDAO();
    public abstract ArticuloDAO getArticuloDAO();
    public abstract PedidoDAO getPedidoDAO();
}