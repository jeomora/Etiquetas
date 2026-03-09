/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.etiquetas;

/**
 *
 * @author PROGRAMEITOR
 */
import java.util.LinkedList;

public class Historial {
    private LinkedList<Producto> historial;
    private final int LIMITE = 5;

    public Historial() {
        historial = new LinkedList<>();
    }

    // Método para agregar un producto al historial
    public void agregarProducto(Producto producto) {
        // Si el historial tiene más de 5 productos, eliminamos el primero (el más antiguo)
        if (historial.size() >= LIMITE) {
            historial.removeFirst();
        }
        historial.add(producto);
    }

    // Obtener un producto por índice
    public Producto obtenerProducto(int index) {
        if (index >= 0 && index < historial.size()) {
            return historial.get(index);
        }
        return null;  // Retorna null si el índice es inválido
    }

    // Obtener el historial completo
    public LinkedList<Producto> obtenerHistorial() {
        return historial;
    }
}
