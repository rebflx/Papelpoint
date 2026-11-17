/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package mx.desarrollo.ui;

/**
 *
 * @author erick
 */
public class PnlSugerencias extends javax.swing.JPanel {

    /**
     * Creates new form PnlSugerencias
     */
   public PnlSugerencias() {
    initComponents();

    setBackground(java.awt.Color.WHITE);

    lstSugerencias.setBackground(java.awt.Color.WHITE);

    lstSugerencias.setFixedCellHeight(48);

    lstSugerencias.setSelectionMode(
            javax.swing.ListSelectionModel.SINGLE_SELECTION);

    lstSugerencias.setBorder(
            javax.swing.BorderFactory.createEmptyBorder());

    lstSugerencias.setFocusable(false);

    jScrollPane1.setBorder(
            javax.swing.BorderFactory.createLineBorder(
                    new java.awt.Color(225, 228, 232)));

    jScrollPane1.getViewport().setBackground(
            java.awt.Color.WHITE);

    jScrollPane1.setHorizontalScrollBarPolicy(
            javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

    jScrollPane1.getVerticalScrollBar().setUnitIncrement(12);
}

  
     
     
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        lstSugerencias = new javax.swing.JList<>();

        setLayout(new java.awt.BorderLayout());

        lstSugerencias.setModel(new javax.swing.DefaultListModel<mx.desarrollo.entity.Producto>());
        jScrollPane1.setViewportView(lstSugerencias);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList<mx.desarrollo.entity.Producto> lstSugerencias;
    // End of variables declaration//GEN-END:variables

   public javax.swing.JList<mx.desarrollo.entity.Producto> getLista() {
        return lstSugerencias;
    }
}
