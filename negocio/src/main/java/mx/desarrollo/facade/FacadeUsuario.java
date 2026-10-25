package mx.desarrollo.facade;

import mx.desarrollo.delegate.DelegateUsuario;
import mx.desarrollo.entity.Usuario;

import java.util.List;

public class FacadeUsuario {

    private final DelegateUsuario delegateUsuario = new DelegateUsuario();

    public Usuario login(String nombreUsuario, char[] contrasena) {
        return delegateUsuario.login(nombreUsuario, contrasena);
    }

    public void guardar(Usuario usuario, String contrasena) {
        delegateUsuario.guardar(usuario, contrasena);
    }

    public void modificar(Usuario usuario, String nuevaContrasena) {
        delegateUsuario.modificar(usuario, nuevaContrasena);
    }

    public void desactivar(Usuario usuario) {
        delegateUsuario.desactivar(usuario);
    }

    public List<Usuario> listar() {
        return delegateUsuario.listar();
    }

    public boolean crearAdminInicialSiNoExiste() {
        return delegateUsuario.crearAdminInicialSiNoExiste();
    }
}

