package org.example.REST.controller;

import io.javalin.http.Handler;
import lombok.Getter;
import org.example.config.DAO.DAO;
import org.example.exception.ApiException;

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

            ctx.json(elements);
            ctx.status(200);

        };
    }

    public Handler getById() {
        return ctx -> {
            try {
                Object element = dao.findById(ctx.pathParam("id"));
                ctx.json(element);
                ctx.status(200);
            }
            catch (Exception e) {
                throw new ApiException(404, "No element found with id: " + ctx.pathParam("id"));
            }
        };
    }
}
