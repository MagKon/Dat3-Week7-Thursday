package org.example.config.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.example.DTO.RoomDTO;
import org.example.config.HibernateConfig;
import org.example.persistence.Hotel;
import org.example.persistence.Room;

import java.util.List;

public class HotelDAO extends DAO<Hotel>{
    public HotelDAO(Class<Hotel> entityClass) {
        super(entityClass);
    }

    public HotelDAO(Class<Hotel> entityClass, EntityManagerFactory emf) {
        super(entityClass, emf);
    }

    public List<Room> getAllRooms(int hotelId) {
        try (EntityManager entityManager = super.getEntityManagerFactory().createEntityManager()) {
            return entityManager.createQuery("SELECT r FROM Room r WHERE r.hotelId = :hotelId", Room.class)
                    .setParameter("hotelId", hotelId)
                    .getResultList();
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
}
