/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.etiquetas;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author JEOS
 */
class FondoPanel extends JPanel {
    private Image imagen;

    public FondoPanel() {
        imagen = new ImageIcon(getClass().getResource("/images/bgpantalla.png")).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Escalar la imagen al tamaño del panel
        g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
    }
}
