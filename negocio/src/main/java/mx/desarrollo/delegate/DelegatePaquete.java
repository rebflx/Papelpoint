package mx.desarrollo.delegate;

import java.util.List;
import java.util.Map;
import mx.desarrollo.entity.Paquete;
import mx.desarrollo.entity.ProductoPaquete;
import mx.desarrollo.persistence.integration.ServiceLocator;

public class DelegatePaquete {
    public List<Paquete> listar() {
        return ServiceLocator.getInstancePaqueteDAO().listarActualizados();
    }
    public List<ProductoPaquete> obtenerProductos(Integer id) {
        return ServiceLocator.getInstancePaqueteDAO().obtenerProductos(id);
    }
    public void guardar(Paquete p, Map<Integer,Integer> cantidades) {
        validar(p, cantidades);
        ServiceLocator.getInstancePaqueteDAO().guardarPaquete(p, cantidades);
    }
    public void modificar(Paquete p, Map<Integer,Integer> cantidades) {
        if (p.getIdPaquete() == null) throw new IllegalArgumentException("Selecciona un paquete.");
        validar(p, cantidades);
        ServiceLocator.getInstancePaqueteDAO().actualizarPaquete(p, cantidades);
    }
    public void eliminar(Integer id) {
        if (id == null) throw new IllegalArgumentException("Selecciona un paquete.");
        ServiceLocator.getInstancePaqueteDAO().desactivar(id);
    }
    private void validar(Paquete p, Map<Integer,Integer> cantidades) {
        if (p == null || p.getNombrePaquete() == null || p.getNombrePaquete().isBlank()
                || p.getNombrePaquete().length() > 100) {
            throw new IllegalArgumentException("Escribe un nombre válido (máximo 100 caracteres).");
        }
        if (p.getPrecio() == null || !Double.isFinite(p.getPrecio()) || p.getPrecio() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor que cero.");
        }
        if (p.getDescripcion() == null || p.getDescripcion().isBlank() || p.getDescripcion().length() > 500) {
            throw new IllegalArgumentException("Escribe una descripción (máximo 500 caracteres).");
        }
        if (cantidades == null || cantidades.isEmpty() || cantidades.values().stream().anyMatch(n -> n == null || n < 1)) {
            throw new IllegalArgumentException("Agrega al menos un producto con cantidad válida.");
        }
    }
}
