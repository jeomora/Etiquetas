/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.etiquetas;

/**
 *
 * @author PROGRAMEITOR
 */

//import gnu.io.*;
import com.fazecast.jSerialComm.SerialPort;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;


public class TestRXTX {
    static String comPort = "ZDesigner GC420t (EPL)";
    static PrintService[] services = null;
    static PrintService zebraService = null;
    static PrintService epsonService = null;

    
    public TestRXTX(){    
        getPrinter();
    }
    
    public static void getPrinter(){
        services = PrintServiceLookup.lookupPrintServices(null, null);
        zebraService = null;
        for (PrintService service : services) {
            System.out.println(service.getName());
            comPort = leerPuertoCOMDesdeArchivo();

            // Si se leyó correctamente, inicializa el basculón
            if (comPort == null) {
                comPort = "ZDesigner GC420t (EPL)";
            }
            if (service.getName().equals(comPort)) {
                zebraService = service;
                break;
            }
        }
    }
    
    public static void testPrinter2(String puerte) {
        SerialPort[] puertos = SerialPort.getCommPorts();
        System.out.println("Puertos disponibles:");
        for (SerialPort puerto : puertos) {
            System.out.println(puerto.getSystemPortName());
        }

        // Supongamos que identificamos que el puerto USB está emulando un puerto COM
        // (por ejemplo, COM3)
        SerialPort puertoZebra = SerialPort.getCommPort("COM1");  // Reemplaza "COM3" por el puerto adecuado
        puertoZebra.setBaudRate(9600); // Ajusta la tasa de baudios si es necesario
        puertoZebra.setNumDataBits(8);
        puertoZebra.setNumStopBits(SerialPort.ONE_STOP_BIT);
        puertoZebra.setParity(SerialPort.NO_PARITY);
        
        if (puertoZebra.openPort()) {
            System.out.println("Conectado a la impresora Zebra.");
            
            // Enviar comandos ZPL
            String comandosZebra = "^XA\n" +   // Inicia la etiqueta
                        "^FO100,100\n" +   // Define la posición (x=100, y=100)
                        "^A0N,50,50\n" +   // Fuente estándar, tamaño 50x50
                        "^FDHola, Mundo\n" +  // Texto a imprimir
                        "^FS\n" +   // Finaliza el campo de texto
                        "^XZ";  // Termina la etiqueta

            puertoZebra.writeBytes(comandosZebra.getBytes(), comandosZebra.length());
            
            System.out.println("Comandos enviados a la impresora.");
            
            puertoZebra.closePort(); // Cerrar el puerto después de imprimir
        } else {
            System.out.println("No se pudo conectar a la impresora.");
        }
    }
    
