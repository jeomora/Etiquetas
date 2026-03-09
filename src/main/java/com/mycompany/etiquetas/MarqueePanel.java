/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.etiquetas;

/**
 *
 * @author PROGRAMEITOR
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MarqueePanel extends JPanel implements ActionListener {
    private String text;
    private int x;
    private Timer timer;

    public MarqueePanel(String text, int speed) {
        this.text = text + "   "; // espacio entre repeticiones
        this.x = getWidth();
        setPreferredSize(new Dimension(600, 30));
        setBackground(Color.BLACK);
        setForeground(Color.YELLOW);
        setFont(new Font("SansSerif", Font.BOLD, 18));
        timer = new Timer(speed, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        x -= 2;
        if (x + textWidth < 0) {
            x = getWidth();
        }
        g.setColor(getForeground());
        g.drawString(text, x, getHeight() / 2 + fm.getAscent() / 2 - 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    public void setText(String newText) {
        this.text = newText + "   ";
        this.x = getWidth();
    }
}
