package org.example.REST.controller;

import io.javalin.http.Handler;
import org.example.REST.converter.IConverter;
import org.example.config.DAO.DAO;

public abstract class AController<Entity, DTO> implements IController {

    protected final DAO<Entity> dao;
    protected final IConverter<Entity, DTO> converter;

    public AController(final DAO<Entity> dao, final IConverter<Entity, DTO> converter) {
        this.dao = dao;
        this.converter = converter;
    }

    @Override
    public Handler getAll() {
        return ctx -> {
            ctx.status(200);
            ctx.json(this.converter.createFromEntities(this.dao.findAll()));
        };
    }

    @Override
    public Handler getById() {
        return ctx -> {
            final String id = ctx.pathParam("id");
            final Entity entity = this.dao.findById(id);
            if (entity == null) {
                ctx.status(404);
                ctx.attribute("message", "Not found");
                return;
            }

            ctx.status(200);
            ctx.json(this.converter.convertFromEntity(entity));
        };
    }

    @Override
    public Handler create() {
        return ctx -> {
            final DTO dto = ctx.bodyAsClass(this.converter.getDtoClass());
            final Entity entity = this.dao.create(this.converter.convertFromDto(dto));
            ctx.status(200);
            ctx.json(this.converter.convertFromEntity(entity));
        };
    }

    @Override
    public Handler update() {
        return ctx -> {
            final String id = ctx.pathParam("id");
            final Entity oldEntity = dao.findById(id);
            if (oldEntity == null) {
                ctx.status(404);
                ctx.attribute("message", "Not found");
                return;
            }

            final DTO dto = ctx.bodyAsClass(this.converter.getDtoClass());
            final Entity newEntity = this.dao.merge(this.converter.convertFromDto(dto));
            ctx.status(200);
            ctx.json(this.converter.convertFromEntity(newEntity));
        };
    }

    @Override
    public Handler delete() {
        return ctx -> {
            final String id = ctx.pathParam("id");
            final Entity entity = dao.findById(id);
            if (entity == null) {
                ctx.status(404);
                ctx.attribute("message", "Not found");
                return;
            }

            this.dao.delete(entity);
            ctx.status(200);
            ctx.json(this.converter.convertFromEntity(entity));
        };
    }
}