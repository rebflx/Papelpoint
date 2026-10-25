import mx.desarrollo.entity.Producto;
import mx.desarrollo.persistence.dao.ProductoDAO;
import mx.desarrollo.persistence.dao.ProfesorDAO;
import mx.desarrollo.persistence.persistence.HibernateUtil;

public class testDAO {

    public static void main(String[] args) {

        ProductoDAO productoDAO =
                new ProductoDAO(HibernateUtil.getEntityManager());

        for (Producto producto : productoDAO.findAll()) {
            System.out.println(
                    "ID: " + producto.getIdProducto()
                            + " | Nombre: " + producto.getNombre()
                            + " | Precio: " + producto.getPrecio()
                            + " | Existencais: " + producto.getExistencias()
                            + " | Stock: " + producto.getStockMinimo()
            );
        }
    }

}
