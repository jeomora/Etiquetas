/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.etiquetas;

import static com.mycompany.etiquetas.ConexionEpson.conectar;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author PROGRAMEITOR
 */
public class Semillas extends javax.swing.JFrame {
    String prodDesc = "";
    String cantBasc = "";
    String codis = "";
    Basculon basculon = new Basculon();
    Historial historial = new Historial();
    TestRXTX impres = new TestRXTX();
    Epson epson;
    ConexionEpson conespon;
    private long lastPrintTime = 0;
    DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Código", "Descripción", "Precio"}, 0){
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;  // Hace que todas las celdas no sean editables
        }
    };
    
    
    public Semillas() {
        initComponents();
        SwingUtilities.invokeLater(() -> btnLast1.requestFocusInWindow());
        JTableHeader header = tblProdos.getTableHeader();
        Font font = new Font("Arial", Font.BOLD, 28);  // Fuente: Arial, Negrita, tamaño 18
        header.setFont(font);
        header.setBackground(Color.BLACK); // Cambiar el color de fondo
        header.setForeground(Color.WHITE); // Cambiar el color del texto
        configurarTabla(tblProdos);
        
        ConexionSemillas loader = new ConexionSemillas();
        loader.cargarProductosProv();
        loader.cargarProductos();
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(40, Integer.MAX_VALUE));
        
        JButton[] botones = {btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9,
                     btn10, btn11, btn12, btn13, btn14, btn15, btn16, btn17, btn18,btn19,btn20,btn21,btn22,btn23,btn24,btn25,btn26};

        for (JButton btn : botones) {
            btn.addActionListener(e -> cargaProductosFiltro(btn.getText()));
        }
        
        txtBusca.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    //Runtime.getRuntime().exec("cmd /c start osk");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        
        txtBusca.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String textoIngresado = txtBusca.getText();
                cargaProductosFiltro(textoIngresado);
            }
        });

        
        tblProdos.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Verificamos si es un doble clic
                if (e.getClickCount() == 2) {
                    int selectedRow = tblProdos.rowAtPoint(e.getPoint()); // Obtener la fila donde se hizo clic
                    accionFila(selectedRow); // Llamar a la acción que desees con la fila seleccionada
                }
            }
        });

        setTitle("SEMILLAS IMPRESIÓN DE ETIQUETAS");
        setSize(1920, 1080);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        String comPort = leerPuertoCOMDesdeArchivo();
        if (comPort != null) {
            basculon.initialize(comPort);
        } else {
            basculon.initialize("COM5");
        }
        
        codigoTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                buscaProducto(evt); 
            }
        });
        setActionsBtns();
    }
    
    private void buscaProducto(ActionEvent evt){
        String codigo = codigoTextField.getText();
        Producto datos = ConexionBaseDeDatos.buscarProductoPorCodigo(codigo);
        if(datos != null){
            historial.agregarProducto(new Producto(datos.getCodigo(), datos.getNombre(),1,""));
            asignarDescripcionBotones(historial, btnLast1, btnLast2, btnLast3, btnLast4, btnLast5);
        }
        buscaProdPrint(codigo);
    }
    
    
    private static void configurarTabla(JTable tblProdos) {
        Font customFont = new Font("Arial", Font.PLAIN, 42);
        tblProdos.setFont(customFont);

        int rowHeight = tblProdos.getFontMetrics(customFont).getHeight();
        tblProdos.setRowHeight(rowHeight);

        TableColumnModel columnModel = tblProdos.getColumnModel();

        columnModel.getColumn(0).setPreferredWidth(0);
        columnModel.getColumn(1).setPreferredWidth(1100);
        columnModel.getColumn(2).setPreferredWidth(250);
        tblProdos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); 
        tblProdos.revalidate();
        tblProdos.repaint();
    }
    
    
    private void accionFila(int selectedRow){
        if (selectedRow >= 0) {
            codis = tblProdos.getValueAt(selectedRow, 0).toString();
            prodDesc = tblProdos.getValueAt(selectedRow, 1).toString();
            String precio = tblProdos.getValueAt(selectedRow, 2).toString();

            String weight = basculon.readWeight();
            resultadoLabel.setText("<html>" + codis + "<br><strong>"+prodDesc+"</strong><br>"+
                    "<strong>PRECIO: $ "+ precio +"</strong></html>");
            lblPeso.setText(weight);
            
            historial.agregarProducto(new Producto(codis, prodDesc,1,""));
            asignarDescripcionBotones(historial, btnLast1, btnLast2, btnLast3, btnLast4, btnLast5);
            
            if(weight == null){
                testPrinterConnectionSin();
            }else if(Float.parseFloat(weight.replaceAll("[^\\d.]", "")) == 0){
                testPrinterConnectionSin();
            }else if(Float.parseFloat(weight.replaceAll("[^\\d.]", "")) < 0.100){
                int option = JOptionPane.showConfirmDialog(
                        null, 
                        "El peso es menor a 0.050. ¿Deseas continuar?", 
                        "Advertencia", 
                        JOptionPane.YES_NO_OPTION
                );
                if (option == JOptionPane.YES_OPTION){
                    weight = basculon.readWeight();
                    cantBasc = weight.replaceAll("[^\\d.]", "");
                    testPrinterConnection();
                }
            }else{
                cantBasc = weight.replaceAll("[^\\d.]", "");
                testPrinterConnection();
            }
        }
    }  
    
    private void testPrinterConnection() {
        try {
            String puerto = "COM1"; // Ajusta según tu configuración
            // Llamar al código de TestRXTX
            //TestRXTX.testPrinter(codigoTextField.getText(),txtCantidad.getText(),prodDesc);
            impres.testPrinter(codis,cantBasc,prodDesc);
             new java.util.Timer().schedule( 
                new java.util.TimerTask() { 
                    @Override 
                    public void run() { 
                        resultadoLabel.setText("");
                        cantBasc = "";
                        lblPeso.setText("");
                        codigoTextField.setText("");
                    } 
                }, 
                1000 // 3 segundos
            );
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al conectar con la impresora: " + e.getMessage(),
                    "Error de Conexión", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    private void testPrinterConnectionSin() {
        try {
            // Usar un puerto COM para la impresora (por ejemplo, USB001 en Windows)
            String puerto = "COM1"; // Ajusta según tu configuración

            // Llamar al código de TestRXTX
            //TestRXTX.testPrinter(codigoTextField.getText(),txtCantidad.getText(),prodDesc);
            impres.testPrinterSin(codis,prodDesc);
            //impres.testPrinter(codis,"0.230",prodDesc);
             new java.util.Timer().schedule( 
                new java.util.TimerTask() { 
                    @Override 
                    public void run() { 
                        resultadoLabel.setText("");
                        cantBasc = "";
                        lblPeso.setText("");
                        codigoTextField.setText("");
                    } 
                }, 
                2000 // 3 segundos
            );
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al conectar con la impresora: " + e.getMessage(),
                    "Error de Conexión", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private String leerPuertoCOMDesdeArchivo() {
        String comPort = null;
        try {
            File archivo = new File("comport.txt");
            if (archivo.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(archivo));
                comPort = reader.readLine();
                reader.close();
            } else {
                System.out.println("El archivo comport.txt no existe.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return comPort;
    }
    
    private void setActionsBtns(){
        btnLast1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Producto producto = historial.obtenerProducto(0);
                if (producto != null) {
                    buscaProdPrint(producto.getCodigo());
                }
            }
        });

        btnLast2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Producto producto = historial.obtenerProducto(1);
                if (producto != null) {
                    buscaProdPrint(producto.getCodigo());
                }
            }
        });

        btnLast3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Producto producto = historial.obtenerProducto(2);
                if (producto != null) {
                    buscaProdPrint(producto.getCodigo());
                }
            }
        });

        btnLast4.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Producto producto = historial.obtenerProducto(3);
                if (producto != null) {
                    buscaProdPrint(producto.getCodigo());
                }
            }
        });

        btnLast5.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Producto producto = historial.obtenerProducto(4);
                if (producto != null) {
                    buscaProdPrint(producto.getCodigo());
                }
            }
        });
    }
    
    private void buscaProdPrint(String codigo){
        Producto datos = ConexionSemillas.buscarProductoPorCodigo(codigo);
        System.out.println(codigo);
        if(datos != null){ 
            String weight = basculon.readWeight();
            codis = datos.getCodigo();
            prodDesc = datos.getNombre();
            lblPeso.setText(weight);
            resultadoLabel.setText("<html>" + datos.getCodigo() + "<br><strong>"+datos.getNombre()+"</strong><br>"+
                "<strong>PRECIO: $ "+datos.getPrecioUno()+"</strong></html>");
            if(weight == null){
                    testPrinterConnectionSin();
                }else if(Float.parseFloat(weight.replaceAll("[^\\d.]", "")) == 0){
                    testPrinterConnectionSin();
                }else if(Float.parseFloat(weight.replaceAll("[^\\d.]", "")) < 0.100){
                    cantBasc = weight.replaceAll("[^\\d.]", "");
                    int option = JOptionPane.showConfirmDialog(
                            null, 
                            "El peso es menor a 0.100. ¿Deseas continuar?", 
                            "Advertencia", 
                            JOptionPane.YES_NO_OPTION
                    );
                    if (option == JOptionPane.YES_OPTION) {
                        testPrinterConnection();
                    }
                }else{
                    cantBasc = weight.replaceAll("[^\\d.]", "");
                    testPrinterConnection();
                }
        }else{
            resultadoLabel.setText("<html>Producto no encontrado.</html>");
        }
    }
    
    private void cargaProductos(int ides){
        System.out.println(""+ides);
        List<Produs> productos = ConexionSemillas.obtenerProductosPorProveedor(ides);
        tableModel.setRowCount(0);
        for (Produs producto : productos) {
            tableModel.addRow(new Object[]{
                producto.getCodigo(),
                producto.getDescripcion(),
                "$ "+producto.getPrecioUno()
            });
        }
        configurarTabla(tblProdos);
    }
    
    private void cargaProductosFiltro(String clave){
        System.out.println(clave);
        List<Producto> productos = ConexionSemillas.buscarPorClave(clave);
        Collections.sort(productos, new Comparator<Producto>() {
            @Override
            public int compare(Producto p1, Producto p2) {
                return p1.getNombre().compareToIgnoreCase(p2.getNombre());
            }
        });
        tableModel.setRowCount(0);
        for (Producto producto : productos) {
            tableModel.addRow(new Object[]{
                producto.getCodigo(),
                producto.getNombre(),
                "$ "+producto.getPrecioUno()
            });
        }
        configurarTabla(tblProdos);
    }
    
    // Método para asignar las descripciones a los botones
    private static void asignarDescripcionBotones(Historial historial, JButton btnLast1, JButton btnLast2,
                                                  JButton btnLast3, JButton btnLast4, JButton btnLast5) {
        // Asignar las descripciones de los productos al texto de los botones
        if (historial.obtenerHistorial().size() > 0) {
            btnLast1.setText(historial.obtenerProducto(0).getNombre());
        }
        if (historial.obtenerHistorial().size() > 1) {
            btnLast2.setText(historial.obtenerProducto(1).getNombre());
        }
        if (historial.obtenerHistorial().size() > 2) {
            btnLast3.setText(historial.obtenerProducto(2).getNombre());
        }
        if (historial.obtenerHistorial().size() > 3) {
            btnLast4.setText(historial.obtenerProducto(3).getNombre());
        }
        if (historial.obtenerHistorial().size() > 4) {
            btnLast5.setText(historial.obtenerProducto(4).getNombre());
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        codigoTextField = new javax.swing.JTextField();
        lblPeso = new javax.swing.JLabel();
        resultadoLabel = new javax.swing.JLabel();
        btnLast5 = new javax.swing.JButton();
        btnLast3 = new javax.swing.JButton();
        btnLast4 = new javax.swing.JButton();
        btnLast2 = new javax.swing.JButton();
        btnLast1 = new javax.swing.JButton();
        btnArroz = new javax.swing.JButton();
        btnDulces = new javax.swing.JButton();
        btnConsome = new javax.swing.JButton();
        btnChiles = new javax.swing.JButton();
        btnSemillas = new javax.swing.JButton();
        scrollPane = new javax.swing.JScrollPane();
        tblProdos = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        btn12 = new javax.swing.JButton();
        btn5 = new javax.swing.JButton();
        btn4 = new javax.swing.JButton();
        btn6 = new javax.swing.JButton();
        btn8 = new javax.swing.JButton();
        btn2 = new javax.swing.JButton();
        btn3 = new javax.swing.JButton();
        btn9 = new javax.swing.JButton();
        btn7 = new javax.swing.JButton();
        btn10 = new javax.swing.JButton();
        txtBusca = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        btn14 = new javax.swing.JButton();
        btn17 = new javax.swing.JButton();
        btn13 = new javax.swing.JButton();
        btn18 = new javax.swing.JButton();
        btn19 = new javax.swing.JButton();
        btn16 = new javax.swing.JButton();
        btn20 = new javax.swing.JButton();
        btnDog = new javax.swing.JButton();
        btn15 = new javax.swing.JButton();
        btn1 = new javax.swing.JButton();
        btn21 = new javax.swing.JButton();
        btn22 = new javax.swing.JButton();
        btn26 = new javax.swing.JButton();
        btnTurno = new javax.swing.JButton();
        btn25 = new javax.swing.JButton();
        btn11 = new javax.swing.JButton();
        btn23 = new javax.swing.JButton();
        btn24 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/logaz2.png"))); // NOI18N
        getContentPane().add(jLabel2);
        jLabel2.setBounds(20, 10, 150, 160);

        codigoTextField.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        getContentPane().add(codigoTextField);
        codigoTextField.setBounds(210, 30, 260, 60);

        lblPeso.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        lblPeso.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(lblPeso);
        lblPeso.setBounds(190, 100, 280, 70);

        resultadoLabel.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        resultadoLabel.setForeground(new java.awt.Color(255, 255, 255));
        resultadoLabel.setToolTipText("a");
        getContentPane().add(resultadoLabel);
        resultadoLabel.setBounds(500, 30, 780, 220);

        btnLast5.setBackground(new java.awt.Color(204, 255, 204));
        btnLast5.setBorder(BorderFactory.createLineBorder(Color.GREEN));
        getContentPane().add(btnLast5);
        btnLast5.setBounds(1640, 80, 210, 40);

        btnLast3.setBackground(new java.awt.Color(204, 255, 204));
        btnLast3.setBorder(BorderFactory.createLineBorder(Color.GREEN));
        btnLast3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLast3ActionPerformed(evt);
            }
        });
        getContentPane().add(btnLast3);
        btnLast3.setBounds(1640, 200, 210, 40);

        btnLast4.setBackground(new java.awt.Color(204, 255, 204));
        btnLast4.setBorder(BorderFactory.createLineBorder(Color.GREEN));
        getContentPane().add(btnLast4);
        btnLast4.setBounds(1640, 140, 210, 40);

        btnLast2.setBackground(new java.awt.Color(204, 255, 204));
        btnLast2.setBorder(BorderFactory.createLineBorder(Color.GREEN));
        getContentPane().add(btnLast2);
        btnLast2.setBounds(1640, 260, 210, 40);

        btnLast1.setBackground(new java.awt.Color(204, 255, 204));
        btnLast1.setBorder(BorderFactory.createLineBorder(Color.GREEN));
        btnLast1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLast1ActionPerformed(evt);
            }
        });
        getContentPane().add(btnLast1);
        btnLast1.setBounds(1640, 320, 210, 40);

        btnArroz.setBackground(new java.awt.Color(153, 204, 255));
        btnArroz.setFont(new java.awt.Font("Segoe Print", 1, 24)); // NOI18N
        btnArroz.setText("ARROZ");
        btnArroz.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 102, 255)));
        btnArroz.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnArrozActionPerformed(evt);
            }
        });
        getContentPane().add(btnArroz);
        btnArroz.setBounds(850, 300, 130, 60);

        btnDulces.setBackground(new java.awt.Color(153, 204, 255));
        btnDulces.setFont(new java.awt.Font("Segoe Print", 1, 24)); // NOI18N
        btnDulces.setText("DULCES");
        btnDulces.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 102, 255)));
        btnDulces.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDulcesActionPerformed(evt);
            }
        });
        getContentPane().add(btnDulces);
        btnDulces.setBounds(1000, 300, 120, 60);

        btnConsome.setBackground(new java.awt.Color(153, 204, 255));
        btnConsome.setFont(new java.awt.Font("Segoe Print", 1, 24)); // NOI18N
        btnConsome.setText("CONSOMES");
        btnConsome.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 102, 255)));
        btnConsome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConsomeActionPerformed(evt);
            }
        });
        getContentPane().add(btnConsome);
        btnConsome.setBounds(1140, 300, 150, 60);

        btnChiles.setBackground(new java.awt.Color(153, 204, 255));
        btnChiles.setFont(new java.awt.Font("Segoe Print", 1, 24)); // NOI18N
        btnChiles.setText("CHILES");
        btnChiles.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 102, 255)));
        btnChiles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChilesActionPerformed(evt);
            }
        });
        getContentPane().add(btnChiles);
        btnChiles.setBounds(1310, 300, 140, 60);

        btnSemillas.setBackground(new java.awt.Color(153, 204, 255));
        btnSemillas.setFont(new java.awt.Font("Segoe Print", 1, 24)); // NOI18N
        btnSemillas.setText("SEMILLAS ");
        btnSemillas.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 102, 255)));
        btnSemillas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSemillasActionPerformed(evt);
            }
        });
        getContentPane().add(btnSemillas);
        btnSemillas.setBounds(1470, 300, 140, 60);

        scrollPane.setBackground(new java.awt.Color(204, 255, 204));

        tblProdos.setBorder(new javax.swing.border.MatteBorder(null));
        tblProdos.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        tblProdos.setModel(tableModel);
        scrollPane.setViewportView(tblProdos);

        getContentPane().add(scrollPane);
        scrollPane.setBounds(500, 390, 1370, 610);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("ÚLTIMOS 5");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(1640, 10, 210, 50);

        btn12.setBackground(new java.awt.Color(204, 255, 204));
        btn12.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btn12.setText("MAIZ");
        btn12.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(btn12);
        btn12.setBounds(250, 550, 210, 40);

        btn5.setBackground(new java.awt.Color(204, 255, 204));
        btn5.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btn5.setText("CHILE");
        btn5.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(btn5);
        btn5.setBounds(20, 700, 210, 40);

        btn4.setBackground(new java.awt.Color(204, 255, 204));
        btn4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btn4.setText("FRIJOL");
        btn4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(btn4);
        btn4.setBounds(20, 650, 210, 40);

        btn6.setBackground(new java.awt.Color(204, 255, 204));
        btn6.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btn6.setText("FRITURA");
        btn6.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(btn6);
        btn6.setBounds(20, 750, 210, 40);

        btn8.setBackground(new java.awt.Color(204, 255, 204));
        btn8.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btn8.setText("MONTES");
        btn8.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(btn8);
        btn8.setBounds(20, 850, 210, 40);

        btn2.setBackground(new java.awt.Color(204, 255, 204));
        btn2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btn2.setText("GOMITAS");
        btn2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn2ActionPerformed(evt);
            }
        });
        getContentPane().add(btn2);
        btn2.setBounds(20, 550, 210, 40);

        btn3.setBackground(new java.awt.Color(204, 255, 204));
        btn3.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btn3.setText("CACAHUATE");
        btn3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(btn3);
        btn3.setBounds(20, 600, 210, 40);

        btn9.setBackground(new java.awt.Color(204, 255, 204));
        btn9.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btn9.setText("CHILARINES");
        btn9.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(btn9);
        btn9.setBounds(20, 900, 210, 40);

        btn7.setBackground(new java.awt.Color(204, 255, 204));
        btn7.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btn7.setText("CHAMOY");
        btn7.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(btn7);
        btn7.setBounds(20, 800, 210, 40);

        btn10.setBackground(new java.awt.Color(204, 255, 204));
        btn10.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btn10.setText("HORCHATA");
        btn10.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(btn10);
        btn10.setBounds(20, 962, 210, 38);

        txtBusca.setBackground(new java.awt.Color(204, 255, 255));
        txtBusca.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        getContentPane().add(txtBusca);
        txtBusca.setBounds(1340, 150, 260, 50);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("BUSCAR PRODUCTO");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(1350, 110, 250, 30);

        btn14.setBackground(new java.awt.Color(204, 255, 204));
        btn14.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btn14.setText("CONFICHOCKY");
        btn14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn14ActionPerformed(evt);
            }
        });
        getContentPane().add(btn14);
        btn14.setBounds(250, 650, 210, 40);

        btn17.setBackground(new java.awt.Color(204, 255, 204));
        btn17.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btn17.setText("CARAMELO");
        getContentPane().add(btn17);
        btn17.setBounds(250, 800, 210, 40);

        btn13.setBackground(new java.awt.Color(204, 255, 204));
        btn13.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btn13.setText("FRUTA");
        getContentPane().add(btn13);
        btn13.setBounds(250, 600, 210, 40);

        btn18.setBackground(new java.awt.Color(204, 255, 204));
        btn18.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btn18.setText("NUEZ ");
        btn18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn18ActionPerformed(evt);
            }
        });
        getContentPane().add(btn18);
        btn18.setBounds(250, 851, 210, 39);

        btn19.setBackground(new java.awt.Color(204, 255, 204));
        btn19.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btn19.setText("CAMARON");
        getContentPane().add(btn19);
        btn19.setBounds(250, 900, 210, 40);

        btn16.setBackground(new java.awt.Color(204, 255, 204));
        btn16.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btn16.setText("CORAZON");
        getContentPane().add(btn16);
        btn16.setBounds(250, 750, 210, 40);

        btn20.setBackground(new java.awt.Color(204, 255, 204));
        btn20.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btn20.setText("PIMIENTA");
        getContentPane().add(btn20);
        btn20.setBounds(250, 960, 210, 40);

        btnDog.setBackground(new java.awt.Color(153, 204, 255));
        btnDog.setFont(new java.awt.Font("Segoe Print", 1, 24)); // NOI18N
        btnDog.setText("ALIMENTO ");
        btnDog.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 102, 255)));
        btnDog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDogActionPerformed(evt);
            }
        });
        getContentPane().add(btnDog);
        btnDog.setBounds(680, 300, 150, 60);

        btn15.setBackground(new java.awt.Color(204, 255, 204));
        btn15.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btn15.setText("LUNETA");
        getContentPane().add(btn15);
        btn15.setBounds(250, 700, 210, 40);

        btn1.setBackground(new java.awt.Color(204, 255, 204));
        btn1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btn1.setText("AJO");
        btn1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(btn1);
        btn1.setBounds(20, 500, 210, 40);

        btn21.setBackground(new java.awt.Color(204, 255, 204));
        btn21.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btn21.setText("PEDIGREE");
        btn21.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(btn21);
        btn21.setBounds(20, 400, 210, 40);

        btn22.setBackground(new java.awt.Color(204, 255, 204));
        btn22.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btn22.setText("WHISCAS");
        btn22.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(btn22);
        btn22.setBounds(20, 450, 210, 40);

        btn26.setBackground(new java.awt.Color(204, 255, 204));
        btn26.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btn26.setText("MININO");
        btn26.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(btn26);
        btn26.setBounds(250, 450, 210, 40);

        btnTurno.setBackground(new java.awt.Color(51, 51, 255));
        btnTurno.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnTurno.setForeground(new java.awt.Color(255, 255, 255));
        btnTurno.setText("NUEVO TURNO");
        btnTurno.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnTurno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTurnoActionPerformed(evt);
            }
        });
        getContentPane().add(btnTurno);
        btnTurno.setBounds(20, 200, 250, 90);

        btn25.setBackground(new java.awt.Color(204, 255, 204));
        btn25.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btn25.setText("CHOW");
        btn25.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(btn25);
        btn25.setBounds(250, 400, 210, 38);

        btn11.setBackground(new java.awt.Color(204, 255, 204));
        btn11.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btn11.setText("HUEVO");
        btn11.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btn11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn11ActionPerformed(evt);
            }
        });
        getContentPane().add(btn11);
        btn11.setBounds(250, 500, 210, 40);

        btn23.setBackground(new java.awt.Color(204, 255, 204));
        btn23.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btn23.setText("SILVER");
        btn23.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(btn23);
        btn23.setBounds(20, 350, 210, 40);

        btn24.setBackground(new java.awt.Color(204, 255, 204));
        btn24.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btn24.setText("PERRO");
        btn24.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(btn24);
        btn24.setBounds(250, 350, 210, 38);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/backnon.png"))); // NOI18N
        jLabel1.setText("jLabel1");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(0, 0, 1920, 1080);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSemillasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSemillasActionPerformed
        cargaProductos(5);
    }//GEN-LAST:event_btnSemillasActionPerformed

    private void btnChilesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChilesActionPerformed
        cargaProductos(37);
    }//GEN-LAST:event_btnChilesActionPerformed

    private void btnDulcesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDulcesActionPerformed
        cargaProductos(40);
    }//GEN-LAST:event_btnDulcesActionPerformed

    private void btnLast1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLast1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLast1ActionPerformed

    private void btn14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn14ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn14ActionPerformed

    private void btn18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn18ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn18ActionPerformed

    private void btnLast3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLast3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLast3ActionPerformed

    private void btn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn2ActionPerformed

    private void btnConsomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConsomeActionPerformed
        cargaProductos(34);
    }//GEN-LAST:event_btnConsomeActionPerformed

    private void btnArrozActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnArrozActionPerformed
        cargaProductos(19);
    }//GEN-LAST:event_btnArrozActionPerformed

    private void btnDogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDogActionPerformed
        cargaProductos(8);
    }//GEN-LAST:event_btnDogActionPerformed

    private void btn11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn11ActionPerformed

    private void btnTurnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTurnoActionPerformed
        long now = System.currentTimeMillis();
        if (now - lastPrintTime >= 3000) { // 3000 ms = 3 segundos
            Connection conn = null;
            PreparedStatement stmt = null;
            ResultSet rs = null;
            System.out.println("inicio turnos");

            try {
                conn = conectar();

                // Leer el número de caja desde caja.txt
                int numeroCaja = 1; // valor por defecto
                File archivoCaja = new File("caja.txt");
                if (archivoCaja.exists()) {
                    try (BufferedReader br = new BufferedReader(new FileReader(archivoCaja))) {
                        String linea = br.readLine();
                        if (linea != null && !linea.isEmpty()) {
                            numeroCaja = Integer.parseInt(linea.trim());
                        }
                    } catch (Exception e) {
                        System.out.println("Error leyendo caja.txt: " + e.getMessage());
                    }
                } else {
                    System.out.println("Archivo caja.txt no encontrado.");
                }

                String query = "SELECT id_turno,fecha_registro,numero,estatus,fecha_inicio FROM turnosemillas " +
                               "WHERE DATE(fecha_registro) = CURDATE() AND estatus = 1 ORDER BY id_turno ASC;";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                System.out.println("realizo consulta");

                if (rs.next()) {
                    int id_turno = rs.getInt("id_turno");
                    int numero = rs.getInt("numero");
                    System.out.println(numero);

                    // Actualizar estatus y agregar campo caja
                    String updateSQL = "UPDATE turnosemillas SET estatus = 2, caja = ? WHERE id_turno = ?";
                    stmt = conn.prepareStatement(updateSQL);
                    stmt.setInt(1, numeroCaja);
                    stmt.setInt(2, id_turno);
                    stmt.executeUpdate();

                    btnTurno.setText("NUEVO TURNO " + numero);
                    // reproducirSonido();

                    // 🔑 Actualizar cooldown
                    lastPrintTime = now;
                } else {
                    btnTurno.setText("NO HAY NÚMEROS SIGUIENTES");
                    lastPrintTime = now; // también actualiza para evitar spam
                }

            } catch (SQLException e) {
                System.out.println("Error EL NÚMERO DE TURNO: " + e.getMessage());
            }
        } else {
            System.out.println("Esperando cooldown de impresión...");
        }

        
    }//GEN-LAST:event_btnTurnoActionPerformed

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
            java.util.logging.Logger.getLogger(Semillas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Semillas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Semillas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Semillas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Semillas().setVisible(true);
            }
        });
    }
    
    public void reproducirSonido() {
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
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn1;
    private javax.swing.JButton btn10;
    private javax.swing.JButton btn11;
    private javax.swing.JButton btn12;
    private javax.swing.JButton btn13;
    private javax.swing.JButton btn14;
    private javax.swing.JButton btn15;
    private javax.swing.JButton btn16;
    private javax.swing.JButton btn17;
    private javax.swing.JButton btn18;
    private javax.swing.JButton btn19;
    private javax.swing.JButton btn2;
    private javax.swing.JButton btn20;
    private javax.swing.JButton btn21;
    private javax.swing.JButton btn22;
    private javax.swing.JButton btn23;
    private javax.swing.JButton btn24;
    private javax.swing.JButton btn25;
    private javax.swing.JButton btn26;
    private javax.swing.JButton btn3;
    private javax.swing.JButton btn4;
    private javax.swing.JButton btn5;
    private javax.swing.JButton btn6;
    private javax.swing.JButton btn7;
    private javax.swing.JButton btn8;
    private javax.swing.JButton btn9;
    private javax.swing.JButton btnArroz;
    private javax.swing.JButton btnChiles;
    private javax.swing.JButton btnConsome;
    private javax.swing.JButton btnDog;
    private javax.swing.JButton btnDulces;
    private javax.swing.JButton btnLast1;
    private javax.swing.JButton btnLast2;
    private javax.swing.JButton btnLast3;
    private javax.swing.JButton btnLast4;
    private javax.swing.JButton btnLast5;
    private javax.swing.JButton btnSemillas;
    private javax.swing.JButton btnTurno;
    private javax.swing.JTextField codigoTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel lblPeso;
    private javax.swing.JLabel resultadoLabel;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JTable tblProdos;
    private javax.swing.JTextField txtBusca;
    // End of variables declaration//GEN-END:variables
}
