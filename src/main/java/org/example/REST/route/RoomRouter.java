package org.example.REST.route;

import io.javalin.apibuilder.EndpointGroup;
import org.example.REST.controller.RoomController;
import org.example.config.DAO.DAO;
import org.example.config.HibernateConfig;
import org.example.persistence.Room;

import static io.javalin.apibuilder.ApiBuilder.*;

public class RoomRouter {
    public static EndpointGroup getRoomRoutes() {
        RoomController roomController = new RoomController(new DAO<>(Room.class, HibernateConfig.getEntityManagerFactoryConfig()));
        return () -> path("/room", () ->
        {
            post("/create", roomController.create());
            post("/brew", roomController.brewCoffee());
            get("", roomController.getAll());

            path("/{id}", () -> {
                get(roomController.getById());
                put(roomController.update());
                delete(roomController.delete());
            });


        });
    }
}
