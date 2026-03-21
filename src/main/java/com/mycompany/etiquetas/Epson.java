/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.etiquetas;


import static com.mycompany.etiquetas.ConexionEpson.conectar;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javax.imageio.ImageIO;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
/**
 *
 * @author JEOS
 */
public class Epson {
    static String comPortEpson = "EPSON TM-T88V Receipt";
    static PrintService[] services = null;
    static PrintService epsonService = null;

    
    public Epson(){    
        getPrinterEpson();
    }
    
    private static String leerPuertoCOMDesdeArchivo() {
        String comPorta = null;
        try {
            // Obtener el archivo "comport.txt" desde la misma carpeta donde está el JAR
            File archivo = new File("epson.txt");

            // Verificar si el archivo existe
            if (archivo.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(archivo));
                comPorta = reader.readLine(); // Leer la primera línea (debe ser el puerto COM)
                reader.close();
            } else {
                System.out.println("El archivo epson.txt no existe.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return comPorta;
    }
    
    public static void testPrinterEpson() {
        int nume = getTurno();
        String numero = ""+nume; // prueba con 13, 21, 999, etc.
        LocalDateTime fechaHoraActual = LocalDateTime.now();
        DateTimeFormatter formatoConDia = DateTimeFormatter.ofPattern("EEEE dd/MM/yyyy HH:mm:ss", new Locale("es", "ES"));
        String fechaHoraFormateada = fechaHoraActual.format(formatoConDia).toUpperCase();
        
        try {
            int printerWidth = 384; // ancho máximo en píxeles de la TM-T88V

            // Calcular tamaño de fuente dinámico
            int fontSize = 10;
            Font font;
            FontMetrics fm;
            int textWidth;
            int textHeight;

            BufferedImage tmpImg = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
            Graphics2D tmpG = tmpImg.createGraphics();

            do {
                fontSize += 5;
                font = new Font("Arial", Font.BOLD, fontSize);
                tmpG.setFont(font);
                fm = tmpG.getFontMetrics();
                textWidth = fm.stringWidth(numero);
                textHeight = fm.getHeight();
            } while (textWidth < printerWidth - 20 && textHeight < 400 && fontSize < 300);

            tmpG.dispose();

            // Ajustar altura de imagen al texto
            int imgHeight = textHeight + 20; // margen de seguridad
            BufferedImage image = new BufferedImage(printerWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, printerWidth, imgHeight);
            g.setColor(Color.BLACK);
            g.setFont(font);

            // Centrar y dibujar
            int x = (printerWidth - textWidth) / 2;
            int y = fm.getAscent();
            g.drawString(numero, x, y);
            g.dispose();

            // Recortar imagen para eliminar espacio en blanco superior
            image = cropTopWhite(image);

            // Convertir a ESC/POS raster
            byte[] escposImage = convertToEscPosRaster(image);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            out.write(new byte[]{0x1B, 0x40}); // inicializar

            // === AGREGADO: imprimir logo al inicio ===
            BufferedImage logo = ImageIO.read(Epson.class.getResource("/images/semlogo.jpeg"));

            // Aplanar transparencia sobre blanco
            BufferedImage logoFinal = new BufferedImage(logo.getWidth(), logo.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = logoFinal.createGraphics();
            g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, logoFinal.getWidth(), logoFinal.getHeight());
            g2.drawImage(logo, 0, 0, null);
            g2.dispose();

            // Recortar logo para eliminar espacio en blanco superior
            logoFinal = cropTopWhite(logoFinal);

            // Convertir a ESC/POS raster
            byte[] escposLogo = convertToEscPosRaster(logoFinal);

            // Imprimir fecha, logo y número
            out.write((fechaHoraFormateada + "\n").getBytes("CP437"));
            out.write(new byte[]{0x1B, 0x64, 0x01}); // pequeño feed
            out.write(("TURNO SEMILLAS \n").getBytes("CP437"));
            out.write(new byte[]{0x1B, 0x64, 0x01}); // pequeño feed
            out.write(escposLogo);
            out.write(new byte[]{0x1B, 0x64, 0x01}); // feed
            out.write(escposImage);

            // Avanzar papel y cortar
            out.write(new byte[]{0x1B, 0x64, 0x05}); // feed
            out.write(new byte[]{0x1D, 0x56, 0x00}); // corte

            byte[] finalData = out.toByteArray();

            if (epsonService != null) {
                DocPrintJob printJob = epsonService.createPrintJob();
                Doc doc = new SimpleDoc(finalData, DocFlavor.BYTE_ARRAY.AUTOSENSE, null);
                printJob.print(doc, null);
                System.out.println("Ticket enviado a Epson con logo y número gigante.");
            } else {
                System.out.println("Impresora Epson no encontrada.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * Recorta las filas superiores en blanco de una imagen.
     */
    private static BufferedImage cropTopWhite(BufferedImage src) {
        int width = src.getWidth();
        int height = src.getHeight();
        int top = 0;

        // Buscar la primera fila que no sea completamente blanca
        outer:
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (src.getRGB(x, y) != Color.WHITE.getRGB()) {
                    top = y;
                    break outer;
                }
            }
        }

        return src.getSubimage(0, top, width, height - top);
    }


    
    public static void getPrinterEpson() {
        services = PrintServiceLookup.lookupPrintServices(null, null);
        epsonService = null;
        for (PrintService service : services) {
            System.out.println(service.getName());
            comPortEpson = leerPuertoCOMDesdeArchivo();

            // Si no se leyó nada del archivo, usar nombre por defecto
            if (comPortEpson == null) {
                comPortEpson = "EPSON TM-T88V Receipt"; 
                // Este nombre depende de cómo aparece instalada en tu sistema
            }

            if (service.getName().equals(comPortEpson)) {
                epsonService = service;
                break;
            }
        }
    }
    
    public static byte[] convertToEscPosRaster(BufferedImage image) throws IOException {
        int width = image.getWidth();
        int height = image.getHeight();

        // El ancho debe ser múltiplo de 8
        int bytesPerRow = (width + 7) / 8;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // Comando ESC/POS: GS v 0
        baos.write(0x1D);
        baos.write('v');
        baos.write('0');
        baos.write(0x00); // modo normal

        // Tamaño en little endian
        baos.write(bytesPerRow % 256);
        baos.write(bytesPerRow / 256);
        baos.write(height % 256);
        baos.write(height / 256);

        // Convertir pixels a bytes
        for (int y = 0; y < height; y++) {
            int bit = 0;
            int currentByte = 0;
            for (int x = 0; x < width; x++) {
                int color = image.getRGB(x, y) & 0xFFFFFF;
                int luminance = (int)(0.299*((color>>16)&0xFF) + 0.587*((color>>8)&0xFF) + 0.114*(color&0xFF));
                boolean black = luminance < 128; // umbral
                currentByte <<= 1;
                if (black) currentByte |= 1;
                bit++;
                if (bit == 8) {
                    baos.write(currentByte);
                    bit = 0;
                    currentByte = 0;
                }
            }
            if (bit > 0) {
                currentByte <<= (8 - bit);
                baos.write(currentByte);
            }
        }

        return baos.toByteArray();
    }
    
    
    // Cargar todos los productos en memoria
    public static int getTurno() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        System.out.println("inicio turnos");
        try {
            conn = conectar();
            String query = "SELECT id_turno,fecha_registro,numero,estatus,fecha_inicio FROM turnosemillas WHERE DATE(fecha_registro) = CURDATE() ORDER BY id_turno DESC;";
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
            System.out.println("realizo consulta");
            System.out.println(rs);
            int nuevo = 0;
            if(rs.next()){
                int numero = rs.getInt("numero");
                nuevo = numero + 1;
            }else{
                int numero = 0;
                nuevo = numero + 1;
            }
            String insertSQL = "INSERT INTO turnosemillas (numero) VALUES (?)";
            stmt = conn.prepareStatement(insertSQL);
            stmt.setInt(1, nuevo);
            stmt.executeUpdate();
            System.out.println("Numero mas alto "+ nuevo +".");
            stmt.close();
            return nuevo;
        } catch (SQLException e) {
            System.out.println("Error EL NUMERO DE TURNO: " + e.getMessage());
        }
        return 0;
    }
    
    public static void testPrinterEpsonCarnes() {
        int nume = getTurnoCarnes();
        String numero = ""+nume; // prueba con 13, 21, 999, etc.
        LocalDateTime fechaHoraActual = LocalDateTime.now();
        DateTimeFormatter formatoConDia = DateTimeFormatter.ofPattern("EEEE dd/MM/yyyy HH:mm:ss", new Locale("es", "ES"));
        String fechaHoraFormateada = fechaHoraActual.format(formatoConDia).toUpperCase();
        
        try {
            int printerWidth = 384; // ancho máximo en píxeles de la TM-T88V

            // Calcular tamaño de fuente dinámico
            int fontSize = 10;
            Font font;
            FontMetrics fm;
            int textWidth;
            int textHeight;

            BufferedImage tmpImg = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
            Graphics2D tmpG = tmpImg.createGraphics();

            do {
                fontSize += 5;
                font = new Font("Arial", Font.BOLD, fontSize);
                tmpG.setFont(font);
                fm = tmpG.getFontMetrics();
                textWidth = fm.stringWidth(numero);
                textHeight = fm.getHeight();
            } while (textWidth < printerWidth - 20 && textHeight < 400 && fontSize < 300);

            tmpG.dispose();

            // Ajustar altura de imagen al texto
            int imgHeight = textHeight + 20; // margen de seguridad
            BufferedImage image = new BufferedImage(printerWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, printerWidth, imgHeight);
            g.setColor(Color.BLACK);
            g.setFont(font);

            // Centrar y dibujar
            int x = (printerWidth - textWidth) / 2;
            int y = fm.getAscent();
            g.drawString(numero, x, y);
            g.dispose();

            // Recortar imagen para eliminar espacio en blanco superior
            image = cropTopWhite(image);

            // Convertir a ESC/POS raster
            byte[] escposImage = convertToEscPosRaster(image);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            out.write(new byte[]{0x1B, 0x40}); // inicializar

            // === AGREGADO: imprimir logo al inicio ===
            BufferedImage logo = ImageIO.read(Epson.class.getResource("/images/carlogo.jpeg"));

            // Aplanar transparencia sobre blanco
            BufferedImage logoFinal = new BufferedImage(logo.getWidth(), logo.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = logoFinal.createGraphics();
            g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, logoFinal.getWidth(), logoFinal.getHeight());
            g2.drawImage(logo, 0, 0, null);
            g2.dispose();

            // Recortar logo para eliminar espacio en blanco superior
            logoFinal = cropTopWhite(logoFinal);

            // Convertir a ESC/POS raster
            byte[] escposLogo = convertToEscPosRaster(logoFinal);

            
            out.write(escposLogo);
            out.write(new byte[]{0x1B, 0x64, 0x01}); // pequeño feed
            out.write(escposImage);
            out.write((fechaHoraFormateada + "\n").getBytes("CP437"));
            out.write(new byte[]{0x1B, 0x64, 0x01}); // pequeño feed
            out.write(("TURNO CARNES FRIAS \n").getBytes("CP437"));
            out.write(new byte[]{0x1B, 0x64, 0x01}); // pequeño feed
            

            // Avanzar papel y cortar
            out.write(new byte[]{0x1B, 0x64, 0x05}); // feed
            out.write(new byte[]{0x1D, 0x56, 0x00}); // corte

            byte[] finalData = out.toByteArray();

            if (epsonService != null) {
                DocPrintJob printJob = epsonService.createPrintJob();
                Doc doc = new SimpleDoc(finalData, DocFlavor.BYTE_ARRAY.AUTOSENSE, null);
                printJob.print(doc, null);
                System.out.println("Ticket enviado a Epson con logo y número gigante.");
            } else {
                System.out.println("Impresora Epson no encontrada.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static int getTurnoCarnes() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        System.out.println("inicio turnos");
        try {
            conn = conectar();
            String query = "SELECT id_turno,fecha_registro,numero,estatus,fecha_inicio FROM turnocarnes WHERE DATE(fecha_registro) = CURDATE() ORDER BY id_turno DESC;";
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
            System.out.println("realizo consulta");
            System.out.println(rs);
            int nuevo = 0;
            if(rs.next()){
                int numero = rs.getInt("numero");
                nuevo = numero + 1;
            }else{
                int numero = 0;
                nuevo = numero + 1;
            }
            String insertSQL = "INSERT INTO turnocarnes (numero) VALUES (?)";
            stmt = conn.prepareStatement(insertSQL);
            stmt.setInt(1, nuevo);
            stmt.executeUpdate();
            System.out.println("Numero mas alto "+ nuevo +".");
            stmt.close();
            return nuevo;
        } catch (SQLException e) {
            System.out.println("Error EL NUMERO DE TURNO: " + e.getMessage());
        }
        return 0;
    }
    
}
