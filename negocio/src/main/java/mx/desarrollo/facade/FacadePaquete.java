package mx.desarrollo.facade;

import java.util.List;
import java.util.Map;
import mx.desarrollo.delegate.DelegatePaquete;
import mx.desarrollo.entity.Paquete;
import mx.desarrollo.entity.ProductoPaquete;

public class FacadePaquete {
    private final DelegatePaquete delegate = new DelegatePaquete();
    public List<Paquete> obtenerPaquetes() { return delegate.listar(); }
    public List<Paquete> obtenerPaquetesActivos() { return delegate.listarActivos(); }
    public List<ProductoPaquete> obtenerComponentesActuales(Integer id) { return delegate.componentesActuales(id); }
    public List<ProductoPaquete> obtenerProductosPaquete(Integer id) { return delegate.obtenerProductos(id); }
    public void guardarPaquete(Paquete p, Map<Integer,Integer> cantidades) { delegate.guardar(p, cantidades); }
    public void modificarPaquete(Paquete p, Map<Integer,Integer> cantidades) { delegate.modificar(p, cantidades); }
    public void eliminarPaquete(Integer id) { delegate.eliminar(id); }
}
