package co.edu.uptc.controller;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;

import co.edu.uptc.model.Service;
import co.edu.uptc.model.User;
import co.edu.uptc.net.JsonManager;
import co.edu.uptc.net.Response;

public class ClientHandler implements Runnable {

    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private ServerController serverController;

    private JsonManager jsonManager;

    public ClientHandler(Socket socket, ServerController serverController) {
        this.socket = socket;
        this.serverController = serverController;
        jsonManager = new JsonManager();
        try {
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.err.println("Error de comunicacion: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            Response requestReponse;
            while ((requestReponse = (Response) input.readObject()) != null) {
                System.out.println("Solicitud recibida: " + requestReponse.getRequest());
                Response response = handleRequest(requestReponse);
                output.writeObject(response);
                output.flush();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Cliente desconectado " + socket.getInetAddress());
        } finally {
            closeConnection();
        }
    }

    private Response handleRequest(Response clientResponse) {
        String request = clientResponse.getRequest();
        String responseBody = clientResponse.getResponseBody();
        Response serverResponse;
        switch (request) {
            case "LOGIN":
                boolean logged = login(responseBody);
                serverResponse = jsonManager.responseRequest(null, logged);
                break;
            case "ADD_CLIENT":
                serverResponse = addClient(responseBody);
                break;
            case "ADD_SERVICE":
                serverResponse = addService(responseBody);
                break;
            case "DELETE_SERVICE":
                serverResponse = deleteService(responseBody);
                break;
            case "ADD_SERVICE_TRAVEL":
                serverResponse = addServiceToTravel(responseBody);
                break;
            case "SEARCH_SERVICE":
                serverResponse = searchService(responseBody);
                break;
            case "SEARCH_USER":
                serverResponse = searchUser(responseBody);
                break;
            case "SEARCH_TRAVELS":
                serverResponse = searchTravels();
                break;
            case "RATE_SERVICE":
                serverResponse = rateService(responseBody);
                break;
            default:
                serverResponse = nullResponse();
                break;
        }
        return serverResponse;
    }

    public boolean login(String responseBody) {
        User user = jsonManager.deserializeJson(responseBody, User.class);
        return serverController.validateUser(user);
    }

    public Response addClient(String responseBody) {
        User user = jsonManager.deserializeJson(responseBody, User.class);
        return jsonManager.responseRequest("CONFIRMATION", serverController.addClient(user));
    }

    public Response addService(String responseBody) {
        Service service = jsonManager.deserializeJson(responseBody, Service.class);
        return jsonManager.responseRequest("CONFIRMATION", serverController.addServices(service));
    }

    public Response deleteService(String responseBody) {
        String serviceId = jsonManager.deserializeJson(responseBody, String.class);
        return jsonManager.responseRequest("CONFIRMATION", serverController.deleteService(serviceId));

    }

    public Response addServiceToTravel(String responseBody) {
        String travelId = jsonManager.getAttributeValue(responseBody, "travelId");
        String serviceId = jsonManager.getAttributeValue(responseBody, "serviceId");
        String strDate = jsonManager.getAttributeValue(responseBody, "date");
        LocalDate date = jsonManager.deserializeJson(strDate, LocalDate.class);
        return jsonManager.responseRequest("CONFIRMATION",
                serverController.addServiceToTravel(travelId, serviceId, date));
    }

    public Response rateService(String responseBody) {
        double rate = Double.parseDouble(jsonManager.getAttributeValue(responseBody, "rate"));
        String serviceId = jsonManager.getAttributeValue(responseBody, "serviceId");
        serverController.rateService(rate, serviceId);
        return nullResponse();
    }

    public Response searchService(String responseBody) {
        System.out.println(jsonManager.deserializeJson(responseBody, String.class));
        Service service = serverController.searchService(jsonManager.deserializeJson(responseBody, String.class));
        return jsonManager.responseRequest("SEARCH", service);
    }

    public Response searchUser(String responseBody) {
        User user = serverController.searchUser(responseBody);
        return jsonManager.responseRequest("SEARCH", user);
    }

    public Response searchTravels() {
        jsonManager.writeJson(serverController.searchTravels(), "data/response.json");
        return jsonManager.responseRequest(null, serverController.searchTravels());
    }

    public Response nullResponse() {
        Response response = new Response();
        response.setResponseBody("UNKNOWN");
        return response;
    }

    private void closeConnection() {
        try {
            if (socket != null)
                socket.close();
            if (input != null)
                input.close();
            if (output != null)
                output.close();
        } catch (IOException e) {
            System.err.println("Error cerrando conexión: " + e.getMessage());
        }
    }
}
