package mx.desarrollo.persistence.integration;

import jakarta.persistence.EntityManager;
import mx.desarrollo.entity.Producto;

import mx.desarrollo.persistence.dao.ProductoDAO;
import mx.desarrollo.persistence.persistence.HibernateUtil;

public class ServiceLocator {

    private static ProductoDAO productoDAO;


    private static EntityManager getEntityManager() {
        return HibernateUtil.getEntityManager();
    }

    public static ProductoDAO getInstanceProductoDAO() {
        if (productoDAO == null) {
            productoDAO = new ProductoDAO(getEntityManager());
        }

        return productoDAO;
    }
/*
    public static UsuarioDAO getInstanceUsuarioDAO() {
        if (usuarioDAO == null) {
            usuarioDAO = new UsuarioDAO(getEntityManager());
        }

        return usuarioDAO;
    }

    public static UnidadDAO getInstanceUnidadAprendizajeDAO() {
        if (unidadAprendizajeDAO == null) {
            unidadAprendizajeDAO = new UnidadDAO(getEntityManager());
        }

        return unidadAprendizajeDAO;
    }

    public static AsignacionDAO getInstanceAsignacionDAO() {
        if (asignacionDAO == null) {
            asignacionDAO = new AsignacionDAO(getEntityManager());
        }

        return asignacionDAO;
    }
    */
}