/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package mx.desarrollo.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import mx.desarrollo.delegate.DelegateCorte;
import mx.desarrollo.delegate.DelegateUsuario;
import mx.desarrollo.delegate.DelegateVenta;
import mx.desarrollo.delegate.ResumenCorte;
import mx.desarrollo.entity.Usuario;
import mx.desarrollo.entity.Venta;
import mx.desarrollo.facade.FacadeCorteCaja;
import mx.desarrollo.integration.ServiceFacadeLocator;


public class CortesCajaPanel extends javax.swing.JPanel {

    private static final Color VERDE = new Color(46, 125, 50);
    private static final Color ROJO = new Color(200, 40, 40);
    private static final Color GRIS = new Color(90, 90, 90);
    private static final Color ENCABEZADO = new Color(232, 240, 229);
    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter FECHA_TITULO = DateTimeFormatter.ofPattern("dd MMM yyyy", new Locale("es", "MX"));
    private static final int MAX_PIEZAS = 1_000_000;

    private final Usuario usuarioActual;
    private final FacadeCorteCaja facade = ServiceFacadeLocator.getInstanceFacadeCorteCaja();
    private final List<Usuario> usuarios = new ArrayList<>();

    private ResumenCorte resumen;

    private boolean actualizando;

    public CortesCajaPanel() {
        this(null);
    }


    public CortesCajaPanel(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
        initComponents();
        formatoTablas();
        if (usuarioActual == null) {
            return;
        }
        prepararPermisos();
        iniciarEventos();
        cargarUsuarios();
        cargarTurno();
    }



    private boolean esAdmin() {
        return DelegateUsuario.ROL_ADMIN.equalsIgnoreCase(usuarioActual.getRol());
    }

    private void prepararPermisos() {
        boolean admin = esAdmin();
        cmbUsuario.setEnabled(admin);
        btnCorteFinal.setEnabled(admin);
        btnHistorial.setEnabled(admin);
        if (!admin) {
            btnCorteFinal.setToolTipText("Solo para administradores");
            btnHistorial.setToolTipText("Solo para administradores");
        }
    }

