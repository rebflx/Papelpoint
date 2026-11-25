package mx.desarrollo.facade;

import mx.desarrollo.delegate.DelegateReporte;
import mx.desarrollo.delegate.DelegateUsuario;
import mx.desarrollo.entity.Usuario;

import java.time.LocalDate;

public class FacadeReporte {

    private final DelegateReporte delegate =
            new DelegateReporte();

    public DelegateReporte.Resumen consultar(
            Usuario usuario,
            LocalDate desde,
            LocalDate hasta,
            String categoria) {

        if (usuario == null ||
                !DelegateUsuario.ROL_ADMIN.equalsIgnoreCase(usuario.getRol())) {

            throw new SecurityException(
                    "Solo administradores pueden consultar reportes.");
        }

        return delegate.consultar(
                desde,
                hasta,
                categoria
        );
    }
}