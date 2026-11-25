package mx.desarrollo.ui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;
import java.time.format.DateTimeFormatter;

import mx.desarrollo.entity.Producto;
import mx.desarrollo.entity.Descuento;
import mx.desarrollo.integration.ServiceFacadeLocator;

import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import javax.swing.DefaultListCellRenderer;


public class Descuentos extends JPanel {

    private static final Color VERDE = new Color(147, 181, 132);
    private static final Color VERDE_OSCURO = new Color(105, 137, 92);
    private static final Color VERDE_CLARO = new Color(231, 239, 226);
    private static final Color BORDE = new Color(180, 180, 180);
    private static final Color BLANCO = Color.WHITE;
    private static final Color GRIS_PLACEHOLDER = new Color(140, 140, 140);

    private JComboBox<String> cbProducto;
    private JTextField txtVigencia;
    private JTextField txtPorcentaje;

    private JButton btnGuardar;
    private JButton btnModificar;
    private JButton btnEliminar;
    private JButton btnCalendario;

    private JTable tablaDescuentos;
    private DefaultTableModel modeloTabla;

    private Integer idDescuentoSeleccionado = null;

    private List<Producto> productosDisponibles =
            new ArrayList<>();

    private List<Descuento> descuentosRegistrados =
            new ArrayList<>();

    private final DateTimeFormatter formatoFecha =
            DateTimeFormatter.ofPattern("dd-MMM-yyyy", new Locale("es", "MX"));


    public Descuentos() {
        setBackground(BLANCO);
        setLayout(new BorderLayout());

        inicializarComponentes();
        configurarEventos();
        cargarProductos();
        cargarDescuentos();
    }


