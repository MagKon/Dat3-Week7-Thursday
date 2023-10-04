package org.example;

import io.javalin.Javalin;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;
import org.example.REST.JavalinConfig;
import org.example.REST.route.HotelRouter;
import org.example.REST.route.RoomRouter;
import org.example.config.HibernateConfig;

public class Main {
    public static void main(String[] args) {
        Javalin config = JavalinConfig.create("/api/", "application/json");
        EndpointGroup hotelRoutes = HotelRouter.getHotelRoutes();
        EndpointGroup roomRoutes = RoomRouter.getRoomRoutes();
        config.routes(hotelRoutes);
        config.routes(roomRoutes);

        EntityManagerFactory entityManagerFactory = HibernateConfig.getEntityManagerFactoryConfig();
    }
}