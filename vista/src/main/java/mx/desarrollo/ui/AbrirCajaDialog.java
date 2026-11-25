package mx.desarrollo.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import mx.desarrollo.delegate.ResumenCorte;
import mx.desarrollo.delegate.SesionCaja;
import mx.desarrollo.entity.Usuario;
import mx.desarrollo.integration.ServiceFacadeLocator;

/**
 * Ventana emergente para abrir la caja: captura el fondo inicial con el que
 * arranca el turno y lo registra en {@link SesionCaja}.
 * <p>
 * La parte visual se edita en NetBeans (AbrirCajaDialog.form); aquí solo queda
 * la lógica y los ajustes que el diseñador no maneja.
 */
public class AbrirCajaDialog extends javax.swing.JDialog {

    private static final Color VERDE_BOTON = new Color(136, 180, 123);
    private static final DateTimeFormatter FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final Usuario usuario;
    private SesionCaja.Apertura apertura;

    public AbrirCajaDialog(Frame parent, Usuario usuario) {
        super(parent, "Abrir caja", true);
        this.usuario = usuario;
        initComponents();
        configurar();
        pack();
        setLocationRelativeTo(parent);
    }

    public SesionCaja.Apertura getApertura() {
        return apertura;
    }

    private void configurar() {
        getContentPane().setBackground(Color.WHITE);
        estilizarBotonPrimario(btnAbrir);

        String nombre = usuario != null && usuario.getNombrePersona() != null ? usuario.getNombrePersona() : "";
        lblCajero.setText(nombre);
        lblFecha.setText(LocalDateTime.now().format(FECHA_HORA));

        lblAviso.setVisible(false);
        String pendientes = textoVentasPendientes();
        if (pendientes != null) {
            lblAviso.setText("<html><body style='width:300px'>" + pendientes + "</body></html>");
            lblAviso.setVisible(true);
        }

        getRootPane().setDefaultButton(btnAbrir);
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cerrar");
        getRootPane().getActionMap().put("cerrar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                txtFondo.requestFocusInWindow();
                txtFondo.selectAll();
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

    private String textoVentasPendientes() {
        try {
            ResumenCorte turno = ServiceFacadeLocator.getInstanceFacadeCorteCaja().consultarTurno(usuario, usuario);
            if (!turno.getVentas().isEmpty()) {
                String desde = turno.getFechaInicio() != null ? turno.getFechaInicio().format(FECHA_HORA) : "antes";
                return "Tienes <b>" + turno.getVentas().size() + "</b> venta(s) sin corte desde " + desde
                        + ". Se incluirán en el corte de este turno.";
            }
        } catch (RuntimeException | ExceptionInInitializerError ex) {
        }
        return null;
    }

    private void abrir() {
        String texto = txtFondo.getText();
        if (texto == null || texto.isBlank()) {
            error("Captura el fondo inicial (puede ser 0.00).");
            return;
        }
        Double fondo = CortesCajaPanel.parseMonto(texto);
        if (fondo == null) {
            error("El fondo inicial no es válido. Usa solo números (hasta 8 dígitos y 2 decimales).");
            return;
        }
        try {
            apertura = SesionCaja.abrir(usuario, fondo);
            dispose();
        } catch (IllegalArgumentException ex) {
            error(ex.getMessage());
        }
    }

    private void error(String mensaje) {
        lblError.setText("<html><body style='width:300px'>" + mensaje + "</body></html>");
        txtFondo.requestFocusInWindow();
        txtFondo.selectAll();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlEncabezado = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        lblSubtitulo = new javax.swing.JLabel();
        pnlCuerpo = new javax.swing.JPanel();
        pnlCajero = new javax.swing.JPanel();
        lblCajeroEtq = new javax.swing.JLabel();
        lblCajero = new javax.swing.JLabel();
        pnlFecha = new javax.swing.JPanel();
        lblFechaEtq = new javax.swing.JLabel();
        lblFecha = new javax.swing.JLabel();
        lblAviso = new javax.swing.JLabel();
        lblFondoEtq = new javax.swing.JLabel();
        pnlCampo = new javax.swing.JPanel();
        lblSigno = new javax.swing.JLabel();
        txtFondo = new javax.swing.JTextField();
        pnlRapidos = new javax.swing.JPanel();
        btn200 = new javax.swing.JButton();
        btn500 = new javax.swing.JButton();
        btn1000 = new javax.swing.JButton();
        lblAyuda = new javax.swing.JLabel();
        lblError = new javax.swing.JLabel();
        pnlPie = new javax.swing.JPanel();
        btnCancelar = new javax.swing.JButton();
        btnAbrir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Abrir caja");
        setResizable(false);
        getContentPane().setLayout(new java.awt.BorderLayout());

        pnlEncabezado.setBackground(new java.awt.Color(196, 217, 191));
        pnlEncabezado.setBorder(javax.swing.BorderFactory.createEmptyBorder(14, 22, 14, 22));
        pnlEncabezado.setLayout(new java.awt.BorderLayout());

        lblTitulo.setText("Abrir caja");
        lblTitulo.setFont(new java.awt.Font("Instrument Sans", 1, 20));
        lblTitulo.setForeground(new java.awt.Color(0, 0, 0));

        pnlEncabezado.add(lblTitulo, java.awt.BorderLayout.PAGE_START);

        lblSubtitulo.setText("Registra el dinero con el que inicia tu turno");
        lblSubtitulo.setFont(new java.awt.Font("Instrument Sans", 0, 12));
        lblSubtitulo.setForeground(new java.awt.Color(51, 51, 51));

        pnlEncabezado.add(lblSubtitulo, java.awt.BorderLayout.PAGE_END);

        getContentPane().add(pnlEncabezado, java.awt.BorderLayout.PAGE_START);

        pnlCuerpo.setBackground(new java.awt.Color(255, 255, 255));
        pnlCuerpo.setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 22, 6, 22));
        pnlCuerpo.setLayout(new java.awt.GridBagLayout());

        pnlCajero.setBackground(new java.awt.Color(255, 255, 255));
        pnlCajero.setLayout(new java.awt.BorderLayout(10, 0));

        lblCajeroEtq.setText("Cajero:");
        lblCajeroEtq.setFont(new java.awt.Font("Instrument Sans", 1, 13));
        lblCajeroEtq.setForeground(new java.awt.Color(51, 51, 51));
        lblCajeroEtq.setPreferredSize(new java.awt.Dimension(100, 20));

        pnlCajero.add(lblCajeroEtq, java.awt.BorderLayout.LINE_START);

        lblCajero.setText(" ");
        lblCajero.setFont(new java.awt.Font("Instrument Sans", 0, 13));
        lblCajero.setForeground(new java.awt.Color(51, 51, 51));

        pnlCajero.add(lblCajero, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 0);
        pnlCuerpo.add(pnlCajero, gridBagConstraints);

        pnlFecha.setBackground(new java.awt.Color(255, 255, 255));
        pnlFecha.setLayout(new java.awt.BorderLayout(10, 0));

        lblFechaEtq.setText("Fecha y hora:");
        lblFechaEtq.setFont(new java.awt.Font("Instrument Sans", 1, 13));
        lblFechaEtq.setForeground(new java.awt.Color(51, 51, 51));
        lblFechaEtq.setPreferredSize(new java.awt.Dimension(100, 20));

        pnlFecha.add(lblFechaEtq, java.awt.BorderLayout.LINE_START);

        lblFecha.setText(" ");
        lblFecha.setFont(new java.awt.Font("Instrument Sans", 0, 13));
        lblFecha.setForeground(new java.awt.Color(51, 51, 51));

        pnlFecha.add(lblFecha, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 0);
        pnlCuerpo.add(pnlFecha, gridBagConstraints);

        lblAviso.setFont(new java.awt.Font("Instrument Sans", 0, 12));
        lblAviso.setForeground(new java.awt.Color(51, 51, 51));
        lblAviso.setBackground(new java.awt.Color(255, 243, 205));
        lblAviso.setOpaque(true);
        lblAviso.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 10, 8, 10));

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 6, 0);
        pnlCuerpo.add(lblAviso, gridBagConstraints);

        lblFondoEtq.setText("Fondo inicial de caja");
        lblFondoEtq.setFont(new java.awt.Font("Instrument Sans", 1, 13));
        lblFondoEtq.setForeground(new java.awt.Color(51, 51, 51));

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 4, 0);
        pnlCuerpo.add(lblFondoEtq, gridBagConstraints);

        pnlCampo.setBackground(new java.awt.Color(255, 255, 255));
        pnlCampo.setLayout(new java.awt.BorderLayout(6, 0));

        lblSigno.setText("$");
        lblSigno.setFont(new java.awt.Font("Instrument Sans", 1, 16));

        pnlCampo.add(lblSigno, java.awt.BorderLayout.LINE_START);

        txtFondo.setFont(new java.awt.Font("Instrument Sans", 0, 16));
        txtFondo.setText("0.00");
        txtFondo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtFondo.setPreferredSize(new java.awt.Dimension(160, 34));

        pnlCampo.add(txtFondo, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 0);
        pnlCuerpo.add(pnlCampo, gridBagConstraints);

        pnlRapidos.setBackground(new java.awt.Color(255, 255, 255));
        pnlRapidos.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 6, 0));

        btn200.setText("$200");
        btn200.setMnemonic('2');
        btn200.setFont(new java.awt.Font("Instrument Sans", 0, 11));
        btn200.setFocusable(false);
        btn200.setActionCommand("200.00");
        btn200.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRapidoActionPerformed(evt);
            }
        });

        pnlRapidos.add(btn200);

        btn500.setText("$500");
        btn500.setMnemonic('5');
        btn500.setFont(new java.awt.Font("Instrument Sans", 0, 11));
        btn500.setFocusable(false);
        btn500.setActionCommand("500.00");
        btn500.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRapidoActionPerformed(evt);
            }
        });

        pnlRapidos.add(btn500);

        btn1000.setText("$1,000");
        btn1000.setMnemonic('1');
        btn1000.setFont(new java.awt.Font("Instrument Sans", 0, 11));
        btn1000.setFocusable(false);
        btn1000.setActionCommand("1000.00");
        btn1000.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRapidoActionPerformed(evt);
            }
        });

        pnlRapidos.add(btn1000);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        pnlCuerpo.add(pnlRapidos, gridBagConstraints);

        lblAyuda.setText("<html><body style='width:300px'>Es el cambio con el que arrancas. Al cerrar la caja se usará para calcular el efectivo esperado.</body></html>");
        lblAyuda.setFont(new java.awt.Font("Instrument Sans", 0, 11));
        lblAyuda.setForeground(new java.awt.Color(110, 110, 110));

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 4, 0);
        pnlCuerpo.add(lblAyuda, gridBagConstraints);

        lblError.setText(" ");
        lblError.setFont(new java.awt.Font("Instrument Sans", 1, 12));
        lblError.setForeground(new java.awt.Color(200, 40, 40));

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        pnlCuerpo.add(lblError, gridBagConstraints);

        getContentPane().add(pnlCuerpo, java.awt.BorderLayout.CENTER);

        pnlPie.setBackground(new java.awt.Color(255, 255, 255));
        pnlPie.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 12, 8, 12));
        pnlPie.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 10));

        btnCancelar.setBackground(new java.awt.Color(195, 217, 189));
        btnCancelar.setFont(new java.awt.Font("Instrument Sans", 1, 12)); // NOI18N
        btnCancelar.setForeground(new java.awt.Color(46, 94, 42));
        btnCancelar.setText("Cancelar");
        btnCancelar.setMnemonic('C');
        btnCancelar.setFocusPainted(false);
        btnCancelar.setPreferredSize(new java.awt.Dimension(110, 34));
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        pnlPie.add(btnCancelar);

        btnAbrir.setBackground(new java.awt.Color(136, 180, 123));
        btnAbrir.setFont(new java.awt.Font("Instrument Sans", 1, 12));
        btnAbrir.setForeground(new java.awt.Color(255, 255, 255));
        btnAbrir.setText("Abrir caja");
        btnAbrir.setMnemonic('A');
        btnAbrir.setFocusPainted(false);
        btnAbrir.setOpaque(true);
        btnAbrir.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 14, 6, 14));
        btnAbrir.setPreferredSize(new java.awt.Dimension(130, 34));
        btnAbrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAbrirActionPerformed(evt);
            }
        });

        pnlPie.add(btnAbrir);

        getContentPane().add(pnlPie, java.awt.BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRapidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRapidoActionPerformed
        txtFondo.setText(evt.getActionCommand());
        lblError.setText(" ");
    }//GEN-LAST:event_btnRapidoActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnAbrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAbrirActionPerformed
        abrir();
    }//GEN-LAST:event_btnAbrirActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn1000;
    private javax.swing.JButton btn200;
    private javax.swing.JButton btn500;
    private javax.swing.JButton btnAbrir;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JLabel lblAviso;
    private javax.swing.JLabel lblAyuda;
    private javax.swing.JLabel lblCajero;
    private javax.swing.JLabel lblCajeroEtq;
    private javax.swing.JLabel lblError;
    private javax.swing.JLabel lblFecha;
    private javax.swing.JLabel lblFechaEtq;
    private javax.swing.JLabel lblFondoEtq;
    private javax.swing.JLabel lblSigno;
    private javax.swing.JLabel lblSubtitulo;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JPanel pnlCajero;
    private javax.swing.JPanel pnlCampo;
    private javax.swing.JPanel pnlCuerpo;
    private javax.swing.JPanel pnlEncabezado;
    private javax.swing.JPanel pnlFecha;
    private javax.swing.JPanel pnlPie;
    private javax.swing.JPanel pnlRapidos;
    private javax.swing.JTextField txtFondo;
    // End of variables declaration//GEN-END:variables
}
