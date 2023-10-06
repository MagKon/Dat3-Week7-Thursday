package org.example.REST.converter;

import lombok.Getter;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class AConverter<DTO, Entity> implements IConverter<DTO, Entity> {

    private final Function<DTO, Entity> fromDto;
    private final Function<Entity, DTO> fromEntity;

    @Getter
    private final Class<DTO> dtoClass;

    public AConverter(final Function<DTO, Entity> fromDto, final Function<Entity, DTO> fromEntity, final Class<DTO> dtoClass) {
        this.fromDto = fromDto;
        this.fromEntity = fromEntity;
        this.dtoClass = dtoClass;
    }

    @Override
    public final Entity convertFromDto(final DTO dto) {
        return fromDto.apply(dto);
    }

    @Override
    public final DTO convertFromEntity(final Entity entity) {
        return fromEntity.apply(entity);
    }

    @Override
    public final List<Entity> createFromDTOs(final Collection<DTO> dtoCollection) {
        return dtoCollection.stream().map(this::convertFromDto).collect(Collectors.toList());
    }

    @Override
    public final List<DTO> createFromEntities(final Collection<Entity> entities) {
        return entities.stream().map(this::convertFromEntity).collect(Collectors.toList());
    }
}