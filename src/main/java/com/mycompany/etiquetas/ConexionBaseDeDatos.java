/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.etiquetas;

/**
 *
 * @author PROGRAMEITOR
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConexionBaseDeDatos {

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
    public static void cargarProductos(String sucursal) {
        productosCache = new HashMap<>();
        String query = "SELECT p.codigo, p.nombre, p.id_producto, IFNULL(spx.preciouno,IFNULL(px.preciouno,0)) AS preciouno " +
                "FROM productos p " +
                "LEFT JOIN precios px ON p.id_producto = px.id_producto AND px.estatus = 1 " +
                "LEFT JOIN sucproductos sp ON p.id_producto = sp.id_prox AND sp.estatus = 1 AND sp.id_sucursal = "+sucursal+" " +
                "LEFT JOIN sucprecios spx ON sp.id_producto = spx.id_producto AND spx.estatus = 1 " +
                "WHERE p.id_producto IN (SELECT id_producto FROM carnes WHERE estatus = 1);";
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

    // Buscar un producto por código (sin realizar otra consulta a la base de datos)
    public static Producto buscarProductoPorCodigo(String codigo) {
        return productosCache.get(codigo);  // Buscar el producto en el mapa
    }
    
    public Proveedor[] cargarProveedores() {
        String query = "SELECT COUNT(c.id_proveedor) as cuantos, c.id_proveedor, z.nombre, z.nick " +
                       "FROM carnes c " +
                       "LEFT JOIN zproveedores z ON c.id_proveedor = z.id_proveedor " +
                       "WHERE c.estatus = 1 " +
                       "GROUP BY c.id_proveedor " +
                       "ORDER BY cuantos DESC";

        ArrayList<Proveedor> proveedoresList = new ArrayList<>();

        try (Connection conn = conectar(); Statement stmt = conn.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    int idProveedor = rs.getInt("id_proveedor");
                    String nombre = rs.getString("nombre");
                    String nick = rs.getString("nick");

                    Proveedor proveedor = new Proveedor(idProveedor, nombre, nick);
                    proveedoresList.add(proveedor);
                }
                System.out.println("Proveedores cargados en memoria.");
            }
        } catch (SQLException e) {
            System.out.println("Error al cargar los proveedores: " + e.getMessage());
        }

        // Convertir el ArrayList a un arreglo y retornarlo
        return proveedoresList.toArray(new Proveedor[0]);
    }
    
    public static void cargarProductosProv(String sucursal) {
        produsCache = new HashMap<>();  // Reiniciar el cache
        String query = "SELECT c.codigo, REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(c.descripcion, 'QUESO', 'QSO'), 'JAMON', 'JAM'), 'CREMA COM', 'CRE'), 'SALCHICHA', 'SAL')"+
                ", 'A GRANEL', ''), 'A GRANEL', ''), 'GRANEL', ''), '1 KG', '') AS descripcion, c.id_producto, c.id_proveedor, IFNULL(spx.preciouno,IFNULL(px.preciouno,0)) AS preciouno,p.ums FROM carnes c "+
                "LEFT JOIN precios px ON c.id_producto = px.id_producto "+
                "LEFT JOIN sucproductos sp ON c.id_producto = sp.id_prox AND sp.estatus = 1 AND sp.id_sucursal = "+sucursal+" " +
                "LEFT JOIN sucprecios spx ON sp.id_producto = spx.id_producto AND spx.estatus = 1 "+
                "LEFT JOIN productos p ON c.id_producto = p.id_producto WHERE c.estatus = 1 GROUP BY c.id_producto ORDER BY c.id_proveedor, c.descripcion;";

        try (Connection conn = conectar(); Statement stmt = conn.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    String codigo = rs.getString("codigo");
                    String descripcion = rs.getString("descripcion");
                    int idProducto = rs.getInt("id_producto");
                    String precioUno = rs.getString("preciouno");
                    int idProveedor = rs.getInt("id_proveedor");

                    Produs producto = new Produs(codigo, descripcion, idProducto, precioUno, idProveedor);

                    if (!produsCache.containsKey(idProveedor)) {
                        produsCache.put(idProveedor, new ArrayList<>());
                    }
                    produsCache.get(idProveedor).add(producto);
                }
                System.out.println("Productos cargados en memoria.");
            }
        } catch (SQLException e) {
            System.out.println("Error al cargar los productos: " + e.getMessage());
        }
    }
    
    public static List<Produs> obtenerProductosPorProveedor(int idProveedor) {
        return produsCache.getOrDefault(idProveedor, new ArrayList<>());
    }
    
    // Cargar todos los productos en memoria
    public String cargarOfertas(String sucursal) {
        String ofertukis = "";
        String query = "SELECT id_producto,codigo,nombre,precio FROM ofertas " +
                "WHERE CURDATE() BETWEEN fecha_inicio AND fecha_termino AND estatus = 1 AND " +
                "id_producto IN (SELECT id_producto FROM carnes WHERE estatus = 1) AND (tipo = "+sucursal+" OR tipo = 9);";
        try (Connection conn = conectar(); Statement stmt = conn.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    ofertukis += "  ✅  "+rs.getString("nombre")+" A SOLO $ "+rs.getString("precio")+"  ➡️ ";
                }
                System.out.println("Ofertas cargadas en memoria.");
                System.out.println(ofertukis);
            }
        } catch (SQLException e) {
            System.out.println("Error al cargar las ofertas: " + e.getMessage());
            System.out.println(ofertukis);
        }
        return ofertukis;
    }
    
}
