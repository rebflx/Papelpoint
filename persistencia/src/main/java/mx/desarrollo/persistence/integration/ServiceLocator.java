package mx.desarrollo.persistence.integration;
import mx.desarrollo.persistence.dao.VentaDAO;

import jakarta.persistence.EntityManager;
import mx.desarrollo.persistence.dao.ProductoDAO;
import mx.desarrollo.persistence.dao.UsuarioDAO;
import mx.desarrollo.persistence.persistence.HibernateUtil;

public class ServiceLocator {

    private static UsuarioDAO usuarioDAO;
    private static ProductoDAO productoDAO;

    private static EntityManager getEntityManager() {
        return HibernateUtil.getEntityManager();
    }

    public static UsuarioDAO getInstanceUsuarioDAO() {
        if (usuarioDAO == null) {
            usuarioDAO = new UsuarioDAO();
        }
        return usuarioDAO;
    }

    public static ProductoDAO getInstanceProductoDAO() {
        if (productoDAO == null) {
            productoDAO = new ProductoDAO(getEntityManager());
        }

        return productoDAO;
    }

    private static VentaDAO ventaDAO;

    public static VentaDAO getInstanceVentaDAO() {
        if (ventaDAO == null) {
            ventaDAO = new VentaDAO();
        }
        return ventaDAO;
    }

}