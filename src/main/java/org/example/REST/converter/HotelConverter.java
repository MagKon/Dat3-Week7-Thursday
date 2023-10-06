package org.example.REST.converter;

import org.example.DTO.HotelDTO;
import org.example.persistence.Hotel;

import java.util.function.Function;

public class HotelConverter extends AConverter<Hotel, HotelDTO>{
    public HotelConverter() {
        super(fromDto, fromEntity, hotelDTOClass);
    }

    private static final Class<HotelDTO> hotelDTOClass = HotelDTO.class;

    private static final Function<HotelDTO, Hotel> fromDto = dto -> {
        Hotel hotel = new Hotel();
        hotel.setName(dto.name());
        hotel.setAddress(dto.address());
        return hotel;
    };

    private static final Function<Hotel, HotelDTO> fromEntity = entity -> new HotelDTO(entity.getId(), entity.getName(), entity.getAddress(), entity.getRooms());
}
