package mx.desarrollo.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import mx.desarrollo.entity.Producto;
import mx.desarrollo.facade.FacadeProducto;
import mx.desarrollo.integration.ServiceFacadeLocator;

public class reabastecimientoPanel1 extends javax.swing.JPanel {

    private List<Producto> listaProductos = new ArrayList<>();
    private List<Producto> productosVisibles = new ArrayList<>();
    private final Map<Integer, Integer> cantidades = new HashMap<>();

    public reabastecimientoPanel1() {
        initComponents();
        configurarBusqueda();
        configurarTabla();
        configurarTamanos();
        cargarProductos();
        jButton1.addActionListener(e -> guardarReabastecimiento());
    }

    private mx.desarrollo.entity.Usuario usuarioActual;

    public reabastecimientoPanel1(mx.desarrollo.entity.Usuario usuario) {
        this();
        this.usuarioActual = usuario;
    }

    private void registrarHistorial(Producto p, String detalles) {
        try {
            ServiceFacadeLocator.getInstanceFacadeHistorial().registrar(usuarioActual, p, detalles);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    private void configurarBusqueda() {
        TextPrompt prompt = new TextPrompt("Buscar productos...", jTextField1);
        prompt.changeAlpha(0.75f);
        prompt.changeStyle(Font.PLAIN);
    }

    private void configurarTabla() {
        // Modelo nuevo: solo la columna Agregar permite interacción.
        DefaultTableModel modelo = new DefaultTableModel(
                new Object[]{"Nombre", "Existencias", "Agregar", "Resultados"}, 0) {
            @Override
            public Class<?> getColumnClass(int columna) {
                return columna == 2 ? Integer.class : Object.class;
            }

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return columna == 2;
            }
        };
        tablaProductos.setModel(modelo);
        tablaProductos.setShowVerticalLines(false);
        tablaProductos.setShowHorizontalLines(true);
        tablaProductos.setGridColor(new Color(220, 220, 220));
        tablaProductos.setRowHeight(48);
        tablaProductos.setRowSelectionAllowed(false);
        tablaProductos.setFocusable(false);
        tablaProductos.getTableHeader().setReorderingAllowed(false);

        tablaProductos.getColumnModel().getColumn(0).setPreferredWidth(350);
        tablaProductos.getColumnModel().getColumn(1).setPreferredWidth(145);
        tablaProductos.getColumnModel().getColumn(2).setPreferredWidth(250);
        tablaProductos.getColumnModel().getColumn(3).setPreferredWidth(145);

        tablaProductos.getColumnModel().getColumn(2).setCellRenderer(new BotonesRenderer());
        tablaProductos.getColumnModel().getColumn(2).setCellEditor(new BotonesEditor());
    }

    private void configurarTamanos() {
        // Ajusta el ancho de la tabla y la barra verde al ancho disponible.
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                ajustarComponentes();
            }
        });
    }

    private void ajustarComponentes() {
        int ancho = getWidth();
        int alto = getHeight();
        if (ancho <= 0 || alto <= 0) return;

        int anchoInterior = Math.max(300, ancho - 50);
        jSeparator1.setBounds(0, 60, ancho, 1);
        jLabel1.setBounds(25, 290, anchoInterior, 42);
        jScrollPane1.setBounds(25, 345, anchoInterior, Math.max(160, alto - 425));
        jButton1.setBounds(Math.max(25, ancho - 190), Math.max(645, alto - 65), 165, 45);
        repaint();
    }

    // Dibujamos los simbolos como iconos para evitar que Swing muestre "...".
    private JButton crearBoton(String texto, boolean verde) {
        JButton boton = new JButton();
        boton.setText(null);
        boton.setPreferredSize(new Dimension(38, 30));
        boton.setMinimumSize(new Dimension(38, 30));
        boton.setMargin(new Insets(0, 0, 0, 0));
        boton.setFocusPainted(false);
        boton.setBackground(verde ? new Color(98, 158, 113) : new Color(235, 235, 235));
        boton.setIcon(new javax.swing.Icon() {
            @Override
            public int getIconWidth() { return 16; }

            @Override
            public int getIconHeight() { return 16; }

            @Override
            public void paintIcon(Component componente, java.awt.Graphics grafico, int x, int y) {
                java.awt.Graphics2D g = (java.awt.Graphics2D) grafico.create();
                try {
                    g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
                            java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                    g.setColor(verde ? Color.WHITE : Color.BLACK);
                    g.setStroke(new java.awt.BasicStroke(2.2f, java.awt.BasicStroke.CAP_ROUND,
                            java.awt.BasicStroke.JOIN_ROUND));
                    g.drawLine(x + 3, y + 8, x + 13, y + 8);
                    if (texto.equals("+")) {
                        g.drawLine(x + 8, y + 3, x + 8, y + 13);
                    }
                } finally {
                    g.dispose();
                }
            }
        });
        return boton;
    }

    private class BotonesRenderer extends JPanel implements TableCellRenderer {
        private final JLabel cantidad = new JLabel("0", SwingConstants.CENTER);

        BotonesRenderer() {
            super(new FlowLayout(FlowLayout.CENTER, 8, 8));
            JButton menos = crearBoton("−", false);
            JButton mas = crearBoton("+", true);
            cantidad.setPreferredSize(new Dimension(34, 30));
            add(menos);
            add(cantidad);
            add(mas);
        }

        @Override
        public Component getTableCellRendererComponent(JTable tabla, Object valor,
                                                       boolean seleccionado, boolean foco, int fila, int columna) {
            cantidad.setText(String.valueOf(valor == null ? 0 : valor));
            setBackground(fila % 2 == 0 ? Color.WHITE : new Color(242, 242, 242));
            return this;
        }
    }

    private class BotonesEditor extends AbstractCellEditor implements TableCellEditor {
        private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        private final JLabel cantidadLabel = new JLabel("0", SwingConstants.CENTER);
        private int cantidadActual;
        private int filaModelo;

        BotonesEditor() {
            JButton menos = crearBoton("−", false);
            JButton mas = crearBoton("+", true);
            cantidadLabel.setPreferredSize(new Dimension(34, 30));
            panel.add(menos);
            panel.add(cantidadLabel);
            panel.add(mas);
            menos.addActionListener(e -> cambiarCantidad(-1));
            mas.addActionListener(e -> cambiarCantidad(1));
        }

        private void cambiarCantidad(int cambio) {
            if (filaModelo < 0 || filaModelo >= productosVisibles.size()) return;
            int nuevaCantidad = Math.max(0, cantidadActual + cambio);
            if (nuevaCantidad == cantidadActual) return;
            cantidadActual = nuevaCantidad;
            cantidadLabel.setText(String.valueOf(cantidadActual));

            Producto producto = productosVisibles.get(filaModelo);
            cantidades.put(producto.getIdProducto(), cantidadActual);

            DefaultTableModel modelo = (DefaultTableModel) tablaProductos.getModel();
            int existencias = producto.getExistencias();
            modelo.setValueAt(existencias + cantidadActual, filaModelo, 3);
            fireEditingStopped(); // Guarda el nuevo valor en la columna Agregar.
        }

        @Override
        public Component getTableCellEditorComponent(JTable tabla, Object valor,
                                                     boolean seleccionado, int fila, int columna) {
            filaModelo = tabla.convertRowIndexToModel(fila);
            cantidadActual = valor instanceof Number ? ((Number) valor).intValue() : 0;
            cantidadLabel.setText(String.valueOf(cantidadActual));
            panel.setBackground(fila % 2 == 0 ? Color.WHITE : new Color(242, 242, 242));
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return cantidadActual;
        }
    }

    private void cargarProductos() {
        try {
            FacadeProducto facade = ServiceFacadeLocator.getInstanceFacadeProducto();
            listaProductos = new ArrayList<>(facade.obtenerProductos());
            aplicarFiltros();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "No se pudieron cargar los productos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aplicarFiltros() {
        if (tablaProductos.isEditing()) {
            tablaProductos.getCellEditor().stopCellEditing();
        }
        String texto = jTextField1.getText().trim().toLowerCase();
        List<Producto> vista = new ArrayList<>();
        for (Producto p : listaProductos) {
            if (p.getNombre() != null && p.getNombre().toLowerCase().contains(texto)) {
                vista.add(p);
            }
        }

        switch (jComboBox1.getSelectedIndex()) {
            case 0: vista.sort(Comparator.comparing(p -> p.getNombre().toLowerCase())); break;
            case 1: vista.sort(Comparator.comparing((Producto p) -> p.getNombre().toLowerCase()).reversed()); break;
            case 2: vista.sort(Comparator.comparing(Producto::getPrecio)); break;
            case 3: vista.sort(Comparator.comparing(Producto::getPrecio).reversed()); break;
            case 4: vista.sort(Comparator.comparing(Producto::getExistencias)); break;
            case 5: vista.sort(Comparator.comparing(Producto::getExistencias).reversed()); break;
            default: break;
        }

        productosVisibles = vista;
        DefaultTableModel modelo = (DefaultTableModel) tablaProductos.getModel();
        modelo.setRowCount(0);
        for (Producto p : vista) {
            int cantidad = cantidades.getOrDefault(p.getIdProducto(), 0);
            modelo.addRow(new Object[]{p.getNombre(), p.getExistencias(), cantidad,
                    p.getExistencias() + cantidad});
        }
    }

    private void guardarReabastecimiento() {

        if (tablaProductos.isEditing()) {
            tablaProductos.getCellEditor().stopCellEditing();
        }

        if (cantidades.isEmpty() ||
                cantidades.values().stream().allMatch(c -> c == 0)) {

            JOptionPane.showMessageDialog(this,
                    "No hay productos para reabastecer.");
            return;
        }

        int confirmar = JOptionPane.showConfirmDialog(this,
                "¿Deseas guardar las nuevas existencias?",
                "Confirmar reabastecimiento",
                JOptionPane.YES_NO_OPTION);

        if (confirmar != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            FacadeProducto facade =
                    ServiceFacadeLocator.getInstanceFacadeProducto();

            int actualizados = 0;

            for (Producto producto : facade.obtenerProductosActualizados()) {

                int cantidad = cantidades.getOrDefault(
                        producto.getIdProducto(), 0);

                if (cantidad > 0) {

                    int existenciasAntes = producto.getExistencias();

                    int nuevasExistencias = Math.addExact(
                            producto.getExistencias(), cantidad);

                    producto.setExistencias(nuevasExistencias);

                    facade.modificarProducto(producto);

                    registrarHistorial(producto,
                            "Existencias: " + existenciasAntes + " → " + nuevasExistencias + " (reabastecimiento)");

                    actualizados++;
                }
            }

            cantidades.clear();
            cargarProductos();

            JOptionPane.showMessageDialog(this,
                    "Reabastecimiento guardado correctamente.\n"
                            + "Productos actualizados: " + actualizados);

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this,
                    "Error al guardar: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaProductos = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jSeparator1.setBackground(new java.awt.Color(136, 180, 123));
        jSeparator1.setForeground(new java.awt.Color(136, 180, 123));
        add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 1010, 20));

        jLabel2.setBackground(new java.awt.Color(136, 180, 123));
        jLabel2.setFont(new java.awt.Font("Instrument Sans", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(136, 180, 123));
        jLabel2.setText("Inventario: Reabastecimiento");
        add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 15, 600, 35));

        jLabel6.setFont(new java.awt.Font("Instrument Sans", 0, 13)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(94, 138, 84));
        jLabel6.setText("Ordenar por");
        add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 148, 150, 18));

        jScrollPane1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(170, 170, 170), 1, true));
        tablaProductos.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{}, new String[]{"Nombre", "Existencias", "Agregar", "Resultados"}) {
            Class[] types = new Class[]{Object.class, Object.class, Integer.class, Object.class};
            boolean[] canEdit = new boolean[]{false, false, false, false};
            public Class getColumnClass(int columnIndex) { return types[columnIndex]; }
            public boolean isCellEditable(int rowIndex, int columnIndex) { return canEdit[columnIndex]; }
        });
        tablaProductos.setFocusable(false);
        tablaProductos.setName(""); // NOI18N
        tablaProductos.setRowHeight(52);
        tablaProductos.setRowSelectionAllowed(false);
        tablaProductos.setShowHorizontalLines(true);
        jScrollPane1.setViewportView(tablaProductos);
        tablaProductos.getAccessibleContext().setAccessibleName("");
        tablaProductos.getAccessibleContext().setAccessibleDescription("");
        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 345, 960, 285));

        jLabel7.setBackground(new java.awt.Color(136, 180, 123));
        jLabel7.setFont(new java.awt.Font("Instrument Sans", 0, 22)); // NOI18N
        jLabel7.setText("Reabastecer productos");
        add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 78, 450, 35));

        jComboBox1.setBackground(new java.awt.Color(217, 217, 217));
        jComboBox1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Nombre (A-Z)", "Nombre (Z-A)", "Precio (Menor a mayor)", "Precio (Mayor a menor)", "Existencias( Menor a mayor)", "Existencias (Mayor a menor)"}));
        jComboBox1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        jComboBox1.setName("listaorden"); // NOI18N
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 172, 460, 38));

        jTextField1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTextField1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(170, 170, 170), 1, true));
        jTextField1.setName("txtBuscar"); // NOI18N
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });
        add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 225, 460, 42));

        jLabel1.setBackground(new java.awt.Color(180, 210, 181));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel1.setText("Productos Registrados");
        jLabel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(230, 225, 150)));
        jLabel1.setName("lblAlerta"); // NOI18N
        jLabel1.setOpaque(true);
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 290, 960, 42));

        jLabel8.setBackground(new java.awt.Color(136, 180, 123));
        jLabel8.setFont(new java.awt.Font("Instrument Sans", 0, 16)); // NOI18N
        jLabel8.setText("Filtrar por:");
        add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 120, 150, 22));

        jButton1.setBackground(new java.awt.Color(98, 158, 113));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Guardar");
        add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 645, 165, 45));
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        aplicarFiltros();
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        aplicarFiltros();
    }//GEN-LAST:event_jComboBox1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTable tablaProductos;
    // End of variables declaration//GEN-END:variables
}
