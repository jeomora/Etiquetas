/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.etiquetas;

/**
 *
 * @author PROGRAMEITOR
 */
public class Producto {
    private String codigo;
    private String nombre;
    private int idProducto;
    private String precioUno;

    // Constructor
    public Producto(String codigo, String nombre, int idProducto, String precioUno) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.idProducto = idProducto;
        this.precioUno = precioUno;
    }

    // Getters y setters
    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public String getPrecioUno() {
        return precioUno;
    }

    @Override
    public String toString() {
        return "Código: " + codigo + ", Nombre: " + nombre + ", Precio: $" + precioUno + ", ID Producto: " + idProducto;
    }
}

