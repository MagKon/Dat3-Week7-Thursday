package org.example.REST.controller;

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
            BodyValidator<Hotel> validator = ctx.bodyValidator(Hotel.class);
            if (validator.errors().isEmpty()) {
                Hotel savedHotel = (Hotel) getDao().merge(validator.get());
                ctx.status(201);
                ctx.json(savedHotel);
            }
            else {
                ctx.status(400);
                ctx.json(validator.errors());
            }
        };
    }

    @Override
    public Handler update() {
        return ctx -> {
            BodyValidator<Hotel> validator = ctx.bodyValidator(Hotel.class);
            if (validator.errors().isEmpty()) {

                // Get Hotel from DB
                Hotel dbHotel = (Hotel) getDao().findById(Integer.parseInt(ctx.pathParam("id")));
                if (dbHotel == null) {
                    ctx.status(404);
                    ctx.json("No hotel found with id: " + ctx.pathParam("id"));
                    return;
                }

                // Update Hotel
                dbHotel.setName(validator.get().getName());
                dbHotel.setAddress(validator.get().getAddress());
                dbHotel.setRooms(validator.get().getRooms());
                Hotel savedHotel = (Hotel) getDao().merge(dbHotel);

                // Return updated Hotel
                ctx.status(200);
                ctx.json(savedHotel);
            }
            else {
                ctx.status(400);
                ctx.json(validator.errors());
            }
        };
    }

    @Override
    public Handler delete() {
        return ctx -> {
            Object element = getDao().findById(Integer.parseInt(ctx.pathParam("id")));
            if (element == null) {
                ctx.status(404);
                ctx.json("No element found with id: " + ctx.pathParam("id"));
            }
            else {
                try {
                    getDao().delete(element);
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
}
