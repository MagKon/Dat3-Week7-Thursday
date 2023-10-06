package org.example.config.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.Getter;
import org.hibernate.exception.ConstraintViolationException;

import javax.lang.model.UnknownEntityException;
import java.util.List;

/**
 * This class is a generic DAO (Data Access Object) that can be used to perform CRUD operations on any entity.
 * @param <T> The entity class that the DAO should be used for.
 */
@Getter
public class DAO<T> extends ADAO<T> {
    // Constructors

    /**
     * This constructor creates a DAO with the entity class specified. It is expected that the EntityManagerFactory is set later.
     * @param entityClass The entity class that the DAO should be used for. This cannot be changed later.
     */
    public DAO(Class<T> entityClass) {
        super(entityClass);
    }

    /**
     * This constructor creates a DAO with the entity class and EntityManagerFactory specified.
     * @param entityClass The entity class that the DAO should be used for. This cannot be changed later.
     * @param emf The EntityManagerFactory that the DAO should use to create EntityManagers.
     */
    public DAO(Class<T> entityClass, EntityManagerFactory emf) {
        super(entityClass, emf);
    }
}

/**
 * This is an abstract class that is used to perform CRUD operations on any entity. It can be extended to gain access to basic CRUD operations.
 * @param <T>
 */
@Getter
abstract class ADAO<T> implements IDAO<T> {
    /**
     * This is the EntityManagerFactory that the DAO should use to create EntityManagers.
     */
    EntityManagerFactory entityManagerFactory;

    /**
     * This is the entity class that the DAO should be used for.
     */
    Class<T> entityClass;

    // Constructors

    /**
     * This constructor creates a DAO with the entity class specified. It is expected that the EntityManagerFactory is set later.
     * @param entityClass The entity class that the DAO should be used for. This cannot be changed later.
     */
    public ADAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * This constructor creates a DAO with the entity class and EntityManagerFactory specified.
     * @param entityClass The entity class that the DAO should be used for. This cannot be changed later.
     * @param emf The EntityManagerFactory that the DAO should use to create EntityManagers.
     */
    public ADAO(Class<T> entityClass, EntityManagerFactory emf) {
        this.entityClass = entityClass;
        this.entityManagerFactory = emf;
    }

    // Setters
    /**
     * This method sets the EntityManagerFactory that the DAO should use to create EntityManagers.
     * @param emf The EntityManagerFactory that the DAO should use to create EntityManagers.
     */
    public void setEntityManagerFactory(EntityManagerFactory emf) {
        this.entityManagerFactory = emf;
    }

    // Queries

    /**
     * This query finds an entity by its id.
     * @param id The id of the entity to find. This is expected to be the primary key of the entity.
     * @return The entity with the specified id.
     */
    public T findById(Object id) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.find(this.entityClass, id);

        }
        catch (UnknownEntityException e) {
            System.out.println("Unknown entity: " + this.entityClass.getSimpleName());
            return null;
        }
    }

    /**
     * This query finds all entities of the type specified.
     * @return A list of all entities of the type specified.
     */
    public List<T> findAll() {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.createQuery("SELECT t FROM " + this.entityClass.getSimpleName() + " t", entityClass).getResultList();
        }
        catch (UnknownEntityException e) {
            System.out.println("Unknown entity: " + this.entityClass.getSimpleName());
            return null;
        }
    }

    // Standard CRUD operations

    /**
     * This method saves an entity to the database using persist.
     * @param t The entity to save.
     * @return True if the entity was saved successfully, false if not.
     */
    public T create(T t) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.persist(t);
            entityManager.getTransaction().commit();
            return t;
        }
        catch (ConstraintViolationException e) {
            System.out.println("Constraint violation: " + e.getMessage());
            return null;
        }
    }

    /**
     * This method updates an entity in the database using merge.
     * @param t The entity to update.
     * @return The updated entity.
     */
    public T merge(T t) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            entityManager.getTransaction().begin();
            T t1 = entityManager.merge(t);
            entityManager.getTransaction().commit();
            return t1;
        }
        catch (ConstraintViolationException e) {
            System.out.println("Constraint violation: " + e.getMessage());
            return null;
        }
    }

    /**
     * This method deletes an entity from the database using remove. This may cause a ConstraintViolationException if the entity is referenced by other entities.
     * @param t The entity to delete.
     */
    public void delete(T t) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.remove(entityManager.merge(t)); // Merge to ensure the entity is in the managed state
            entityManager.getTransaction().commit();
        }
        catch (ConstraintViolationException e) {
            System.out.println("Constraint violation: " + e.getMessage());
        }
    }

    /**
     * This method truncates the table of the entity type specified. This method should handle any foreign key constraints, but may cause a ConstraintViolationException if the cascade does not work.
     * NEVER use this method in important parts of the code. It exists primarily for testing purposes, yet is always exposed.
     */
    public void truncate() {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            entityManager.getTransaction().begin();

            String tableName = this.entityClass.getSimpleName();
            String sql = "TRUNCATE TABLE " + tableName + " RESTART IDENTITY CASCADE"; // CASCADE drops dependent objects

            entityManager.createNativeQuery(sql).executeUpdate();

            entityManager.getTransaction().commit();

            // Restart sequence if it exists
            try {
                entityManager.getTransaction().begin();

                sql = "ALTER SEQUENCE " + tableName + "_id_seq RESTART WITH 1";

                entityManager.createNativeQuery(sql).executeUpdate();

                entityManager.getTransaction().commit();
            }
            catch (Exception e) {
                System.out.println("Sequence does not exist: " + tableName + "_id_seq");
            }
        }
        catch (ConstraintViolationException e) {
            System.out.println("Constraint violation: " + e.getMessage());
        }
    }

    /**
     * This method closes the EntityManagerFactory. This is usually performed automatically when the program exits and should only be used in special cases.
     */
    public void close() {
        entityManagerFactory.close();
    }
}

/**
 * This is an interface for making a DAO (Data Access Object) that can be used to perform CRUD operations on any entity.
 * It is expected that the children of this interface will accept a Class<T> in their constructor.
 * @param <T>
 */
interface IDAO<T> {
    Class<T> getEntityClass();
    EntityManagerFactory getEntityManagerFactory();
    void setEntityManagerFactory(EntityManagerFactory emf);
    T findById(Object id);
    List<T> findAll();
    T create(T t);
    T merge(T t);
    void delete(T t);
    void truncate();
    void close();
}
