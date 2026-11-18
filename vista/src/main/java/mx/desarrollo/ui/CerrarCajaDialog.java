package mx.desarrollo.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import mx.desarrollo.delegate.DelegateCorte;
import mx.desarrollo.delegate.ResumenCorte;
import mx.desarrollo.delegate.SesionCaja;
import mx.desarrollo.entity.Usuario;
import mx.desarrollo.facade.FacadeCorteCaja;
import mx.desarrollo.integration.ServiceFacadeLocator;

/**
 * Ventana emergente para cerrar la caja del usuario en sesión: muestra el
 * resumen del turno, permite contar el efectivo (total o por denominación),
 * calcula la diferencia y registra el corte.
 * <p>
 * La parte visual se edita en NetBeans (CerrarCajaDialog.form); aquí solo queda
 * la lógica y los ajustes que el diseñador no maneja.
 */
public class CerrarCajaDialog extends javax.swing.JDialog {

    private static final Color VERDE_BOTON = new Color(136, 180, 123);
    private static final Color VERDE = new Color(46, 125, 50);
    private static final Color ROJO = new Color(200, 40, 40);
    private static final Color GRIS = new Color(110, 110, 110);
    private static final DateTimeFormatter FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final int MAX_PIEZAS = 1_000_000;

    private final Usuario usuario;
    private final ResumenCorte turno;
    private final SesionCaja.Apertura apertura;
    private final FacadeCorteCaja facade = ServiceFacadeLocator.getInstanceFacadeCorteCaja();

    /** Modelos de las tablas definidas en el diseñador (se obtienen en {@link #configurar()}). */
    private DefaultTableModel modeloResumen;
    private DefaultTableModel modeloDenom;

    private boolean actualizando = false;
    private ResumenCorte corteCerrado;

    public CerrarCajaDialog(Frame parent, Usuario usuario, ResumenCorte turno) {
        super(parent, "Cerrar caja", true);
        this.usuario = usuario;
        this.turno = turno;
        this.apertura = SesionCaja.de(usuario);
        initComponents();
        configurar();
        llenarDenominaciones();
        refrescar();
        pack();
        setMinimumSize(getSize());
        setLocationRelativeTo(parent);
    }

    public ResumenCorte getCorteCerrado() {
        return corteCerrado;
    }


