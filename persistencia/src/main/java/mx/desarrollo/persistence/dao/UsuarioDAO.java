package mx.desarrollo.persistence.dao;

import jakarta.persistence.EntityManager;
import mx.desarrollo.entity.Usuario;
import mx.desarrollo.persistence.persistence.AbstractDAO;
import mx.desarrollo.persistence.persistence.HibernateUtil;

public class UsuarioDAO extends AbstractDAO<Usuario> {
    public UsuarioDAO() { super(Usuario.class); }

    @Override
    protected EntityManager getEntityManager() {
        return HibernateUtil.getEntityManager();
    }

    public Usuario buscarPorNombreUsuario(String nombreUsuario) {
        return findByOneParameterUnique(nombreUsuario, "nombreUsuario");
    }
}