    private void iniciarEventos() {
        txtFondo.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                cambioFondo();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                cambioFondo();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                cambioFondo();
            }
        });
        txtEfectivoContado.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                actualizarDiferencia();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                actualizarDiferencia();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                actualizarDiferencia();
            }
        });
        tablaDenom.getModel().addTableModelListener(e -> {
            if (!actualizando && e.getType() == TableModelEvent.UPDATE && e.getColumn() == 1) {
                recalcularDenominaciones();
            }
        });
        new TextPrompt("0.00", txtEfectivoContado);
    }

    private void cargarUsuarios() {
        actualizando = true;
        try {
            usuarios.clear();
            usuarios.addAll(facade.usuariosParaCorte(usuarioActual));
            List<String> nombres = new ArrayList<>();
            int seleccion = 0;
            for (int i = 0; i < usuarios.size(); i++) {
                Usuario u = usuarios.get(i);
                nombres.add(u.getNombrePersona() + " (" + u.getNombreUsuario() + ")");
                if (u.getIdUsuario().equals(usuarioActual.getIdUsuario())) {
                    seleccion = i;
                }
            }
            cmbUsuario.setModel(new javax.swing.DefaultComboBoxModel<>(nombres.toArray(new String[0])));
            if (!nombres.isEmpty()) {
                cmbUsuario.setSelectedIndex(seleccion);
            }
        } catch (RuntimeException | ExceptionInInitializerError ex) {
            mostrarError(ex);
        } finally {
            actualizando = false;
        }
    }

    private Usuario usuarioSeleccionado() {
        int i = cmbUsuario.getSelectedIndex();
        return (i >= 0 && i < usuarios.size()) ? usuarios.get(i) : usuarioActual;
    }

    private void formatoTablas() {
        estilizar(tablaResumen, 28);
        estilizar(tablaDenom, 20);
        estilizar(tablaVentas, 26);

        tablaResumen.getColumnModel().getColumn(0).setCellRenderer(renderResumen(SwingConstants.LEFT));
        tablaResumen.getColumnModel().getColumn(1).setCellRenderer(renderResumen(SwingConstants.CENTER));
        tablaResumen.getColumnModel().getColumn(2).setCellRenderer(renderResumen(SwingConstants.RIGHT));
        anchos(tablaResumen, 300, 130, 168);

        tablaDenom.getColumnModel().getColumn(0).setCellRenderer(render(SwingConstants.CENTER));
        tablaDenom.getColumnModel().getColumn(1).setCellRenderer(render(SwingConstants.CENTER));
        tablaDenom.getColumnModel().getColumn(2).setCellRenderer(render(SwingConstants.RIGHT));
        anchos(tablaDenom, 200, 150, 226);
        llenarDenominaciones();

        for (int i = 0; i < tablaVentas.getColumnCount(); i++) {
            tablaVentas.getColumnModel().getColumn(i).setCellRenderer(render(i == 5 ? SwingConstants.RIGHT : SwingConstants.CENTER));
        }
        anchos(tablaVentas, 110, 180, 330, 150, 150, 264);
    }

    private static void estilizar(JTable t, int alto) {
        t.setRowHeight(alto);
        t.setShowVerticalLines(false);
        t.setShowHorizontalLines(true);
        t.setGridColor(new Color(220, 220, 220));
        t.setSelectionBackground(new Color(232, 240, 229));
        t.setSelectionForeground(Color.BLACK);
        t.getTableHeader().setReorderingAllowed(false);
        t.getTableHeader().setFont(new Font("Instrument Sans", Font.BOLD, 13));
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

    private void llenarDenominaciones() {
        DefaultTableModel m = (DefaultTableModel) tablaDenom.getModel();
        actualizando = true;
        try {
            m.setRowCount(0);
            for (double d : DelegateCorte.DENOMINACIONES) {
                m.addRow(new Object[]{nombreDenominacion(d), 0, dinero(0)});
            }
        } finally {
            actualizando = false;
        }
    }



    private void cargarTurno() {
        try {
            mostrar(facade.consultarTurno(usuarioActual, usuarioSeleccionado()));
        } catch (RuntimeException | ExceptionInInitializerError ex) {
            mostrarError(ex);
        }
    }

    private boolean esTurno() {
        return resumen != null && !resumen.isCerrado() && resumen.getDia() == null;
    }

    private void mostrar(ResumenCorte r) {
        resumen = r;
        boolean turno = esTurno();

        lblTotalValor.setText(dinero(r.getTotalVentas()));
        lblEfectivoValor.setText(dinero(r.getVentasEfectivo()));
        lblTarjetaValor.setText(dinero(r.getVentasNoEfectivo()));

        if (r.getDia() != null) {
            lblResumenTitulo.setText("  Corte final del día " + r.getDia().format(FECHA));
            lblVentasTitulo.setText("  Ventas de todos los usuarios del día");
        } else if (r.isCerrado()) {
            lblResumenTitulo.setText("  Corte #" + r.getIdCorte() + " cerrado - " + r.getFechaCierre().format(FECHA_HORA));
            lblVentasTitulo.setText("  Ventas incluidas en el corte");
        } else {
            LocalDate dia = r.getFechaInicio() != null ? r.getFechaInicio().toLocalDate() : LocalDate.now();
            lblResumenTitulo.setText("  Resumen del turno - " + dia.format(FECHA_TITULO));
            lblVentasTitulo.setText("  Ventas pendientes de corte");
        }

        llenarResumen();
        llenarVentas(r);
        habilitarCaptura(turno);
        btnCerrarCaja.setEnabled(turno && !r.getVentas().isEmpty());
        btnImprimir.setEnabled(!r.getVentas().isEmpty());
        actualizarDiferencia();
    }

    private void llenarResumen() {
        if (resumen == null) {
            return;
        }
        ResumenCorte r = resumen;
        DefaultTableModel m = (DefaultTableModel) tablaResumen.getModel();
        m.setRowCount(0);
        for (Map.Entry<String, ResumenCorte.Linea> e : r.getPorMetodo().entrySet()) {
            m.addRow(new Object[]{"Ventas " + etiquetaMetodo(e.getKey()), e.getValue().getTransacciones(), dinero(e.getValue().getMonto())});
        }
        if (r.getPorMetodo().isEmpty()) {
            m.addRow(new Object[]{"Sin ventas completadas", 0, dinero(0)});
        }
        if (r.getNumCanceladas() > 0) {
            m.addRow(new Object[]{"Cancelaciones", r.getNumCanceladas(), dinero(-r.getMontoCanceladas())});
        }
        m.addRow(new Object[]{"Total de ventas", r.getNumVentas(), dinero(r.getTotalVentas())});

        if (esTurno()) {
            double fondo = fondoActual();
            m.addRow(new Object[]{"Fondo inicial de caja", "", dinero(fondo)});
            m.addRow(new Object[]{"Efectivo esperado en caja", "", dinero(r.efectivoEsperadoEnCaja(fondo))});
        } else if (r.isCerrado()) {
            m.addRow(new Object[]{"Total contado", "", dinero(r.getTotalContado())});
        } else {
            m.addRow(new Object[]{"Ventas sin corte", r.getVentasSinCorte(), ""});
        }
    }

    private void llenarVentas(ResumenCorte r) {
        DefaultTableModel m = (DefaultTableModel) tablaVentas.getModel();
        m.setRowCount(0);
        for (Venta v : r.getVentas()) {
            m.addRow(new Object[]{
                DelegateVenta.folio(v.getIdVenta()),
                v.getFecha().format(FECHA_HORA),
                v.getUsuario() != null ? v.getUsuario().getNombrePersona() : "",
                capitalizar(v.getMetodoPago()),
                capitalizar(v.getEstado()),
                dinero(v.getTotal())
            });
        }
    }

    private void habilitarCaptura(boolean habilitar) {
        tablaDenom.setEnabled(habilitar);
        txtEfectivoContado.setEnabled(habilitar);
        txtFondo.setEnabled(habilitar);
    }

    private void limpiarCaptura() {
        actualizando = true;
        try {
            txtFondo.setText("0.00");
            txtEfectivoContado.setText("");
            llenarDenominaciones();
        } finally {
            actualizando = false;
        }
    }



    private void cambioFondo() {
        if (actualizando || resumen == null) {
            return;
        }
        if (esTurno()) {
            llenarResumen();
        }
        actualizarDiferencia();
    }

    private double fondoActual() {
        Double f = parseMonto(txtFondo.getText());
        return f == null ? 0 : f;
    }

    private void recalcularDenominaciones() {
        actualizando = true;
        try {
            DefaultTableModel m = (DefaultTableModel) tablaDenom.getModel();
            Map<Double, Integer> piezas = new LinkedHashMap<>();
            for (int i = 0; i < DelegateCorte.DENOMINACIONES.length; i++) {
                double d = DelegateCorte.DENOMINACIONES[i];
                int n = leerEntero(m.getValueAt(i, 1));
                m.setValueAt(n, i, 1);
                m.setValueAt(dinero(d * n), i, 2);
                piezas.put(d, n);
            }
            txtEfectivoContado.setText(String.format(Locale.US, "%.2f", facade.totalDenominaciones(piezas)));
        } catch (RuntimeException | ExceptionInInitializerError ex) {
            mostrarError(ex);
        } finally {
            actualizando = false;
        }
        actualizarDiferencia();
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

    private void actualizarDiferencia() {
        if (resumen == null) {
            return;
        }
        if (resumen.isCerrado()) {
            mostrarDiferencia(resumen.getDiferencia());
            return;
        }
        Double contado = parseMonto(txtEfectivoContado.getText());
        if (resumen.getDia() != null || contado == null) {
            lblDiferenciaValor.setText("—");
            lblDiferenciaValor.setForeground(GRIS);
            return;
        }
        mostrarDiferencia(resumen.diferenciaCon(contado, fondoActual()));
    }

    private void mostrarDiferencia(double dif) {
        if (dif > 0) {
            lblDiferenciaValor.setText("Sobrante " + dinero(dif));
            lblDiferenciaValor.setForeground(VERDE);
        } else if (dif < 0) {
            lblDiferenciaValor.setText("Faltante " + dinero(-dif));
            lblDiferenciaValor.setForeground(ROJO);
        } else {
            lblDiferenciaValor.setText("Sin diferencia");
            lblDiferenciaValor.setForeground(VERDE);
        }
    }



    private void imprimir() {
        if (resumen == null) {
            return;
        }
        try {
            boolean ticket = rbTicket.isSelected();
            ImpresionCorte.imprimir(this, facade.lineasImpresion(resumen, ticket ? 40 : 64), ticket, "Corte de caja");
        } catch (RuntimeException | ExceptionInInitializerError ex) {
            mostrarError(ex);
        }
    }



    static String dinero(double monto) {
        String s = String.format(Locale.US, "$%,.2f", Math.abs(monto));
        return monto < 0 ? "-" + s : s;
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

    static String capitalizar(String s) {
        if (s == null || s.isBlank()) {
            return "";
        }
        String m = s.toLowerCase(Locale.ROOT);
        return Character.toUpperCase(m.charAt(0)) + m.substring(1);
    }

    static Double parseMonto(String texto) {
        if (texto == null) {
            return null;
        }
        String s = texto.replace("$", "").replace(",", "").trim();
        if (!s.matches("\\d{1,8}(\\.\\d{1,2})?")) {
            return null;
        }
        double v = Double.parseDouble(s);
        return v > DelegateCorte.MONTO_MAXIMO ? null : v;
    }

    static LocalDate leerDia(String texto) {
        String t = texto == null ? "" : texto.trim();
        for (String patron : new String[]{"dd/MM/yyyy", "dd-MM-yyyy", "yyyy-MM-dd"}) {
            try {
                return LocalDate.parse(t, DateTimeFormatter.ofPattern(patron));
            } catch (DateTimeParseException ex) {
            }
        }
        return null;
    }

    private void aviso(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Corte de caja", JOptionPane.WARNING_MESSAGE);
    }

    private void mostrarError(Throwable ex) {
        if (ex instanceof IllegalArgumentException) {
            aviso(ex.getMessage());
            return;
        }
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this,
                "No se pudo completar la operación con la base de datos.",
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grupoImpresora = new javax.swing.ButtonGroup();
        lblTitulo = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        lblUsuario = new javax.swing.JLabel();
        cmbUsuario = new javax.swing.JComboBox<>();
        lblFondo = new javax.swing.JLabel();
        txtFondo = new javax.swing.JTextField();
        lblImpresora = new javax.swing.JLabel();
        rbTicket = new javax.swing.JRadioButton();
        rbLaser = new javax.swing.JRadioButton();
        btnCerrarCaja = new javax.swing.JButton();
        btnCorteFinal = new javax.swing.JButton();
        btnImprimir = new javax.swing.JButton();
        btnHistorial = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        panelTotal = new javax.swing.JPanel();
        lblTotalTitulo = new javax.swing.JLabel();
        lblTotalValor = new javax.swing.JLabel();
        panelEfectivo = new javax.swing.JPanel();
        lblEfectivoTitulo = new javax.swing.JLabel();
        lblEfectivoValor = new javax.swing.JLabel();
        panelTarjeta = new javax.swing.JPanel();
        lblTarjetaTitulo = new javax.swing.JLabel();
        lblTarjetaValor = new javax.swing.JLabel();
        lblResumenTitulo = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaResumen = new javax.swing.JTable();
        lblDesgloseTitulo = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaDenom = new javax.swing.JTable();
        panelCuadre = new javax.swing.JPanel();
        lblEfectivoContadoTitulo = new javax.swing.JLabel();
        txtEfectivoContado = new javax.swing.JTextField();
        lblDiferenciaTitulo = new javax.swing.JLabel();
        lblDiferenciaValor = new javax.swing.JLabel();
        lblVentasTitulo = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaVentas = new javax.swing.JTable();

        setBackground(new java.awt.Color(255, 255, 255));
        setMinimumSize(new java.awt.Dimension(1280, 770));
        setPreferredSize(new java.awt.Dimension(1280, 770));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTitulo.setFont(new java.awt.Font("Instrument Sans", 0, 24)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(136, 180, 123));
        lblTitulo.setText("Corte de caja");
        add(lblTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        jSeparator1.setBackground(new java.awt.Color(136, 180, 123));
        jSeparator1.setForeground(new java.awt.Color(136, 180, 123));
        add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 1280, 20));

        lblUsuario.setFont(new java.awt.Font("Instrument Sans", 0, 14)); // NOI18N
        lblUsuario.setText("Usuario:");
        add(lblUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 92, -1, -1));

        cmbUsuario.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cargando..." }));
        cmbUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbUsuarioActionPerformed(evt);
            }
        });
        add(cmbUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 86, 250, 32));

        lblFondo.setFont(new java.awt.Font("Instrument Sans", 0, 14)); // NOI18N
        lblFondo.setText("Fondo inicial:");
        add(lblFondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(385, 92, -1, -1));

        txtFondo.setFont(new java.awt.Font("Instrument Sans", 0, 14)); // NOI18N
        txtFondo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txtFondo.setText("0.00");
        add(txtFondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(485, 86, 110, 32));

        lblImpresora.setFont(new java.awt.Font("Instrument Sans", 0, 14)); // NOI18N
        lblImpresora.setText("Impresora:");
        add(lblImpresora, new org.netbeans.lib.awtextra.AbsoluteConstraints(625, 92, -1, -1));

        grupoImpresora.add(rbTicket);
        rbTicket.setBackground(new java.awt.Color(255, 255, 255));
        rbTicket.setFont(new java.awt.Font("Instrument Sans", 0, 14)); // NOI18N
        rbTicket.setText("Ticket");
        rbTicket.setSelected(true);
        add(rbTicket, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 88, 70, 28));

        grupoImpresora.add(rbLaser);
        rbLaser.setBackground(new java.awt.Color(255, 255, 255));
        rbLaser.setFont(new java.awt.Font("Instrument Sans", 0, 14)); // NOI18N
        rbLaser.setText("Láser");
        add(rbLaser, new org.netbeans.lib.awtextra.AbsoluteConstraints(785, 88, 70, 28));

        btnCerrarCaja.setBackground(new java.awt.Color(136, 180, 123));
        btnCerrarCaja.setFont(new java.awt.Font("Instrument Sans", 1, 12)); // NOI18N
        btnCerrarCaja.setForeground(new java.awt.Color(255, 255, 255));
        btnCerrarCaja.setText("Cerrar caja");
        btnCerrarCaja.setFocusPainted(false);
        btnCerrarCaja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarCajaActionPerformed(evt);
            }
        });
        add(btnCerrarCaja, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 130, 150, 34));

        btnCorteFinal.setBackground(new java.awt.Color(136, 180, 123));
        btnCorteFinal.setFont(new java.awt.Font("Instrument Sans", 1, 12)); // NOI18N
        btnCorteFinal.setForeground(new java.awt.Color(255, 255, 255));
        btnCorteFinal.setText("Corte final");
        btnCorteFinal.setFocusPainted(false);
        btnCorteFinal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCorteFinalActionPerformed(evt);
            }
        });
        add(btnCorteFinal, new org.netbeans.lib.awtextra.AbsoluteConstraints(205, 130, 150, 34));

        btnImprimir.setBackground(new java.awt.Color(136, 180, 123));
        btnImprimir.setFont(new java.awt.Font("Instrument Sans", 1, 12)); // NOI18N
        btnImprimir.setForeground(new java.awt.Color(255, 255, 255));
        btnImprimir.setText("Imprimir corte");
        btnImprimir.setFocusPainted(false);
        btnImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimirActionPerformed(evt);
            }
        });
        add(btnImprimir, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 130, 150, 34));

        btnHistorial.setBackground(new java.awt.Color(136, 180, 123));
        btnHistorial.setFont(new java.awt.Font("Instrument Sans", 1, 12)); // NOI18N
        btnHistorial.setForeground(new java.awt.Color(255, 255, 255));
        btnHistorial.setText("Historial");
        btnHistorial.setFocusPainted(false);
        btnHistorial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHistorialActionPerformed(evt);
            }
        });
        add(btnHistorial, new org.netbeans.lib.awtextra.AbsoluteConstraints(535, 130, 150, 34));

        btnActualizar.setBackground(new java.awt.Color(136, 180, 123));
        btnActualizar.setFont(new java.awt.Font("Instrument Sans", 1, 12)); // NOI18N
        btnActualizar.setForeground(new java.awt.Color(255, 255, 255));
        btnActualizar.setText("Actualizar");
        btnActualizar.setFocusPainted(false);
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });
        add(btnActualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 130, 150, 34));

        panelTotal.setBackground(new java.awt.Color(255, 255, 255));
        panelTotal.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(170, 170, 170), 1, true));
        panelTotal.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTotalTitulo.setFont(new java.awt.Font("Instrument Sans", 0, 14)); // NOI18N
        lblTotalTitulo.setForeground(new java.awt.Color(90, 90, 90));
        lblTotalTitulo.setText("Total de ventas");
        panelTotal.add(lblTotalTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 10, -1, -1));

        lblTotalValor.setFont(new java.awt.Font("Instrument Sans", 1, 20)); // NOI18N
        lblTotalValor.setText("$0.00");
        panelTotal.add(lblTotalValor, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 36, 360, -1));
        add(panelTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 175, 390, 72));

        panelEfectivo.setBackground(new java.awt.Color(255, 255, 255));
        panelEfectivo.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(170, 170, 170), 1, true));
        panelEfectivo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblEfectivoTitulo.setFont(new java.awt.Font("Instrument Sans", 0, 14)); // NOI18N
        lblEfectivoTitulo.setForeground(new java.awt.Color(90, 90, 90));
        lblEfectivoTitulo.setText("Ventas en efectivo");
        panelEfectivo.add(lblEfectivoTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 10, -1, -1));

        lblEfectivoValor.setFont(new java.awt.Font("Instrument Sans", 1, 20)); // NOI18N
        lblEfectivoValor.setText("$0.00");
        panelEfectivo.add(lblEfectivoValor, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 36, 360, -1));
        add(panelEfectivo, new org.netbeans.lib.awtextra.AbsoluteConstraints(445, 175, 390, 72));

        panelTarjeta.setBackground(new java.awt.Color(255, 255, 255));
        panelTarjeta.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(170, 170, 170), 1, true));
        panelTarjeta.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTarjetaTitulo.setFont(new java.awt.Font("Instrument Sans", 0, 14)); // NOI18N
        lblTarjetaTitulo.setForeground(new java.awt.Color(90, 90, 90));
        lblTarjetaTitulo.setText("Ventas tarjeta/digital");
        panelTarjeta.add(lblTarjetaTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 10, -1, -1));

        lblTarjetaValor.setFont(new java.awt.Font("Instrument Sans", 1, 20)); // NOI18N
        lblTarjetaValor.setText("$0.00");
        panelTarjeta.add(lblTarjetaValor, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 36, 360, -1));
        add(panelTarjeta, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 175, 390, 72));

        lblResumenTitulo.setBackground(new java.awt.Color(232, 240, 229));
        lblResumenTitulo.setFont(new java.awt.Font("Instrument Sans", 0, 18)); // NOI18N
        lblResumenTitulo.setOpaque(true);
        lblResumenTitulo.setText("  Resumen del turno");
        add(lblResumenTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 265, 600, 36));

        jScrollPane1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(170, 170, 170), 1, true));
        tablaResumen.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Concepto", "Transacciones", "Monto"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tablaResumen);

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 301, 600, 176));

        lblDesgloseTitulo.setBackground(new java.awt.Color(232, 240, 229));
        lblDesgloseTitulo.setFont(new java.awt.Font("Instrument Sans", 0, 18)); // NOI18N
        lblDesgloseTitulo.setOpaque(true);
        lblDesgloseTitulo.setText("  Desglose por denominación (efectivo)");
        add(lblDesgloseTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 265, 580, 36));

        jScrollPane2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(170, 170, 170), 1, true));
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
        jScrollPane2.setViewportView(tablaDenom);

        add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 301, 580, 246));

        panelCuadre.setBackground(new java.awt.Color(255, 255, 255));
        panelCuadre.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(170, 170, 170), 1, true));
        panelCuadre.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblEfectivoContadoTitulo.setFont(new java.awt.Font("Instrument Sans", 0, 14)); // NOI18N
        lblEfectivoContadoTitulo.setText("Efectivo contado:");
        panelCuadre.add(lblEfectivoContadoTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 18, -1, -1));

        txtEfectivoContado.setFont(new java.awt.Font("Instrument Sans", 0, 14)); // NOI18N
        txtEfectivoContado.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        panelCuadre.add(txtEfectivoContado, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 14, 110, 32));

        lblDiferenciaTitulo.setFont(new java.awt.Font("Instrument Sans", 0, 14)); // NOI18N
        lblDiferenciaTitulo.setText("Diferencia:");
        panelCuadre.add(lblDiferenciaTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 18, -1, -1));

        lblDiferenciaValor.setFont(new java.awt.Font("Instrument Sans", 1, 16)); // NOI18N
        lblDiferenciaValor.setText("—");
        panelCuadre.add(lblDiferenciaValor, new org.netbeans.lib.awtextra.AbsoluteConstraints(375, 16, 215, -1));
        add(panelCuadre, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 487, 600, 60));

        lblVentasTitulo.setBackground(new java.awt.Color(232, 240, 229));
        lblVentasTitulo.setFont(new java.awt.Font("Instrument Sans", 0, 18)); // NOI18N
        lblVentasTitulo.setOpaque(true);
        lblVentasTitulo.setText("  Ventas del periodo");
        add(lblVentasTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 562, 1200, 32));

        jScrollPane3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(170, 170, 170), 1, true));
        tablaVentas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Folio", "Fecha y hora", "Cajero", "Método", "Estado", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tablaVentas);

        add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 594, 1200, 160));
    }// </editor-fold>//GEN-END:initComponents

    private void cmbUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbUsuarioActionPerformed
        if (actualizando || usuarioActual == null) {
            return;
        }
        limpiarCaptura();
        cargarTurno();
    }//GEN-LAST:event_cmbUsuarioActionPerformed

    private void btnCerrarCajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarCajaActionPerformed
        if (resumen == null || !esTurno()) {
            aviso("Solo se puede cerrar la caja desde el turno en curso. Pulsa Actualizar.");
            return;
        }
        Double contado = parseMonto(txtEfectivoContado.getText());
        if (contado == null) {
            aviso("Captura el efectivo contado (o el desglose por denominación) antes de cerrar la caja.");
            txtEfectivoContado.requestFocus();
            return;
        }
        Double fondo = txtFondo.getText().isBlank() ? Double.valueOf(0) : parseMonto(txtFondo.getText());
        if (fondo == null) {
            aviso("El fondo inicial no es válido.");
            txtFondo.requestFocus();
            return;
        }
        double dif = resumen.diferenciaCon(contado, fondo);
        String estadoCuadre = dif > 0 ? "Sobrante: " + dinero(dif)
                : dif < 0 ? "Faltante: " + dinero(-dif) : "Sin diferencia";
        int op = JOptionPane.showConfirmDialog(this,
                "Se cerrará el turno de " + usuarioSeleccionado().getNombrePersona() + ":\n\n"
                + "Ventas: " + resumen.getNumVentas() + " por " + dinero(resumen.getTotalVentas()) + "\n"
                + "Efectivo esperado en caja: " + dinero(resumen.efectivoEsperadoEnCaja(fondo)) + "\n"
                + "Efectivo contado: " + dinero(contado) + "\n"
                + estadoCuadre + "\n\n¿Cerrar la caja?",
                "Cerrar caja", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (op != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            ResumenCorte cerrado = facade.cerrarCorte(usuarioActual, usuarioSeleccionado(), contado, fondo);
            mostrar(cerrado);
            int imp = JOptionPane.showConfirmDialog(this,
                    "Corte #" + cerrado.getIdCorte() + " registrado.\n¿Deseas imprimirlo ahora?",
                    "Corte registrado", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
            if (imp == JOptionPane.YES_OPTION) {
                imprimir();
            }
        } catch (RuntimeException | ExceptionInInitializerError ex) {
            mostrarError(ex);
            cargarTurno();
        }
    }//GEN-LAST:event_btnCerrarCajaActionPerformed

    private void btnCorteFinalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCorteFinalActionPerformed
        String texto = JOptionPane.showInputDialog(this,
                "Día a consolidar (dd/MM/yyyy):", LocalDate.now().format(FECHA));
        if (texto == null) {
            return;
        }
        LocalDate dia = leerDia(texto);
        if (dia == null) {
            aviso("Fecha no válida. Usa el formato dd/MM/yyyy.");
            return;
        }
        try {
            mostrar(facade.corteFinal(usuarioActual, dia));
        } catch (RuntimeException | ExceptionInInitializerError ex) {
            mostrarError(ex);
        }
    }//GEN-LAST:event_btnCorteFinalActionPerformed

    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed
        imprimir();
    }//GEN-LAST:event_btnImprimirActionPerformed

    private void btnHistorialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHistorialActionPerformed
        java.awt.Window ventana = SwingUtilities.getWindowAncestor(this);
        HistorialCortesDialog dialogo = new HistorialCortesDialog(
                ventana instanceof java.awt.Frame ? (java.awt.Frame) ventana : null, true, usuarioActual);
        dialogo.setLocationRelativeTo(this);
        dialogo.setVisible(true);
    }//GEN-LAST:event_btnHistorialActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        limpiarCaptura();
        cargarTurno();
    }//GEN-LAST:event_btnActualizarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnCerrarCaja;
    private javax.swing.JButton btnCorteFinal;
    private javax.swing.JButton btnHistorial;
    private javax.swing.JButton btnImprimir;
    private javax.swing.JComboBox<String> cmbUsuario;
    private javax.swing.ButtonGroup grupoImpresora;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblDesgloseTitulo;
    private javax.swing.JLabel lblDiferenciaTitulo;
    private javax.swing.JLabel lblDiferenciaValor;
    private javax.swing.JLabel lblEfectivoContadoTitulo;
    private javax.swing.JLabel lblEfectivoTitulo;
    private javax.swing.JLabel lblEfectivoValor;
    private javax.swing.JLabel lblFondo;
    private javax.swing.JLabel lblImpresora;
    private javax.swing.JLabel lblResumenTitulo;
    private javax.swing.JLabel lblTarjetaTitulo;
    private javax.swing.JLabel lblTarjetaValor;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblTotalTitulo;
    private javax.swing.JLabel lblTotalValor;
    private javax.swing.JLabel lblUsuario;
    private javax.swing.JLabel lblVentasTitulo;
    private javax.swing.JPanel panelCuadre;
    private javax.swing.JPanel panelEfectivo;
    private javax.swing.JPanel panelTarjeta;
    private javax.swing.JPanel panelTotal;
    private javax.swing.JRadioButton rbLaser;
    private javax.swing.JRadioButton rbTicket;
    private javax.swing.JTable tablaDenom;
    private javax.swing.JTable tablaResumen;
    private javax.swing.JTable tablaVentas;
    private javax.swing.JTextField txtEfectivoContado;
    private javax.swing.JTextField txtFondo;
    // End of variables declaration//GEN-END:variables

}
