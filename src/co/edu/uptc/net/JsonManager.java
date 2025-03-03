package co.edu.uptc.net;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class JsonManager {
    private final Gson gson;

    public JsonManager() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
    }

    // este es para deserializar
    public <T> T deserializeJson(String json, Class<T> clazz) {
        try {
            return gson.fromJson(json, clazz);
        } catch (JsonSyntaxException e) {
            System.err.println("Error al deserializar el JSON: " + e.getMessage());
            throw e;
        }
    }

    // recibe un objeto crea un Json y lo retorna como String
    private String buildJson(Object object) {
        return gson.toJson(object);
    }

    private String buildJson(List<Object> objects) {
        return gson.toJson(objects);
    }

    // crear una respuesta recibe un Objeto y lo serializa
    public Response responseRequest(String request, Object bodyResponse) {
        Response response = new Response();
        response.setRequest(request);
        response.setResponseBody(buildJson(bodyResponse));
        return response;
    }

    public Response responseRequest(String request, List<Object> bodyResponse) {

        Response response = new Response();
        response.setRequest(request);
        response.setResponseBody(buildJson(bodyResponse));
        return response;
    }

    // este es para crear un Json sin estructura de objeto. sino mas bien como un
    // valor atributo
    public Response responseRequest(String request, Map<String, String> attributes) {
        JsonObject jsonObject = new JsonObject();
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            String value = entry.getValue();
            jsonObject.addProperty(entry.getKey(), value);
        }
        return new Response(request, jsonObject.toString());
    }

    // retorna un atributo de un json sin estructura
    public String getAttributeValue(String responseBody, String attribute) {
        try {
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = jsonParser.parse(responseBody).getAsJsonObject();
            return jsonObject.has(attribute) ? jsonObject.get(attribute).getAsString() : null;
        } catch (Exception e) {
            return null;
        }
    }

    // se supone que aca va la persistencia(yo pensaba hacer otra clase)
    public void writeJson(Object object, String filePath) {
        try (java.io.FileWriter writer = new java.io.FileWriter(filePath)) {
            gson.toJson(object, writer);
        } catch (java.io.IOException e) {
            System.err.println("Error al escribir el archivo JSON: " + e.getMessage());
        }
    }

    private class LocalDateAdapter extends TypeAdapter<LocalDate> {
        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

        @Override
        public void write(JsonWriter out, LocalDate value) throws IOException {
            if (value != null) {
                out.value(value.format(FORMATTER));
            } else {
                out.nullValue();
            }
        }

        @Override
        public LocalDate read(JsonReader in) throws IOException {
            String date = in.nextString();
            return LocalDate.parse(date, FORMATTER);
        }
    }
}
