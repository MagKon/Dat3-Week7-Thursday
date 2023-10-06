package org.example.REST.converter;

import java.util.Collection;
import java.util.List;

public interface IConverter<Entity, DTO> {

    Class<DTO> getDtoClass();

    Entity convertFromDto(final DTO dto);

    DTO convertFromEntity(final Entity entity);

    List<Entity> createFromDTOs(final Collection<DTO> dtoCollection);

    List<DTO> createFromEntities(final Collection<Entity> entities);
}