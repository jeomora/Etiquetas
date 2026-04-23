/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.etiquetas;

import static com.mycompany.etiquetas.ConexionEpsonCarnes.conectar;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author PROGRAMEITOR
 */
public class Touch extends javax.swing.JFrame {
    String prodDesc = "";
    String cantBasc = "";
    String codis = "";
    Basculon basculon = new Basculon();
    Historial historial = new Historial();
    TestRXTX impres = new TestRXTX();
    Map<String, String> config = leerConfiguracion();

    String sucuch = config.getOrDefault("SUCURSAL","7");
    String zebra = config.getOrDefault("ZEBRA","");
    String comPort = config.getOrDefault("PUERTO BASCULA","COM6");
    String epson = config.getOrDefault("EPSON","");
    String ipTurnos = config.getOrDefault("IP TURNOS","");
    
    //String  sucuch = "7";
    private static int x = 0;
    private long lastPrintTime = 0;
    DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Código", "Descripción", "Precio"}, 0){
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;  // Hace que todas las celdas no sean editables
        }
    };

    /**
     * Creates new form Touch
     */
    public Touch() {
        initComponents();
        //sucuch = leerPuertoSucursal();
        System.out.println(sucuch);
        System.out.println(zebra);
        System.out.println(comPort);
        impres.procesarPendientesNoEncontrados();
        setTitle("IMPRESIÓN DE ETIQUETAS");
        setSize(1920, 1080);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTableHeader header = tblProdos.getTableHeader();
        //impres.printTry();
        impres.printTry();
        //epson.testPrinterEpson();
        sProvedores.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int notches = e.getWheelRotation();  // La dirección del desplazamiento (positivo si va hacia abajo, negativo si va hacia arriba)
                int scrollSpeed = 50;  // Factor de velocidad para el desplazamiento (ajustable según lo que necesites)
                JScrollBar vertical = sProvedores.getVerticalScrollBar();
                if (notches < 0) {
                    vertical.setValue(vertical.getValue() - scrollSpeed);  // Si notches es negativo, sube la barra
                } else {
                    vertical.setValue(vertical.getValue() + scrollSpeed);  // Si notches es positivo, baja la barra
                }
            }
        });
        tblProdos.setRowHeight(60);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(40, Integer.MAX_VALUE));
        sProvedores.getVerticalScrollBar().setPreferredSize(new Dimension(30, Integer.MAX_VALUE));
        Font font = new Font("Arial", Font.BOLD, 28);  // Fuente: Arial, Negrita, tamaño 18
        header.setFont(font);
        header.setBackground(Color.BLACK); // Cambiar el color de fondo
        header.setForeground(Color.WHITE); // Cambiar el color del texto
        configurarTabla(tblProdos);
        ConexionBaseDeDatos loader = new ConexionBaseDeDatos();
        Proveedor[] proveedores = loader.cargarProveedores();
        loader.cargarProductosProv(sucuch);
        
        pProveedores.setLayout(new BoxLayout(pProveedores, BoxLayout.Y_AXIS));
        for (Proveedor p : proveedores) {
            JLabel label = new JLabel("lbl" + (p.getIdProveedor()));
            Font fonta = new Font("Arial", Font.BOLD, 32);
            label.setFont(fonta);
            ImageIcon icon = loadImage("img" + p.getIdProveedor() + ".jpg");

            if (icon != null) {
                label.setIcon(icon);
                label.setText("");
            } else {
                // Intentar cargar una imagen alternativa
                icon = loadImage("img" + p.getIdProveedor() + ".png");
                if (icon != null) {
                    label.setIcon(icon);
                    label.setText("");
                } else {
                    label.setText(p.getNick());  // Si no hay imagen, mostrar el nombre de usuario
                }
            }

            label.setAlignmentX(Component.LEFT_ALIGNMENT);
            label.setMaximumSize(new Dimension(Integer.MAX_VALUE, label.getPreferredSize().height));
            Border border = BorderFactory.createLineBorder(Color.BLACK, 1); 
            label.setBorder(border); 
            Border padding = new EmptyBorder(30, 0, 30, 0);
            label.setBorder(BorderFactory.createCompoundBorder(border, padding));
            label.setPreferredSize(new Dimension(200, 180));
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    cargaProductos(p.getIdProveedor());
                }
            });
            pProveedores.add(label);
        }
        loader.cargarProductos(sucuch);
       
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
        
        txtBusca.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Runtime.getRuntime().exec("cmd /c start osk");
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
        
        
        //String comPort = leerPuertoCOMDesdeArchivo();
        
        if (comPort != null) {
            basculon.initialize(comPort);
        } else {
            basculon.initialize("COM6");
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
        
        codigoTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                buscaProducto(evt); 
            }
        });
        setActionsBtns();
        loader.cargarOfertas(sucuch);
        lblOfertas.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        lblOfertas.setOpaque(true); // 🔴 Esto es obligatorio para que se vea el fondo
        lblOfertas.setBackground(Color.YELLOW); // o cualquier otro color
        String texto = "  💥  OFERTAS  💥  "+loader.cargarOfertas(sucuch);
        final StringBuilder buffer = new StringBuilder(texto);

        Timer timer = new Timer(100, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Simula desplazamiento al mover el primer carácter al final
                char firstChar = buffer.charAt(0);
                buffer.deleteCharAt(0);
                buffer.append(firstChar);
                lblOfertas.setText(buffer.toString());
            }
        });

        timer.start();
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
    
    private void buscaProducto(ActionEvent evt){
        String codigo = codigoTextField.getText();
        Producto datos = ConexionBaseDeDatos.buscarProductoPorCodigo(codigo);
        if(datos != null){
            historial.agregarProducto(new Producto(datos.getCodigo(), datos.getNombre(),1,""));
            asignarDescripcionBotones(historial, btnLast1, btnLast2, btnLast3, btnLast4, btnLast5);
        }
        buscaProdPrint(codigo);
    }
    
    private void buscaProdPrint(String codigo){
        Producto datos = ConexionBaseDeDatos.buscarProductoPorCodigo(codigo);
        System.out.println(datos);
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
                        "El peso es menor a 0.100. ¿Deseas continuar?", 
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
    
    /*private String leerPuertoCOMDesdeArchivo() {
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
    }*/
    
    private static ImageIcon loadImage(String imageName) {
        // Construir la ruta a la imagen de forma dinámica, por ejemplo: "images/img284.jpg"
        String imagePath = "/images/" + imageName;

        // Usar getClass().getResource() para obtener la imagen del JAR
        java.net.URL imageURL = Touch.class.getResource(imagePath);

        // Verifica si la imagen existe en el JAR
        if (imageURL != null) {
            return new ImageIcon(imageURL);  // Si existe, devuelve la imagen
        } else {
            System.out.println("Imagen no encontrada: " + imagePath);  // Si no se encuentra, imprime un mensaje de error
            return null;
        }
    }
    
    private void cargaProductos(int ides){
        List<Produs> productos = ConexionBaseDeDatos.obtenerProductosPorProveedor(ides);
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
    
    private static void configurarTabla(JTable tblProdos) {
        Font customFont = new Font("Arial", Font.PLAIN, 34);
        tblProdos.setFont(customFont);

        int rowHeight = tblProdos.getFontMetrics(customFont).getHeight();
        tblProdos.setRowHeight(rowHeight);

        TableColumnModel columnModel = tblProdos.getColumnModel();

        columnModel.getColumn(0).setPreferredWidth(0);
        columnModel.getColumn(1).setPreferredWidth(850);
        columnModel.getColumn(2).setPreferredWidth(160);
        tblProdos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); 
        tblProdos.revalidate();
        tblProdos.repaint();
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
    
    private void cargaProductosFiltro(String clave){
        List<Producto> productos = ConexionBaseDeDatos.buscarPorClave(clave);
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
    
    private static Map<String, String> leerConfiguracion() {
        Map<String, String> config = new HashMap<>();

        try {
            File archivo = new File("txtetiquetas.txt");

            if (archivo.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(archivo));
                String linea;

                while ((linea = reader.readLine()) != null) {
                    linea = linea.trim();

                    // Ignorar líneas vacías o comentarios
                    if (linea.isEmpty() || linea.startsWith("#")) {
                        continue;
                    }

                    String[] partes = linea.split(":", 2);

                    if (partes.length == 2) {
                        String clave = partes[0].trim();
                        String valor = partes[1].trim();
                        config.put(clave, valor);
                    } else {
                        System.out.println("Línea inválida: " + linea);
                    }
                }

                reader.close();
            } else {
                System.out.println("El archivo sucursal.txt no existe.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return config;
    }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollPane = new javax.swing.JScrollPane();
        tblProdos = new javax.swing.JTable();
        sProvedores = new javax.swing.JScrollPane();
        pProveedores = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        codigoTextField = new javax.swing.JTextField();
        lblPeso = new javax.swing.JLabel();
        resultadoLabel = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        btnLast1 = new javax.swing.JButton();
        btnLast2 = new javax.swing.JButton();
        btnLast3 = new javax.swing.JButton();
        btnLast4 = new javax.swing.JButton();
        btnLast5 = new javax.swing.JButton();
        btnJamon = new javax.swing.JButton();
        btnCrema = new javax.swing.JButton();
        btnQueso = new javax.swing.JButton();
        btnSalchi = new javax.swing.JButton();
        btnChori = new javax.swing.JButton();
        btnTocino = new javax.swing.JButton();
        lblOfertas = new javax.swing.JLabel();
        btnCarne = new javax.swing.JButton();
        btnChule = new javax.swing.JButton();
        btnArra = new javax.swing.JButton();
        btnLonga = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtBusca = new javax.swing.JTextField();
        btnTurno = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(1980, 1080));
        setMinimumSize(new java.awt.Dimension(1980, 1080));
        getContentPane().setLayout(null);

        scrollPane.setBackground(new java.awt.Color(255, 204, 204));
        scrollPane.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        scrollPane.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        tblProdos.setBorder(new javax.swing.border.MatteBorder(null));
        tblProdos.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        tblProdos.setModel(tableModel);
        scrollPane.setViewportView(tblProdos);

        getContentPane().add(scrollPane);
        scrollPane.setBounds(240, 240, 1040, 410);

        sProvedores.setViewportView(pProveedores);

        getContentPane().add(sProvedores);
        sProvedores.setBounds(10, 240, 220, 410);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/apachi.png"))); // NOI18N
        getContentPane().add(jLabel2);
        jLabel2.setBounds(1120, 240, 290, 490);

        codigoTextField.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        codigoTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                codigoTextFieldActionPerformed(evt);
            }
        });
        getContentPane().add(codigoTextField);
        codigoTextField.setBounds(200, 20, 190, 50);

        lblPeso.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        lblPeso.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(lblPeso);
        lblPeso.setBounds(180, 70, 260, 60);

        resultadoLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        resultadoLabel.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(resultadoLabel);
        resultadoLabel.setBounds(470, 10, 420, 110);

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/logaz2.png"))); // NOI18N
        getContentPane().add(jLabel5);
        jLabel5.setBounds(0, 0, 150, 150);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Últimos 5");
        getContentPane().add(jLabel6);
        jLabel6.setBounds(920, 0, 120, 30);

        btnLast1.setBackground(new java.awt.Color(204, 255, 204));
        btnLast1.setBorder(BorderFactory.createLineBorder(Color.GREEN));
        getContentPane().add(btnLast1);
        btnLast1.setBounds(910, 200, 210, 30);

        btnLast2.setBackground(new java.awt.Color(204, 255, 204));
        btnLast2.setBorder(BorderFactory.createLineBorder(Color.GREEN));
        getContentPane().add(btnLast2);
        btnLast2.setBounds(910, 160, 210, 30);

        btnLast3.setBackground(new java.awt.Color(204, 255, 204));
        btnLast3.setBorder(BorderFactory.createLineBorder(Color.GREEN));
        getContentPane().add(btnLast3);
        btnLast3.setBounds(910, 120, 210, 30);

        btnLast4.setBackground(new java.awt.Color(204, 255, 204));
        btnLast4.setBorder(BorderFactory.createLineBorder(Color.GREEN));
        getContentPane().add(btnLast4);
        btnLast4.setBounds(910, 80, 210, 30);

        btnLast5.setBackground(new java.awt.Color(204, 255, 204));
        btnLast5.setBorder(BorderFactory.createLineBorder(Color.GREEN)
        );
        getContentPane().add(btnLast5);
        btnLast5.setBounds(910, 40, 210, 30);

        btnJamon.setBackground(new java.awt.Color(153, 255, 204));
        btnJamon.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnJamon.setText("JAMON");
        btnJamon.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnJamon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnJamonActionPerformed(evt);
            }
        });
        getContentPane().add(btnJamon);
        btnJamon.setBounds(250, 200, 120, 30);

        btnCrema.setBackground(new java.awt.Color(153, 255, 204));
        btnCrema.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnCrema.setText("CREMA");
        btnCrema.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnCrema.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCremaActionPerformed(evt);
            }
        });
        getContentPane().add(btnCrema);
        btnCrema.setBounds(380, 200, 110, 30);

        btnQueso.setBackground(new java.awt.Color(153, 255, 204));
        btnQueso.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnQueso.setText("QUESO");
        btnQueso.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnQueso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuesoActionPerformed(evt);
            }
        });
        getContentPane().add(btnQueso);
        btnQueso.setBounds(500, 200, 110, 30);

        btnSalchi.setBackground(new java.awt.Color(153, 255, 204));
        btnSalchi.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnSalchi.setText("SALCHICHA");
        btnSalchi.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnSalchi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalchiActionPerformed(evt);
            }
        });
        getContentPane().add(btnSalchi);
        btnSalchi.setBounds(620, 200, 120, 30);

        btnChori.setBackground(new java.awt.Color(153, 204, 255));
        btnChori.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnChori.setText("CHORIZO");
        btnChori.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnChori.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChoriActionPerformed(evt);
            }
        });
        getContentPane().add(btnChori);
        btnChori.setBounds(250, 160, 120, 30);

        btnTocino.setBackground(new java.awt.Color(153, 204, 255));
        btnTocino.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnTocino.setText("TOCINO");
        btnTocino.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnTocino.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTocinoActionPerformed(evt);
            }
        });
        getContentPane().add(btnTocino);
        btnTocino.setBounds(380, 160, 110, 30);

        lblOfertas.setBackground(new java.awt.Color(0, 0, 0));
        lblOfertas.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        lblOfertas.setText("OFERTAS : ");
        getContentPane().add(lblOfertas);
        lblOfertas.setBounds(10, 650, 1270, 50);

        btnCarne.setBackground(new java.awt.Color(153, 204, 255));
        btnCarne.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnCarne.setText("CARNE");
        btnCarne.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnCarne.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCarneActionPerformed(evt);
            }
        });
        getContentPane().add(btnCarne);
        btnCarne.setBounds(500, 160, 110, 30);

        btnChule.setBackground(new java.awt.Color(153, 204, 255));
        btnChule.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnChule.setText("CHULETA");
        btnChule.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnChule.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChuleActionPerformed(evt);
            }
        });
        getContentPane().add(btnChule);
        btnChule.setBounds(750, 160, 120, 30);

        btnArra.setBackground(new java.awt.Color(153, 204, 255));
        btnArra.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnArra.setText("ARRACHERA");
        btnArra.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnArra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnArraActionPerformed(evt);
            }
        });
        getContentPane().add(btnArra);
        btnArra.setBounds(620, 160, 120, 30);

        btnLonga.setBackground(new java.awt.Color(153, 255, 204));
        btnLonga.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnLonga.setText("LONGANIZA");
        btnLonga.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnLonga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLongaActionPerformed(evt);
            }
        });
        getContentPane().add(btnLonga);
        btnLonga.setBounds(750, 200, 120, 30);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("BUSCAR PRODUCTO");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(10, 180, 220, 30);

        txtBusca.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        getContentPane().add(txtBusca);
        txtBusca.setBounds(10, 210, 220, 31);

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
        btnTurno.setBounds(1140, 40, 140, 100);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/backs.png"))); // NOI18N
        jLabel1.setMaximumSize(new java.awt.Dimension(1920, 1080));
        jLabel1.setMinimumSize(new java.awt.Dimension(1920, 1080));
        jLabel1.setPreferredSize(new java.awt.Dimension(1920, 1080));
        getContentPane().add(jLabel1);
        jLabel1.setBounds(0, 0, 2020, 1080);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnJamonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnJamonActionPerformed
        // TODO add your handling code here:
        cargaProductosFiltro("JAMON");
    }//GEN-LAST:event_btnJamonActionPerformed

    private void btnCremaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCremaActionPerformed
        // TODO add your handling code here:
        cargaProductosFiltro("CREMA COM");
    }//GEN-LAST:event_btnCremaActionPerformed

    private void btnQuesoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuesoActionPerformed
        // TODO add your handling code here:
        cargaProductosFiltro("QUESO");
    }//GEN-LAST:event_btnQuesoActionPerformed

    private void btnSalchiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalchiActionPerformed
        // TODO add your handling code here:
        cargaProductosFiltro("SALCHICHA");
    }//GEN-LAST:event_btnSalchiActionPerformed

    private void btnChoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChoriActionPerformed
        // TODO add your handling code here:
        cargaProductosFiltro("CHORIZO");
    }//GEN-LAST:event_btnChoriActionPerformed

    private void btnTocinoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTocinoActionPerformed
        // TODO add your handling code here:
        cargaProductosFiltro("TOCIN");
    }//GEN-LAST:event_btnTocinoActionPerformed

    private void btnCarneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCarneActionPerformed
        // TODO add your handling code here:
        cargaProductosFiltro("CARNE");
    }//GEN-LAST:event_btnCarneActionPerformed

    private void btnArraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnArraActionPerformed
        // TODO add your handling code here:
        cargaProductosFiltro("ARRACHERA");
    }//GEN-LAST:event_btnArraActionPerformed

    private void btnChuleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChuleActionPerformed
        // TODO add your handling code here:
        cargaProductosFiltro("CHULETA");
    }//GEN-LAST:event_btnChuleActionPerformed

    private void btnLongaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLongaActionPerformed
        // TODO add your handling code here:
        cargaProductosFiltro("LONGANI");
    }//GEN-LAST:event_btnLongaActionPerformed

    private void codigoTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_codigoTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_codigoTextFieldActionPerformed

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

                String query = "SELECT id_turno,fecha_registro,numero,estatus,fecha_inicio FROM turnocarnes " +
                               "WHERE DATE(fecha_registro) = CURDATE() AND estatus = 1 ORDER BY id_turno ASC;";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                System.out.println("realizo consulta");

                if (rs.next()) {
                    int id_turno = rs.getInt("id_turno");
                    int numero = rs.getInt("numero");
                    System.out.println(numero);

                    // Actualizar estatus y agregar campo caja
                    String updateSQL = "UPDATE turnocarnes SET estatus = 2, caja = ? WHERE id_turno = ?";
                    stmt = conn.prepareStatement(updateSQL);
                    stmt.setInt(1, numeroCaja);
                    stmt.setInt(2, id_turno);
                    stmt.executeUpdate();

                    btnTurno.setText("<html><div style='text-align:center;'>NUEVO TURNO <br>" + numero +"</div></html>");
                    // reproducirSonido();

                    // 🔑 Actualizar cooldown
                    lastPrintTime = now;
                } else {
                    btnTurno.setText("<html>NO HAY<br>NÚMEROS<br>SIGUIENTES</html>");
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
            java.util.logging.Logger.getLogger(Touch.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Touch.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Touch.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Touch.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        UIManager.put("ScrollBar.width", 40); // Ancho de la barra de desplazamiento
        UIManager.put("ScrollBar.thumbHeight", 80); // Altura del "pulgar"
        UIManager.put("ScrollBar.thumbWidth", 40);  // Ancho del "pulgar"
        UIManager.put("ScrollBar.trackWidth", 20); // Ancho de la pista
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Touch().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnArra;
    private javax.swing.JButton btnCarne;
    private javax.swing.JButton btnChori;
    private javax.swing.JButton btnChule;
    private javax.swing.JButton btnCrema;
    private javax.swing.JButton btnJamon;
    private javax.swing.JButton btnLast1;
    private javax.swing.JButton btnLast2;
    private javax.swing.JButton btnLast3;
    private javax.swing.JButton btnLast4;
    private javax.swing.JButton btnLast5;
    private javax.swing.JButton btnLonga;
    private javax.swing.JButton btnQueso;
    private javax.swing.JButton btnSalchi;
    private javax.swing.JButton btnTocino;
    private javax.swing.JButton btnTurno;
    private javax.swing.JTextField codigoTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel lblOfertas;
    private javax.swing.JLabel lblPeso;
    private javax.swing.JPanel pProveedores;
    private javax.swing.JLabel resultadoLabel;
    private javax.swing.JScrollPane sProvedores;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JTable tblProdos;
    private javax.swing.JTextField txtBusca;
    // End of variables declaration//GEN-END:variables
}
