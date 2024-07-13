import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Main {

    private static final String API_KEY = "8bfdd556954dde07d217d4c1"; // Reemplaza con tu API key
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/pair/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Conversor de Monedas a Pesos Colombianos");
            System.out.println("1. Dólar (USD)");
            System.out.println("2. Euro (EUR)");
            System.out.println("3. Libra Esterlina (GBP)");
            System.out.println("4. Yen Japonés (JPY)");
            System.out.println("5. Franco Suizo (CHF)");
            System.out.println("6. Dólar Canadiense (CAD)");
            System.out.println("7. Dólar Australiano (AUD)");
            System.out.println("8. Salir");
            System.out.println();
            System.out.println("Seleccione una opción: ");

            int opcion = scanner.nextInt();
            if (opcion == 8) {
                break;
            }

            System.out.print("Ingrese la cantidad a convertir: ");
            System.out.println();
            double cantidad = scanner.nextDouble();

            String moneda = obtenerMoneda(opcion);
            if (moneda != null) {
                double tasaConversion = obtenerTasaConversion(moneda, "COP");
                if (tasaConversion != 0) {
                    double resultado = cantidad * tasaConversion;
                    System.out.println("El equivalente en Pesos Colombianos es: " + resultado);
                    System.out.println();
                } else {
                    System.out.println("Error al obtener la tasa de conversión. Intente de nuevo.");
                }
            } else {
                System.out.println("Opción inválida. Por favor, intente de nuevo.");
            }
        }

        scanner.close();
    }

    private static String obtenerMoneda(int opcion) {
        switch (opcion) {
            case 1: return "USD";
            case 2: return "EUR";
            case 3: return "GBP";
            case 4: return "JPY";
            case 5: return "CHF";
            case 6: return "CAD";
            case 7: return "AUD";
            default: return null;
        }
    }

    private static double obtenerTasaConversion(String fromCurrency, String toCurrency) {
        try {
            String url = BASE_URL + fromCurrency + "/" + toCurrency;
            System.out.println("Solicitando URL: " + url); // Mensaje de depuración
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                System.out.println("Error en la solicitud HTTP: " + response.statusCode());
                return 0;
            }

            String json = response.body();
            JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
            double tasaConversion = jsonObject.get("conversion_rate").getAsDouble();

            return tasaConversion;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
