package datacoders.factory;

import datacoders.dao.ArticuloDAO;
import datacoders.dao.ClienteDAO;
import datacoders.dao.PedidoDAO;
import datacoders.dao.mysql.MySqlArticuloDAO;
import datacoders.dao.mysql.MySqlClienteDAO;
import datacoders.dao.mysql.MySqlPedidoDAO;


public class MySqlDAOFactory extends DAOFactory {

    @Override
    public ClienteDAO getClienteDAO() {
        return new MySqlClienteDAO();
    }

    @Override
    public ArticuloDAO getArticuloDAO() {
        return new MySqlArticuloDAO();
    }

    @Override
    public PedidoDAO getPedidoDAO() {
        return new MySqlPedidoDAO();
    }
}