    private void inicializarComponentes() {

        JPanel contenido = new JPanel();
        contenido.setBackground(BLANCO);
        contenido.setLayout(new BorderLayout(0, 25));
        contenido.setBorder(BorderFactory.createEmptyBorder(25, 20, 30, 30));


        JLabel titulo = new JLabel("Descuentos");
        titulo.setFont(new Font("Instrument Sans", Font.PLAIN, 30));
        titulo.setForeground(VERDE_OSCURO);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 10, 18, 0));

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(BLANCO);
        encabezado.add(titulo, BorderLayout.CENTER);

        JPanel linea = new JPanel();
        linea.setBackground(VERDE);
        linea.setPreferredSize(new Dimension(0, 1));
        encabezado.add(linea, BorderLayout.SOUTH);

        contenido.add(encabezado, BorderLayout.NORTH);


        JPanel centro = new JPanel();
        centro.setBackground(BLANCO);
        centro.setLayout(new BorderLayout(0, 25));


        JLabel lblAsignar = new JLabel("Asignar descuento");
        lblAsignar.setFont(new Font("Instrument Sans", Font.PLAIN, 28)
        );
        lblAsignar.setForeground(Color.BLACK);


        JPanel formulario = new JPanel(new BorderLayout());
        formulario.setBackground(BLANCO);
        formulario.setBorder(BorderFactory.createCompoundBorder(new LineBorder(BORDE, 1, true), BorderFactory.createEmptyBorder(15, 12, 15, 12)
        ));
        formulario.setPreferredSize(new Dimension(0, 210));

        JPanel campos = new JPanel(new GridLayout(1, 3, 25, 0));
        campos.setBackground(BLANCO);

        JPanel panelProducto = crearCampo("Seleccionar producto");
        cbProducto = new JComboBox<>();
        cbProducto.setFont(new Font("Instrument Sans", Font.PLAIN, 14));
        cbProducto.setBackground(BLANCO);
        cbProducto.setPreferredSize(new Dimension(180, 36));
        cbProducto.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value == null) {
                    setText("Ej. Lapiz");
                    setForeground(GRIS_PLACEHOLDER);
                }
                return c;
            }
        });
        panelProducto.add(cbProducto, BorderLayout.CENTER);

        JPanel panelVigencia = crearCampo("Vigencia:");
        txtVigencia = new JTextField();
        txtVigencia.setFont(new Font("Instrument Sans", Font.PLAIN, 14)
        );
        txtVigencia.setToolTipText("Formato: dd-MMM-yyyy (ej. 30-nov-2026)");
        txtVigencia.setPreferredSize(new Dimension(180, 36));
        TextPrompt promptVigencia = new TextPrompt("Ej. 15-oct-2026", txtVigencia);
        promptVigencia.setForeground(GRIS_PLACEHOLDER);

        btnCalendario = new JButton(new IconoCalendario());
        btnCalendario.setToolTipText("Elegir fecha (Alt+F)");
        btnCalendario.setMnemonic('F');
        btnCalendario.setBorderPainted(false);
        btnCalendario.setContentAreaFilled(false);
        btnCalendario.setFocusPainted(false);
        btnCalendario.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCalendario.setPreferredSize(new Dimension(36, 30));
        txtVigencia.add(btnCalendario, BorderLayout.EAST);
        panelVigencia.add(txtVigencia, BorderLayout.CENTER);


        JPanel panelPorcentaje = crearCampo("Descuento (%):");
        txtPorcentaje = new JTextField();
        txtPorcentaje.setFont(new Font("Instrument Sans", Font.PLAIN, 14));
        txtPorcentaje.setToolTipText("Ingresa un porcentaje entre 1 y 100");
        txtPorcentaje.setPreferredSize(new Dimension(180, 36));
        TextPrompt promptPorcentaje = new TextPrompt("Ej. 5%", txtPorcentaje);
        promptPorcentaje.setForeground(GRIS_PLACEHOLDER);
        panelPorcentaje.add(txtPorcentaje, BorderLayout.CENTER);

        campos.add(panelProducto);
        campos.add(panelVigencia);
        campos.add(panelPorcentaje);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        botones.setBackground(BLANCO);
        botones.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));

        btnGuardar = crearBoton("Guardar", new IconoGuardar());
        btnModificar = crearBoton("Modificar", null);
        btnEliminar = crearBoton("Eliminar", null);
        btnGuardar.setMnemonic('G');
        btnModificar.setMnemonic('M');
        btnEliminar.setMnemonic('N');

        botones.add(btnGuardar);
        botones.add(Box.createHorizontalStrut(30));
        botones.add(btnModificar);
        botones.add(Box.createHorizontalStrut(30));
        botones.add(btnEliminar);


        JPanel superior = new JPanel(new BorderLayout(0, 0));
        superior.setBackground(BLANCO);
        superior.add(campos, BorderLayout.NORTH);
        superior.add(botones, BorderLayout.CENTER);

        formulario.add(superior, BorderLayout.NORTH);

        JPanel seccionFormulario = new JPanel(
                new BorderLayout(0, 30)
        );
        seccionFormulario.setBackground(BLANCO);
        seccionFormulario.add(lblAsignar, BorderLayout.NORTH);
        seccionFormulario.add(formulario, BorderLayout.CENTER);

        centro.add(seccionFormulario, BorderLayout.NORTH);


        JPanel seccionTabla = new JPanel(new BorderLayout(0, 0));
        seccionTabla.setBackground(BLANCO);

        JLabel lblRegistrados = new JLabel("Descuentos Registrados");
        lblRegistrados.setFont(
                new Font("Instrument Sans", Font.PLAIN, 20)
        );
        lblRegistrados.setOpaque(true);
        lblRegistrados.setBackground(VERDE_CLARO);
        lblRegistrados.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(BORDE, 1, true), BorderFactory.createEmptyBorder(16, 15, 16, 15)
                )
        );

        String[] columnas = {
                "ID producto",
                "Nombre",
                "ID descuento",
                "Porcentaje",
                "Vigencia",
                "Precio Normal",
                "Precio Especial"
        };

        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaDescuentos = new JTable(modeloTabla);
        tablaDescuentos.setFont(
                new Font("Instrument Sans", Font.PLAIN, 14)
        );
        tablaDescuentos.setRowHeight(52);
        tablaDescuentos.setGridColor(
                new Color(220, 220, 220)
        );
        tablaDescuentos.setShowHorizontalLines(true);
        tablaDescuentos.setShowVerticalLines(false);
        tablaDescuentos.setIntercellSpacing(new Dimension(0, 1));
        tablaDescuentos.setSelectionBackground(VERDE_CLARO);
        tablaDescuentos.setSelectionForeground(Color.BLACK);
        tablaDescuentos.setFillsViewportHeight(true);

        tablaDescuentos.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tabla, Object valor, boolean seleccionada,
                                                           boolean enfocada, int fila, int columna) {
                JLabel etiqueta = (JLabel) super.getTableCellRendererComponent(
                        tabla, valor, seleccionada, false, fila, columna);
                etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
                etiqueta.setBackground(seleccionada ? VERDE_CLARO : BLANCO);
                etiqueta.setForeground(Color.BLACK);
                etiqueta.setBorder(null);
                if (valor instanceof Icon) {
                    etiqueta.setText("");
                    etiqueta.setIcon((Icon) valor);
                } else {
                    etiqueta.setIcon(null);
                }
                return etiqueta;
            }
        });
        tablaDescuentos.getColumnModel().getColumn(0).setMaxWidth(110);
        JTableHeader header = tablaDescuentos.getTableHeader();
        header.setReorderingAllowed(false);
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tabla, Object valor, boolean seleccionada,
                                                           boolean enfocada, int fila, int columna) {
                JLabel etiqueta = (JLabel) super.getTableCellRendererComponent(
                        tabla, valor, false, false, fila, columna);
                etiqueta.setOpaque(true);
                etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
                etiqueta.setFont(new Font("Instrument Sans", Font.PLAIN, 13));
                etiqueta.setBackground(BLANCO);
                etiqueta.setForeground(new Color(115, 115, 115));
                etiqueta.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDE));
                return etiqueta;
            }
        });
        header.setPreferredSize(new Dimension(0, 42));

        JScrollPane scroll = new JScrollPane(tablaDescuentos);
        scroll.setBorder(new LineBorder(BORDE, 1, true));
        scroll.getViewport().setBackground(BLANCO);

        seccionTabla.add(lblRegistrados, BorderLayout.NORTH);
        seccionTabla.add(scroll, BorderLayout.CENTER);

        centro.add(seccionTabla, BorderLayout.CENTER);

        contenido.add(centro, BorderLayout.CENTER);
        add(contenido, BorderLayout.CENTER);

        btnGuardar.setEnabled(true);
        btnModificar.setEnabled(false);
        btnEliminar.setEnabled(false);
    }

    private JPanel crearCampo(String titulo) {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(BLANCO);

        JLabel label = new JLabel(titulo);
        label.setFont(
                new Font("Instrument Sans", Font.PLAIN, 14)
        );
        label.setForeground(Color.BLACK);

        panel.add(label, BorderLayout.NORTH);

        return panel;
    }


    private JButton crearBoton(String texto, Icon icono) {
        return new BotonVerde(texto, icono);
    }

    private void mostrarCalendario() {
        LocalDate inicial = LocalDate.now();
        try {
            inicial = LocalDate.parse(txtVigencia.getText().trim(), formatoFecha);
        } catch (DateTimeParseException ignorada) {

        }

        final JPopupMenu popup = new JPopupMenu();
        popup.setBorder(new LineBorder(BORDE, 1));
        popup.add(new PanelCalendario(inicial, fecha -> {
            txtVigencia.setText(fecha.format(formatoFecha));
            popup.setVisible(false);
        }));
        popup.show(btnCalendario,
                btnCalendario.getWidth() - popup.getPreferredSize().width,
                btnCalendario.getHeight());
    }

    private Icon cargarIcono(String ruta) {
        try {
            if (ruta != null && !ruta.trim().isEmpty()) {
                java.io.File archivo = new java.io.File(ruta.trim());
                java.net.URL url = archivo.exists()
                        ? archivo.toURI().toURL()
                        : getClass().getResource(ruta.trim());
                if (url != null) {
                    ImageIcon original = new ImageIcon(url);
                    if (original.getIconWidth() > 0) {
                        return new ImageIcon(
                                original.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
                    }
                }
            }
        } catch (Exception ignorada) {
        }
        return new IconoProducto();
    }


    public void agregarProducto(String nombre) {
        cbProducto.addItem(nombre);
    }


    public void agregarDescuento(
            int idProducto,
            String nombre,
            int id,
            double porcentaje,
            LocalDate vigencia,
            double precioNormal) {

        double precioEspecial =
                precioNormal * (1 - porcentaje / 100.0);

        DateTimeFormatter formato =
                DateTimeFormatter.ofPattern(
                        "dd-MMM-yyyy",
                        new java.util.Locale("es", "MX")
                );

        modeloTabla.addRow(new Object[]{
                idProducto,
                nombre,
                id,
                porcentaje + "%",
                vigencia.format(formato),
                String.format("$%.2f", precioNormal),
                String.format("$%.2f", precioEspecial)
        });
    }

    private void configurarEventos() {

        btnCalendario.addActionListener(e -> mostrarCalendario());

        btnGuardar.addActionListener(e -> guardarDescuento());

        btnModificar.addActionListener(e -> modificarDescuento());

        btnEliminar.addActionListener(e -> eliminarDescuento());

        tablaDescuentos.getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {
                        seleccionarDescuento();
                    }
                });
    }


    private void cargarProductos() {
        try {
            productosDisponibles =
                    ServiceFacadeLocator.getInstanceFacadeProducto().obtenerProductosActualizados();

            cbProducto.removeAllItems();

            for (Producto producto : productosDisponibles) {
                if (Boolean.TRUE.equals(producto.getActivo())) {
                    cbProducto.addItem(producto.getIdProducto() + " - " + producto.getNombre());
                }
            }
            cbProducto.setSelectedIndex(-1);

        } catch (Exception e) {
            mostrarError(
                    "No se pudieron cargar los productos: " + e.getMessage());
        }
    }

    private Producto obtenerProductoSeleccionado() {

        String seleccionado =
                (String) cbProducto.getSelectedItem();

        if (seleccionado == null) {
            throw new IllegalArgumentException("Debes seleccionar un producto.");
        }

        for (Producto producto : productosDisponibles) {

            String texto =
                    producto.getIdProducto() + " - " + producto.getNombre();

            if (texto.equals(seleccionado)) {
                return producto;
            }
        }

        throw new IllegalArgumentException(
                "No se encontro el producto seleccionado."
        );
    }

    private double obtenerPorcentaje() {

        try {
            String texto = txtPorcentaje.getText().trim().replace("%", "").replace(",", ".");

            double porcentaje = Double.parseDouble(texto);

            if (!Double.isFinite(porcentaje) ||
                    porcentaje < 1 || porcentaje > 100) {
                throw new IllegalArgumentException(
                        "El porcentaje debe estar entre 1 y 100."
                );
            }

            return porcentaje;

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Ingresa un porcentaje numerico valido.");
        }
    }

    private LocalDateTime obtenerVigencia() {

        try {
            LocalDate fecha = LocalDate.parse(txtVigencia.getText().trim(),formatoFecha);

            LocalDateTime vigencia =
                    fecha.atTime(23, 59, 59);

            if (!vigencia.isAfter(LocalDateTime.now())) {
                throw new IllegalArgumentException("La fecha de vigencia no puede ser pasada.");
            }

            return vigencia;

        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Usa una fecha valida. Ejemplo: 30-nov-2026.");
        }
    }

    private void guardarDescuento() {

        try {
            Producto producto = obtenerProductoSeleccionado();
            double porcentaje = obtenerPorcentaje();
            LocalDateTime vigencia = obtenerVigencia();
            ServiceFacadeLocator.getInstanceFacadeDescuento().asignarDescuento(producto.getIdProducto(), porcentaje, vigencia);
            JOptionPane.showMessageDialog(this, "Descuento guardado correctamente.", "Exito", JOptionPane.INFORMATION_MESSAGE);

            cargarDescuentos();
            limpiarFormulario();

        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    private void cargarDescuentos() {

        try {
            descuentosRegistrados = ServiceFacadeLocator.getInstanceFacadeDescuento().obtenerDescuentos();

            modeloTabla.setRowCount(0);

            for (Descuento descuento : descuentosRegistrados) {

                Producto producto = descuento.getProducto();

                double precioNormal = producto.getPrecio();
                double porcentaje = descuento.getPorcentajeDescuento();

                double precioEspecial = Math.round(precioNormal * (1 - porcentaje / 100.0) * 100) / 100.0;

                modeloTabla.addRow(new Object[]{
                        producto.getIdProducto(),
                        producto.getNombre(),
                        descuento.getIdDescuento(),
                        String.format("%.2f%%", porcentaje),
                        descuento.getVigencia()
                                .format(formatoFecha),
                        String.format("$%.2f", precioNormal),
                        String.format("$%.2f", precioEspecial)
                });
            }

        } catch (Exception e) {
            mostrarError(
                    "Error al cargar descuentos: " + e.getMessage());
        }
    }

    private void seleccionarDescuento() {

        int fila = tablaDescuentos.getSelectedRow();

        if (fila < 0) {
            idDescuentoSeleccionado = null;
            btnModificar.setEnabled(false);
            btnEliminar.setEnabled(false);
            return;
        }


        int filaModelo =
                tablaDescuentos.convertRowIndexToModel(fila);

        Integer idDescuento = (Integer)
                modeloTabla.getValueAt(filaModelo, 2);

        for (Descuento descuento : descuentosRegistrados) {

            if (descuento.getIdDescuento()
                    .equals(idDescuento)) {

                idDescuentoSeleccionado = idDescuento;

                Producto producto = descuento.getProducto();

                cbProducto.setSelectedItem(
                        producto.getIdProducto() + " - " + producto.getNombre()
                );

                txtPorcentaje.setText(
                        descuento.getPorcentajeDescuento().toString()
                );

                txtVigencia.setText(
                        descuento.getVigencia()
                                .format(formatoFecha)
                );

                btnModificar.setEnabled(true);
                btnEliminar.setEnabled(true);

                break;
            }
        }
    }


    private void modificarDescuento() {

        try {
            if (idDescuentoSeleccionado == null) {
                throw new IllegalArgumentException(
                        "Selecciona un descuento de la tabla."
                );
            }

            Producto producto = obtenerProductoSeleccionado();

            double porcentaje = obtenerPorcentaje();

            LocalDateTime vigencia = obtenerVigencia();

            ServiceFacadeLocator
                    .getInstanceFacadeDescuento()
                    .modificarDescuento(
                            idDescuentoSeleccionado,
                            producto.getIdProducto(),
                            porcentaje,
                            vigencia
                    );

            JOptionPane.showMessageDialog(
                    this,
                    "Descuento modificado correctamente."
            );

            limpiarFormulario();
            cargarDescuentos();

        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }


    private void eliminarDescuento() {

        if (idDescuentoSeleccionado == null) {
            mostrarError("Selecciona un descuento para eliminar.");
            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(this, "¿Deseas eliminar este descuento?", "Confirmar eliminacion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE
        );

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            ServiceFacadeLocator.getInstanceFacadeDescuento().eliminarDescuento(idDescuentoSeleccionado);
            JOptionPane.showMessageDialog(
                    this,
                    "Descuento eliminado correctamente."
            );

            limpiarFormulario();
            cargarDescuentos();

        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }


    private void limpiarFormulario() {

        idDescuentoSeleccionado = null;

        txtPorcentaje.setText("");
        txtVigencia.setText("");

        tablaDescuentos.clearSelection();

        cbProducto.setSelectedIndex(-1);

        btnGuardar.setEnabled(true);
        btnModificar.setEnabled(false);
        btnEliminar.setEnabled(false);
    }

    private void mostrarError(String mensaje) {

        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private static class BotonVerde extends JButton {

        private boolean encima = false;

        BotonVerde(String texto, Icon icono) {
            super(texto, icono);
            setFont(new Font("Instrument Sans", Font.BOLD, 14));
            setForeground(BLANCO);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(200, 40));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    encima = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    encima = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            Color fondo = VERDE;
            if (!isEnabled()) {
                fondo = new Color(196, 214, 187);
            } else if (getModel().isPressed()) {
                fondo = VERDE_OSCURO;
            } else if (encima) {
                fondo = new Color(125, 160, 108);
            }
            g2.setColor(fondo);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            Icon icono = getIcon();
            int espacio = icono != null ? 8 : 0;
            int total = (icono != null ? icono.getIconWidth() : 0) + espacio + fm.stringWidth(getText());
            int x = (getWidth() - total) / 2;
            if (icono != null) {
                icono.paintIcon(this, g2, x, (getHeight() - icono.getIconHeight()) / 2);
                x += icono.getIconWidth() + espacio;
            }
            g2.setColor(BLANCO);
            g2.drawString(getText(), x, (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
            g2.dispose();
        }
    }

    private static class IconoGuardar implements Icon {
        public int getIconWidth() { return 16; }
        public int getIconHeight() { return 16; }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(BLANCO);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(x + 1, y + 1, 14, 14, 3, 3);
            g2.drawRect(x + 5, y + 1, 6, 4);
            g2.drawRect(x + 4, y + 9, 8, 5);
            g2.dispose();
        }
    }



    private static class IconoCalendario implements Icon {
        public int getIconWidth() { return 22; }
        public int getIconHeight() { return 22; }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(60, 60, 60));
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(x + 2, y + 4, 18, 16, 4, 4);
            g2.drawLine(x + 2, y + 9, x + 20, y + 9);
            g2.drawLine(x + 7, y + 2, x + 7, y + 6);
            g2.drawLine(x + 15, y + 2, x + 15, y + 6);
            for (int fila = 0; fila < 2; fila++) {
                for (int col = 0; col < 3; col++) {
                    g2.fillRect(x + 6 + col * 4, y + 12 + fila * 4, 2, 2);
                }
            }
            g2.dispose();
        }
    }

    private static class IconoProducto implements Icon {
        public int getIconWidth() { return 40; }
        public int getIconHeight() { return 40; }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(238, 238, 238));
            g2.fillRoundRect(x, y, 40, 40, 6, 6);
            g2.setColor(new Color(225, 190, 90));
            g2.setStroke(new BasicStroke(4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawLine(x + 11, y + 29, x + 29, y + 11);
            g2.dispose();
        }
    }

    private static class PanelCalendario extends JPanel {

        private static final Locale MX = new Locale("es", "MX");

        private YearMonth mes;
        private final LocalDate resaltada;
        private final Consumer<LocalDate> alElegir;
        private final JLabel lblMes = new JLabel("", SwingConstants.CENTER);
        private final JPanel dias = new JPanel(new GridLayout(0, 7, 2, 2));

        PanelCalendario(LocalDate inicial, Consumer<LocalDate> alElegir) {
            super(new BorderLayout(0, 6));
            this.mes = YearMonth.from(inicial);
            this.resaltada = inicial;
            this.alElegir = alElegir;

            setBackground(BLANCO);
            setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

            lblMes.setFont(new Font("Instrument Sans", Font.BOLD, 14));
            lblMes.setForeground(VERDE_OSCURO);

            JPanel barra = new JPanel(new BorderLayout());
            barra.setBackground(BLANCO);
            barra.add(crearFlecha("<", -1), BorderLayout.WEST);
            barra.add(lblMes, BorderLayout.CENTER);
            barra.add(crearFlecha(">", 1), BorderLayout.EAST);

            dias.setBackground(BLANCO);

            add(barra, BorderLayout.NORTH);
            add(dias, BorderLayout.CENTER);
            setPreferredSize(new Dimension(260, 250));
            pintar();
        }

        private JLabel crearFlecha(String texto, int salto) {
            JLabel flecha = new JLabel(texto, SwingConstants.CENTER);
            flecha.setFont(new Font("Instrument Sans", Font.BOLD, 16));
            flecha.setForeground(VERDE_OSCURO);
            flecha.setPreferredSize(new Dimension(30, 24));
            flecha.setCursor(new Cursor(Cursor.HAND_CURSOR));
            flecha.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    mes = mes.plusMonths(salto);
                    pintar();
                }
            });
            return flecha;
        }

        private void pintar() {
            dias.removeAll();

            String nombreMes = mes.getMonth().getDisplayName(TextStyle.FULL, MX);
            lblMes.setText(nombreMes.substring(0, 1).toUpperCase() + nombreMes.substring(1) + " " + mes.getYear());

            for (String d : new String[]{"L", "M", "M", "J", "V", "S", "D"}) {
                JLabel cab = new JLabel(d, SwingConstants.CENTER);
                cab.setFont(new Font("Instrument Sans", Font.PLAIN, 12));
                cab.setForeground(GRIS_PLACEHOLDER);
                dias.add(cab);
            }

            int huecos = mes.atDay(1).getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue();
            int celdas = 0;
            for (int i = 0; i < huecos; i++, celdas++) {
                dias.add(new JLabel());
            }

            LocalDate hoy = LocalDate.now();
            for (int d = 1; d <= mes.lengthOfMonth(); d++, celdas++) {
                final LocalDate fecha = mes.atDay(d);
                final boolean permitido = !fecha.isBefore(hoy);

                final JLabel dia = new JLabel(String.valueOf(d), SwingConstants.CENTER);
                dia.setFont(new Font("Instrument Sans", Font.PLAIN, 13));
                dia.setOpaque(true);
                final Color fondoBase = fecha.equals(resaltada) ? VERDE_CLARO : BLANCO;
                dia.setBackground(fondoBase);
                dia.setForeground(permitido ? Color.BLACK : new Color(190, 190, 190));
                if (fecha.equals(hoy)) {
                    dia.setBorder(new LineBorder(VERDE, 1, true));
                }

                if (permitido) {
                    dia.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    dia.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(MouseEvent e) {
                            dia.setBackground(VERDE);
                            dia.setForeground(BLANCO);
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {
                            dia.setBackground(fondoBase);
                            dia.setForeground(Color.BLACK);
                        }

                        @Override
                        public void mouseClicked(MouseEvent e) {
                            alElegir.accept(fecha);
                        }
                    });
                }
                dias.add(dia);
            }

            for (; celdas < 42; celdas++) {
                dias.add(new JLabel());
            }

            dias.revalidate();
            dias.repaint();
        }
    }

}
