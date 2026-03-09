/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.etiquetas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.util.Map;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;
import javax.swing.SwingConstants;

/**
 *
 * @author JEOS
 */
public class Pantalla extends javax.swing.JFrame {

    private JLabel lblt1, lblt2, lblt3, lblt4;
    // Variables para guardar el último turno mostrado
    private String lastT1 = "", lastT2 = "", lastT3 = "", lastT4 = "";
    private JPanel panelCaja1, panelCaja2, panelCaja3, panelCaja4;



    public Pantalla() {
        this.setUndecorated(true); // sin bordes
        initComponents();
        java.awt.GraphicsDevice gd = 
            java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        gd.setFullScreenWindow(this);

        FondoPanel fondo = new FondoPanel();
        setContentPane(fondo);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        configurarPantalla();
        iniciarActualizacionAutomatica();
        
    }

    private void configurarPantalla() {
        // Crear un JLayeredPane para superponer elementos
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayout(new OverlayLayout(layeredPane));
        layeredPane.setOpaque(false);

        // Panel con las 4 cajas
        JPanel panelCajas = new JPanel(new GridLayout(2, 2));
        panelCajas.setOpaque(false);

        // Guardar referencia a cada panel
        panelCaja1 = crearCajaConImagen("Caja 1", lblt1 = new JLabel(), "/images/roba.png", true);
        panelCaja2 = crearCajaConImagen("Caja 2", lblt2 = new JLabel(), "/images/apachi.png", false);
        panelCaja3 = crearCajaConImagen("Caja 3", lblt3 = new JLabel(), "/images/roba.png", true);
        panelCaja4 = crearCajaConImagen("Caja 4", lblt4 = new JLabel(), "/images/apachi.png", false);


        panelCajas.add(panelCaja1);
        panelCajas.add(panelCaja2);
        panelCajas.add(panelCaja3);
        panelCajas.add(panelCaja4);

        // Logo al centro (superpuesto)
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/aztecalogo.png"));
        Image img = icon.getImage();

        // Escalar la imagen a la mitad de su tamaño original
        Image scaledImg = img.getScaledInstance(
                icon.getIconWidth()/2 ,
                icon.getIconHeight()/2,
                Image.SCALE_SMOOTH
        );

        JLabel lblLogo = new JLabel(new ImageIcon(scaledImg));
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        lblLogo.setVerticalAlignment(SwingConstants.CENTER);
        lblLogo.setAlignmentX(0.5f);
        lblLogo.setAlignmentY(0.5f);

        // Agregar al layeredPane
        layeredPane.add(panelCajas, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(lblLogo, JLayeredPane.PALETTE_LAYER);
        
        // Agregar al frame
        setLayout(new BorderLayout());
        add(layeredPane, BorderLayout.CENTER);
    }


    private JPanel crearCajaConImagen(String titulo, JLabel lblTurno, String imagePath, boolean izquierda) {
    JPanel panel = new JPanel(new GridBagLayout());
    panel.setOpaque(false);
    panel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weightx = 1.0;

    // Título
    JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
    lblTitulo.setForeground(Color.WHITE);
    lblTitulo.setFont(new Font("Arial", Font.BOLD, 64));

    gbc.gridy = 0;
    gbc.weighty = 1;
    gbc.insets = new Insets(20, 0, 20, 0);
    panel.add(lblTitulo, gbc);

    // Panel interno con BorderLayout
    JPanel turnoPanel = new JPanel(new BorderLayout());
    turnoPanel.setOpaque(false);

    // Imagen
    ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
    Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH); // más grande
    JLabel lblImagen = new JLabel(new ImageIcon(img));

    // Turno
    lblTurno.setText("");
    lblTurno.setForeground(Color.WHITE);
    lblTurno.setHorizontalAlignment(SwingConstants.CENTER);

    // Colocar según posición
    if (izquierda) {
        turnoPanel.add(lblImagen, BorderLayout.WEST);   // imagen pegada a la izquierda
        turnoPanel.add(lblTurno, BorderLayout.CENTER);  // turno ocupa el centro
    } else {
        turnoPanel.add(lblTurno, BorderLayout.CENTER);  // turno ocupa el centro
        turnoPanel.add(lblImagen, BorderLayout.EAST);   // imagen pegada a la derecha
    }

    gbc.gridy = 1;
    gbc.weighty = 2;
    gbc.insets = new Insets(20, 0, 20, 0);
    panel.add(turnoPanel, gbc);

    // Ajuste dinámico de fuente para que el turno sea grande
    panel.addComponentListener(new java.awt.event.ComponentAdapter() {
        @Override
        public void componentResized(java.awt.event.ComponentEvent e) {
            int ancho = panel.getWidth();
            int alto = panel.getHeight();
            int fontSize = Math.min(ancho / 3, alto / 2); // más grande que antes
            lblTurno.setFont(new Font("Arial", Font.BOLD, fontSize));
        }
    });

    return panel;
}
    
