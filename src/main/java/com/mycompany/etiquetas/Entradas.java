/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.etiquetas;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 *
 * @author PROGRAMEITOR
 */
public class Entradas extends javax.swing.JFrame {
    String prodDesc = "";
    String cantPrint = "";
    String codis = "";
    Basculon basculon = new Basculon();
    Historial historial = new Historial();
    TestRXTX impres = new TestRXTX();
    DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Código", "Descripción", "Precio"}, 0){
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;  // Hace que todas las celdas no sean editables
        }
    };
    
    
    public Entradas() {
        initComponents();
        setTitle("IMPRESIÓN DE ETIQUETAS");
        setSize(950, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTableHeader header = tblProdos.getTableHeader();
        tblProdos.setRowHeight(60);
        Font font = new Font("Arial", Font.BOLD, 22);  // Fuente: Arial, Negrita, tamaño 18
        header.setFont(font);
        header.setBackground(Color.BLACK); // Cambiar el color de fondo
        header.setForeground(Color.WHITE); // Cambiar el color del texto
        configurarTabla(tblProdos);
        
        ConexionEntradas loader = new ConexionEntradas();
        loader.cargarProductos();
        ((AbstractDocument) txtNumero.getDocument()).setDocumentFilter(new IntFilter());
        txtBusca.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                testPrinterConnectionValida();
            }
        });
        txtNumero.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                testPrinterConnectionValida();
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
    }
    
    private void testPrinterConnectionValida() {
        try {
            String textoIngresado = txtBusca.getText();
            List<Producto> productos = ConexionEntradas.buscarPorCodigo(textoIngresado);
            String cuantas = "1";
            if(productos.size() == 1){
                prodDesc = productos.get(0).getNombre();
                if(txtNumero.getText() == ""){
                    txtNumero.setText("1");
                    cuantas = "1";
                }else{
                    cuantas = txtNumero.getText();
                }
                impres.testPrinterEntradas(textoIngresado,cuantas,prodDesc);
                new java.util.Timer().schedule( 
                   new java.util.TimerTask() { 
                       @Override 
                       public void run() { 

                       } 
                   }, 
                   1000 // 3 segundos
               );
            }else{
                cargaProductosFiltro(textoIngresado);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al conectar con la impresora: " + e.getMessage(),
                    "Error de Conexión", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    
   
   
    private void buscaProdPrint(String codigo){
        Producto datos = ConexionEntradas.buscarProductoPorCodigo(codigo);
        System.out.println(datos);
        if(datos != null){ 
            codis = datos.getCodigo();
            prodDesc = datos.getNombre();
            testPrinterConnection();
        }
    }
    
    private void testPrinterConnection() {
        try {
            impres.testPrinter(codis,cantPrint,prodDesc);
             new java.util.Timer().schedule( 
                new java.util.TimerTask() { 
                    @Override 
                    public void run() { 

                    } 
                }, 
                1000 
            );
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al conectar con la impresora: " + e.getMessage(),
                    "Error de Conexión", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    private void cargaProductosCodigo(String clave){
        List<Producto> productos = ConexionEntradas.buscarPorCodigo(clave);
        Collections.sort(productos, new Comparator<Producto>() {
            @Override
            public int compare(Producto p1, Producto p2) {
                return p1.getNombre().compareToIgnoreCase(p2.getNombre());
            }
        });
        System.out.println(productos.size());
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
    
    private void cargaProductosFiltro(String clave){
        List<Producto> productos = ConexionEntradas.buscarPorClave(clave);
        Collections.sort(productos, new Comparator<Producto>() {
            @Override
            public int compare(Producto p1, Producto p2) {
                return p1.getNombre().compareToIgnoreCase(p2.getNombre());
            }
        });
        System.out.println(productos.size());
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
    
    // Filtro personalizado que solo permite enteros
    static class IntFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {
            if (isNumeric(string)) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {
            if (isNumeric(text)) {
                super.replace(fb, offset, length, text, attrs);
            }
        }

        private boolean isNumeric(String text) {
            return text.matches("-?\\d*"); // Acepta enteros negativos también
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
        txtBusca = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProdos = new javax.swing.JTable();
        txtNumero = new javax.swing.JTextField();
        btnPrint = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/logaz2.png"))); // NOI18N
        getContentPane().add(jLabel2);
        jLabel2.setBounds(0, 0, 150, 170);

        txtBusca.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        txtBusca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscaActionPerformed(evt);
            }
        });
        getContentPane().add(txtBusca);
        txtBusca.setBounds(170, 70, 210, 50);

        tblProdos.setModel(tableModel);
        jScrollPane1.setViewportView(tblProdos);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(20, 210, 900, 280);

        txtNumero.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        getContentPane().add(txtNumero);
        txtNumero.setBounds(400, 70, 110, 50);

        btnPrint.setBackground(new java.awt.Color(153, 204, 255));
        btnPrint.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btnPrint.setText("Imprimir");
        btnPrint.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        getContentPane().add(btnPrint);
        btnPrint.setBounds(540, 70, 150, 50);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Código");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(170, 10, 210, 50);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("# Etiquetas");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(390, 10, 140, 50);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/backno.png"))); // NOI18N
        getContentPane().add(jLabel1);
        jLabel1.setBounds(0, 0, 950, 510);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        testPrinterConnectionValida();
    }//GEN-LAST:event_btnPrintActionPerformed

    private void txtBuscaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscaActionPerformed
        //System.out.println("a");
    }//GEN-LAST:event_txtBuscaActionPerformed

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
            java.util.logging.Logger.getLogger(Entradas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Entradas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Entradas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Entradas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Entradas().setVisible(true);
            }
        });
    }
    
    private static void configurarTabla(JTable tblProdos) {
        Font customFont = new Font("Arial", Font.PLAIN, 24);
        tblProdos.setFont(customFont);

        int rowHeight = tblProdos.getFontMetrics(customFont).getHeight();
        tblProdos.setRowHeight(rowHeight);

        TableColumnModel columnModel = tblProdos.getColumnModel();

        columnModel.getColumn(0).setPreferredWidth(170);
        columnModel.getColumn(1).setPreferredWidth(600);
        columnModel.getColumn(2).setPreferredWidth(120);
        tblProdos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); 
        tblProdos.revalidate();
        tblProdos.repaint();
    }
    
    private void accionFila(int selectedRow){
        if (selectedRow >= 0) {
            codis = tblProdos.getValueAt(selectedRow, 0).toString();
            txtBusca.setText(codis);
            txtNumero.requestFocus();
            testPrinterConnectionValida();
        }
    }  

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnPrint;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblProdos;
    private javax.swing.JTextField txtBusca;
    private javax.swing.JTextField txtNumero;
    // End of variables declaration//GEN-END:variables
}
