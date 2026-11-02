package mx.desarrollo.delegate;

import mx.desarrollo.entity.HistorialModificacionProducto;
import mx.desarrollo.entity.Producto;
import mx.desarrollo.entity.Usuario;
import mx.desarrollo.persistence.integration.ServiceLocator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class DelegateHistorial {

    public List<HistorialModificacionProducto> getHistorial() {
        return ServiceLocator.getInstanceHistorialDAO().findAllOrdenado();
    }

    public void registrar(Usuario usuario, Producto producto, String detalles) {
        if (usuario == null || usuario.getIdUsuario() == null
                || producto == null || producto.getIdProducto() == null
                || detalles == null || detalles.isBlank()) {
            return;
        }
        ServiceLocator.getInstanceHistorialDAO()
                .registrar(usuario.getIdUsuario(), producto.getIdProducto(), detalles);
    }

    public String describirCambios(Producto antes, Producto despues) {
        List<String> cambios = new ArrayList<>();

        if (!Objects.equals(antes.getNombre(), despues.getNombre())) {
            cambios.add("Nombre: " + antes.getNombre() + " → " + despues.getNombre());
        }
        if (!Objects.equals(antes.getPrecio(), despues.getPrecio())) {
            cambios.add("Precio: " + dinero(antes.getPrecio()) + " → " + dinero(despues.getPrecio()));
        }
        if (!Objects.equals(antes.getExistencias(), despues.getExistencias())) {
            cambios.add("Existencias: " + antes.getExistencias() + " → " + despues.getExistencias());
        }
        if (!Objects.equals(antes.getStockMinimo(), despues.getStockMinimo())) {
            cambios.add("Stock mínimo: " + antes.getStockMinimo() + " → " + despues.getStockMinimo());
        }
        return String.join("; ", cambios);
    }

    private String dinero(Double valor) {
        return valor == null ? "-" : String.format(Locale.US, "$%.2f", valor);
    }
}
