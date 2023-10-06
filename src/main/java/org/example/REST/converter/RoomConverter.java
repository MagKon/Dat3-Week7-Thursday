package org.example.REST.converter;

import org.example.DTO.RoomDTO;
import org.example.persistence.Room;

import java.util.function.Function;

public class RoomConverter extends AConverter<Room, RoomDTO> {

    public RoomConverter() {
        super(fromDto, fromEntity, roomDTOClass);
    }

    private static final Class<RoomDTO> roomDTOClass = RoomDTO.class;

    private static final Function<RoomDTO, Room> fromDto = dto -> {
        Room room = new Room();
        room.setRoomNumber(dto.roomNumber());
        room.setCapacity(0);
        room.setPrice(dto.price());
        return room;
    };

    private static final Function<Room, RoomDTO> fromEntity = entity -> new RoomDTO(entity.getId(), entity.getHotelId().getId(), entity.getRoomNumber(), entity.getPrice());
}