    public static void testPrinter(String codigo,String cantidad,String desco){
        String uno = "";String dos = "";
        String[] lineas = dividirEnDosRenglones(desco, 22);
        System.out.print(lineas[0]);
        System.out.print(lineas[1]);
        LocalDateTime fechaHoraActual = LocalDateTime.now();
        DateTimeFormatter formatoCorto = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String fechaHoraFormateada = fechaHoraActual.format(formatoCorto);

        try {
            System.out.println(desco);
            uno = lineas[0];
            dos = lineas[1];
            System.out.println(uno);
            System.out.println(dos);
           String zpl = "^XA" +
                   
             "^PW600" +        // Ancho fijo de la etiqueta
             "^LL200" +        // Alto fijo de la etiqueta
             "^LH0,0" + 

             "^FO23,15" +  // Posición del código QR en el lado izquierdo (comienza en (20,20))
             "^BQN,2,8" +  // QR Code (modo 2, tamaño reducido de 5)
             "^FDMA,"+ cantidad +"\t"+ codigo +"^FS" +  // Contenido del QR: "2.989" en la primera línea y "0008" en la segunda línea

             "^FO210,20" +  
             "^A0N,30,30" +  
             "^FD "+uno+" ^FS" +  // Primera línea de la descripción
             
             "^FO210,50" +  // Posición de la segunda línea de descripción
             "^A0N,30,30" +  // Fuente (tamaño 30 puntos)
             "^FD "+dos+" ^FS" +  // Segunda línea de la descripción
                   
             "^FO250,90" +  // Posición de la segunda línea de descripción
             "^A0N,30,30" +  // Fuente (tamaño 30 puntos)
             "^FD "+cantidad+"   Kgs/Pzs ^FS" +  // Segunda línea de la descripción
                   
             "^FO210,180" +  // Posición de la segunda línea de descripción
             "^A0N,20,20" +  // Fuente (tamaño 30 puntos)
             "^FD "+fechaHoraFormateada+"^FS" +  // Segunda línea de la descripción
                   
                   //"^FO350,120^GFA,962,962,13,,:X06,Q0CK03C,J08K018K0FC,J0EK038J03F8,J0F8J07J01FF,J07CJ0FJ07FE,J07CI01EI03FFC,J07EI07EI0IF,J03EI0FC007FFC,J03F001FC01IF8,J03F003F807FFE,J03F007F81IFC001FFE,J01F80FF07IF001IFE,J01F80FF1IFC01JF8,J01F81FE3IF80JFE,J01FC1FE7FFE03JF8,K0FC3FCIFC0JFE,K0FC3FDIF83JF8,K0FE3FBIF07IFE,K0FE3JFE1JF8,K07E3JFC3IFE,007807E7JFC7IF8,007C07F7JF9IFE,003E07F7JF3IF801F8,001F07LF7FFE01IF,I0F87OF81JF8,I0F83FF81JFE0KFC,I07C3FE003IFCLFE,I07E3FCI0QF,I03E3F8FE07OFE,I03F3F1F183NFC,I01F3E3E0C1MFC,I01FBE3C061IFC1FF,I01FFE3C070IF003E03FC,J0FFE1C0F8FFCI081FFE,J0IF0IFC7F81C003IF8,J0IF80FFE3F83F807IF8,J0IFC003F1F83FF1JFC,J07IFI0F0F83MFC,J07JFE0787C3MFC,J07KF83C1E3MFC,J07KFE1E061MFC,J07LF0F001MFC,J03LF87F00MF8,J03LFC1FE0MF,J03LFE03E07KFE,J01IF8M07FFEFF8,V03FFC38,S0FF83FF8,J03E1FF0FF1FFC1FF8,J07F3FF0FF1FFC1FF8,J07F1FF1FF1FFC1FF8,J07F1FF1FF1FF83FF8,J07E1FF1FF1FE03FF8,J03C07C0F80E00IF8,P06J01IF8,U07IF8,J07FFM01JF8,J03JFE00MF8,K07SF8,N01PF8,O03JF03FFE,O01IFC,P0IFC2,O01IFE6,O0FFE3FE,O0FF81FC,O03C007,,:::^FS"+
                   
            "^FO490,150^GFA,559,559,13,,:I0601DI02R0C,I0C03FF0IF00FEI0C001C,001C03FFDIF01FF003F8038,007C07LF07FE00FF80F8,007FI0FFE600F8201FF80FE,"+
            "007F8001F0701EI038180FF,00FF8I0F0701CI070181FF,00EF8001E0703CI0E0181EF,01E78001E0F03C001C0381C78,01C38003C0F03800380783C7,"+
            "01C3800380F0380038078383,0383800700F0780078078383,0383800F00E078F8700707038,0383800E00E073FC700F07038,0703801C00E07FFC700F0703,"+
            "0703C03800E07E00F0060E038,0E3FC07I0E07I0E0040E7F8,1IF80EI0C07I0EI03IF,3FE180E001C06I0FI07FC38,3F0181C001C0EI0EI07E03,3C03838001C0EI0FI07803,"+
            "180387I01C0EI0FI03803,18038F0071C0E0067800F003,38031E5FF1C0E07E7801F003,30031IF80C0IF87E073003,30031FF800C0FFE03FFE6003,70031FCI0C07F001FF860"+
            "02,6003S03C06002,6Y0C,,::T01K01,I01C1E0E0F0F87C3E3C7C,I01C130E0D8D8E6083064,I0161E0B0D8D8C6083C38,I0361B1B0F0F8C608300C,I03E1B1F0D8D87E083C6C,"+
            "I0631E318D8C83C083C3C,,:^FS"+
 
             "^XZ";  // Fin del trabajo de impresión

            // Obtener el servicio de impresión
            

            if (zebraService != null) {
                DocPrintJob printJob = zebraService.createPrintJob();
                Doc doc = new SimpleDoc(zpl.getBytes(), DocFlavor.BYTE_ARRAY.AUTOSENSE, null);
                printJob.print(doc, null);
                System.out.println("Impresión de etiqueta con descripción y código de barras enviada a Zebra.");
                registrarEtiqueta(codigo,cantidad,desco);
            } else {
                getPrinter();
                if (zebraService != null) {
                    DocPrintJob printJob = zebraService.createPrintJob();
                    Doc doc = new SimpleDoc(zpl.getBytes(), DocFlavor.BYTE_ARRAY.AUTOSENSE, null);
                    printJob.print(doc, null);
                    System.out.println("Impresión de etiqueta con descripción y código de barras enviada a Zebra.");
                    registrarEtiqueta(codigo,cantidad,desco);
                }
                System.out.println("Impresora Zebra no encontrada.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static String[] dividirEnDosRenglones(String texto, int limite) {
        if (texto.length() <= limite) {
            return new String[]{texto, ""}; // Si es menor o igual al límite, todo va en la primera línea
        }

        int corte = texto.lastIndexOf(" ", limite); // Buscar el último espacio antes del límite
        if (corte == -1) {
            corte = limite; // Si no hay espacios, corta en el límite exacto
        }

        String linea1 = texto.substring(0, corte).trim(); // Primera línea con palabras completas
        String linea2 = texto.substring(corte).trim();   // Segunda línea con el resto del texto

        return new String[]{linea1, linea2};
    }
    
    private static String leerPuertoCOMDesdeArchivo() {
        String comPorta = null;
        try {
            // Obtener el archivo "comport.txt" desde la misma carpeta donde está el JAR
            File archivo = new File("zebra.txt");

            // Verificar si el archivo existe
            if (archivo.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(archivo));
                comPorta = reader.readLine(); // Leer la primera línea (debe ser el puerto COM)
                reader.close();
            } else {
                System.out.println("El archivo zebra.txt no existe.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return comPorta;
    }
    
    public static void testPrinterSin(String codigo,String desco){
        String uno = "";String dos = "";
        String[] lineas = dividirEnDosRenglones(desco, 22);
        System.out.print(lineas[0]);
        System.out.print(lineas[1]);
        LocalDateTime fechaHoraActual = LocalDateTime.now();
        DateTimeFormatter formatoCorto = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String fechaHoraFormateada = fechaHoraActual.format(formatoCorto);

        try {
            System.out.println(desco);
            uno = lineas[0];
            dos = lineas[1];
            System.out.println(uno);
            System.out.println(dos);
           String zpl = "^XA" +
             "^PW600" +        // Ancho fijo de la etiqueta
             "^LL200" +        // Alto fijo de la etiqueta
             "^LH0,0" + 
             "^FO23,20" +  
             "^A0N,30,30" +  
             "^FD "+uno+" ^FS" +  // Primera línea de la descripción
             
             "^FO23,50" +  // Posición de la segunda línea de descripción
             "^A0N,30,30" +  // Fuente (tamaño 30 puntos)
             "^FD "+dos+" ^FS" +  // Segunda línea de la descripción
                   
             "^FO23,180" +  // Posición de la segunda línea de descripción
             "^A0N,20,20" +  // Fuente (tamaño 30 puntos)
             "^FD "+fechaHoraFormateada+"^FS" +  // Segunda línea de la descripción
                   
                   //"^FO350,120^GFA,962,962,13,,:X06,Q0CK03C,J08K018K0FC,J0EK038J03F8,J0F8J07J01FF,J07CJ0FJ07FE,J07CI01EI03FFC,J07EI07EI0IF,J03EI0FC007FFC,J03F001FC01IF8,J03F003F807FFE,J03F007F81IFC001FFE,J01F80FF07IF001IFE,J01F80FF1IFC01JF8,J01F81FE3IF80JFE,J01FC1FE7FFE03JF8,K0FC3FCIFC0JFE,K0FC3FDIF83JF8,K0FE3FBIF07IFE,K0FE3JFE1JF8,K07E3JFC3IFE,007807E7JFC7IF8,007C07F7JF9IFE,003E07F7JF3IF801F8,001F07LF7FFE01IF,I0F87OF81JF8,I0F83FF81JFE0KFC,I07C3FE003IFCLFE,I07E3FCI0QF,I03E3F8FE07OFE,I03F3F1F183NFC,I01F3E3E0C1MFC,I01FBE3C061IFC1FF,I01FFE3C070IF003E03FC,J0FFE1C0F8FFCI081FFE,J0IF0IFC7F81C003IF8,J0IF80FFE3F83F807IF8,J0IFC003F1F83FF1JFC,J07IFI0F0F83MFC,J07JFE0787C3MFC,J07KF83C1E3MFC,J07KFE1E061MFC,J07LF0F001MFC,J03LF87F00MF8,J03LFC1FE0MF,J03LFE03E07KFE,J01IF8M07FFEFF8,V03FFC38,S0FF83FF8,J03E1FF0FF1FFC1FF8,J07F3FF0FF1FFC1FF8,J07F1FF1FF1FFC1FF8,J07F1FF1FF1FF83FF8,J07E1FF1FF1FE03FF8,J03C07C0F80E00IF8,P06J01IF8,U07IF8,J07FFM01JF8,J03JFE00MF8,K07SF8,N01PF8,O03JF03FFE,O01IFC,P0IFC2,O01IFE6,O0FFE3FE,O0FF81FC,O03C007,,:::^FS"+
                   
            "^FO490,150^GFA,559,559,13,,:I0601DI02R0C,I0C03FF0IF00FEI0C001C,001C03FFDIF01FF003F8038,007C07LF07FE00FF80F8,007FI0FFE600F8201FF80FE,"+
            "007F8001F0701EI038180FF,00FF8I0F0701CI070181FF,00EF8001E0703CI0E0181EF,01E78001E0F03C001C0381C78,01C38003C0F03800380783C7,"+
            "01C3800380F0380038078383,0383800700F0780078078383,0383800F00E078F8700707038,0383800E00E073FC700F07038,0703801C00E07FFC700F0703,"+
            "0703C03800E07E00F0060E038,0E3FC07I0E07I0E0040E7F8,1IF80EI0C07I0EI03IF,3FE180E001C06I0FI07FC38,3F0181C001C0EI0EI07E03,3C03838001C0EI0FI07803,"+
            "180387I01C0EI0FI03803,18038F0071C0E0067800F003,38031E5FF1C0E07E7801F003,30031IF80C0IF87E073003,30031FF800C0FFE03FFE6003,70031FCI0C07F001FF860"+
            "02,6003S03C06002,6Y0C,,::T01K01,I01C1E0E0F0F87C3E3C7C,I01C130E0D8D8E6083064,I0161E0B0D8D8C6083C38,I0361B1B0F0F8C608300C,I03E1B1F0D8D87E083C6C,"+
            "I0631E318D8C83C083C3C,,:^FS"+
 
             "^XZ";  // Fin del trabajo de impresión

            if (zebraService != null) {
                DocPrintJob printJob = zebraService.createPrintJob();
                Doc doc = new SimpleDoc(zpl.getBytes(), DocFlavor.BYTE_ARRAY.AUTOSENSE, null);
                printJob.print(doc, null);
                System.out.println("Impresión de etiqueta con descripción y código de barras enviada a Zebra.");
            } else {
                getPrinter();
                if (zebraService != null) {
                    DocPrintJob printJob = zebraService.createPrintJob();
                    Doc doc = new SimpleDoc(zpl.getBytes(), DocFlavor.BYTE_ARRAY.AUTOSENSE, null);
                    printJob.print(doc, null);
                    System.out.println("Impresión de etiqueta con descripción y código de barras enviada a Zebra.");
                }
                System.out.println("Impresora Zebra no encontrada.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void testPrinterEntradas(String codigo,String cantidad,String desco){
        String uno = "";String dos = "";String tres = "";
        if (cantidad == null || cantidad.trim().isEmpty()) {
            cantidad = "1";
        }
        String[] lineas = dividirEnTresRenglones(desco, 18,18);
        System.out.print(lineas[0]);
        System.out.print(lineas[1]);
        LocalDateTime fechaHoraActual = LocalDateTime.now();
        DateTimeFormatter formatoCorto = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String fechaHoraFormateada = fechaHoraActual.format(formatoCorto);

        try {
            System.out.println(desco);
            uno = lineas[0];
            dos = lineas[1];
            tres = lineas[2];
            System.out.println(uno);
            System.out.println(dos);
           String zpl = "^XA" +
                   
             "^PW600" +        // Ancho fijo de la etiqueta
             "^LL200" +        // Alto fijo de la etiqueta
             "^LH0,0" + 

             "^FO23,15" +  // Posición del código QR en el lado izquierdo (comienza en (20,20))
             "^BQN,2,8" +  // QR Code (modo 2, tamaño reducido de 5)
             "^FDMA,"+ codigo +"^FS" +  // Contenido del QR: "2.989" en la primera línea y "0008" en la segunda línea*/

             "^FO 190,20" +  
             "^A0N,45,45" +  
             "^FD "+uno+" ^FS" +  // Primera línea de la descripción
             
             "^FO 190,70" +  // Posición de la segunda línea de descripción
             "^A0N,45,45" +  // Fuente (tamaño 30 puntos)
             "^FD "+dos+" ^FS" +  // Segunda línea de la descripción
                   
             "^FO 190,120" +  // Posición de la segunda línea de descripción
             "^A0N,35,35" +  // Fuente (tamaño 30 puntos)
             "^FD "+tres+" "+codigo+" ^FS" +  // Segunda línea de la descripción*/
                   
             "^FO 200,180" +  // Posición de la segunda línea de descripción
             "^A0N,20,20" +  // Fuente (tamaño 30 puntos)
             "^FD "+fechaHoraFormateada+"^FS" +  // Segunda línea de la descripción
                   
                   //"^FO350,120^GFA,962,962,13,,:X06,Q0CK03C,J08K018K0FC,J0EK038J03F8,J0F8J07J01FF,J07CJ0FJ07FE,J07CI01EI03FFC,J07EI07EI0IF,J03EI0FC007FFC,J03F001FC01IF8,J03F003F807FFE,J03F007F81IFC001FFE,J01F80FF07IF001IFE,J01F80FF1IFC01JF8,J01F81FE3IF80JFE,J01FC1FE7FFE03JF8,K0FC3FCIFC0JFE,K0FC3FDIF83JF8,K0FE3FBIF07IFE,K0FE3JFE1JF8,K07E3JFC3IFE,007807E7JFC7IF8,007C07F7JF9IFE,003E07F7JF3IF801F8,001F07LF7FFE01IF,I0F87OF81JF8,I0F83FF81JFE0KFC,I07C3FE003IFCLFE,I07E3FCI0QF,I03E3F8FE07OFE,I03F3F1F183NFC,I01F3E3E0C1MFC,I01FBE3C061IFC1FF,I01FFE3C070IF003E03FC,J0FFE1C0F8FFCI081FFE,J0IF0IFC7F81C003IF8,J0IF80FFE3F83F807IF8,J0IFC003F1F83FF1JFC,J07IFI0F0F83MFC,J07JFE0787C3MFC,J07KF83C1E3MFC,J07KFE1E061MFC,J07LF0F001MFC,J03LF87F00MF8,J03LFC1FE0MF,J03LFE03E07KFE,J01IF8M07FFEFF8,V03FFC38,S0FF83FF8,J03E1FF0FF1FFC1FF8,J07F3FF0FF1FFC1FF8,J07F1FF1FF1FFC1FF8,J07F1FF1FF1FF83FF8,J07E1FF1FF1FE03FF8,J03C07C0F80E00IF8,P06J01IF8,U07IF8,J07FFM01JF8,J03JFE00MF8,K07SF8,N01PF8,O03JF03FFE,O01IFC,P0IFC2,O01IFE6,O0FFE3FE,O0FF81FC,O03C007,,:::^FS"+
                   
            "^FO490,150^GFA,559,559,13,,:I0601DI02R0C,I0C03FF0IF00FEI0C001C,001C03FFDIF01FF003F8038,007C07LF07FE00FF80F8,007FI0FFE600F8201FF80FE,"+
            "007F8001F0701EI038180FF,00FF8I0F0701CI070181FF,00EF8001E0703CI0E0181EF,01E78001E0F03C001C0381C78,01C38003C0F03800380783C7,"+
            "01C3800380F0380038078383,0383800700F0780078078383,0383800F00E078F8700707038,0383800E00E073FC700F07038,0703801C00E07FFC700F0703,"+
            "0703C03800E07E00F0060E038,0E3FC07I0E07I0E0040E7F8,1IF80EI0C07I0EI03IF,3FE180E001C06I0FI07FC38,3F0181C001C0EI0EI07E03,3C03838001C0EI0FI07803,"+
            "180387I01C0EI0FI03803,18038F0071C0E0067800F003,38031E5FF1C0E07E7801F003,30031IF80C0IF87E073003,30031FF800C0FFE03FFE6003,70031FCI0C07F001FF860"+
            "02,6003S03C06002,6Y0C,,::T01K01,I01C1E0E0F0F87C3E3C7C,I01C130E0D8D8E6083064,I0161E0B0D8D8C6083C38,I0361B1B0F0F8C608300C,I03E1B1F0D8D87E083C6C,"+
            "I0631E318D8C83C083C3C,,:^FS"+
 
             "^XZ";  // Fin del trabajo de impresión

            if (zebraService != null) {
                for (int i = 0; i < Integer.parseInt(cantidad); i++) {
                    DocPrintJob printJob = zebraService.createPrintJob();
                    Doc doc = new SimpleDoc(zpl.getBytes(), DocFlavor.BYTE_ARRAY.AUTOSENSE, null);
                    printJob.print(doc, null);
                }
                System.out.println("Impresión de etiqueta con descripción y código de barras enviada a Zebra.");
            } else {
                getPrinter();
                if (zebraService != null) {
                    for (int i = 0; i < Integer.parseInt(cantidad); i++) {
                        DocPrintJob printJob = zebraService.createPrintJob();
                        Doc doc = new SimpleDoc(zpl.getBytes(), DocFlavor.BYTE_ARRAY.AUTOSENSE, null);
                        printJob.print(doc, null);
                    }
                    System.out.println("Impresión de etiqueta con descripción y código de barras enviada a Zebra.");
                }
                System.out.println("Impresora Zebra no encontrada.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static String[] dividirEnTresRenglones(String texto, int limite1, int limite2) {
        String linea1 = "";
        String linea2 = "";
        String linea3 = "";

        if (texto.length() <= limite1) {
            // Todo cabe en la primera línea
            return new String[]{texto, "", ""};
        }

        int corte1 = texto.lastIndexOf(" ", limite1);
        if (corte1 == -1) {
            corte1 = limite1;
        }

        linea1 = texto.substring(0, corte1).trim();
        String restante = texto.substring(corte1).trim();

        if (restante.length() <= limite2) {
            return new String[]{linea1, restante, ""};
        }

        int corte2 = restante.lastIndexOf(" ", limite2);
        if (corte2 == -1) {
            corte2 = limite2;
        }

        linea2 = restante.substring(0, corte2).trim();
        linea3 = restante.substring(corte2).trim();

        return new String[]{linea1, linea2, linea3};
    }
    
    public static void registrarEtiqueta(String codigo, String cantidad, String descripcion) {
        LocalDateTime fechaHoraActual = LocalDateTime.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fecha_venta = fechaHoraActual.format(formato);
        String sucus = leerPuertoSucursal();

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        Integer idProducto = null;

        try {
            conn = ConexionBaseDeDatos.conectar();

            // Paso 1: Buscar el id_producto por el código
            String selectSQL = "SELECT id_producto FROM productos WHERE codigo = ? AND estatus = 1";
            stmt = conn.prepareStatement(selectSQL);
            stmt.setString(1, codigo);
            rs = stmt.executeQuery();

            if (rs.next()) {
                idProducto = rs.getInt("id_producto");
                // Cerrar stmt y reutilizarlo para el INSERT
                stmt.close();

                // Paso 2: Insertar en etiquetas_log
                String insertSQL = "INSERT INTO etiquetas_log (id_producto, codigo, cantidad, descripcion, fecha_venta,id_sucursal) VALUES (?, ?, ?, ?, ?, ?)";
                stmt = conn.prepareStatement(insertSQL);
                stmt.setInt(1, idProducto);
                stmt.setString(2, codigo);
                stmt.setString(3, cantidad);
                stmt.setString(4, descripcion);
                stmt.setString(5, fecha_venta);
                stmt.setString(6, sucus);
                stmt.executeUpdate();

                System.out.println("Etiqueta registrada exitosamente en la BD.");
            } else {
                // No se encontró el producto, guardar en archivo especial
                guardarProductoNoEncontrado(codigo, cantidad, descripcion, fecha_venta);
                System.err.println("Producto no encontrado. Registro guardado en archivo especial.");
                return;
            }

        } catch (Exception e) {
            System.err.println("Error al registrar en BD, guardando en archivo local...");
            guardarEnArchivoLocal(codigo, cantidad, descripcion, fecha_venta);
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private static String leerPuertoSucursal() {
        String comPorta = null;
        try {
            // Obtener el archivo "comport.txt" desde la misma carpeta donde está el JAR
            File archivo = new File("sucursal.txt");

            // Verificar si el archivo existe
            if (archivo.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(archivo));
                comPorta = reader.readLine(); // Leer la primera línea (debe ser el puerto COM)
                reader.close();
            } else {
                System.out.println("El archivo sucursal.txt no existe.");
                comPorta = "7";
            }
        } catch (IOException e) {
            e.printStackTrace();
            comPorta = "7";
        }
        return comPorta;
    }
    
    public static void guardarEnArchivoLocal(String codigo, String cantidad, String descripcion, String fechaHora) {
        String linea = codigo + "|" + cantidad + "|" + descripcion + "|" + fechaHora;

        try (FileWriter fw = new FileWriter("etiquetas_pendientes.txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(linea);
            System.out.println("Registro guardado localmente.");
        } catch (IOException e) {
            System.err.println("Error al guardar en archivo local.");
            e.printStackTrace();
        }
    }
    
    public static void guardarProductoNoEncontrado(String codigo, String cantidad, String descripcion, String fechaHora) {
        String linea = codigo + "|" + cantidad + "|" + descripcion + "|" + fechaHora;

        try (FileWriter fw = new FileWriter("etiquetas_productos_no_encontrados.txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(linea);
            System.out.println("Registro guardado en archivo de productos no encontrados.");
        } catch (IOException e) {
            System.err.println("Error al guardar en archivo de productos no encontrados.");
            e.printStackTrace();
        }
    }
    
    public static void procesarPendientesNoEncontrados() {
        File archivo = new File("etiquetas_pendientes.txt");
        if (!archivo.exists()) {
            System.out.println("No hay pendientes por procesar.");
            return;
        }

        List<String> lineasRestantes = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split("\\|");
                if (partes.length != 4) {
                    System.err.println("Formato incorrecto en línea: " + linea);
                    continue;
                }

                String codigo = partes[0];
                String cantidad = partes[1];
                String descripcion = partes[2];
                String fechaHora = partes[3];

                Boolean resultado = intentarRegistrarEnBD(codigo, cantidad, descripcion, fechaHora);

                if (Boolean.TRUE.equals(resultado)) {
                    System.out.println("Registro procesado correctamente: " + codigo);
                } else if (resultado == null) {
                    // Producto no encontrado, reenviar a archivo de productos no encontrados
                    guardarProductoNoEncontrado(codigo, cantidad, descripcion, fechaHora);
                } else {
                    // Error en BD u otro problema, mantener en lista para reintentar
                    lineasRestantes.add(linea);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de pendientes.");
            e.printStackTrace();
            return;
        }

        // Reescribir archivo con líneas no procesadas
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(archivo, false)))) {
            for (String l : lineasRestantes) {
                out.println(l);
            }
        } catch (IOException e) {
            System.err.println("Error al reescribir archivo de pendientes.");
            e.printStackTrace();
        }
    }

    private static Boolean intentarRegistrarEnBD(String codigo, String cantidad, String descripcion, String fechaHora) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        Integer idProducto = null;

        try {
            conn = ConexionBaseDeDatos.conectar();

            // Buscar id_producto con estatus = 1
            String selectSQL = "SELECT id_producto FROM productos WHERE codigo = ? AND estatus = 1";
            stmt = conn.prepareStatement(selectSQL);
            stmt.setString(1, codigo);
            rs = stmt.executeQuery();

            if (rs.next()) {
                idProducto = rs.getInt("id_producto");
            } else {
                return null; // No se encontró el producto
            }

            stmt.close(); // Cerrar y reusar

            // Insertar en etiquetas_log
            String insertSQL = "INSERT INTO etiquetas_log (id_producto, codigo, cantidad, descripcion, fecha_venta) VALUES (?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(insertSQL);
            stmt.setInt(1, idProducto);
            stmt.setString(2, codigo);
            stmt.setString(3, cantidad);
            stmt.setString(4, descripcion);
            stmt.setString(5, fechaHora);
            stmt.executeUpdate();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void printTry(){
        String uno = "";String dos = "";
        LocalDateTime fechaHoraActual = LocalDateTime.now();
        DateTimeFormatter formatoCorto = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String fechaHoraFormateada = fechaHoraActual.format(formatoCorto);
        System.out.println("si entro");
        try {
            System.out.println(uno);
            System.out.println(dos);
            String zpl = "^XA" +
                    
             "^PW600" +        // Ancho fijo de la etiqueta
             "^LL200" +        // Alto fijo de la etiqueta
             "^LH0,0" + 

             "^FO23,30" +  
             "^A0N,30,30" +  
             "^FD SUCURSALES ^FS" +  // Primera línea de la descripción
             
             "^FO23,67" +  // Posición de la segunda línea de descripción
             "^A0N,60,60" +  // Fuente (tamaño 30 puntos)
             "^FD RESPALDOS ^FS" +  // Segunda línea de la descripción
                   
                   //"^FO350,120^GFA,962,962,13,,:X06,Q0CK03C,J08K018K0FC,J0EK038J03F8,J0F8J07J01FF,J07CJ0FJ07FE,J07CI01EI03FFC,J07EI07EI0IF,J03EI0FC007FFC,J03F001FC01IF8,J03F003F807FFE,J03F007F81IFC001FFE,J01F80FF07IF001IFE,J01F80FF1IFC01JF8,J01F81FE3IF80JFE,J01FC1FE7FFE03JF8,K0FC3FCIFC0JFE,K0FC3FDIF83JF8,K0FE3FBIF07IFE,K0FE3JFE1JF8,K07E3JFC3IFE,007807E7JFC7IF8,007C07F7JF9IFE,003E07F7JF3IF801F8,001F07LF7FFE01IF,I0F87OF81JF8,I0F83FF81JFE0KFC,I07C3FE003IFCLFE,I07E3FCI0QF,I03E3F8FE07OFE,I03F3F1F183NFC,I01F3E3E0C1MFC,I01FBE3C061IFC1FF,I01FFE3C070IF003E03FC,J0FFE1C0F8FFCI081FFE,J0IF0IFC7F81C003IF8,J0IF80FFE3F83F807IF8,J0IFC003F1F83FF1JFC,J07IFI0F0F83MFC,J07JFE0787C3MFC,J07KF83C1E3MFC,J07KFE1E061MFC,J07LF0F001MFC,J03LF87F00MF8,J03LFC1FE0MF,J03LFE03E07KFE,J01IF8M07FFEFF8,V03FFC38,S0FF83FF8,J03E1FF0FF1FFC1FF8,J07F3FF0FF1FFC1FF8,J07F1FF1FF1FFC1FF8,J07F1FF1FF1FF83FF8,J07E1FF1FF1FE03FF8,J03C07C0F80E00IF8,P06J01IF8,U07IF8,J07FFM01JF8,J03JFE00MF8,K07SF8,N01PF8,O03JF03FFE,O01IFC,P0IFC2,O01IFE6,O0FFE3FE,O0FF81FC,O03C007,,:::^FS"+
                   
            "^FO490,150^GFA,559,559,13,,:I0601DI02R0C,I0C03FF0IF00FEI0C001C,001C03FFDIF01FF003F8038,007C07LF07FE00FF80F8,007FI0FFE600F8201FF80FE,"+
            "007F8001F0701EI038180FF,00FF8I0F0701CI070181FF,00EF8001E0703CI0E0181EF,01E78001E0F03C001C0381C78,01C38003C0F03800380783C7,"+
            "01C3800380F0380038078383,0383800700F0780078078383,0383800F00E078F8700707038,0383800E00E073FC700F07038,0703801C00E07FFC700F0703,"+
            "0703C03800E07E00F0060E038,0E3FC07I0E07I0E0040E7F8,1IF80EI0C07I0EI03IF,3FE180E001C06I0FI07FC38,3F0181C001C0EI0EI07E03,3C03838001C0EI0FI07803,"+
            "180387I01C0EI0FI03803,18038F0071C0E0067800F003,38031E5FF1C0E07E7801F003,30031IF80C0IF87E073003,30031FF800C0FFE03FFE6003,70031FCI0C07F001FF860"+
            "02,6003S03C06002,6Y0C,,::T01K01,I01C1E0E0F0F87C3E3C7C,I01C130E0D8D8E6083064,I0161E0B0D8D8C6083C38,I0361B1B0F0F8C608300C,I03E1B1F0D8D87E083C6C,"+
            "I0631E318D8C83C083C3C,,:^FS"+
 
             "^XZ";  // Fin del trabajo de impresión

            // Obtener el servicio de impresión
            

            if (zebraService != null) {
                DocPrintJob printJob = zebraService.createPrintJob();
                Doc doc = new SimpleDoc(zpl.getBytes(), DocFlavor.BYTE_ARRAY.AUTOSENSE, null);
                printJob.print(doc, null);
                System.out.println("Impresión de etiqueta con descripción y código de barras enviada a Zebra.");
            } else {
                getPrinter();
                if (zebraService != null) {
                    DocPrintJob printJob = zebraService.createPrintJob();
                    Doc doc = new SimpleDoc(zpl.getBytes(), DocFlavor.BYTE_ARRAY.AUTOSENSE, null);
                    printJob.print(doc, null);
                    System.out.println("Impresión de etiqueta con descripción y código de barras enviada a Zebra.");
                }
                System.out.println("Impresora Zebra no encontrada.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