    private void iniciarActualizacionAutomatica() {
        int delay = 1500; // 1.5 segundos
        new javax.swing.Timer(delay, e -> actualizarTurnos()).start();
    }

    private void actualizarTurnos() {
        Map<Integer, Integer> turnos = ConexionEpson.cargarXCaja();
        if (turnos == null) return;

        actualizarLabel(lblt1, String.valueOf(turnos.getOrDefault(1, 0)), 1, panelCaja1);
        actualizarLabel(lblt2, String.valueOf(turnos.getOrDefault(2, 0)), 2, panelCaja2);
        actualizarLabel(lblt3, String.valueOf(turnos.getOrDefault(3, 0)), 3, panelCaja3);
        actualizarLabel(lblt4, String.valueOf(turnos.getOrDefault(4, 0)), 4, panelCaja4);
    }


    private void actualizarLabel(JLabel lbl, String nuevoTurno, int caja, JPanel panel) {
        String lastTurno = switch (caja) {
            case 1 -> lastT1;
            case 2 -> lastT2;
            case 3 -> lastT3;
            case 4 -> lastT4;
            default -> "";
        };

        if (!nuevoTurno.equals(lastTurno)) {
            // Actualizar texto
            lbl.setText(nuevoTurno);

            // Guardar nuevo valor
            switch (caja) {
                case 1 -> lastT1 = nuevoTurno;
                case 2 -> lastT2 = nuevoTurno;
                case 3 -> lastT3 = nuevoTurno;
                case 4 -> lastT4 = nuevoTurno;
            }

            // Activar parpadeo de toda la caja
            reproducirSonido(lbl);
        }
    }

    public void reproducirSonido(JLabel panel) {
        new Thread(() -> { // Hilo para no bloquear la interfaz
            try {
                // Primer bloque de 2 beeps
                for (int i = 0; i < 2; i++) {
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(
                            getClass().getResource("/sounds/turno.wav")
                    );

                    Clip clip = AudioSystem.getClip();
                    clip.open(audioStream);
                    clip.start();
                    parpadearTurno(panel);

                    // Espera 1.5 segundos después del primer beep
                    if (i == 0) {
                        Thread.sleep(500);
                    }
                }

                // Espera 4 segundos antes del segundo bloque
                Thread.sleep(4000);

                // Segundo bloque de 2 beeps
                for (int i = 0; i < 2; i++) {
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(
                            getClass().getResource("/sounds/turno.wav")
                    );

                    Clip clip = AudioSystem.getClip();
                    clip.open(audioStream);
                    clip.start();
                    parpadearTurno(panel);
                    if (i == 0) {
                        Thread.sleep(1500);
                    }
                }

                System.out.println("Reproducción completada");

            } catch (Exception e) {
                System.out.println("Error al reproducir sonido: " + e.getMessage());
            }
        }).start();
    }


    private void parpadearCaja(JPanel panel) {
        final int[] count = {0};
        javax.swing.Timer blinkTimer = new javax.swing.Timer(300, e -> {
            for (Component comp : panel.getComponents()) {
                if (comp instanceof JLabel lbl) {
                    lbl.setVisible(!lbl.isVisible());
                }
            }
            count[0]++;
            if (count[0] >= 6) {
                ((javax.swing.Timer) e.getSource()).stop();
                // asegurar que todos los labels queden visibles
                for (Component comp : panel.getComponents()) {
                    if (comp instanceof JLabel lbl) {
                        lbl.setVisible(true);
                    }
                }
            }
        });
        blinkTimer.start();
    }
    
    private void parpadearTurno(JLabel lblTurno) {
        final int[] count = {0};
        javax.swing.Timer blinkTimer = new javax.swing.Timer(300, e -> {
            lblTurno.setVisible(!lblTurno.isVisible()); // alterna visibilidad del número
            count[0]++;
            if (count[0] >= 6) { // 3 ciclos ON/OFF
                ((javax.swing.Timer) e.getSource()).stop();
                lblTurno.setVisible(true); // asegurar que quede visible
            }
        });
        blinkTimer.start();
    }







    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1184, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 729, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Pantalla.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Pantalla.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Pantalla.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Pantalla.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Pantalla().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
