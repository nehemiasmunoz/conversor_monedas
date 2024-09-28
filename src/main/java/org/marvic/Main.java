package org.marvic;


import com.google.gson.Gson;
import org.marvic.models.Response;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int option = 1;
        double valor = 0;
        String baseCode;
        String targetCode;
        Response result = new Response();
        Scanner scanner = new Scanner(System.in);

        HttpClient client = HttpClient.newHttpClient();

        do{
            showMenu();
            option = scanner.nextInt();
            if(option == 0) break;
            scanner.nextLine();
            System.out.print("Ingresa el valor que deseas convertir: ");
            valor = scanner.nextDouble();
            switch (option){
                case 1:
                    targetCode =  "CLP";
                    baseCode ="USD";
                    result = getData(valor, baseCode,targetCode,client);
                    break;
                case 2:
                    baseCode ="CLP";
                    targetCode = "USD";
                    result = getData(valor,baseCode,targetCode,client);
                    break;
                case 3:
                    baseCode = "USD";
                    targetCode = "ARS";
                    result = getData(valor, baseCode, targetCode,client);
                    break;
                case 4:
                    baseCode = "ARS";
                    targetCode = "USD";
                    result = getData(valor, baseCode, targetCode,client);
                    break;
                default:
                    System.out.println("Opcion invalida");
                    break;
            }

            showResult(valor,result.getConversion_result(), result.getBase_code(), result.getTarget_code());

        }
        while (true);
        System.out.println("Finalizado");
    }



    public static void showMenu(){
        String menu = """
                *************************************
                Bienvenido al conversor de monedas
                
                1) Dolar ==> Peso Chileno.
                2) Peso Chileno ==> Dolar.
                3) Dolar ==> Peso Argentino.
                4) Peso Argentino ==> Dolar.
                
                0) Salir.
                *************************************
                
                Ingrese una opcion valida:
                """;
        System.out.print(menu);
    }

    public static Response getData(double value, String baseCode, String targetCode, HttpClient client){
        String json;
        try{
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://v6.exchangerate-api.com/v6/"+System.getenv("API_KEY")+"/pair/%s/%s/%f".formatted(baseCode, targetCode, value))).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            json = response.body();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Gson gson = new Gson();
        Response response = gson.fromJson(json, Response.class);
        return response;
    }
    
    public static void showResult(double valor,double result, String baseCode, String targetCode){
        String mensaje = "El valor de %f[%s] corresponde a %f[%s] ".formatted(valor,baseCode,result, targetCode);
        System.out.println(mensaje);
    }
}

