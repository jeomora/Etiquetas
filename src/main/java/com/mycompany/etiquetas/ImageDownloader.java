/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.etiquetas;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class ImageDownloader {

    public static void descargar(String url, String destino) {
        try (InputStream in = new URL(url).openStream()) {
            Files.copy(in, Path.of(destino), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            System.out.println("Error descargando: " + e.getMessage());
        }
    }
}