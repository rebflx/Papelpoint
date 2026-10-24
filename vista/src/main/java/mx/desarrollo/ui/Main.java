package mx.desarrollo.ui;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            JFrame ventana = new JFrame("PapelPoint");

            ventana.setSize(900, 600);
            ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            ventana.setLocationRelativeTo(null);

            JLabel titulo = new JLabel(
                    "Bienvenido a PapelPoint",
                    SwingConstants.CENTER
            );

            ventana.add(titulo);
            ventana.setVisible(true);
        });
    }
}