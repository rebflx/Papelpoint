package mx.desarrollo.persistence.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.LockModeType;
import jakarta.persistence.TypedQuery;
import mx.desarrollo.entity.CorteCaja;
import mx.desarrollo.entity.Usuario;
import mx.desarrollo.entity.Venta;
import mx.desarrollo.persistence.persistence.AbstractDAO;
import mx.desarrollo.persistence.persistence.HibernateUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public class CorteCajaDAO extends AbstractDAO<CorteCaja> {

    public CorteCajaDAO() {
        super(CorteCaja.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return HibernateUtil.getEntityManager();
    }

    public List<Venta> ventasPendientes(Integer idUsuario) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery(
                            "SELECT v FROM Venta v WHERE v.usuario.idUsuario = :id AND v.corteCaja IS NULL "
                                    + "ORDER BY v.fecha, v.idVenta", Venta.class)
                    .setParameter("id", idUsuario)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Venta> ventasDeCorte(Integer idCorte) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery(
                            "SELECT v FROM Venta v WHERE v.corteCaja.idCorte = :id "
                                    + "ORDER BY v.fecha, v.idVenta", Venta.class)
                    .setParameter("id", idCorte)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Venta> ventasDelDia(LocalDate dia) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery(
                            "SELECT v FROM Venta v WHERE v.fecha >= :ini AND v.fecha < :fin "
                                    + "ORDER BY v.fecha, v.idVenta", Venta.class)
                    .setParameter("ini", dia.atStartOfDay())
                    .setParameter("fin", dia.plusDays(1).atStartOfDay())
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<CorteCaja> buscarCortes(LocalDate desde, LocalDate hasta, Integer idUsuario, int limite) {
        StringBuilder jpql = new StringBuilder(
                "SELECT c FROM CorteCaja c JOIN FETCH c.usuario WHERE c.fechaCierre IS NOT NULL");
        if (desde != null) {
            jpql.append(" AND c.fechaCierre >= :ini");
        }
        if (hasta != null) {
            jpql.append(" AND c.fechaCierre < :fin");
        }
        if (idUsuario != null) {
            jpql.append(" AND c.usuario.idUsuario = :idUsuario");
        }
        jpql.append(" ORDER BY c.fechaCierre DESC, c.idCorte DESC");

        EntityManager em = getEntityManager();
        try {
            TypedQuery<CorteCaja> q = em.createQuery(jpql.toString(), CorteCaja.class);
            if (desde != null) {
                q.setParameter("ini", desde.atStartOfDay());
            }
            if (hasta != null) {
                q.setParameter("fin", hasta.plusDays(1).atStartOfDay());
            }
            if (idUsuario != null) {
                q.setParameter("idUsuario", idUsuario);
            }
            q.setMaxResults(limite);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public CorteCaja buscarConUsuario(Integer idCorte) {
        EntityManager em = getEntityManager();
        try {
            List<CorteCaja> r = em.createQuery(
                            "SELECT c FROM CorteCaja c JOIN FETCH c.usuario WHERE c.idCorte = :id", CorteCaja.class)
                    .setParameter("id", idCorte)
                    .getResultList();
            return r.isEmpty() ? null : r.get(0);
        } finally {
            em.close();
        }
    }


    public CorteCaja registrarCorte(Usuario usuario, double efectivoContado, double fondoInicial,
                                    String metodoEfectivo, String estadoValido) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            List<Venta> pendientes = em.createQuery(
                            "SELECT v FROM Venta v WHERE v.usuario.idUsuario = :id AND v.corteCaja IS NULL "
                                    + "ORDER BY v.fecha, v.idVenta", Venta.class)
                    .setParameter("id", usuario.getIdUsuario())
                    .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                    .getResultList();

            if (pendientes.isEmpty()) {
                throw new IllegalArgumentException("No hay ventas pendientes de corte para este usuario");
            }

            double totalVentas = 0;
            double efectivo = 0;
            LocalDateTime inicio = null;
            for (Venta v : pendientes) {
                if (inicio == null || v.getFecha().isBefore(inicio)) {
                    inicio = v.getFecha();
                }
                if (!estadoValido.equals(v.getEstado())) {
                    continue;
                }
                totalVentas += v.getTotal();
                if (metodoEfectivo.equals(v.getMetodoPago())) {
                    efectivo += v.getTotal();
                }
            }
            totalVentas = redondear(totalVentas);
            efectivo = redondear(efectivo);
            double noEfectivo = redondear(totalVentas - efectivo);

            double efectivoNeto = redondear(efectivoContado - fondoInicial);
            if (efectivoNeto < 0) {
                throw new IllegalArgumentException("El efectivo contado no puede ser menor al fondo inicial de caja");
            }

            CorteCaja corte = new CorteCaja();
            corte.setUsuario(em.find(Usuario.class, usuario.getIdUsuario()));
            corte.setFechaInicio(inicio);
            corte.setFechaCierre(LocalDateTime.now());
            corte.setTotalCalculado(totalVentas);
            corte.setTotalContado(redondear(efectivoNeto + noEfectivo));
            em.persist(corte);

            for (Venta v : pendientes) {
                v.setCorteCaja(corte);
            }

            tx.commit();
            return corte;
        } catch (RuntimeException ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
    }

    private static double redondear(double valor) {
        return Math.round(valor * 100) / 100.0;
    }
}
