package co.edu.uptc.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import co.edu.uptc.models.Rol;
import co.edu.uptc.models.Service;
import co.edu.uptc.models.Travel;
import co.edu.uptc.models.User;
import co.edu.uptc.net.JsonManager;
import co.edu.uptc.net.Response;
import co.edu.uptc.view.CustomMessage;
import co.edu.uptc.view.View;

public class ClientController {
    private final String host;
    private final int port;
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    private View view;
    private JsonManager jsonManager;

    public ClientController() {
        host = "localhost";
        port = 666;
        view = new View(this);
        jsonManager = new JsonManager();
    }

    public void connect() throws IOException {
        socket = new Socket(host, port);
        input = new ObjectInputStream(socket.getInputStream());
        output = new ObjectOutputStream(socket.getOutputStream());
        jsonManager = new JsonManager();
    }

    public Response sendRequest(Response response) {
        try {
            if (output != null) {
                output.writeObject(response);
                output.flush();
                System.out.println("Petición enviada al servidor: " + response);
                return (Response) input.readObject();
            } else {
                throw new IOException();
            }
        } catch (IOException | ClassNotFoundException e) {
            CustomMessage.showServerMessage(view, "ERROR: ERROR DE SERVIDOR");
        }
        return null;
    }

    public void sendMessage(String message) {
        try {
            connect();
            String data[] = message.split("##");
            if (data.length == 2) {
                User user = new User(data[0], data[1]);
                user.setRol(Rol.ADMIN);
                Response response = sendRequest(jsonManager.responseRequest("LOGIN", user));
                boolean logged = jsonManager.deserializeJson(response.getResponseBody(), boolean.class);
                if (logged) {
                    view.showView();
                } else {
                    disconnect();
                    view.showLoginErrorMessage("USUARIO O CONTRASEÑA INCORRECTOS");
                }
            } else {
                view.showLoginErrorMessage("INFORMACION INCOMPLETA");
            }
        } catch (IOException e) {
            CustomMessage.showServerMessage(view, "ERROR: SERVIDOR DESCONECTADO");
        }

    }

    public void addService(Service service) {
        if (service != null) {
            Response response = sendRequest(jsonManager.responseRequest("ADD_SERVICE", service));
            boolean add = jsonManager.deserializeJson(response.getResponseBody(), boolean.class);
            if (add) {
                view.showMessage("SERVICIO :" + service.getName() + "\nAGREGADO CORRECTAMENTE");
            } else {
                view.showErrorMessage("ID SERVICIO EXISTENTE");
            }
        }
    }

    public void deleteService(String serviceId) {
        if (!serviceId.isEmpty()) {
            Response response = sendRequest(jsonManager.responseRequest("DELETE_SERVICE", serviceId));
            System.out.println(response);
            boolean deleted = jsonManager.deserializeJson(response.getResponseBody(), boolean.class);
            if (deleted) {
                view.showMessage("SERVICIO ELIMINADO");
            } else {
                view.showErrorMessage("Error: NO HA PODIDO ELIMINARSE");
            }
        }
    }

    public Service searchService(String serviceId) {
        if (!serviceId.isEmpty()) {
            Response response = sendRequest(jsonManager.responseRequest("SEARCH_SERVICE", serviceId));
            System.out.println(response);
            return jsonManager.deserializeJson(response.getResponseBody(), Service.class);
        }
        return null;
    }

    public List<Travel> searchTravels() {
        Response response = sendRequest(jsonManager.responseRequest("SEARCH_TRAVELS", "NULL"));
        System.out.println(response);
        List<Travel> travels = jsonManager.deserializeJsonList(response.getResponseBody(), Travel.class);
        for (Travel travel : travels) {
            System.out.println(travel);
        }
        return travels;
    }

    public void disconnect() {
        try {
            if (socket != null)
                socket.close();
        } catch (IOException e) {
            System.err.println("Error al desconectar: " + e.getMessage());
        }
    }

}
