package org.example.REST.controller;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.validation.BodyValidator;
import org.example.config.DAO.DAO;
import org.example.persistence.Hotel;

public class HotelController extends AController {

    public HotelController(DAO<Object> dao) {
        super(dao);
    }

    @Override
    public Handler create() {
        return ctx -> {
            Hotel hotel = validateHotel(ctx);

            Hotel savedHotel = (Hotel) getDao().merge(hotel);
            ctx.status(201);
            ctx.json(savedHotel);
        };
    }

    @Override
    public Handler update() {
        return ctx -> {
            // Validate Hotel
            Hotel hotel = validateHotel(ctx);

            // Get Hotel from DB
            Hotel dbHotel = (Hotel) getDao().findById(Integer.parseInt(ctx.pathParam("id")));
            if (dbHotel == null) {
                ctx.status(404);
                ctx.json("No hotel found with id: " + ctx.pathParam("id"));
                return;
            }

            // Update Hotel
            dbHotel.setName(hotel.getName());
            dbHotel.setAddress(hotel.getAddress());
            dbHotel.setRooms(hotel.getRooms());
            Hotel savedHotel = (Hotel) getDao().merge(dbHotel);

            // Return updated Hotel
            ctx.status(200);
            ctx.json(savedHotel);

        };
    }

    @Override
    @SuppressWarnings("all")
    public Handler delete() {
        return ctx -> {
            Hotel hotel = null;
            try {
                hotel = (Hotel) getDao().findById(Integer.parseInt(ctx.pathParam("id")));
            }
            catch (Exception e) {
                ctx.status(400);
                ctx.json(e.getMessage());
            }

            if (hotel == null) {
                ctx.status(404);
                ctx.json("No element found with id: " + ctx.pathParam("id"));
            }
            else {
                try {
                    getDao().delete(hotel);
                    ctx.status(200);
                    ctx.json("Deleted element with id: " + ctx.pathParam("id"));
                }
                catch (Exception e) {
                    ctx.status(400);
                    ctx.json(e.getMessage());
                }
            }
        };
    }

    public Hotel validateHotel(Context ctx) {
        return ctx.bodyValidator(Hotel.class)
                .check(hotel -> hotel.getName() != null && !hotel.getName().isEmpty(), "Name cannot be null or empty")
                .check(hotel -> hotel.getAddress() != null && !hotel.getAddress().isEmpty(), "Address cannot be null or empty")
                .check(hotel -> hotel.getRooms() != null && !hotel.getRooms().isEmpty(), "Rooms cannot be null or empty")
                .get();
    }
}
