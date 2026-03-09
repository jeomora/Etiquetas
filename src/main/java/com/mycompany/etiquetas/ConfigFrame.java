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

public class ConfigFrame extends JFrame {

    private JTextField comPortField;
    private JButton btnSave;
    private String comPort;
    private Etis mainFrame;  // Hacemos una referencia a la clase Etis

    // Constructor de ConfigFrame que recibe el puerto COM y la referencia de Etis
    public ConfigFrame(String comPort, Etis mainFrame) {
        this.comPort = comPort;
        this.mainFrame = mainFrame;  // Guardamos la referencia a Etis

        setTitle("Configuración del puerto COM");
        setSize(300, 150);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Layout
        setLayout(new GridLayout(3, 2));

        // Crear componentes
        JLabel comPortLabel = new JLabel("Puerto COM:");
        comPortField = new JTextField(comPort);
        btnSave = new JButton("Guardar");

        // Acción del botón "Guardar"
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newComPort = comPortField.getText(); // Obtener el nuevo puerto COM
                System.out.println("Nuevo puerto COM: " + newComPort);
 
                // Llamamos directamente a la referencia de Etis para actualizar el puerto COM
                mainFrame.actualizarPuertoCOM(newComPort);

                dispose(); // Cerrar el JFrame de configuración
            }
        });

        // Agregar los componentes al JFrame
        add(comPortLabel);
        add(comPortField);
        add(new JLabel()); // Espacio vacío
        add(btnSave);
    }
}
