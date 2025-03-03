package co.edu.uptc.controller;

import java.time.LocalDate;
import java.util.List;

import co.edu.uptc.model.AdminTraveler;
import co.edu.uptc.model.Rol;
import co.edu.uptc.model.Service;
import co.edu.uptc.model.Travel;
import co.edu.uptc.model.User;

public class ServerController {
    private Server server;
    private AdminTraveler adminTraveler;

    public ServerController() {
        server = new Server(this);
        adminTraveler = new AdminTraveler();
        test();
    }

    public void startServer() {
        new Thread(server).start();
    }

    public boolean validateUser(User validateUser) {
        return adminTraveler.loginUser(validateUser);
    }

    public boolean addClient(User user) {
        return adminTraveler.addUser(user);
    }

    public boolean addServices(Service service) {
        return adminTraveler.addService(service);
    }

    public boolean deleteService(String serviceId) {
        return adminTraveler.deleteService(serviceId);
    }

    public boolean addServiceToTravel(String travelId, String serviceId, LocalDate date) {
        return adminTraveler.addServiceToTravel(travelId, serviceId, date);
    }

    public void rateService(double rate, String serviceId) {
        adminTraveler.rateService(rate, serviceId);
    }

    public void sendMessage(String message) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendMessage'");
    }

    public Service searchService(String id) {
        return adminTraveler.searchService(id);
    }

    public Travel searchTravel(String id) {
        return adminTraveler.searchTravel(id);
    }

    public User searchUser(String id) {
        return adminTraveler.searchUser(id);
    }

    public List<Travel> searchTravels() {
        return adminTraveler.searchTravels();
    }

    public void test() {
        User user = new User("funnyflower", "1212");
        user.setName("juan");
        user.setLastName("cartas");
        user.setPhoneNumber("3144432146");
        user.setBirthDay(LocalDate.of(2005, 11, 10));
        user.setRol(Rol.ADMIN);
        addClient(user);

        User user1 = new User("damis", "1010");
        user1.setName("Camilo");
        user1.setLastName("Antioquia");
        user1.setPhoneNumber("3132907690");
        user1.setBirthDay(LocalDate.of(2001, 11, 9));
        user1.setRol(Rol.CLIENT);
        addClient(user1);

        Service service = new Service(3);
        service.setServiceId("S1");
        service.setName("bellaqueo");
        service.setDescription("guou");
        service.setPrice(150000);
        service.setCategory("venezolanos");
        addServices(service);

        Service service2 = new Service(3);
        service2.setServiceId("S2");
        service2.setName("paseo en moto");
        service2.setDescription("paseo en moto por las calles de medellin con paisa incluido");
        service2.setPrice(15000);
        service2.setCategory("Medellin");
        addServices(service2);

        for (int i = 1; i < 100; i++) {
            Travel travel = new Travel("T" + i);
            adminTraveler.addTravel(travel, "damis");
            for (int j = 0; j < 25; j++) {
                Service service3 = new Service(5);
                service3.setServiceId(i + "" + j);
                service3.setName("paseo en moto");
                service3.setDescription("paseo en moto por las calles de medellin con paisa incluido");
                service3.setPrice(15000);
                service3.setCategory("Medellin");
                addServices(service3);
                adminTraveler.addServiceToTravel("T" + i, i + "" + j, LocalDate.now().plusDays(j));
            }

            adminTraveler.addServiceToTravel("T" + i, "S2", LocalDate.now());

        }

    }

}
