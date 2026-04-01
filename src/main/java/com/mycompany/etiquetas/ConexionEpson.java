/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.etiquetas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author JEOS
 */
public class ConexionEpson {
    private static final String URL = "jdbc:mysql://192.168.1.4:3306/turnosemillas";
    //private static final String URL = "jdbc:mysql://localhost:3306/turnosemillas";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "";

    // Conectar a la base de datos
    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
    }

    public static int getTurnos() {
        int numero = 0;
        System.out.println("inicio turnos");

        String query = "SELECT id_turno,fecha_registro,numero,estatus,fecha_inicio " +
                       "FROM turnosemillas " +
                       "WHERE DATE(fecha_registro) = CURDATE() " +
                       "ORDER BY id_turno DESC;";

        // try-with-resources cierra automáticamente conn, stmt y rs
        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("realizo consulta");

            if (rs.next()) {
                numero = rs.getInt("numero");
            }

        } catch (SQLException e) {
            System.out.println("Error EL NUMERO DE TURNO: " + e.getMessage());
        }

        return numero;
    }
    
    public static int getAtento() {
        int numero = 0;
        System.out.println("inicio turnos");

        String query = "SELECT id_turno,fecha_registro,numero,estatus,fecha_inicio " +
                       "FROM turnosemillas " +
                       "WHERE DATE(fecha_registro) = CURDATE() AND estatus = 2 " +
                       "ORDER BY id_turno DESC;";

        // try-with-resources cierra automáticamente conn, stmt y rs
        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                numero = rs.getInt("numero");
            }

        } catch (SQLException e) {
            System.out.println("Error EL NUMERO DE TURNO: " + e.getMessage());
        }

        return numero;
    }
    
    
    // Cargar siguientes turnos
    public static ResultSet cargarSiguientes() {
        String query = "SELECT id_turno,numero FROM turnosemillas WHERE DATE(fecha_registro) = CURDATE() AND estatus = 1 ORDER BY numero ASC LIMIT 6";
        try (Connection conn = conectar(); Statement stmt = conn.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(query)) {
                return rs;
            }
        } catch (SQLException e) {
            System.out.println("Error al cargar los siguientes: " + e.getMessage());
        }
        return null;
    }
    
    // En ConexionEpson
    public static Map<Integer, Integer> cargarXCaja() {
        Map<Integer, Integer> turnos = new HashMap<>();
        String query = "SELECT caja, MAX(numero) as numero " +
                       "FROM turnosemillas " +
                       "WHERE estatus = 2 AND DATE(fecha_registro) = CURDATE() " +
                       "GROUP BY caja;";
        try (Connection conn = conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int caja = rs.getInt("caja");
                int numero = rs.getInt("numero");
                turnos.put(caja, numero);
            }
        } catch (SQLException e) {
            System.out.println("Error al cargar los siguientes: " + e.getMessage());
        }
        return turnos;
    }
    
}
