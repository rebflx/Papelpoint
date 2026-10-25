package mx.desarrollo.persistence.integration;

import mx.desarrollo.persistence.dao.UsuarioDAO;

public class ServiceLocator {

    private static UsuarioDAO usuarioDAO;

    public static UsuarioDAO getInstanceUsuarioDAO() {
        if (usuarioDAO == null) {
            usuarioDAO = new UsuarioDAO();
        }
        return usuarioDAO;
    }

}