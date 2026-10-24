import mx.desarrollo.entity.Profesor;
import mx.desarrollo.persistence.dao.ProfesorDAO;
import mx.desarrollo.persistence.persistence.HibernateUtil;

public class testDAO {

    public static void main(String[] args) {

        ProfesorDAO profesorDAO =
                new ProfesorDAO(HibernateUtil.getEntityManager());

        for (Profesor profesor : profesorDAO.findAll()) {
            System.out.println(
                    "ID: " + profesor.getId()
                            + " | Nombre: " + profesor.getNombre()
                            + " | Apellido P: " + profesor.getApellidoP()
                            + " | Apellido M: " + profesor.getApellidoM()
            );
        }
    }
}