    private void configurar() {
        getContentPane().setBackground(Color.WHITE);
        estilizarBotonPrimario(btnCerrar);

        modeloResumen = (DefaultTableModel) tablaResumen.getModel();
        modeloDenom = (DefaultTableModel) tablaDenom.getModel();

        String nombre = usuario.getNombrePersona() != null ? usuario.getNombrePersona() : "";
        String inicio;
        if (apertura != null) {
            inicio = "Caja abierta el " + apertura.getHoraApertura().format(FECHA_HORA);
        } else if (turno.getFechaInicio() != null) {
            inicio = "Primera venta: " + turno.getFechaInicio().format(FECHA_HORA);
        } else {
            inicio = "Turno en curso";
        }
        lblSubtitulo.setText("Cajero: " + nombre + "   ·   " + inicio);

        estilizarEncabezado(tablaResumen);
        tablaResumen.getColumnModel().getColumn(0).setCellRenderer(renderResumen(SwingConstants.LEFT));
        tablaResumen.getColumnModel().getColumn(1).setCellRenderer(renderResumen(SwingConstants.CENTER));
        tablaResumen.getColumnModel().getColumn(2).setCellRenderer(renderResumen(SwingConstants.RIGHT));
        anchos(tablaResumen, 190, 60, 110);
        spResumen.getViewport().setBackground(Color.WHITE);
        pnlDiferencia.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(215, 225, 212)),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));

        if (apertura != null) {
            txtFondo.setText(String.format(Locale.US, "%.2f", apertura.getFondoInicial()));
            txtFondo.setEditable(false);
            txtFondo.setBackground(new Color(240, 240, 240));
            txtFondo.setToolTipText("Registrado al abrir la caja");
        }

        estilizarEncabezado(tablaDenom);
        tablaDenom.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        tablaDenom.getColumnModel().getColumn(0).setCellRenderer(render(SwingConstants.CENTER));
        tablaDenom.getColumnModel().getColumn(1).setCellRenderer(render(SwingConstants.CENTER));
        tablaDenom.getColumnModel().getColumn(2).setCellRenderer(render(SwingConstants.RIGHT));
        anchos(tablaDenom, 110, 90, 140);
        spDenom.getViewport().setBackground(Color.WHITE);
        modeloDenom.addTableModelListener(e -> {
            if (!actualizando && e.getType() == TableModelEvent.UPDATE && e.getColumn() == 1) {
                recalcularDenominaciones();
            }
        });

        DocumentListener refresco = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                cambioCaptura();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                cambioCaptura();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                cambioCaptura();
            }
        };
        txtFondo.getDocument().addDocumentListener(refresco);
        txtContado.getDocument().addDocumentListener(refresco);

        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cerrar");
        getRootPane().getActionMap().put("cerrar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }


    private static void estilizarBotonPrimario(JButton b) {
        Font fuente = b.getFont();
        Color texto = b.getForeground();
        Border borde = b.getBorder();
        b.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        b.setOpaque(true);
        b.setFont(fuente);
        b.setForeground(texto);
        b.setBorder(borde);
        b.setBackground(VERDE_BOTON);
        b.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        b.getModel().addChangeListener(e ->
                b.setBackground(b.getModel().isPressed() ? new Color(104, 150, 92)
                        : b.getModel().isRollover() ? new Color(120, 168, 107) : VERDE_BOTON));
        b.setRolloverEnabled(true);
    }

    //LOGICA
    private void cambioCaptura() {
        if (actualizando) {
            return;
        }
        lblError.setText(" ");
        refrescar();
    }

    private void refrescar() {
        llenarResumen();
        actualizarDiferencia();
    }

    private double fondoActual() {
        String t = txtFondo.getText();
        if (t == null || t.isBlank()) {
            return 0;
        }
        Double f = CortesCajaPanel.parseMonto(t);
        return f == null ? 0 : f;
    }

    private void llenarResumen() {
        modeloResumen.setRowCount(0);
        for (Map.Entry<String, ResumenCorte.Linea> e : turno.getPorMetodo().entrySet()) {
            modeloResumen.addRow(new Object[]{"Ventas " + etiquetaMetodo(e.getKey()),
                e.getValue().getTransacciones(), CortesCajaPanel.dinero(e.getValue().getMonto())});
        }
        if (turno.getPorMetodo().isEmpty()) {
            modeloResumen.addRow(new Object[]{"Sin ventas completadas", 0, CortesCajaPanel.dinero(0)});
        }
        if (turno.getNumCanceladas() > 0) {
            modeloResumen.addRow(new Object[]{"Cancelaciones", turno.getNumCanceladas(),
                CortesCajaPanel.dinero(-turno.getMontoCanceladas())});
        }
        modeloResumen.addRow(new Object[]{"Total de ventas", turno.getNumVentas(),
            CortesCajaPanel.dinero(turno.getTotalVentas())});
        double fondo = fondoActual();
        modeloResumen.addRow(new Object[]{"Fondo inicial de caja", "", CortesCajaPanel.dinero(fondo)});
        modeloResumen.addRow(new Object[]{"Efectivo esperado en caja", "",
            CortesCajaPanel.dinero(turno.efectivoEsperadoEnCaja(fondo))});
    }

    private void actualizarDiferencia() {
        Double contado = CortesCajaPanel.parseMonto(txtContado.getText());
        if (contado == null) {
            lblDiferencia.setText("—");
            lblDiferencia.setForeground(GRIS);
            return;
        }
        double dif = turno.diferenciaCon(contado, fondoActual());
        if (dif > 0) {
            lblDiferencia.setText("Sobrante " + CortesCajaPanel.dinero(dif));
            lblDiferencia.setForeground(VERDE);
        } else if (dif < 0) {
            lblDiferencia.setText("Faltante " + CortesCajaPanel.dinero(-dif));
            lblDiferencia.setForeground(ROJO);
        } else {
            lblDiferencia.setText("Sin diferencia");
            lblDiferencia.setForeground(VERDE);
        }
    }

    private void llenarDenominaciones() {
        actualizando = true;
        try {
            modeloDenom.setRowCount(0);
            for (double d : DelegateCorte.DENOMINACIONES) {
                modeloDenom.addRow(new Object[]{nombreDenominacion(d), 0, CortesCajaPanel.dinero(0)});
            }
        } finally {
            actualizando = false;
        }
    }

    private void recalcularDenominaciones() {
        actualizando = true;
        try {
            Map<Double, Integer> piezas = new LinkedHashMap<>();
            for (int i = 0; i < DelegateCorte.DENOMINACIONES.length; i++) {
                double d = DelegateCorte.DENOMINACIONES[i];
                int n = leerEntero(modeloDenom.getValueAt(i, 1));
                modeloDenom.setValueAt(n, i, 1);
                modeloDenom.setValueAt(CortesCajaPanel.dinero(d * n), i, 2);
                piezas.put(d, n);
            }
            txtContado.setText(String.format(Locale.US, "%.2f", facade.totalDenominaciones(piezas)));
        } catch (RuntimeException | ExceptionInInitializerError ex) {
            mostrarError(ex);
        } finally {
            actualizando = false;
        }
        lblError.setText(" ");
        actualizarDiferencia();
    }

    private void cerrarCaja() {
        if (tablaDenom.isEditing()) {
            tablaDenom.getCellEditor().stopCellEditing();
        }
        Double contado = CortesCajaPanel.parseMonto(txtContado.getText());
        if (contado == null) {
            error("Captura el efectivo contado (o el desglose por denominación) antes de cerrar la caja.");
            txtContado.requestFocusInWindow();
            return;
        }
        Double fondo = txtFondo.getText().isBlank() ? Double.valueOf(0) : CortesCajaPanel.parseMonto(txtFondo.getText());
        if (fondo == null) {
            error("El fondo inicial no es válido.");
            txtFondo.requestFocusInWindow();
            return;
        }
        if (contado < fondo) {
            error("El efectivo contado no puede ser menor al fondo inicial de caja.");
            txtContado.requestFocusInWindow();
            return;
        }

        double dif = turno.diferenciaCon(contado, fondo);
        String estadoCuadre = dif > 0 ? "Sobrante: " + CortesCajaPanel.dinero(dif)
                : dif < 0 ? "Faltante: " + CortesCajaPanel.dinero(-dif) : "Sin diferencia";
        int op = JOptionPane.showConfirmDialog(this,
                "Se cerrará el turno de " + usuario.getNombrePersona() + ":\n\n"
                + "Ventas: " + turno.getNumVentas() + " por " + CortesCajaPanel.dinero(turno.getTotalVentas()) + "\n"
                + "Efectivo esperado en caja: " + CortesCajaPanel.dinero(turno.efectivoEsperadoEnCaja(fondo)) + "\n"
                + "Efectivo contado: " + CortesCajaPanel.dinero(contado) + "\n"
                + estadoCuadre + "\n\n¿Cerrar la caja?",
                "Cerrar caja", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (op != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            corteCerrado = facade.cerrarCorte(usuario, usuario, contado, fondo);
        } catch (RuntimeException | ExceptionInInitializerError ex) {
            mostrarError(ex);
            return;
        }

        String[] opciones = {"Ticket", "Hoja completa", "No imprimir"};
        int imp = JOptionPane.showOptionDialog(this,
                "Corte #" + corteCerrado.getIdCorte() + " registrado.\n¿Deseas imprimirlo ahora?",
                "Corte registrado", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, opciones, opciones[2]);
        if (imp == 0 || imp == 1) {
            imprimir(imp == 0);
        }
        dispose();
    }

    private void imprimir(boolean ticket) {
        try {
            ImpresionCorte.imprimir(this, facade.lineasImpresion(corteCerrado, ticket ? 40 : 64), ticket, "Corte de caja");
        } catch (RuntimeException | ExceptionInInitializerError ex) {
            mostrarError(ex);
        }
    }

    private void error(String mensaje) {
        lblError.setText("<html><body style='width:480px'>" + mensaje + "</body></html>");
    }

    private void mostrarError(Throwable ex) {
        if (ex instanceof IllegalArgumentException) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Cerrar caja", JOptionPane.WARNING_MESSAGE);
            return;
        }
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this,
                "No se pudo completar la operación con la base de datos.",
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    //  Utilidades de formato

    private static void estilizarEncabezado(JTable t) {
        t.getTableHeader().setReorderingAllowed(false);
        t.getTableHeader().setFont(new Font("Instrument Sans", Font.BOLD, 12));
    }

    private static void anchos(JTable t, int... anchos) {
        for (int i = 0; i < anchos.length; i++) {
            t.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }
    }

    private static DefaultTableCellRenderer render(int alineacion) {
        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setHorizontalAlignment(alineacion);
        return r;
    }

    private static DefaultTableCellRenderer renderResumen(int alineacion) {
        DefaultTableCellRenderer r = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                String concepto = String.valueOf(t.getValueAt(row, 0));
                boolean negrita = concepto.startsWith("Total") || concepto.startsWith("Efectivo esperado");
                c.setFont(c.getFont().deriveFont(negrita ? Font.BOLD : Font.PLAIN));
                return c;
            }
        };
        r.setHorizontalAlignment(alineacion);
        return r;
    }

    private static int leerEntero(Object valor) {
        if (valor == null) {
            return 0;
        }
        try {
            int n = Integer.parseInt(valor.toString().trim());
            return Math.max(0, Math.min(n, MAX_PIEZAS));
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    private static String nombreDenominacion(double d) {
        return d == Math.floor(d) ? String.format(Locale.US, "$%,d", (long) d) : String.format(Locale.US, "$%.2f", d);
    }

    private static String etiquetaMetodo(String metodo) {
        if (DelegateCorte.METODO_EFECTIVO.equals(metodo)) {
            return "en efectivo";
        }
        if ("TARJETA".equals(metodo)) {
            return "con tarjeta";
        }
        return "por " + (metodo == null ? "otro medio" : metodo.toLowerCase(Locale.ROOT));
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlEncabezado = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        lblSubtitulo = new javax.swing.JLabel();
        pnlCentro = new javax.swing.JPanel();
        pnlResumen = new javax.swing.JPanel();
        lblResumenTitulo = new javax.swing.JLabel();
        spResumen = new javax.swing.JScrollPane();
        tablaResumen = new javax.swing.JTable();
        pnlDiferencia = new javax.swing.JPanel();
        lblDiferenciaEtq = new javax.swing.JLabel();
        lblDiferencia = new javax.swing.JLabel();
        pnlConteo = new javax.swing.JPanel();
        lblConteoTitulo = new javax.swing.JLabel();
        pnlFondo = new javax.swing.JPanel();
        lblFondoEtq = new javax.swing.JLabel();
        pnlFondoCampo = new javax.swing.JPanel();
        lblSignoFondo = new javax.swing.JLabel();
        txtFondo = new javax.swing.JTextField();
        pnlContado = new javax.swing.JPanel();
        lblContadoEtq = new javax.swing.JLabel();
        pnlContadoCampo = new javax.swing.JPanel();
        lblSignoContado = new javax.swing.JLabel();
        txtContado = new javax.swing.JTextField();
        lblAyuda = new javax.swing.JLabel();
        spDenom = new javax.swing.JScrollPane();
        tablaDenom = new javax.swing.JTable();
        pnlPie = new javax.swing.JPanel();
        lblError = new javax.swing.JLabel();
        pnlBotones = new javax.swing.JPanel();
        btnCancelar = new javax.swing.JButton();
        btnCerrar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Cerrar caja");
        getContentPane().setLayout(new java.awt.BorderLayout());

        pnlEncabezado.setBackground(new java.awt.Color(196, 217, 191));
        pnlEncabezado.setBorder(javax.swing.BorderFactory.createEmptyBorder(14, 22, 14, 22));
        pnlEncabezado.setLayout(new java.awt.BorderLayout());

        lblTitulo.setText("Cerrar caja");
        lblTitulo.setFont(new java.awt.Font("Instrument Sans", 1, 20));
        lblTitulo.setForeground(new java.awt.Color(0, 0, 0));

        pnlEncabezado.add(lblTitulo, java.awt.BorderLayout.PAGE_START);

        lblSubtitulo.setText("Cajero:");
        lblSubtitulo.setFont(new java.awt.Font("Instrument Sans", 0, 12));
        lblSubtitulo.setForeground(new java.awt.Color(51, 51, 51));

        pnlEncabezado.add(lblSubtitulo, java.awt.BorderLayout.PAGE_END);

        getContentPane().add(pnlEncabezado, java.awt.BorderLayout.PAGE_START);

        pnlCentro.setBackground(new java.awt.Color(255, 255, 255));
        pnlCentro.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 20, 4, 20));
        pnlCentro.setLayout(new java.awt.GridLayout(1, 2, 18, 0));

        pnlResumen.setBackground(new java.awt.Color(255, 255, 255));
        pnlResumen.setLayout(new java.awt.BorderLayout(0, 8));

        lblResumenTitulo.setText("Resumen del turno");
        lblResumenTitulo.setFont(new java.awt.Font("Instrument Sans", 1, 14));
        lblResumenTitulo.setForeground(new java.awt.Color(51, 51, 51));

        pnlResumen.add(lblResumenTitulo, java.awt.BorderLayout.PAGE_START);

        spResumen.setPreferredSize(new java.awt.Dimension(380, 260));

        tablaResumen.setGridColor(new java.awt.Color(220, 220, 220));
        tablaResumen.setRowHeight(28);
        tablaResumen.setSelectionBackground(new java.awt.Color(232, 240, 229));
        tablaResumen.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tablaResumen.setShowVerticalLines(false);
        tablaResumen.setFocusable(false);
        tablaResumen.setRowSelectionAllowed(false);
        tablaResumen.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {

        },
        new String [] {
            "Concepto", "Ventas", "Monto"
        }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        spResumen.setViewportView(tablaResumen);

        pnlResumen.add(spResumen, java.awt.BorderLayout.CENTER);

        pnlDiferencia.setBackground(new java.awt.Color(245, 248, 244));
        pnlDiferencia.setLayout(new java.awt.BorderLayout());

        lblDiferenciaEtq.setText("Diferencia:");
        lblDiferenciaEtq.setFont(new java.awt.Font("Instrument Sans", 1, 14));
        lblDiferenciaEtq.setForeground(new java.awt.Color(51, 51, 51));

        pnlDiferencia.add(lblDiferenciaEtq, java.awt.BorderLayout.LINE_START);

        lblDiferencia.setText("—");
        lblDiferencia.setFont(new java.awt.Font("Instrument Sans", 1, 16));
        lblDiferencia.setForeground(new java.awt.Color(110, 110, 110));
        lblDiferencia.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        pnlDiferencia.add(lblDiferencia, java.awt.BorderLayout.CENTER);

        pnlResumen.add(pnlDiferencia, java.awt.BorderLayout.PAGE_END);

        pnlCentro.add(pnlResumen);

        pnlConteo.setBackground(new java.awt.Color(255, 255, 255));
        pnlConteo.setLayout(new java.awt.GridBagLayout());

        lblConteoTitulo.setText("Conteo de efectivo");
        lblConteoTitulo.setFont(new java.awt.Font("Instrument Sans", 1, 14));
        lblConteoTitulo.setForeground(new java.awt.Color(51, 51, 51));

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 8, 0);
        pnlConteo.add(lblConteoTitulo, gridBagConstraints);

        pnlFondo.setBackground(new java.awt.Color(255, 255, 255));
        pnlFondo.setLayout(new java.awt.BorderLayout(10, 0));

        lblFondoEtq.setText("Fondo inicial:");
        lblFondoEtq.setFont(new java.awt.Font("Instrument Sans", 1, 13));
        lblFondoEtq.setForeground(new java.awt.Color(51, 51, 51));
        lblFondoEtq.setPreferredSize(new java.awt.Dimension(130, 30));

        pnlFondo.add(lblFondoEtq, java.awt.BorderLayout.LINE_START);

        pnlFondoCampo.setBackground(new java.awt.Color(255, 255, 255));
        pnlFondoCampo.setLayout(new java.awt.BorderLayout(4, 0));

        lblSignoFondo.setText("$");
        lblSignoFondo.setFont(new java.awt.Font("Instrument Sans", 1, 14));

        pnlFondoCampo.add(lblSignoFondo, java.awt.BorderLayout.LINE_START);

        txtFondo.setFont(new java.awt.Font("Instrument Sans", 0, 14));
        txtFondo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtFondo.setPreferredSize(new java.awt.Dimension(150, 30));
        txtFondo.setText("0.00");

        pnlFondoCampo.add(txtFondo, java.awt.BorderLayout.CENTER);

        pnlFondo.add(pnlFondoCampo, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 8, 0);
        pnlConteo.add(pnlFondo, gridBagConstraints);

        pnlContado.setBackground(new java.awt.Color(255, 255, 255));
        pnlContado.setLayout(new java.awt.BorderLayout(10, 0));

        lblContadoEtq.setText("Efectivo contado:");
        lblContadoEtq.setFont(new java.awt.Font("Instrument Sans", 1, 13));
        lblContadoEtq.setForeground(new java.awt.Color(51, 51, 51));
        lblContadoEtq.setPreferredSize(new java.awt.Dimension(130, 30));

        pnlContado.add(lblContadoEtq, java.awt.BorderLayout.LINE_START);

        pnlContadoCampo.setBackground(new java.awt.Color(255, 255, 255));
        pnlContadoCampo.setLayout(new java.awt.BorderLayout(4, 0));

        lblSignoContado.setText("$");
        lblSignoContado.setFont(new java.awt.Font("Instrument Sans", 1, 14));

        pnlContadoCampo.add(lblSignoContado, java.awt.BorderLayout.LINE_START);

        txtContado.setFont(new java.awt.Font("Instrument Sans", 0, 14));
        txtContado.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtContado.setPreferredSize(new java.awt.Dimension(150, 30));
        txtContado.setToolTipText("Escribe el total o llena las piezas por denominación");

        pnlContadoCampo.add(txtContado, java.awt.BorderLayout.CENTER);

        pnlContado.add(pnlContadoCampo, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 8, 0);
        pnlConteo.add(pnlContado, gridBagConstraints);

        lblAyuda.setText("<html><body style='width:320px'>El efectivo contado incluye el fondo inicial. Puedes escribir el total o capturar las piezas de cada denominación.</body></html>");
        lblAyuda.setFont(new java.awt.Font("Instrument Sans", 0, 11));
        lblAyuda.setForeground(new java.awt.Color(110, 110, 110));

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 8, 0);
        pnlConteo.add(lblAyuda, gridBagConstraints);

        spDenom.setPreferredSize(new java.awt.Dimension(340, 268));

        tablaDenom.setGridColor(new java.awt.Color(220, 220, 220));
        tablaDenom.setRowHeight(22);
        tablaDenom.setSelectionBackground(new java.awt.Color(232, 240, 229));
        tablaDenom.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tablaDenom.setShowVerticalLines(false);
        tablaDenom.setSurrendersFocusOnKeystroke(true);
        tablaDenom.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {

        },
        new String [] {
            "Denominación", "Piezas", "Importe"
        }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        spDenom.setViewportView(tablaDenom);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        pnlConteo.add(spDenom, gridBagConstraints);

        pnlCentro.add(pnlConteo);

        getContentPane().add(pnlCentro, java.awt.BorderLayout.CENTER);

        pnlPie.setBackground(new java.awt.Color(255, 255, 255));
        pnlPie.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 20, 8, 20));
        pnlPie.setLayout(new java.awt.BorderLayout());

        lblError.setText(" ");
        lblError.setFont(new java.awt.Font("Instrument Sans", 1, 12));
        lblError.setForeground(new java.awt.Color(200, 40, 40));

        pnlPie.add(lblError, java.awt.BorderLayout.CENTER);

        pnlBotones.setBackground(new java.awt.Color(255, 255, 255));
        pnlBotones.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 8));

        btnCancelar.setText("Cancelar");
        btnCancelar.setFont(new java.awt.Font("Instrument Sans", 0, 12));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setPreferredSize(new java.awt.Dimension(110, 34));
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        pnlBotones.add(btnCancelar);

        btnCerrar.setBackground(new java.awt.Color(136, 180, 123));
        btnCerrar.setFont(new java.awt.Font("Instrument Sans", 1, 12));
        btnCerrar.setForeground(new java.awt.Color(255, 255, 255));
        btnCerrar.setText("Cerrar caja");
        btnCerrar.setFocusPainted(false);
        btnCerrar.setOpaque(true);
        btnCerrar.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 14, 6, 14));
        btnCerrar.setPreferredSize(new java.awt.Dimension(140, 34));
        btnCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarActionPerformed(evt);
            }
        });

        pnlBotones.add(btnCerrar);

        pnlPie.add(pnlBotones, java.awt.BorderLayout.LINE_END);

        getContentPane().add(pnlPie, java.awt.BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarActionPerformed
        cerrarCaja();
    }//GEN-LAST:event_btnCerrarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnCerrar;
    private javax.swing.JLabel lblAyuda;
    private javax.swing.JLabel lblContadoEtq;
    private javax.swing.JLabel lblConteoTitulo;
    private javax.swing.JLabel lblDiferencia;
    private javax.swing.JLabel lblDiferenciaEtq;
    private javax.swing.JLabel lblError;
    private javax.swing.JLabel lblFondoEtq;
    private javax.swing.JLabel lblResumenTitulo;
    private javax.swing.JLabel lblSignoContado;
    private javax.swing.JLabel lblSignoFondo;
    private javax.swing.JLabel lblSubtitulo;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JPanel pnlBotones;
    private javax.swing.JPanel pnlCentro;
    private javax.swing.JPanel pnlContado;
    private javax.swing.JPanel pnlContadoCampo;
    private javax.swing.JPanel pnlConteo;
    private javax.swing.JPanel pnlDiferencia;
    private javax.swing.JPanel pnlEncabezado;
    private javax.swing.JPanel pnlFondo;
    private javax.swing.JPanel pnlFondoCampo;
    private javax.swing.JPanel pnlPie;
    private javax.swing.JPanel pnlResumen;
    private javax.swing.JScrollPane spDenom;
    private javax.swing.JScrollPane spResumen;
    private javax.swing.JTable tablaDenom;
    private javax.swing.JTable tablaResumen;
    private javax.swing.JTextField txtContado;
    private javax.swing.JTextField txtFondo;
    // End of variables declaration//GEN-END:variables
}
