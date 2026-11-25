package mx.desarrollo.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import mx.desarrollo.entity.Paquete;
import mx.desarrollo.entity.Producto;
import mx.desarrollo.entity.ProductoPaquete;
import mx.desarrollo.integration.ServiceFacadeLocator;

/** Panel de paquetes. Componentes dinámicos compatibles con el formulario de NetBeans. */
public class Paquetes extends JPanel {
    private static final Color VERDE = new Color(136, 180, 123);
    private static final Color VERDE_OSCURO = new Color(91, 135, 80);
    private static final Color GRIS = new Color(220, 220, 220);
    private static final java.util.Locale MX = java.util.Locale.forLanguageTag("es-MX");

    private final List<Producto> productos = new ArrayList<>();
    private final Map<Integer,Integer> cantidades = new LinkedHashMap<>();
    private final List<Paquete> paquetes = new ArrayList<>();
    private Integer paqueteSeleccionado;
    private mx.desarrollo.entity.Usuario usuarioActual;

    private final JTextArea txtDescripcion = new JTextArea();
    private final JButton btnNuevo = new JButton("Limpiar");
    private final JTextField txtBuscarProductos = new JTextField();
    private final JTextField txtBuscarPaquetes = new JTextField();
    private final JTable tablaSeleccionados = new JTable();
    private final JTable tablaProductos = new JTable();
    private final JTable tablaPaquetes = new JTable();
    private final JLabel lblCostoProductos = new JLabel("Costo de productos: $0.00");
    private final JPopupMenu popupProductos = new JPopupMenu();
    private final PnlSugerencias pnlSugerencias = new PnlSugerencias();
    private final JList<Producto> listaSugerencias = pnlSugerencias.getLista();
    private final DefaultListModel<Producto> modeloSugerencias = new DefaultListModel<>();
    private boolean seleccionandoSugerencia;

    public Paquetes() {
        initComponents();
        construirPanel();
        cargarProductos();
        cargarPaquetes();
    }

    public Paquetes(mx.desarrollo.entity.Usuario usuario) {
        this();
        usuarioActual = usuario;
    }

