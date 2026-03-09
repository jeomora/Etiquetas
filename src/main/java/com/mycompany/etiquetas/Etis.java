/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.etiquetas;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;


/**
 *
 * @author PROGRAMEITOR
 */
public class Etis extends javax.swing.JFrame {

    String prodDesc = "";
    String cantBasc = "";
    String codis = "";
    Basculon basculon = new Basculon();
    public Etis() {
        initComponents();
        ConexionBaseDeDatos.cargarProductos("8");
        agregarActionListeners();
        String comPort = leerPuertoCOMDesdeArchivo();
        
        // Si se leyó correctamente, inicializa el basculón
        if (comPort != null) {
            basculon.initialize(comPort);
        } else {
            basculon.initialize("COM8");
        }
        
        
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String weight = basculon.readWeight();

        if (weight != null) {
            System.out.println("El peso recibido es: " + weight);
        } else {
            System.out.println("No se recibió peso.");
        }
        
        jScrollPane1.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int notches = e.getWheelRotation();  // La dirección del desplazamiento (positivo si va hacia abajo, negativo si va hacia arriba)
                int scrollSpeed = 15;  // Factor de velocidad para el desplazamiento (ajustable según lo que necesites)
                JScrollBar vertical = jScrollPane1.getVerticalScrollBar();
                if (notches < 0) {
                    vertical.setValue(vertical.getValue() - scrollSpeed);  // Si notches es negativo, sube la barra
                } else {
                    vertical.setValue(vertical.getValue() + scrollSpeed);  // Si notches es positivo, baja la barra
                }
            }
        });
        
        jScrollPane2.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int notches = e.getWheelRotation();  // La dirección del desplazamiento (positivo si va hacia abajo, negativo si va hacia arriba)
                int scrollSpeed = 15;  // Factor de velocidad para el desplazamiento (ajustable según lo que necesites)
                JScrollBar vertical = jScrollPane2.getVerticalScrollBar();
                if (notches < 0) {
                    vertical.setValue(vertical.getValue() - scrollSpeed);  // Si notches es negativo, sube la barra
                } else {
                    vertical.setValue(vertical.getValue() + scrollSpeed);  // Si notches es positivo, baja la barra
                }
            }
        });

        setTitle("IMPRESIÓN DE ETIQUETAS");
        setSize(1280, 1024); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
        codigoTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                buscaProducto(evt); 
            }
        });
    }
    private String leerPuertoCOMDesdeArchivo() {
        String comPort = null;
        try {
            // Obtener el archivo "comport.txt" desde la misma carpeta donde está el JAR
            File archivo = new File("comport.txt");

            // Verificar si el archivo existe
            if (archivo.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(archivo));
                comPort = reader.readLine(); // Leer la primera línea (debe ser el puerto COM)
                reader.close();
            } else {
                System.out.println("El archivo comport.txt no existe.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return comPort;
    }
    
    private void agregarActionListeners() {
        JButton[] botones = {btn00008,btn775019127,btn775019128,btnQCREMACORO,btn775019118,btn775019024,btn775019022,btn75089652715,btn77502019,btn775019120,btn75002,btn775019070
                ,btn7506326001,btn775019116,btn775019114,btn775019110,btn775019107,btn775019106,btn775019102,btn775019100,btn775019066,btn775019027,btn775019026,btn75010400202,btn75010400201,
                btn75010400204,btn775019121,btn775019073,btn775019072,btn775019065,btn775019064,btn750190710,btn752384007,btn752384001,btn752384005,btn752384004,btn752384003,
                btn775019013,btn775019011,btn775019117,btn775019113,btn775019105,btn775019111,btn775019103,btn00002246,btn75013416,btn7502563008,btn775019019,btn775019020,btn75014534801,
                btn7504690501,btn7502048608,btn7504690502,btn7504690504,btn775030335,btn775019125,btn775019123,btn7750191131,btn775019126,btn7503050,btn775019075,btn75889601,btn7501801,
                btn75010,btn75003,btn750190711,btn775030336,btn750695101,btn775019051,btn775019050,btn775019015,btn775019014,btn775019012,btn775019010,btn775019009,btn775019181,
                btn775019018,btn775019017,btn775019008,btn775019007,btn00026};
        String[] nombres = {"btn00008","btn775019127","btn775019128","btnQCREMACORO","btn775019118","btn775019024","btn775019022","btn75089652715","btn77502019","btn775019120",
            "btn75002","btn775019070","btn7506326001","btn775019116","btn775019114","btn775019110","btn775019107","btn775019106","btn775019102","btn775019100","btn775019066","btn775019027",
            "btn775019026","btn75010400202","btn75010400201","btn75010400204","btn775019121","btn775019073","btn775019072","btn775019065","btn775019064","btn750190710","btn752384007",
            "btn752384001","btn752384005","btn752384004","btn752384003","btn775019013","btn775019011","btn775019117","btn775019113","btn775019105","btn775019111","btn775019103",
            "btn00002246","btn75013416","btn7502563008","btn775019019","btn775019020","btn75014534801","btn7504690501","btn7502048608","btn7504690502","btn7504690504","btn775030335",
            "btn775019125","btn775019123","btn7750191131","btn775019126","btn7503050","btn775019075","btn75889601","btn7501801","btn75010","btn75003","btn750190711","btn775030336",
            "btn750695101","btn775019051","btn775019050","btn775019015","btn775019014","btn775019012","btn775019010","btn775019009","btn775019181","btn775019018","btn775019017",
            "btn775019008","btn775019007","btn00026"};

        for (int i = 0; i < botones.length; i++) {
            botones[i].addActionListener(new BotonActionListener());
            botones[i].setActionCommand(nombres[i]);
        }
        
        btnConfig.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String comPortActual = "COM7"; // Este es el puerto COM actual, cambia esto si es necesario
                ConfigFrame configFrame = new ConfigFrame(comPortActual,Etis.this);
                configFrame.setVisible(true); // Muestra el JFrame de configuración
            }
        });
    }


    
    class BotonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Obtener el nombre del botón presionado (actionCommand)
            String botonPresionado = e.getActionCommand();
            
            // Eliminar la primera letra del nombre del producto
            if (botonPresionado.length() > 1) {
                botonPresionado = botonPresionado.substring(3);  // Quitar la primera letra
            }
            System.out.println(botonPresionado);
            // Realizar la acción dependiendo del producto seleccionado
            Producto datos = ConexionBaseDeDatos.buscarProductoPorCodigo(botonPresionado);  // Usamos el nombre modificado como parámetro
            String codigo = botonPresionado;
            System.out.println(datos);
            if(datos != null){ 

                String weight = basculon.readWeight();
                if(weight == null){
                    //txtCantidad.requestFocus();
                    codis = datos.getCodigo();
                    prodDesc = datos.getNombre();
                    lblPeso.setText(weight);
                    //cantBasc = weight.replaceAll("[^\\d.]", "");
                    resultadoLabel.setText("<html>" + datos.getCodigo() + "<br><strong>"+datos.getNombre()+"</strong><br>"+
                        "<strong>PRECIO: $ "+datos.getPrecioUno()+"</strong></html>");
                    testPrinterConnectionSin();
                }else{
                    //txtCantidad.requestFocus();
                    codis = datos.getCodigo();
                    prodDesc = datos.getNombre();
                    lblPeso.setText(weight);
                    cantBasc = weight.replaceAll("[^\\d.]", "");
                    resultadoLabel.setText("<html>" + datos.getCodigo() + "<br><strong>"+datos.getNombre()+"</strong><br>"+
                        "<strong>PRECIO: $ "+datos.getPrecioUno()+"</strong></html>");
                    testPrinterConnection();
                }
                
            }else{
                resultadoLabel.setText("<html>Producto no encontrado.</html>");
            }
        }
    }
    
    private void testPrinterConnection() {
        try {
            // Usar un puerto COM para la impresora (por ejemplo, USB001 en Windows)
            String puerto = "COM1"; // Ajusta según tu configuración

            // Llamar al código de TestRXTX
            //TestRXTX.testPrinter(codigoTextField.getText(),txtCantidad.getText(),prodDesc);
            TestRXTX.testPrinter(codis,cantBasc,prodDesc);
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
    
    private void testPrinterConnectionSin() {
        try {
            // Usar un puerto COM para la impresora (por ejemplo, USB001 en Windows)
            String puerto = "COM1"; // Ajusta según tu configuración

            // Llamar al código de TestRXTX
            //TestRXTX.testPrinter(codigoTextField.getText(),txtCantidad.getText(),prodDesc);
            TestRXTX.testPrinterSin(codis,prodDesc);
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
    
    
    private void buscaProducto(ActionEvent evt){
        String codigo = codigoTextField.getText();
        Producto datos = ConexionBaseDeDatos.buscarProductoPorCodigo(codigo);
        System.out.println(datos);
        if(datos != null){ 
            
            String weight = basculon.readWeight();

            if(weight == null){
                    //txtCantidad.requestFocus();
                    codis = datos.getCodigo();
                    prodDesc = datos.getNombre();
                    lblPeso.setText(weight);
                    //cantBasc = weight.replaceAll("[^\\d.]", "");
                    resultadoLabel.setText("<html>" + datos.getCodigo() + "<br><strong>"+datos.getNombre()+"</strong><br>"+
                        "<strong>PRECIO: $ "+datos.getPrecioUno()+"</strong></html>");
                    testPrinterConnectionSin();
                }else{
                    //txtCantidad.requestFocus();
                    codis = datos.getCodigo();
                    prodDesc = datos.getNombre();
                    lblPeso.setText(weight);
                    cantBasc = weight.replaceAll("[^\\d.]", "");
                    resultadoLabel.setText("<html>" + datos.getCodigo() + "<br><strong>"+datos.getNombre()+"</strong><br>"+
                        "<strong>PRECIO: $ "+datos.getPrecioUno()+"</strong></html>");
                    testPrinterConnection();
                }
        }else{
            resultadoLabel.setText("<html>Producto no encontrado.</html>");
        }
    }
    
    class DecimalFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string == null) return;
            if (esValido(fb.getDocument().getText(0, fb.getDocument().getLength()) + string)) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text == null) return;
            if (esValido(fb.getDocument().getText(0, fb.getDocument().getLength()) + text)) {
                super.replace(fb, offset, length, text, attrs);
            }
        }

        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            super.remove(fb, offset, length);
        }

        private boolean esValido(String texto) {
            return texto.matches("\\d*\\.?\\d*"); // Permite solo números y un punto decimal
        }
    }
    
    public void actualizarPuertoCOM(String nuevoPuerto) {
    // Actualizamos el puerto COM en la clase Etis
        System.out.println("Puerto COM actualizado a: " + nuevoPuerto);
        basculon.initialize(nuevoPuerto); // Actualizamos la conexión con el nuevo puerto COM
    }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        codigoTextField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        resultadoLabel = new javax.swing.JLabel();
        lblPeso = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btn00008 = new javax.swing.JButton();
        btn775019128 = new javax.swing.JButton();
        btnQCREMACORO = new javax.swing.JButton();
        btn775019127 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        btn775019118 = new javax.swing.JButton();
        btn775019024 = new javax.swing.JButton();
        btn75089652715 = new javax.swing.JButton();
        btn77502019 = new javax.swing.JButton();
        btn775019022 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        btn775019120 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        btn775019070 = new javax.swing.JButton();
        btn7506326001 = new javax.swing.JButton();
        btn75002 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        btn775019121 = new javax.swing.JButton();
        btn775019073 = new javax.swing.JButton();
        btn775019072 = new javax.swing.JButton();
        btn750190711 = new javax.swing.JButton();
        btn775019065 = new javax.swing.JButton();
        btn775019064 = new javax.swing.JButton();
        btn750190710 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        btn75013416 = new javax.swing.JButton();
        btn7502563008 = new javax.swing.JButton();
        btn775019019 = new javax.swing.JButton();
        btn775019020 = new javax.swing.JButton();
        btn75014534801 = new javax.swing.JButton();
        btn7504690501 = new javax.swing.JButton();
        btn7502048608 = new javax.swing.JButton();
        btn7504690502 = new javax.swing.JButton();
        btn7504690504 = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        btn775030335 = new javax.swing.JButton();
        btn775019125 = new javax.swing.JButton();
        btn775019123 = new javax.swing.JButton();
        btn7750191131 = new javax.swing.JButton();
        btn775019126 = new javax.swing.JButton();
        btn7503050 = new javax.swing.JButton();
        btn775019075 = new javax.swing.JButton();
        btn75889601 = new javax.swing.JButton();
        btn7501801 = new javax.swing.JButton();
        btn75010 = new javax.swing.JButton();
        btn75003 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        btn752384007 = new javax.swing.JButton();
        btn752384001 = new javax.swing.JButton();
        btn752384005 = new javax.swing.JButton();
        btn752384004 = new javax.swing.JButton();
        btn752384003 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel5 = new javax.swing.JPanel();
        btn775019116 = new javax.swing.JButton();
        btn775019114 = new javax.swing.JButton();
        btn775019107 = new javax.swing.JButton();
        btn775019110 = new javax.swing.JButton();
        btn775019106 = new javax.swing.JButton();
        btn775019102 = new javax.swing.JButton();
        btn775019100 = new javax.swing.JButton();
        btn775019066 = new javax.swing.JButton();
        btn775019027 = new javax.swing.JButton();
        btn775019026 = new javax.swing.JButton();
        btn75010400202 = new javax.swing.JButton();
        btn75010400201 = new javax.swing.JButton();
        btn75010400204 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        btn775019013 = new javax.swing.JButton();
        btn775019011 = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        btn775019117 = new javax.swing.JButton();
        btn775019105 = new javax.swing.JButton();
        btn775019111 = new javax.swing.JButton();
        btn775019113 = new javax.swing.JButton();
        btn775019103 = new javax.swing.JButton();
        btn00002246 = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel12 = new javax.swing.JPanel();
        btn775030336 = new javax.swing.JButton();
        btn750695101 = new javax.swing.JButton();
        btn775019051 = new javax.swing.JButton();
        btn775019050 = new javax.swing.JButton();
        btn775019015 = new javax.swing.JButton();
        btn775019014 = new javax.swing.JButton();
        btn775019012 = new javax.swing.JButton();
        btn775019010 = new javax.swing.JButton();
        btn775019009 = new javax.swing.JButton();
        btn775019181 = new javax.swing.JButton();
        btn775019018 = new javax.swing.JButton();
        btn775019017 = new javax.swing.JButton();
        btn775019008 = new javax.swing.JButton();
        btn775019007 = new javax.swing.JButton();
        btn00026 = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        btnRefresh = new javax.swing.JButton();
        btnConfig = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setFocusCycleRoot(false);
        setMaximumSize(new java.awt.Dimension(1280, 1024));
        setMinimumSize(new java.awt.Dimension(1280, 1024));
        setPreferredSize(new java.awt.Dimension(1280, 1024));
        setResizable(false);
        getContentPane().setLayout(null);

        codigoTextField.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        codigoTextField.setMargin(new java.awt.Insets(6, 6, 2, 6));
        codigoTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                codigoTextFieldActionPerformed(evt);
            }
        });
        codigoTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                codigoTextFieldKeyPressed(evt);
            }
        });
        getContentPane().add(codigoTextField);
        codigoTextField.setBounds(40, 180, 206, 38);

        jLabel1.setFont(new java.awt.Font("Segoe UI Emoji", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("INGRESA EL CÓDIGO");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(40, 150, 210, 14);

        resultadoLabel.setBackground(new java.awt.Color(204, 255, 255));
        resultadoLabel.setFont(new java.awt.Font("Segoe UI Symbol", 1, 24)); // NOI18N
        resultadoLabel.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(resultadoLabel);
        resultadoLabel.setBounds(670, 10, 510, 270);

        lblPeso.setBackground(new java.awt.Color(255, 255, 255));
        lblPeso.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        lblPeso.setForeground(new java.awt.Color(255, 255, 255));
        lblPeso.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        getContentPane().add(lblPeso);
        lblPeso.setBounds(280, 110, 280, 70);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/corona.png"))); // NOI18N
        getContentPane().add(jLabel2);
        jLabel2.setBounds(10, 240, 180, 90);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("CORONA"));
        jPanel1.setToolTipText("");

        btn00008.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn00008.setText("Queso Ranchero");

        btn775019128.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn775019128.setText("Queso Asadero");
        btn775019128.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn775019128ActionPerformed(evt);
            }
        });

        btnQCREMACORO.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnQCREMACORO.setText("Queso Crema");
        btnQCREMACORO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQCREMACOROActionPerformed(evt);
            }
        });

        btn775019127.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn775019127.setText("Crema Com");
        btn775019127.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn775019127ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn00008, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
                    .addComponent(btn775019128, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnQCREMACORO, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn775019127, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(btn00008)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn775019128)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnQCREMACORO)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn775019127)
                .addGap(0, 53, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1);
        jPanel1.setBounds(10, 330, 180, 220);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("MARAVATIO"));

        btn775019118.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn775019118.setText("Doble Crema");

        btn775019024.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn775019024.setText("Queso Ranchero");

        btn75089652715.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn75089652715.setText("Tocineta");

        btn77502019.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn77502019.setText("Chorizo");

        btn775019022.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn775019022.setText("Crema Com");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn775019118, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn775019024, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
                    .addComponent(btn75089652715, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn77502019, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn775019022, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(btn775019118)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn775019024)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn75089652715)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn77502019)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn775019022)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2);
        jPanel2.setBounds(210, 330, 180, 220);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("ANY"));

        btn775019120.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn775019120.setText("Salchicha Pavo");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn775019120, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn775019120)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel3);
        jPanel3.setBounds(660, 650, 180, 70);

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/any.jpg"))); // NOI18N
        getContentPane().add(jLabel6);
        jLabel6.setBounds(660, 550, 180, 100);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("EL INDIO"));

        btn775019070.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn775019070.setText("Queso Ranchero");

        btn7506326001.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn7506326001.setText("Queso Panela");

        btn75002.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn75002.setText("Queso Asadero");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn775019070, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
                    .addComponent(btn7506326001, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn75002, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(btn775019070)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn7506326001)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn75002)
                .addGap(0, 22, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel4);
        jPanel4.setBounds(850, 810, 180, 150);

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/indio.png"))); // NOI18N
        getContentPane().add(jLabel7);
        jLabel7.setBounds(890, 700, 90, 110);

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/fud.png"))); // NOI18N
        getContentPane().add(jLabel8);
        jLabel8.setBounds(430, 240, 180, 90);

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/maravatio.png"))); // NOI18N
        jLabel4.setText("jLabel4");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(210, 240, 180, 90);

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/capistrano.jpg"))); // NOI18N
        getContentPane().add(jLabel9);
        jLabel9.setBounds(10, 560, 180, 90);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("CAPISTRANO"));

        btn775019121.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn775019121.setText("Salchicha Pavo");

        btn775019073.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn775019073.setText("Chorizo");

        btn775019072.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn775019072.setText("Queso Puerco");

        btn750190711.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn750190711.setText("Jamon R. Fam");

        btn775019065.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn775019065.setText("Jam. California");

        btn775019064.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn775019064.setText("Jam. Americano");

        btn750190710.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn750190710.setText("Jam. Pierna Fam");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn775019121, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn775019073, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn775019072, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn750190711, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn775019065, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn775019064, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn750190710, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(btn775019121)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn775019073)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn775019072)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn750190711)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn775019065)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn775019064)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn750190710)
                .addGap(0, 22, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel7);
        jPanel7.setBounds(10, 650, 180, 270);

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/nutri.png"))); // NOI18N
        getContentPane().add(jLabel10);
        jLabel10.setBounds(660, 240, 180, 90);

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/delicia.png"))); // NOI18N
        getContentPane().add(jLabel11);
        jLabel11.setBounds(660, 760, 190, 100);

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder("SAN ANTONIO"));

        btn75013416.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn75013416.setText("Americano Tab");
        btn75013416.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn75013416ActionPerformed(evt);
            }
        });

        btn7502563008.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn7502563008.setText("Jam. Pavo");

        btn775019019.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn775019019.setText("Americano Tort");

        btn775019020.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn775019020.setText("Jam. Espaldilla");

        btn75014534801.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn75014534801.setText("Jam. Pierna Horn");

        btn7504690501.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn7504690501.setText("Jam. Cocido Pavo");

        btn7502048608.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn7502048608.setText("Queso Manchego");

        btn7504690502.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn7504690502.setText("Sal. Frankf Pavo");

        btn7504690504.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn7504690504.setText("Sal. Con Pavo");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btn75013416, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btn7502563008, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btn775019019, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btn775019020, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btn75014534801, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btn7504690501, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
            .addComponent(btn7502048608, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btn7504690502, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btn7504690504, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(btn75013416)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn7502563008)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn775019019)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn775019020)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn75014534801)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn7504690501)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn7502048608)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn7504690502)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn7504690504)
                .addGap(0, 26, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel10);
        jPanel10.setBounds(850, 330, 180, 340);

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder("IDEAL"));
        jPanel11.setAutoscrolls(true);

        btn775030335.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn775030335.setText("Salchi. Super S.");

        btn775019125.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn775019125.setText("Sal. Pavo Hot-Dog");

        btn775019123.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn775019123.setText("Salchi. Viena");

        btn7750191131.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn7750191131.setText("Sal. Pavo Frankf");

        btn775019126.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn775019126.setText("Queso De Puerco");

        btn7503050.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn7503050.setText("Jam. Pavo Lunch");

        btn775019075.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn775019075.setText("Jam. Virginia Pavo");
        btn775019075.setToolTipText("");

        btn75889601.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn75889601.setText("Jam. Super Lunch");

        btn7501801.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn7501801.setText("Jam. York");

        btn75010.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn75010.setText("Jam. Americano Rect");

        btn75003.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn75003.setText("Jam. Pavo Sandw Zepp");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btn775030335, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btn775019125, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btn775019123, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btn7750191131, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btn775019126, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btn7503050, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btn775019075, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btn75889601, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btn7501801, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btn75010, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btn75003, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(btn775030335)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn775019125)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn775019123)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn7750191131)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn775019126)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn7503050)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn775019075)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn75889601)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn7501801)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn75010)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn75003)
                .addGap(0, 30, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel11);
        jPanel11.setBounds(1050, 320, 200, 410);

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("NUTRI DELI"));

        btn752384007.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn752384007.setText("Jam. Cocido");

        btn752384001.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn752384001.setText("Jam. Horneado");

        btn752384005.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn752384005.setText("Sal. Hot Dog ");

        btn752384004.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn752384004.setText("Sal. Viena");

        btn752384003.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn752384003.setText("Sal. Pavo");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn752384007, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn752384001, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
                    .addComponent(btn752384005, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn752384004, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn752384003, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(btn752384007)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn752384001)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn752384005)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn752384004)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn752384003)
                .addGap(0, 38, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel8);
        jPanel8.setBounds(660, 330, 180, 220);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("FUD"));

        btn775019116.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn775019116.setText("Sal. Pavo");

        btn775019114.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn775019114.setText("Sal. Pavo Hot-Dog");

        btn775019107.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn775019107.setText("Sal. Viena Hot Dog");

        btn775019110.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn775019110.setText("Jam. Virgina Pavo");

        btn775019106.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn775019106.setText("Virginia Pavo/Cerdo");

        btn775019102.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn775019102.setText("Jam. Americano Pavo");

        btn775019100.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn775019100.setText("Jam. Pierna");

        btn775019066.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn775019066.setText("Jam. Pechuga Pavo");

        btn775019027.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn775019027.setText("Jam. York");

        btn775019026.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn775019026.setText("Jam. AMERICANO");

        btn75010400202.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn75010400202.setText("Jam. Cuida-T Virg. Pavo");

        btn75010400201.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn75010400201.setText("Pech Pavo Cuida-T Virg");

        btn75010400204.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn75010400204.setText("Pechuga Pollo Rostiza");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn775019107, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn775019110, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn775019114, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn775019116, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn775019106, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn775019102, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn775019100, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn775019066, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn775019027, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn775019026, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn75010400202, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn75010400201, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn75010400204, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn775019116)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn775019114)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn775019107)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn775019110)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn775019106)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn775019102)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn775019100)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn775019066)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn775019027)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn775019026)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn75010400202)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn75010400201)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn75010400204)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(jPanel5);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(400, 330, 250, 220);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Delicia"));

        btn775019013.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn775019013.setText("Jam. Delicia");

        btn775019011.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn775019011.setText("El Mexicano Cuad");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btn775019013, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btn775019011, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(btn775019013)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn775019011)
                .addGap(0, 17, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel6);
        jPanel6.setBounds(660, 860, 180, 100);

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("CHIMEX"));

        btn775019117.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn775019117.setText("Jam. Espaldilla");

        btn775019105.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn775019105.setText("Jam. Tradic Pavo");

        btn775019111.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn775019111.setText(" Tocino Ahuma");

        btn775019113.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn775019113.setText("Sal. Frankf Pavo");

        btn775019103.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn775019103.setText("Sal. Viena");

        btn00002246.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn00002246.setText("Sal. Asadisima");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btn775019117, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btn775019105, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
            .addComponent(btn775019111, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btn775019113, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btn775019103, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btn00002246, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(btn775019117)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn775019105)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn775019111)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn775019113)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn775019103)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn00002246)
                .addGap(0, 15, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel9);
        jPanel9.setBounds(210, 650, 180, 230);

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/antonio.png"))); // NOI18N
        getContentPane().add(jLabel13);
        jLabel13.setBounds(850, 240, 180, 90);

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/apachi.png"))); // NOI18N
        jLabel5.setText("jLabel5");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(990, 540, 300, 490);

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/chimex.png"))); // NOI18N
        jLabel12.setText("jLabel12");
        getContentPane().add(jLabel12);
        jLabel12.setBounds(210, 570, 180, 80);

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ideal.jpg"))); // NOI18N
        getContentPane().add(jLabel14);
        jLabel14.setBounds(1060, 220, 200, 110);

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder("EL MEXICANO"));

        btn775030336.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn775030336.setText("Jam. Lonchi Pavo Cuad");

        btn750695101.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn750695101.setText("Jam. Lonchi Pavo");

        btn775019051.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn775019051.setText("Jam. Natural Cuad");

        btn775019050.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn775019050.setText("Jam. Pierna Econom");
        btn775019050.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn775019050ActionPerformed(evt);
            }
        });

        btn775019015.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn775019015.setText("Jam. York");

        btn775019014.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn775019014.setText("Jam. Virginia Pavo");

        btn775019012.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn775019012.setText("Jam. Pierna Natural");

        btn775019010.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn775019010.setText("Jam. Campirano Tort");

        btn775019009.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn775019009.setText("Jam. Americano Lad");

        btn775019181.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn775019181.setText("Tocineta Ahumada");

        btn775019018.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn775019018.setText("Salami Cocido");

        btn775019017.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn775019017.setText("Queso De Puerco");

        btn775019008.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn775019008.setText("Chuleta ");

        btn775019007.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn775019007.setText("Chorizo");

        btn00026.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn00026.setText("Salchicha Pavo");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btn775030336, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
            .addComponent(btn750695101, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btn775019051, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btn775019050, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btn775019015, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btn775019014, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btn775019012, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btn775019010, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btn775019009, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btn775019181, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btn775019018, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btn775019017, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btn775019008, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btn775019007, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btn00026, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(btn775030336)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn750695101)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn775019051)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn775019050)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn775019015)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn775019014)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn775019012)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn775019010)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn775019009)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn775019181)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn775019018)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn775019017)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn775019008)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn775019007)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn00026)
                .addGap(0, 48, Short.MAX_VALUE))
        );

        jScrollPane2.setViewportView(jPanel12);

        getContentPane().add(jScrollPane2);
        jScrollPane2.setBounds(410, 650, 240, 310);

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/mexicano.jpg"))); // NOI18N
        getContentPane().add(jLabel15);
        jLabel15.setBounds(440, 570, 180, 87);

        btnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/refresh.jpg"))); // NOI18N
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });
        getContentPane().add(btnRefresh);
        btnRefresh.setBounds(1210, 0, 66, 60);

        btnConfig.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/configuracion.png"))); // NOI18N
        getContentPane().add(btnConfig);
        btnConfig.setBounds(1210, 70, 66, 60);

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/fondo.png"))); // NOI18N
        jLabel3.setMaximumSize(new java.awt.Dimension(1000, 899));
        jLabel3.setMinimumSize(new java.awt.Dimension(1000, 899));
        jLabel3.setPreferredSize(new java.awt.Dimension(1000, 899));
        getContentPane().add(jLabel3);
        jLabel3.setBounds(0, 0, 1320, 1070);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void codigoTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_codigoTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_codigoTextFieldActionPerformed

    private void codigoTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codigoTextFieldKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_codigoTextFieldKeyPressed

    private void btn775019128ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn775019128ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn775019128ActionPerformed

    private void btnQCREMACOROActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQCREMACOROActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnQCREMACOROActionPerformed

    private void btn775019127ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn775019127ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn775019127ActionPerformed

    private void btn75013416ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn75013416ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn75013416ActionPerformed

    private void btn775019050ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn775019050ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn775019050ActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        ConexionBaseDeDatos.cargarProductos("8");
    }//GEN-LAST:event_btnRefreshActionPerformed

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
            java.util.logging.Logger.getLogger(Etis.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Etis.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Etis.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Etis.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Etis().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn00002246;
    private javax.swing.JButton btn00008;
    private javax.swing.JButton btn00026;
    private javax.swing.JButton btn75002;
    private javax.swing.JButton btn75003;
    private javax.swing.JButton btn75010;
    private javax.swing.JButton btn75010400201;
    private javax.swing.JButton btn75010400202;
    private javax.swing.JButton btn75010400204;
    private javax.swing.JButton btn75013416;
    private javax.swing.JButton btn75014534801;
    private javax.swing.JButton btn7501801;
    private javax.swing.JButton btn750190710;
    private javax.swing.JButton btn750190711;
    private javax.swing.JButton btn7502048608;
    private javax.swing.JButton btn7502563008;
    private javax.swing.JButton btn7503050;
    private javax.swing.JButton btn7504690501;
    private javax.swing.JButton btn7504690502;
    private javax.swing.JButton btn7504690504;
    private javax.swing.JButton btn7506326001;
    private javax.swing.JButton btn750695101;
    private javax.swing.JButton btn75089652715;
    private javax.swing.JButton btn752384001;
    private javax.swing.JButton btn752384003;
    private javax.swing.JButton btn752384004;
    private javax.swing.JButton btn752384005;
    private javax.swing.JButton btn752384007;
    private javax.swing.JButton btn75889601;
    private javax.swing.JButton btn775019007;
    private javax.swing.JButton btn775019008;
    private javax.swing.JButton btn775019009;
    private javax.swing.JButton btn775019010;
    private javax.swing.JButton btn775019011;
    private javax.swing.JButton btn775019012;
    private javax.swing.JButton btn775019013;
    private javax.swing.JButton btn775019014;
    private javax.swing.JButton btn775019015;
    private javax.swing.JButton btn775019017;
    private javax.swing.JButton btn775019018;
    private javax.swing.JButton btn775019019;
    private javax.swing.JButton btn775019020;
    private javax.swing.JButton btn775019022;
    private javax.swing.JButton btn775019024;
    private javax.swing.JButton btn775019026;
    private javax.swing.JButton btn775019027;
    private javax.swing.JButton btn775019050;
    private javax.swing.JButton btn775019051;
    private javax.swing.JButton btn775019064;
    private javax.swing.JButton btn775019065;
    private javax.swing.JButton btn775019066;
    private javax.swing.JButton btn775019070;
    private javax.swing.JButton btn775019072;
    private javax.swing.JButton btn775019073;
    private javax.swing.JButton btn775019075;
    private javax.swing.JButton btn775019100;
    private javax.swing.JButton btn775019102;
    private javax.swing.JButton btn775019103;
    private javax.swing.JButton btn775019105;
    private javax.swing.JButton btn775019106;
    private javax.swing.JButton btn775019107;
    private javax.swing.JButton btn775019110;
    private javax.swing.JButton btn775019111;
    private javax.swing.JButton btn775019113;
    private javax.swing.JButton btn7750191131;
    private javax.swing.JButton btn775019114;
    private javax.swing.JButton btn775019116;
    private javax.swing.JButton btn775019117;
    private javax.swing.JButton btn775019118;
    private javax.swing.JButton btn775019120;
    private javax.swing.JButton btn775019121;
    private javax.swing.JButton btn775019123;
    private javax.swing.JButton btn775019125;
    private javax.swing.JButton btn775019126;
    private javax.swing.JButton btn775019127;
    private javax.swing.JButton btn775019128;
    private javax.swing.JButton btn775019181;
    private javax.swing.JButton btn77502019;
    private javax.swing.JButton btn775030335;
    private javax.swing.JButton btn775030336;
    private javax.swing.JButton btnConfig;
    private javax.swing.JButton btnQCREMACORO;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JTextField codigoTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblPeso;
    private javax.swing.JLabel resultadoLabel;
    // End of variables declaration//GEN-END:variables
}
