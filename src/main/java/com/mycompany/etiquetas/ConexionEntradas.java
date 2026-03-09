/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.etiquetas;

/**
 *
 * @author PROGRAMEITOR
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConexionEntradas {

    private static final String URL = "jdbc:mysql://72.167.221.212:3306/precios_azteca";
    private static final String USUARIO = "precios_azteca";
    private static final String CONTRASENA = "$AbbeyRoad01Precios";

    private static Map<String, Producto> productosCache = new HashMap<>();  // Mapa para almacenar los productos en memoria
    
    private static Map<Integer, List<Produs>> produsCache = new HashMap<>();

    // Conectar a la base de datos
    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
    }

    // Cargar todos los productos en memoria
    public static void cargarProductos() {
        productosCache = new HashMap<>();
        String query = "SELECT p.codigo, p.nombre, p.id_producto, px.preciouno FROM productos p LEFT JOIN precios px ON p.id_producto = px.id_producto AND px.estatus = 1 where p.estatus = 1"+
                " AND p.id_producto IN (SELECT id_caja FROM paquetes where estatus = 1 GROUP BY id_caja);";
        try (Connection conn = conectar(); Statement stmt = conn.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    String codigo = rs.getString("codigo");
                    String nombre = rs.getString("nombre");
                    int idProducto = rs.getInt("id_producto");
                    String precioUno = rs.getString("preciouno");
                    Producto producto = new Producto(codigo, nombre, idProducto, precioUno);
                    productosCache.put(codigo, producto);  // Almacenar el producto en el mapa
                }
                System.out.println("Productos cargados en memoria.");
            }
        } catch (SQLException e) {
            System.out.println("Error al cargar los productos: " + e.getMessage());
        }
    }
    
    public static List<Producto> buscarPorClave(String palabraClave) {
        List<Producto> resultados = new ArrayList<>();

        for (Producto producto : productosCache.values()) {
            if (producto.getNombre().toUpperCase().contains(palabraClave.toUpperCase())) {
                resultados.add(producto);
            }
        }

        return resultados;
    }
    
    public static List<Producto> buscarPorCodigo(String palabraClave) {
        List<Producto> resultados = new ArrayList<>();

        for (Producto producto : productosCache.values()) {
            if (producto.getCodigo().toUpperCase().contains(palabraClave.toUpperCase())) {
                resultados.add(producto);
            }
        }

        return resultados;
    }

    // Buscar un producto por código (sin realizar otra consulta a la base de datos)
    public static Producto buscarProductoPorCodigo(String codigo) {
        return productosCache.get(codigo);  // Buscar el producto en el mapa
    }
    
}