    private void construirPanel() {
        lblSubtitulo.setText("Creación de paquetes");
        // Separar el título del borde superior del formulario sin editar initComponents().
        remove(lblSubtitulo);
        agregar(this, lblSubtitulo, 30, 72, 500, 30);
        jPanel1.setBorder(BorderFactory.createTitledBorder("Datos del paquete"));
        jPanel1.removeAll();
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        remove(jPanel1);
        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 112, 970, 247));

        agregar(jPanel1, etiqueta("Nombre*"), 25, 20, 170, 20);
        agregar(jPanel1, nombreTxt, 25, 43, 230, 35);
        agregar(jPanel1, etiqueta("Precio*"), 275, 20, 150, 20);
        agregar(jPanel1, precioTxt, 275, 43, 190, 35);
        agregar(jPanel1, etiqueta("Descripción*"), 25, 93, 190, 20);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        JScrollPane descripcionScroll = new JScrollPane(txtDescripcion);
        agregar(jPanel1, descripcionScroll, 25, 115, 230, 105);
        agregar(jPanel1, etiqueta("Productos del paquete*"), 275, 93, 220, 20);
        configurarTablaSeleccionados();
        agregar(jPanel1, new JScrollPane(tablaSeleccionados), 275, 115, 457, 105);
        lblCostoProductos.setFont(new Font("Instrument Sans", Font.PLAIN, 12));
        agregar(jPanel1, lblCostoProductos, 275, 222, 440, 18);

        for (JButton boton : new JButton[]{btnGuardar, btnModificar, btnEliminar, btnNuevo}) {
            aplicarEstiloVerde(boton);
        }
        agregar(jPanel1, btnGuardar, 785, 44, 155, 36);
        agregar(jPanel1, btnModificar, 785, 99, 155, 36);
        agregar(jPanel1, btnEliminar, 785, 154, 155, 36);
        btnModificar.setEnabled(false);
        btnEliminar.setEnabled(false);
        btnNuevo.addActionListener(e -> limpiar());
        agregar(jPanel1, btnNuevo, 785, 203, 155, 30);

        agregar(this, tituloSeccion("Productos"), 25, 365, 230, 34);
        agregar(this, txtBuscarProductos, 340, 368, 625, 34);
        new TextPrompt("Buscar productos...", txtBuscarProductos);
        configurarBusquedaProductos();
        configurarTablaProductos();
        agregar(this, new JScrollPane(tablaProductos), 25, 408, 940, 126);

        agregar(this, tituloSeccion("Paquetes activos"), 25, 543, 300, 30);
        agregar(this, txtBuscarPaquetes, 565, 544, 400, 31);
        new TextPrompt("Buscar paquete...", txtBuscarPaquetes);
        configurarTablaPaquetes();
        agregar(this, new JScrollPane(tablaPaquetes), 25, 580, 940, 112);
        txtBuscarPaquetes.getDocument().addDocumentListener(escuchar(this::filtrarPaquetes));
        tablaPaquetes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarPaqueteSeleccionado();
        });
        revalidate();
        repaint();
    }

    private void agregar(JPanel padre, Component componente, int x, int y, int w, int h) {
        padre.add(componente, new org.netbeans.lib.awtextra.AbsoluteConstraints(x,y,w,h));
    }

    // Mantiene los cuatro botones principales verdes incluso cuando Modificar
    // y Eliminar están deshabilitados hasta seleccionar un paquete.
    private void aplicarEstiloVerde(JButton boton) {
        boton.setUI(new BotonVerdeUI());
        boton.setFont(new Font("Instrument Sans", Font.BOLD, 12));
        boton.setForeground(Color.WHITE);
        boton.setBackground(VERDE);
        boton.setFocusPainted(false);
        boton.setContentAreaFilled(false);
        boton.setOpaque(false);
        boton.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private static class BotonVerdeUI extends javax.swing.plaf.basic.BasicButtonUI {
        @Override public void paint(Graphics g, JComponent c) {
            AbstractButton b = (AbstractButton) c;
            Graphics2D g2 = (Graphics2D) g.create();
            try {
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                Color fondo = !b.isEnabled() ? new Color(183, 209, 175)
                        : b.getModel().isPressed() ? VERDE_OSCURO : VERDE;
                g2.setColor(fondo);
                g2.fillRoundRect(0, 0, c.getWidth() - 1, c.getHeight() - 1, 9, 9);
            } finally {
                g2.dispose();
            }
            super.paint(g, c);
        }

        @Override protected void paintText(Graphics g, JComponent c,
                                           Rectangle textRect, String text) {
            AbstractButton b = (AbstractButton) c;
            g.setFont(b.getFont());
            g.setColor(Color.WHITE);
            FontMetrics fm = g.getFontMetrics();
            g.drawString(text, textRect.x, textRect.y + fm.getAscent());
        }
    }

    private JLabel etiqueta(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Instrument Sans", Font.PLAIN, 13));
        return lbl;
    }

    private JLabel tituloSeccion(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Instrument Sans", Font.PLAIN, 23));
        return lbl;
    }

    private static DocumentListener escuchar(Runnable accion) {
        return new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { accion.run(); }
            @Override public void removeUpdate(DocumentEvent e) { accion.run(); }
            @Override public void changedUpdate(DocumentEvent e) { accion.run(); }
        };
    }

    // Buscador reutilizando PnlSugerencias y PnlFilaSugerencia de Ventas.
    private void configurarBusquedaProductos() {
        listaSugerencias.setModel(modeloSugerencias);
        listaSugerencias.setCellRenderer(new PnlFilaSugerencia());
        listaSugerencias.setFixedCellHeight(42);
        listaSugerencias.setFocusable(false);
        popupProductos.add(pnlSugerencias);
        txtBuscarProductos.getDocument().addDocumentListener(escuchar(() -> {
            if (!seleccionandoSugerencia) SwingUtilities.invokeLater(this::mostrarSugerencias);
        }));
        txtBuscarProductos.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) { cargarProductos(); }
            @Override public void focusLost(FocusEvent e) {
                SwingUtilities.invokeLater(() -> {
                    if (!listaSugerencias.isShowing()) popupProductos.setVisible(false);
                });
            }
        });
        listaSugerencias.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int i = listaSugerencias.locationToIndex(e.getPoint());
                if (i >= 0 && i < modeloSugerencias.size()) elegirProducto(modeloSugerencias.get(i));
            }
        });
        txtBuscarProductos.addActionListener(e -> {
            Producto elegido = listaSugerencias.getSelectedValue();
            if (elegido == null && !modeloSugerencias.isEmpty()) elegido = modeloSugerencias.get(0);
            if (elegido != null) elegirProducto(elegido);
        });
        txtBuscarProductos.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,0), "abajo");
        txtBuscarProductos.getActionMap().put("abajo", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                int n = modeloSugerencias.size();
                if (n > 0) listaSugerencias.setSelectedIndex((listaSugerencias.getSelectedIndex()+1)%n);
            }
        });
        txtBuscarProductos.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0), "cerrar");
        txtBuscarProductos.getActionMap().put("cerrar", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { popupProductos.setVisible(false); }
        });
    }

    private void mostrarSugerencias() {
        if (!txtBuscarProductos.isShowing()) return;
        String q = txtBuscarProductos.getText().trim().toLowerCase(MX);
        modeloSugerencias.clear();
        if (!q.isEmpty()) {
            for (Producto p : productos) {
                if (p.getNombre() != null && p.getNombre().toLowerCase(MX).contains(q)) {
                    modeloSugerencias.addElement(p);
                    if (modeloSugerencias.size() == 7) break;
                }
            }
        }
        filtrarProductos();
        if (modeloSugerencias.isEmpty() || !txtBuscarProductos.hasFocus()) {
            popupProductos.setVisible(false);
            return;
        }
        popupProductos.setPopupSize(Math.max(380,txtBuscarProductos.getWidth()),
                Math.min(5,modeloSugerencias.size())*42+4);
        popupProductos.show(txtBuscarProductos,0,txtBuscarProductos.getHeight());
    }

    private void elegirProducto(Producto producto) {
        if (producto == null) return;
        popupProductos.setVisible(false);
        seleccionandoSugerencia = true;
        try { txtBuscarProductos.setText(""); }
        finally { seleccionandoSugerencia = false; }
        agregarProducto(producto);
        filtrarProductos();
        txtBuscarProductos.requestFocusInWindow();
    }

    private void cargarProductos() {
        try {
            List<Producto> nuevos = ServiceFacadeLocator.getInstanceFacadeProducto()
                    .obtenerProductosActualizados();
            productos.clear();
            for (Producto p : nuevos) {
                if (Boolean.TRUE.equals(p.getActivo())) productos.add(p);
            }
            filtrarProductos();
            actualizarSeleccionados();
        } catch (Exception ex) { error("No se pudieron cargar los productos", ex); }
    }

    private void configurarTablaProductos() {
        tablaProductos.setModel(new DefaultTableModel(new Object[]{"Nombre","Precio","Existencias"},0) {
            @Override public boolean isCellEditable(int r,int c) { return false; }
        });
        estiloTabla(tablaProductos,38);
        tablaProductos.getColumnModel().getColumn(0).setPreferredWidth(460);
        tablaProductos.getColumnModel().getColumn(1).setPreferredWidth(220);
        tablaProductos.getColumnModel().getColumn(2).setPreferredWidth(220);
        tablaProductos.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() != 2) return;
                int fila = tablaProductos.getSelectedRow();
                if (fila >= 0 && fila < productosFiltrados.size()) agregarProducto(productosFiltrados.get(fila));
            }
        });
    }

    private final List<Producto> productosFiltrados = new ArrayList<>();
    private void filtrarProductos() {
        if (tablaProductos.getColumnCount() == 0) return;
        String q = txtBuscarProductos.getText().trim().toLowerCase(MX);
        DefaultTableModel m = (DefaultTableModel) tablaProductos.getModel();
        m.setRowCount(0);
        productosFiltrados.clear();
        for (Producto p : productos) {
            if (p.getNombre() != null && p.getNombre().toLowerCase(MX).contains(q)) {
                productosFiltrados.add(p);
                m.addRow(new Object[]{p.getNombre(),moneda(p.getPrecio()),p.getExistencias()});
            }
        }
    }

    private void agregarProducto(Producto p) {
        int actual = cantidades.getOrDefault(p.getIdProducto(),0);
        if (p.getExistencias() == null || actual >= p.getExistencias()) {
            JOptionPane.showMessageDialog(this,"No hay suficientes existencias de " + p.getNombre());
            return;
        }
        cantidades.put(p.getIdProducto(),actual+1);
        actualizarSeleccionados();
    }

    // Tabla de cantidades reutilizando el patrón Renderer/Editor de Reabastecimiento.
    private void configurarTablaSeleccionados() {
        tablaSeleccionados.setModel(new DefaultTableModel(
                new Object[]{"Producto","Precio","Cantidad","Importe"},0) {
            @Override public boolean isCellEditable(int r,int c) { return c == 2; }
        });
        estiloTabla(tablaSeleccionados,38);
        tablaSeleccionados.getColumnModel().getColumn(0).setPreferredWidth(145);
        tablaSeleccionados.getColumnModel().getColumn(1).setPreferredWidth(78);
        tablaSeleccionados.getColumnModel().getColumn(2).setPreferredWidth(150);
        tablaSeleccionados.getColumnModel().getColumn(3).setPreferredWidth(84);
        tablaSeleccionados.getColumnModel().getColumn(2).setCellRenderer(new CantidadRenderer());
        tablaSeleccionados.getColumnModel().getColumn(2).setCellEditor(new CantidadEditor());
    }

    private final List<Producto> productosSeleccionados = new ArrayList<>();
    private void actualizarSeleccionados() {
        if (tablaSeleccionados.getColumnCount() == 0) return;
        if (tablaSeleccionados.isEditing()) tablaSeleccionados.getCellEditor().stopCellEditing();
        productosSeleccionados.clear();
        DefaultTableModel m = (DefaultTableModel) tablaSeleccionados.getModel();
        m.setRowCount(0);
        double costo = 0;
        for (Map.Entry<Integer,Integer> entrada : cantidades.entrySet()) {
            Producto p = buscarProducto(entrada.getKey());
            if (p == null) continue;
            int n = entrada.getValue();
            productosSeleccionados.add(p);
            double importe = p.getPrecio() * n;
            costo += importe;
            m.addRow(new Object[]{p.getNombre(),moneda(p.getPrecio()),n,moneda(importe)});
        }
        lblCostoProductos.setText("Costo de productos: " + moneda(costo));
    }

    private Producto buscarProducto(Integer id) {
        for (Producto p : productos) if (Objects.equals(p.getIdProducto(),id)) return p;
        return null;
    }

    // Botones compactos de la tabla. El guion ASCII evita que el signo menos
    // aparezca truncado como "..." con ciertos temas de Swing.
    private static JButton botonCantidad(String texto) {
        JButton b = new JButton(texto);
        b.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        b.setFont(new Font("Dialog", Font.BOLD, 17));
        b.setPreferredSize(new Dimension(34, 27));
        b.setMinimumSize(new Dimension(34, 27));
        b.setMargin(new Insets(0, 0, 0, 0));
        b.setBackground(new Color(209, 230, 202));
        b.setForeground(VERDE_OSCURO);
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createLineBorder(new Color(155, 192, 146)));
        return b;
    }

    private class CantidadRenderer extends JPanel implements TableCellRenderer {
        private final JLabel cantidad = new JLabel("0",SwingConstants.CENTER);
        CantidadRenderer() {
            super(new FlowLayout(FlowLayout.CENTER, 2, 3));
            add(botonCantidad("-"));
            cantidad.setPreferredSize(new Dimension(23,26));
            add(cantidad);
            add(botonCantidad("+"));
        }
        @Override public Component getTableCellRendererComponent(JTable t,Object v,boolean sel,boolean focus,int r,int c) {
            cantidad.setText(String.valueOf(v));
            setBackground(sel ? new Color(232,240,229) : Color.WHITE);
            return this;
        }
    }

    private class CantidadEditor extends AbstractCellEditor implements TableCellEditor {
        private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 3));
        private final JLabel cantidad = new JLabel("0",SwingConstants.CENTER);
        private int fila;
        private int valor;
        CantidadEditor() {
            JButton menos = botonCantidad("-"), mas = botonCantidad("+");
            cantidad.setPreferredSize(new Dimension(23,26));
            panel.add(menos); panel.add(cantidad); panel.add(mas);
            menos.addActionListener(e -> cambiar(-1));
            mas.addActionListener(e -> cambiar(1));
        }
        private void cambiar(int delta) {
            if (fila < 0 || fila >= productosSeleccionados.size()) return;
            Producto p = productosSeleccionados.get(fila);
            int nuevo = valor + delta;
            if (nuevo > (p.getExistencias() == null ? 0 : p.getExistencias())) {
                JOptionPane.showMessageDialog(Paquetes.this,"No hay suficientes existencias.");
                return;
            }
            if (nuevo < 0) return;
            valor = nuevo;
            cantidad.setText(String.valueOf(nuevo));
            if (nuevo == 0) cantidades.remove(p.getIdProducto());
            else cantidades.put(p.getIdProducto(),nuevo);
            fireEditingStopped();
            SwingUtilities.invokeLater(Paquetes.this::actualizarSeleccionados);
        }
        @Override public Component getTableCellEditorComponent(JTable t,Object v,boolean sel,int r,int c) {
            fila = t.convertRowIndexToModel(r);
            valor = v instanceof Number ? ((Number)v).intValue() : 0;
            cantidad.setText(String.valueOf(valor));
            return panel;
        }
        @Override public Object getCellEditorValue() { return valor; }
    }

    // Tabla de consultas para paquetes registrados.
    private void configurarTablaPaquetes() {
        tablaPaquetes.setModel(new DefaultTableModel(
                new Object[]{"ID","Nombre","Precio","Descripción","Estado"},0) {
            @Override public boolean isCellEditable(int r,int c) { return false; }
        });
        estiloTabla(tablaPaquetes,34);
        int[] anchos = {55,225,115,400,125};
        for (int i=0;i<anchos.length;i++) tablaPaquetes.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        tablaPaquetes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaPaquetes.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t,Object v,boolean sel,boolean focus,int r,int c) {
                JLabel celda = (JLabel) super.getTableCellRendererComponent(t,v,sel,focus,r,c);
                celda.setHorizontalAlignment(SwingConstants.CENTER);
                if (!sel) {
                    boolean activo = "Activo".equals(String.valueOf(v));
                    celda.setBackground(activo ? new Color(204,233,204) : new Color(255,224,178));
                    celda.setForeground(activo ? new Color(40,110,40) : new Color(130,75,0));
                }
                return celda;
            }
        });
    }

    private void estiloTabla(JTable t,int alto) {
        t.setRowHeight(alto);
        t.setShowVerticalLines(false);
        t.setGridColor(GRIS);
        t.setFillsViewportHeight(true);
        t.getTableHeader().setReorderingAllowed(false);
    }

    private void cargarPaquetes() {
        try {
            paquetes.clear();
            paquetes.addAll(ServiceFacadeLocator.getInstanceFacadePaquete().obtenerPaquetes());
            filtrarPaquetes();
        } catch (Exception ex) { error("No se pudieron consultar los paquetes",ex); }
    }

    private void filtrarPaquetes() {
        if (tablaPaquetes.getColumnCount() == 0) return;
        String q = txtBuscarPaquetes.getText().trim().toLowerCase(MX);
        DefaultTableModel m = (DefaultTableModel) tablaPaquetes.getModel();
        m.setRowCount(0);
        for (Paquete p : paquetes) {
            if (!Boolean.TRUE.equals(p.getActiva())) continue;
            if (!p.getNombrePaquete().toLowerCase(MX).contains(q)
                    && !String.valueOf(p.getIdPaquete()).contains(q)) continue;
            m.addRow(new Object[]{p.getIdPaquete(), p.getNombrePaquete(),
                    moneda(p.getPrecio()), p.getDescripcion(), "Activo"});
        }
    }

    private void cargarPaqueteSeleccionado() {
        int fila = tablaPaquetes.getSelectedRow();
        if (fila < 0) return;
        Integer id = (Integer) tablaPaquetes.getValueAt(fila,0);
        Paquete seleccionado = null;
        for (Paquete p : paquetes) if (Objects.equals(p.getIdPaquete(),id)) seleccionado = p;
        if (seleccionado == null) return;
        paqueteSeleccionado = id;
        nombreTxt.setText(seleccionado.getNombrePaquete());
        precioTxt.setText(String.format(java.util.Locale.US,"%.2f",seleccionado.getPrecio()));
        txtDescripcion.setText(seleccionado.getDescripcion());
        cantidades.clear();
        try {
            for (ProductoPaquete pp : ServiceFacadeLocator.getInstanceFacadePaquete().obtenerProductosPaquete(id)) {
                cantidades.put(pp.getProducto().getIdProducto(),pp.getCantidad());
            }
        } catch (Exception ex) { error("No se pudieron cargar los productos del paquete",ex); }
        actualizarSeleccionados();
        btnModificar.setEnabled(true);
        btnEliminar.setEnabled(true);
    }

    private Paquete leerFormulario() {
        String nombre = nombreTxt.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        double precio;
        try { precio = Double.parseDouble(precioTxt.getText().trim().replace(',','.')); }
        catch (NumberFormatException ex) { throw new IllegalArgumentException("Escribe un precio válido."); }
        Paquete p = new Paquete();
        p.setIdPaquete(paqueteSeleccionado);
        p.setNombrePaquete(nombre);
        p.setPrecio(precio);
        p.setDescripcion(descripcion);
        p.setActiva(true);
        return p;
    }

    private void guardar() {
        try {
            if (paqueteSeleccionado != null) {
                JOptionPane.showMessageDialog(this,"Hay un paquete seleccionado. Usa Modificar o limpia el formulario.");
                return;
            }
            ServiceFacadeLocator.getInstanceFacadePaquete().guardarPaquete(leerFormulario(),new LinkedHashMap<>(cantidades));
            JOptionPane.showMessageDialog(this,"Paquete guardado correctamente.");
            limpiar(); cargarPaquetes();
        } catch (IllegalArgumentException ex) { aviso(ex.getMessage()); }
        catch (Exception ex) { error("No se pudo guardar el paquete",ex); }
    }

    private void modificar() {
        if (paqueteSeleccionado == null) { aviso("Selecciona un paquete de la tabla."); return; }
        try {
            ServiceFacadeLocator.getInstanceFacadePaquete().modificarPaquete(leerFormulario(),new LinkedHashMap<>(cantidades));
            JOptionPane.showMessageDialog(this,"Paquete modificado correctamente.");
            limpiar(); cargarPaquetes();
        } catch (IllegalArgumentException ex) { aviso(ex.getMessage()); }
        catch (Exception ex) { error("No se pudo modificar el paquete",ex); }
    }

    private void eliminar() {
        if (paqueteSeleccionado == null) { aviso("Selecciona un paquete."); return; }
        if (JOptionPane.showConfirmDialog(this,"¿Desactivar el paquete seleccionado?", "Confirmar",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        try {
            ServiceFacadeLocator.getInstanceFacadePaquete().eliminarPaquete(paqueteSeleccionado);
            limpiar(); cargarPaquetes();
        } catch (Exception ex) { error("No se pudo desactivar el paquete",ex); }
    }

    private void limpiar() {
        paqueteSeleccionado = null;
        nombreTxt.setText(""); precioTxt.setText(""); txtDescripcion.setText("");
        cantidades.clear(); actualizarSeleccionados();
        tablaPaquetes.clearSelection();
        btnModificar.setEnabled(false); btnEliminar.setEnabled(false);
    }

    private String moneda(double valor) {
        return java.text.NumberFormat.getCurrencyInstance(MX).format(valor);
    }
    private void aviso(String msg) { JOptionPane.showMessageDialog(this,msg,"Revisa los datos",JOptionPane.WARNING_MESSAGE); }
    private void error(String msg,Throwable ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this,msg + ": " + ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
    }

    // Eventos del formulario existente (NetBeans).
    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) { guardar(); }
    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) { modificar(); }
    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) { eliminar(); }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        lblSubtitulo = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();
        precioTxt = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        nombreTxt = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        btnModificar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setMinimumSize(new java.awt.Dimension(1010, 710));
        setPreferredSize(new java.awt.Dimension(1010, 710));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jSeparator1.setBackground(new java.awt.Color(136, 180, 123));
        jSeparator1.setForeground(new java.awt.Color(136, 180, 123));
        add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 1010, 2));

        jLabel2.setBackground(new java.awt.Color(136, 180, 123));
        jLabel2.setFont(new java.awt.Font("Instrument Sans", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(136, 180, 123));
        jLabel2.setText("Inventario: Paquetes");
        add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 15, 500, 35));

        lblSubtitulo.setFont(new java.awt.Font("Instrument Sans", 0, 24)); // NOI18N
        lblSubtitulo.setText("Registrar un nuevo Servicio");
        add(lblSubtitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 85, 500, 30));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos del servicio", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Instrument Sans", 0, 16))); // NOI18N
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnGuardar.setBackground(new java.awt.Color(136, 180, 123));
        btnGuardar.setFont(new java.awt.Font("Instrument Sans", 1, 12)); // NOI18N
        btnGuardar.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardar.setText("Guardar");
        btnGuardar.setMnemonic('G');
        btnGuardar.setFocusPainted(false);
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        jPanel1.add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 70, 140, 36));
        jPanel1.add(precioTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(395, 58, 310, 35));

        jLabel5.setFont(new java.awt.Font("Instrument Sans", 0, 14)); // NOI18N
        jLabel5.setText("Precio *");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(395, 35, 150, 20));
        jPanel1.add(nombreTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(35, 60, 170, 35));

        jLabel6.setFont(new java.awt.Font("Instrument Sans", 0, 14)); // NOI18N
        jLabel6.setText("Nombre del Paquete*");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(35, 35, 200, 20));

        btnModificar.setBackground(new java.awt.Color(136, 180, 123));
        btnModificar.setFont(new java.awt.Font("Instrument Sans", 1, 12)); // NOI18N
        btnModificar.setForeground(new java.awt.Color(255, 255, 255));
        btnModificar.setText("Modificar");
        btnModificar.setMnemonic('M');
        btnModificar.setFocusPainted(false);
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });
        jPanel1.add(btnModificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 120, 140, 36));

        btnEliminar.setBackground(new java.awt.Color(136, 180, 123));
        btnEliminar.setFont(new java.awt.Font("Instrument Sans", 1, 12)); // NOI18N
        btnEliminar.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminar.setText("Eliminar");
        btnEliminar.setMnemonic('N');
        btnEliminar.setFocusPainted(false);
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });
        jPanel1.add(btnEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 170, 140, 36));

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 990, 260));
        jPanel1.getAccessibleContext().setAccessibleName("Datos del paquete");
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblSubtitulo;
    private javax.swing.JTextField nombreTxt;
    private javax.swing.JTextField precioTxt;
    // End of variables declaration//GEN-END:variables
}
