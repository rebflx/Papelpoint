package mx.desarrollo.persistence.integration;
import mx.desarrollo.persistence.dao.*;
import mx.desarrollo.persistence.dao.servicioDAO;
import jakarta.persistence.EntityManager;
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

    private static HistorialDAO historialDAO;

    public static HistorialDAO getInstanceHistorialDAO() {
        if (historialDAO == null) {
            historialDAO = new HistorialDAO();
        }
        return historialDAO;
    }


    private static servicioDAO servicioDAO;

    public static servicioDAO getInstanceServicioDAO() {
        if (servicioDAO == null) {
            servicioDAO = new servicioDAO(getEntityManager());
        }

        return servicioDAO;
    }


    private static CorteCajaDAO corteCajaDAO;

    public static CorteCajaDAO getInstanceCorteCajaDAO() {
        if (corteCajaDAO == null) {
            corteCajaDAO = new CorteCajaDAO();
        }
        return corteCajaDAO;
    }

}