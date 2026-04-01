/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.etiquetas;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class FacebookService {

    private static final String ACCESS_TOKEN = "EAAItwk8UBIMBRLimZBZC0bqde6uvVWhFNRMGMeDIaahtA7aZCpyCeZCY5qZCcLLNCEI1ZC02voXRepmbaio48Ti6f5aRXUZBxe9YdfZBNRChPfWrCT6H6HEFmdgnTCww2nEmZBU4W0E10RKO6SZBxL7lo4ZChBxzthOcUhGHbo1tXT2894o21FcBqXF7aI5CdZAGDO3o3FmNFhOt6INC1ns72O8WsoSDGgyen05wzZBNYfgZDZD";
    private static final String PAGE_ID = "102056437878185";

    public List<String> obtenerImagenes() {
        List<String> imagenes = new ArrayList<>();

        try {
            String url = "https://graph.facebook.com/v19.0/" + PAGE_ID +
                    "/posts?fields=full_picture&access_token=" + ACCESS_TOKEN;

            System.out.println("Consultando: " + url);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Respuesta cruda:");
            System.out.println(response.body());

            JSONObject json = new JSONObject(response.body());

            // 🔥 VALIDACIÓN CLAVE
            if (!json.has("data")) {
                System.out.println("⚠️ No viene 'data', respuesta:");
                System.out.println(json.toString(2));
                return imagenes;
            }

            JSONArray data = json.getJSONArray("data");

            for (int i = 0; i < data.length(); i++) {
                JSONObject post = data.getJSONObject(i);

                if (post.has("full_picture")) {
                    String img = post.getString("full_picture");
                    imagenes.add(img);
                }
            }

            System.out.println("Total imágenes encontradas: " + imagenes.size());

        } catch (Exception e) {
            System.out.println("Error Facebook:");
            e.printStackTrace();
        }

        return imagenes;
    }
}