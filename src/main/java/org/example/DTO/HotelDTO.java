package org.example.DTO;

import org.example.persistence.Room;

import java.util.Set;

public record HotelDTO(int id, String name, String address, Set<Room> rooms) {
}
