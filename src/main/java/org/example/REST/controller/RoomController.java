package org.example.REST.controller;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.validation.BodyValidator;
import org.example.DTO.RoomDTO;
import org.example.REST.converter.IConverter;
import org.example.REST.converter.RoomConverter;
import org.example.config.DAO.DAO;
import org.example.exception.ApiException;
import org.example.persistence.Hotel;
import org.example.persistence.Room;

public class RoomController extends AController<Room, RoomDTO> {
    public RoomController(DAO<Room> dao) {
        super(dao, new RoomConverter());
    }

//    public RoomController(DAO<Room> dao) {
//        super(dao);
//    }
//
//    @Override
//    public Handler create() {
//        return ctx -> {
//            Room room = validateRoom(ctx);
//
//            Room savedHotel = (Room) getDao().merge(room);
//
//            ctx.contentType("application/json");
//            ctx.status(201);
//            ctx.json(savedHotel);
//
//        };
//    }
//
//    @Override
//    public Handler update() {
//        return ctx -> {
//            Room validator = validateRoom(ctx);
//
//            // Get Room from DB
//            Room dbRoom = (Room) getDao().findById(Integer.parseInt(ctx.pathParam("id")));
//            if (dbRoom == null) {
////                ctx.status(404);
////                ctx.json("No hotel found with id: " + ctx.pathParam("id"));
////                return;
//                throw new ApiException(404, "No room found with id: " + ctx.pathParam("id"));
//            }
//
//            // Update Room
//            dbRoom.setRoomNumber(validator.getRoomNumber());
//            dbRoom.setCapacity(validator.getCapacity());
//            dbRoom.setPrice(validator.getPrice());
//            Room savedRoom = (Room) getDao().merge(dbRoom);
//
//            // Return updated Hotel
//            ctx.contentType("application/json");
//            ctx.status(200);
//            ctx.json(savedRoom);
//
//        };
//    }
//
//    @Override
//    @SuppressWarnings("all")
//    public Handler delete() {
//        return ctx -> {
//            Room room = null;
//            try {
//                room = (Room) getDao().findById(Integer.parseInt(ctx.pathParam("id")));
//            }
//            catch (Exception e) {
////                ctx.status(400);
////                ctx.json("Invalid id: " + ctx.pathParam("id"));
////                return;
//                throw new ApiException(400, "Invalid id: " + ctx.pathParam("id"));
//            }
//            if (room == null) {
////                ctx.status(404);
////                ctx.json("No element found with id: " + ctx.pathParam("id"));
//                throw new ApiException(404, "No element found with id: " + ctx.pathParam("id"));
//            }
//            else {
//                try {
//                    getDao().delete(room);
//                    ctx.contentType("application/json");
//                    ctx.status(200);
//                    ctx.json("Deleted element with id: " + ctx.pathParam("id"));
//                }
//                catch (Exception e) {
////                    ctx.status(400);
////                    ctx.json(e.getMessage());
//                    throw new ApiException(400, e.getMessage());
//                }
//
//            }
//        };
//    }

    public Handler brewCoffee() {
        return ctx -> {
            ctx.status(418);
            ctx.json("I'm a teapot");
        };
    }

    public Room validateRoom(Context ctx) {
        return ctx.bodyValidator(Room.class)
                .check(room -> room.getRoomNumber() > 0 && room.getRoomNumber() < 1000, "Room number must be between 1 and 999")
                .check(room -> room.getCapacity() > 0 && room.getCapacity() < 10, "Capacity must be between 1 and 9")
                .check(room -> room.getPrice() > 0 && room.getPrice() < 10000, "Price must be between 1 and 9999")
                .get();
    }
}
