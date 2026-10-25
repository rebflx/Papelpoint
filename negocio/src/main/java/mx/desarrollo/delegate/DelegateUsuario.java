package mx.desarrollo.delegate;

import at.favre.lib.crypto.bcrypt.BCrypt;
import mx.desarrollo.entity.Usuario;
import mx.desarrollo.persistence.integration.ServiceLocator;

import java.util.List;

/**
 * Logica de negocio de Login y Usuarios.
 */
public class DelegateUsuario {

    public static final String ROL_ADMIN = "ADMIN";
    public static final String ROL_EMPLEADO = "EMPLEADO";

    private static final int COSTO_BCRYPT = 12;
    private static final int LONGITUD_MIN_CONTRASENA = 6;

    public Usuario login(String nombreUsuario, char[] contrasena) {
        if (nombreUsuario == null || nombreUsuario.isBlank()
                || contrasena == null || contrasena.length == 0) {
            return null;
        }
        Usuario u = ServiceLocator.getInstanceUsuarioDAO()
                .buscarPorNombreUsuario(nombreUsuario.trim());
        if (u == null || !Boolean.TRUE.equals(u.getActivo())) {
            return null;
        }
        boolean ok = BCrypt.verifyer().verify(contrasena, u.getContrasena()).verified;
        return ok ? u : null;
    }

    public void guardar(Usuario u, String contrasenaPlano) {
        validar(u);
        validarContrasena(contrasenaPlano);
        if (ServiceLocator.getInstanceUsuarioDAO()
                .buscarPorNombreUsuario(u.getNombreUsuario()) != null) {
            throw new IllegalArgumentException("El nombre de usuario ya existe");
        }
        u.setContrasena(hash(contrasenaPlano));
        if (u.getActivo() == null) {
            u.setActivo(true);
        }
        ServiceLocator.getInstanceUsuarioDAO().save(u);
    }

    public void modificar(Usuario u, String nuevaContrasena) {
        validar(u);
        Usuario otro = ServiceLocator.getInstanceUsuarioDAO()
                .buscarPorNombreUsuario(u.getNombreUsuario());
        if (otro != null && !otro.getIdUsuario().equals(u.getIdUsuario())) {
            throw new IllegalArgumentException("El nombre de usuario ya existe");
        }
        if (nuevaContrasena != null && !nuevaContrasena.isBlank()) {
            validarContrasena(nuevaContrasena);
            u.setContrasena(hash(nuevaContrasena));
        }
        ServiceLocator.getInstanceUsuarioDAO().update(u);
    }


    public void desactivar(Usuario u) {
        u.setActivo(false);
        ServiceLocator.getInstanceUsuarioDAO().update(u);
    }

    public List<Usuario> listar() {
        return ServiceLocator.getInstanceUsuarioDAO().findAll();
    }

    public boolean crearAdminInicialSiNoExiste() {
        if (!listar().isEmpty()) {
            return false;
        }
        Usuario admin = new Usuario();
        admin.setNombrePersona("Administrador");
        admin.setNombreUsuario("admin");
        admin.setRol(ROL_ADMIN);
        admin.setActivo(true);
        guardar(admin, "admin123");
        return true;
    }

    // ---------- helpers ----------

    private void validar(Usuario u) {
        if (u == null) {
            throw new IllegalArgumentException("Usuario nulo");
        }
        if (u.getNombrePersona() == null || u.getNombrePersona().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (u.getNombreUsuario() == null || u.getNombreUsuario().isBlank()) {
            throw new IllegalArgumentException("El nombre de usuario es obligatorio");
        }
        u.setNombrePersona(u.getNombrePersona().trim());
        u.setNombreUsuario(u.getNombreUsuario().trim());
        if (u.getNombrePersona().length() > 100) {
            throw new IllegalArgumentException("El nombre no puede pasar de 100 caracteres");
        }
        if (u.getNombreUsuario().length() > 50) {
            throw new IllegalArgumentException("El usuario no puede pasar de 50 caracteres");
        }
        if (!ROL_ADMIN.equals(u.getRol()) && !ROL_EMPLEADO.equals(u.getRol())) {
            throw new IllegalArgumentException("Rol invalido");
        }
    }

    private void validarContrasena(String contrasena) {
        if (contrasena == null || contrasena.length() < LONGITUD_MIN_CONTRASENA) {
            throw new IllegalArgumentException(
                    "La contraseña debe tener al menos " + LONGITUD_MIN_CONTRASENA + " caracteres");
        }
    }

    private String hash(String contrasenaPlano) {
        return BCrypt.withDefaults().hashToString(COSTO_BCRYPT, contrasenaPlano.toCharArray());
    }
}
