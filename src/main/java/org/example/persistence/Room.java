package org.example.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Room {

    @Id
    private int id;

    @ManyToOne
    private Hotel hotelId;

    private int roomNumber;

    private int capacity;

    private int price;

    private boolean isAvailable;
}
