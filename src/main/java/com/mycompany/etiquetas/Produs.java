/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.etiquetas;

/**
 *
 * @author PROGRAMEITOR
 */
public class Produs {
    private String codigo;
    private String descripcion;
    private int idProducto;
    private int idProveedor;
    private String precioUno;

    // Constructor
    public Produs(String codigo, String descripcion, int idProducto, String precioUno,int idProveedor) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.idProducto = idProducto;
        this.idProveedor = idProveedor;
        this.precioUno = precioUno;
    }

    // Getters y setters
    public String getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getIdProducto() {
        return idProducto;
    }
    
    public int getIdProveedor() {
        return idProveedor;
    }

    public String getPrecioUno() {
        return precioUno;
    }

    @Override
    public String toString() {
        return "Código: " + codigo + ", Nombre: " + descripcion + ", Precio: $" + precioUno + ", ID Producto: " + idProducto + ", ID Proveedor: " + idProveedor;
    }
}