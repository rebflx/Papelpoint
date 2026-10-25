package mx.desarrollo.persistence.integration;

import jakarta.persistence.EntityManager;
import mx.desarrollo.persistence.dao.AsignacionDAO;
import mx.desarrollo.persistence.dao.ProfesorDAO;
import mx.desarrollo.persistence.dao.UnidadDAO;
import mx.desarrollo.persistence.dao.UsuarioDAO;
import mx.desarrollo.persistence.persistence.HibernateUtil;

public class ServiceLocator {

    private static ProfesorDAO profesorDAO;
    private static UsuarioDAO usuarioDAO;
    private static UnidadDAO unidadAprendizajeDAO;
    private static AsignacionDAO asignacionDAO;

    private static EntityManager getEntityManager() {
        return HibernateUtil.getEntityManager();
    }

    public static ProfesorDAO getInstanceProfesorDAO() {
        if (profesorDAO == null) {
            profesorDAO = new ProfesorDAO(getEntityManager());
        }

        return profesorDAO;
    }

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
}