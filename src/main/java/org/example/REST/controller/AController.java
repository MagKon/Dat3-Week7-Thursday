package org.example.REST.controller;

import io.javalin.http.Handler;
import lombok.Getter;
import org.example.config.DAO.DAO;

import java.util.List;

@Getter
public abstract class AController implements IController {
    private final DAO dao;

    public AController(DAO dao) {
        this.dao = dao;
    }

    public Handler getAll() {
        return ctx -> {
            List<?> elements = dao.getAll();
            if (elements.isEmpty()) {
                ctx.status(404);
                ctx.json("No elements found");
            }
            else {
                ctx.json(elements);
                ctx.status(200);
            }
        };
    }

    public Handler getById() {
        return ctx -> {
            Object element = dao.findById(Integer.parseInt(ctx.pathParam("id")));
            if (element == null) {
                ctx.status(404);
                ctx.json("No element found with id: " + ctx.pathParam("id"));
            }
            else {
                ctx.json(element);
                ctx.status(200);
            }
        };
    }
}
