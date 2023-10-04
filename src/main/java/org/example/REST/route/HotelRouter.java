package org.example.REST.route;

import io.javalin.apibuilder.EndpointGroup;
import org.example.REST.controller.HotelController;
import org.example.config.DAO.DAO;
import org.example.config.HibernateConfig;
import org.example.persistence.Hotel;

import static io.javalin.apibuilder.ApiBuilder.*;

public class HotelRouter {
    public static EndpointGroup getHotelRoutes() {
        HotelController hotelController = new HotelController(new DAO<>(Hotel.class, HibernateConfig.getEntityManagerFactoryConfig()));
        return () -> {
            path("/hotel", () -> {
                post("/create", hotelController.create());
                post("/brew", hotelController.brewCoffee());
                get("", hotelController.getAll());

                path("/{id}", () -> {
                    get(hotelController.getById());
                    put(hotelController.update());
                    delete(hotelController.delete());
                });
            });
        };
    }
}
