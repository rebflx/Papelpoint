package mx.desarrollo.persistence.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.LockModeType;
import mx.desarrollo.entity.*;
import mx.desarrollo.persistence.persistence.AbstractDAO;
import mx.desarrollo.persistence.persistence.HibernateUtil;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class VentaDAO extends AbstractDAO<Venta> {

    public static final String ESTADO_COMPLETADA = "COMPLETADA";
    public static final String ESTADO_CANCELADA = "CANCELADA";

    public VentaDAO(){
        super(Venta.class);
    }

    @Override
    protected EntityManager getEntityManager(){
        return HibernateUtil.getEntityManager();
    }

    public static String generarFolio(Integer idVenta){
        return "NV-" + idVenta;
    }


    public Venta registrarVenta(Venta venta, List<DetalleVentaProducto> detalles){
        return registrarVenta(venta, detalles, List.of());
    }

    public Venta registrarVenta(Venta venta, List<DetalleVentaProducto> detalles,
                                List<DetalleVentaServicio> servicios){
        return registrarVenta(venta, detalles, servicios, List.of());
    }

    public Venta registrarVenta(Venta venta, List<DetalleVentaProducto> detalles,
                                List<DetalleVentaServicio> servicios,
                                List<DetalleVentaPaquete> paquetes){
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();

            double subtotal = 0;
            for (DetalleVentaProducto d: detalles){
                if(d.getCantidad() == null || d.getCantidad() < 1){
                    throw new IllegalArgumentException("La cantidad debe ser de al menos 1");
                }

                Producto p = em.find(Producto.class, d.getProducto().getIdProducto(), LockModeType.PESSIMISTIC_WRITE);

                if(p==null||!Boolean.TRUE.equals(p.getActivo())){
                    throw new IllegalArgumentException("Un producto del carrito ya no esta disponible");
                }
                if(p.getExistencias() < d.getCantidad()){
                    throw new IllegalArgumentException("No hay existencias suficientes de \"" + p.getNombre() + "\" (disponibles: " + p.getExistencias() + ")");
                }

                p.setExistencias(p.getExistencias() - d.getCantidad());
                d.setVenta(venta);
                d.setProducto(p);

                double precioOriginal = p.getPrecio();
                double precioFinal = obtenerPrecioFinal(em, p);

                d.setPrecioOriginal(precioOriginal);
                d.setPrecioUnitario(precioFinal);

                subtotal += precioFinal * d.getCantidad();

            }

            for (DetalleVentaServicio d : servicios) {
                if (d.getCantidad() == null || d.getCantidad() < 1) {
                    throw new IllegalArgumentException("La cantidad del servicio debe ser mayor que cero.");
                }
                String cliente = d.getNombreCliente() == null ? "" : d.getNombreCliente().trim();
                if (cliente.isEmpty() || cliente.length() > 100) {
                    throw new IllegalArgumentException("El nombre del cliente no es valido.");
                }
                if (d.getServicio() == null || d.getServicio().getIdServicio() == null) {
                    throw new IllegalArgumentException("No se selecciono un servicio valido.");
                }
                Servicio servicio = em.find(Servicio.class, d.getServicio().getIdServicio());
                if (servicio == null || !Boolean.TRUE.equals(servicio.getDisponible())) {
                    throw new IllegalArgumentException("Un servicio del carrito ya no esta disponible.");
                }
                String descripcion = d.getDescripcionTrabajo() == null ? "" : d.getDescripcionTrabajo().trim();
                if (descripcion.isEmpty() || descripcion.length() > 500) {
                    throw new IllegalArgumentException("La descripcion del trabajo es obligatoria (maximo 500 caracteres).");
                }
                if (d.getProductoMaterial() == null || d.getProductoMaterial().getIdProducto() == null) {
                    throw new IllegalArgumentException("Selecciona un material del inventario para el servicio.");
                }
                Producto material = em.find(Producto.class, d.getProductoMaterial().getIdProducto(),
                        LockModeType.PESSIMISTIC_WRITE);
                if (material == null || !Boolean.TRUE.equals(material.getActivo()) ||
                        material.getExistencias() < d.getCantidad()) {
                    throw new IllegalArgumentException("No hay suficientes materiales o existencias para el servicio "
                            + servicio.getNombre() + ".");
                }
                material.setExistencias(material.getExistencias() - d.getCantidad());
                d.setProductoMaterial(material);
                d.setDescripcionTrabajo(descripcion);
                d.setServicio(servicio);
                d.setVenta(venta);
                d.setNombreCliente(cliente);
                d.setEstadoTrabajo("Pendiente");
                d.setPrecioUnitario(servicio.getPrecio());
                subtotal += servicio.getPrecio() * d.getCantidad();
            }

            for (DetalleVentaPaquete d : paquetes) {
                if (d.getCantidad() == null || d.getCantidad() < 1) {
                    throw new IllegalArgumentException("La cantidad del paquete debe ser de al menos 1");
                }
                if (d.getPaquete() == null || d.getPaquete().getIdPaquete() == null) {
                    throw new IllegalArgumentException("No se selecciono un paquete valido.");
                }
                Paquete paquete = em.find(Paquete.class, d.getPaquete().getIdPaquete());
                if (paquete == null || !Boolean.TRUE.equals(paquete.getActiva())) {
                    throw new IllegalArgumentException("Un paquete del carrito ya no esta disponible.");
                }
                List<ProductoPaquete> componentes = em.createQuery(
                                "SELECT pp FROM ProductoPaquete pp WHERE pp.paquete.idPaquete = :id "
                                        + "ORDER BY pp.idProductoPaquete", ProductoPaquete.class)
                        .setParameter("id", paquete.getIdPaquete())
                        .getResultList();
                if (componentes.isEmpty()) {
                    throw new IllegalArgumentException("El paquete \"" + paquete.getNombrePaquete() + "\" no tiene productos.");
                }
                for (ProductoPaquete pp : componentes) {
                    Producto p = em.find(Producto.class, pp.getProducto().getIdProducto(), LockModeType.PESSIMISTIC_WRITE);
                    if (p == null || !Boolean.TRUE.equals(p.getActivo())) {
                        throw new IllegalArgumentException("Un producto del paquete \"" + paquete.getNombrePaquete()
                                + "\" ya no esta disponible.");
                    }
                    int requeridas = pp.getCantidad() * d.getCantidad();
                    if (p.getExistencias() == null || p.getExistencias() < requeridas) {
                        throw new IllegalArgumentException("No hay existencias suficientes de \"" + p.getNombre()
                                + "\" para el paquete \"" + paquete.getNombrePaquete() + "\" (disponibles: "
                                + (p.getExistencias() == null ? 0 : p.getExistencias()) + ", requeridas: " + requeridas + ")");
                    }
                    p.setExistencias(p.getExistencias() - requeridas);
                }
                d.setVenta(venta);
                d.setPaquete(paquete);
                d.setPrecioUnitario(paquete.getPrecio());
                subtotal += paquete.getPrecio() * d.getCantidad();
            }

            venta.setUsuario(em.getReference(Usuario.class, venta.getUsuario().getIdUsuario()));
            venta.setSubtotal(redondear(subtotal));
            venta.setTotal(redondear(subtotal));

            em.persist(venta);
            for(DetalleVentaProducto d: detalles){
                em.persist(d);
            }
            for(DetalleVentaServicio d: servicios){
                em.persist(d);
            }
            for(DetalleVentaPaquete d: paquetes){
                em.persist(d);
            }

            Ticket ticket = new Ticket();
            ticket.setVenta(venta);
            ticket.setFechaExpedicion(LocalDateTime.now());
            ticket.setFolio(generarFolio(venta.getIdVenta()));
            em.persist(ticket);

            tx.commit();
            return venta;
        }catch (RuntimeException ex){
            if(tx.isActive()){
                tx.rollback();
            }
            throw ex;
        }finally {
            em.close();
        }
    }


    public int numeroClienteDelDia(Venta venta){
        LocalDateTime inicio = venta.getFecha().toLocalDate().atStartOfDay();
        LocalDateTime fin = inicio.plusDays(1);
        return execute(em -> Math.toIntExact(em.createQuery(
                        "SELECT COUNT(v) FROM Venta v WHERE v.fecha >= :ini AND v.fecha < :fin AND v.idVenta <= :id",
                        Long.class)
                .setParameter("ini", inicio)
                .setParameter("fin", fin)
                .setParameter("id", venta.getIdVenta())
                .getSingleResult()));
    }


    public List<Ticket> buscarTickets(String folio, LocalDate dia, int limite){
        String texto = folio == null ? "" : folio.replaceAll("[%_\\\\]", "").trim().toLowerCase();

        StringBuilder jpql = new StringBuilder(
                "SELECT t FROM Ticket t JOIN FETCH t.venta v JOIN FETCH v.usuario WHERE 1 = 1");
        jpql.append(" AND v.estado <> :cancelada");
        if(!texto.isEmpty()){
            jpql.append(" AND LOWER(t.folio) LIKE :folio");
        }
        if(dia != null){
            jpql.append(" AND t.fechaExpedicion >= :ini AND t.fechaExpedicion < :fin");
        }
        jpql.append(" ORDER BY t.fechaExpedicion DESC, t.idTicket DESC");

        EntityManager em = getEntityManager();
        try{
            TypedQuery<Ticket> q = em.createQuery(jpql.toString(), Ticket.class);
            q.setParameter("cancelada", ESTADO_CANCELADA);
            if(!texto.isEmpty()){
                q.setParameter("folio", "%" + texto + "%");
            }
            if(dia != null){
                q.setParameter("ini", dia.atStartOfDay());
                q.setParameter("fin", dia.plusDays(1).atStartOfDay());
            }
            q.setMaxResults(limite);
            return q.getResultList();
        }finally {
            em.close();
        }
    }

    public List<DetalleVentaProducto> detallesDeVenta(Integer idVenta){
        EntityManager em = getEntityManager();
        try{
            return em.createQuery(
                            "SELECT d FROM DetalleVentaProducto d JOIN FETCH d.producto "
                                    + "WHERE d.venta.idVenta = :id ORDER BY d.idDetalleProducto",
                            DetalleVentaProducto.class)
                    .setParameter("id", idVenta)
                    .getResultList();
        }finally {
            em.close();
        }
    }

    public List<DetalleVentaServicio> detallesServicioDeVenta(Integer idVenta) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery(
                            "SELECT d FROM DetalleVentaServicio d JOIN FETCH d.servicio LEFT JOIN FETCH d.productoMaterial "
                                    + "WHERE d.venta.idVenta = :id ORDER BY d.idDetalleServicio",
                            DetalleVentaServicio.class)
                    .setParameter("id", idVenta)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<DetalleVentaPaquete> detallesPaqueteDeVenta(Integer idVenta) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery(
                            "SELECT d FROM DetalleVentaPaquete d JOIN FETCH d.paquete "
                                    + "WHERE d.venta.idVenta = :id ORDER BY d.idDetallePaquete",
                            DetalleVentaPaquete.class)
                    .setParameter("id", idVenta)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<DetalleVentaServicio> listarTrabajosServicio() {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery(
                            "SELECT d FROM DetalleVentaServicio d JOIN FETCH d.servicio LEFT JOIN FETCH d.productoMaterial "
                                    + "ORDER BY d.idDetalleServicio DESC",
                            DetalleVentaServicio.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public void actualizarEstadoTrabajo(Integer idDetalle, String nuevoEstado) {
        if (idDetalle == null || !"Realizado".equals(nuevoEstado)) {
            throw new IllegalArgumentException("ID o estado del trabajo no valido.");
        }
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            DetalleVentaServicio detalle = em.find(
                    DetalleVentaServicio.class, idDetalle, LockModeType.PESSIMISTIC_WRITE);
            if (detalle == null) {
                throw new IllegalArgumentException("El trabajo ya no existe.");
            }
            if (!"Pendiente".equalsIgnoreCase(detalle.getEstadoTrabajo())) {
                throw new IllegalArgumentException("El trabajo ya no esta pendiente.");
            }
            detalle.setEstadoTrabajo("Realizado");
            tx.commit();
        } catch (RuntimeException ex) {
            if (tx.isActive()) tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    public List<Venta> ventasPendientesDeCorte(Integer idUsuario) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery(
                            "SELECT v FROM Venta v JOIN FETCH v.usuario "
                                    + "WHERE v.corteCaja IS NULL AND v.estado = :est "
                                    + "AND v.usuario.idUsuario = :u "
                                    + "ORDER BY v.fecha DESC, v.idVenta DESC", Venta.class)
                    .setParameter("est", ESTADO_COMPLETADA)
                    .setParameter("u", idUsuario)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public void cancelarVenta(Integer idVenta, Integer idUsuario) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Venta v = em.find(Venta.class, idVenta, LockModeType.PESSIMISTIC_WRITE);
            if (v == null) {
                throw new IllegalArgumentException("La venta ya no existe.");
            }
            if (!ESTADO_COMPLETADA.equals(v.getEstado())) {
                throw new IllegalArgumentException("La venta ya fue cancelada.");
            }
            if (v.getCorteCaja() != null) {
                throw new IllegalArgumentException("La venta ya pertenece a un corte de caja.");
            }
            if (!v.getUsuario().getIdUsuario().equals(idUsuario)) {
                throw new IllegalArgumentException("Solo puedes cancelar ventas de tu turno.");
            }

            List<DetalleVentaServicio> servicios = em.createQuery(
                            "SELECT d FROM DetalleVentaServicio d LEFT JOIN FETCH d.productoMaterial "
                                    + "WHERE d.venta.idVenta = :id", DetalleVentaServicio.class)
                    .setParameter("id", idVenta).getResultList();
            for (DetalleVentaServicio d : servicios) {
                if ("Realizado".equalsIgnoreCase(d.getEstadoTrabajo())) {
                    throw new IllegalArgumentException(
                            "No se puede cancelar: un servicio de esta venta ya fue realizado.");
                }
            }

            // Productos sueltos
            List<DetalleVentaProducto> productos = em.createQuery(
                            "SELECT d FROM DetalleVentaProducto d WHERE d.venta.idVenta = :id",
                            DetalleVentaProducto.class)
                    .setParameter("id", idVenta).getResultList();
            for (DetalleVentaProducto d : productos) {
                Producto p = em.find(Producto.class, d.getProducto().getIdProducto(),
                        LockModeType.PESSIMISTIC_WRITE);
                p.setExistencias(p.getExistencias() + d.getCantidad());
            }

            // Materiales usados en servicios
            for (DetalleVentaServicio d : servicios) {
                if (d.getProductoMaterial() != null) {
                    Producto m = em.find(Producto.class, d.getProductoMaterial().getIdProducto(),
                            LockModeType.PESSIMISTIC_WRITE);
                    m.setExistencias(m.getExistencias() + d.getCantidad());
                }
            }

            // Productos que componen cada paquete vendido
            List<DetalleVentaPaquete> paquetes = em.createQuery(
                            "SELECT d FROM DetalleVentaPaquete d WHERE d.venta.idVenta = :id",
                            DetalleVentaPaquete.class)
                    .setParameter("id", idVenta).getResultList();
            for (DetalleVentaPaquete d : paquetes) {
                List<ProductoPaquete> componentes = em.createQuery(
                                "SELECT pp FROM ProductoPaquete pp WHERE pp.paquete.idPaquete = :id",
                                ProductoPaquete.class)
                        .setParameter("id", d.getPaquete().getIdPaquete()).getResultList();
                for (ProductoPaquete pp : componentes) {
                    Producto p = em.find(Producto.class, pp.getProducto().getIdProducto(),
                            LockModeType.PESSIMISTIC_WRITE);
                    p.setExistencias(p.getExistencias() + pp.getCantidad() * d.getCantidad());
                }
            }

            v.setEstado(ESTADO_CANCELADA);
            tx.commit();
        } catch (RuntimeException ex) {
            if (tx.isActive()) tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    private static double redondear(double valor){
        return Math.round(valor * 100) / 100.0;
    }


    private double obtenerPrecioFinal(
            EntityManager em,
            Producto producto) {

        List<Descuento> descuentos = em.createQuery("" +
                "SELECT d FROM Descuento d " +
                "WHERE d.producto.idProducto = :idProducto " +
                "AND d.vigencia >= :ahora " +
                "ORDER BY d.vigencia ASC, d.idDescuento DESC", Descuento.class
        ).setParameter("idProducto", producto.getIdProducto()).setParameter("ahora", LocalDateTime.now()).setMaxResults(1).getResultList();
        double precio = producto.getPrecio();
        if (!descuentos.isEmpty()) {

            double porcentaje = descuentos.get(0).getPorcentajeDescuento();
            precio = precio * (1 - porcentaje / 100.0);
        }
        return Math.round(precio * 100.0) / 100.0;
    }


}