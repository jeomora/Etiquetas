/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.etiquetas;

/**
 *
 * @author PROGRAMEITOR
 */
import com.fazecast.jSerialComm.SerialPort;

public class Basculon {

    private SerialPort serialPort;
    private String portinho = "";

    // Inicializar la conexión con el puerto serie
    public void initialize(String portName) {
        serialPort = SerialPort.getCommPort(portName);
        portinho = portName;

        if (serialPort == null) {
            System.out.println("Puerto serie no encontrado: " + portName);
            return;
        }

        // Configurar el puerto
        serialPort.setBaudRate(9600);  
        serialPort.setNumDataBits(8);
        serialPort.setNumStopBits(1);
        serialPort.setParity(SerialPort.NO_PARITY);
        serialPort.setFlowControl(SerialPort.FLOW_CONTROL_DISABLED);

        // Abrir el puerto
        if (serialPort.openPort()) {
            System.out.println("Puerto abierto con éxito: " + portName);
            serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 1000, 1000);
        } else {
            close();
            System.out.println("No se pudo abrir el puerto: " + portName);
        }
    }

    // Enviar el comando "P" y leer el peso
    public String readWeight() {
        if (serialPort == null || !serialPort.isOpen()) {
            System.out.println("Puerto serie no está abierto. Intentando abrirlo...");
            close();
            initialize(portinho);  // Método para intentar abrir el puerto
            if (serialPort == null || !serialPort.isOpen()) {
                System.out.println("No se pudo abrir el puerto serie.");
                return null;
            }
        }

        // Enviar el comando "P" + retorno de carro (0x0D)
        byte[] command = {0x50, 0x0D}; 
        serialPort.writeBytes(command, command.length);
        System.out.println("Comando enviado: P (0x50)");

        // Esperar antes de leer (algunas básculas tardan en responder)
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Leer la respuesta de la báscula
        byte[] readBuffer = new byte[64];
        int numRead = serialPort.readBytes(readBuffer, readBuffer.length);

        if (numRead > 0) {
            String data = new String(readBuffer, 0, numRead).trim();
            System.out.println("Peso recibido: " + data);
            return data;
        } else {
            System.out.println("No se recibió respuesta de la báscula.");
        }

        return null;
    }
    
    

    // Cerrar la conexión
    public void close() {
        if (serialPort != null && serialPort.isOpen()) {
            serialPort.closePort();
            System.out.println("Puerto cerrado.");
        }
    }

    public static void main(String[] args) {
        
    }
}
