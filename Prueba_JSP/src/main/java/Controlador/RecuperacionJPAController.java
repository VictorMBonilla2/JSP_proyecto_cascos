package Controlador;

import Modelo.TbRecuperacion;
import Utilidades.JPAUtils;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Controlador JPA para la entidad TbRecuperacion. Proporciona operaciones CRUD
 * y métodos para la gestión de objetos TbRecuperacion en la base de datos.
 */
public class RecuperacionJPAController implements Serializable {

    private EntityManagerFactory fabricaEntidades;

    /**
     * Constructor que inicializa el EntityManagerFactory.
     */
    public RecuperacionJPAController() {
        this.fabricaEntidades = JPAUtils.getEntityManagerFactory();
    }

    /**
     * Obtiene una instancia de EntityManager.
     *
     * @return EntityManager creado a partir de la fábrica de entidades.
     */
    public EntityManager getEntityManager() {
        return fabricaEntidades.createEntityManager();
    }

    /**
     * Crea y persiste un nuevo TbRecuperacion en la base de datos.
     *
     * @param recuperacion El objeto TbRecuperacion que se desea crear.
     * @throws Exception si ocurre un error durante la persistencia.
     */
    public void create(TbRecuperacion recuperacion) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(recuperacion);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error de persistencia: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al persistir la entidad", e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Edita un TbRecuperacion existente en la base de datos.
     *
     * @param recuperacion El objeto TbRecuperacion que se desea editar.
     * @throws Exception si ocurre un error al editar o si el TbRecuperacion no existe.
     */
    public void edit(TbRecuperacion recuperacion) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            recuperacion = em.merge(recuperacion);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.isEmpty()) {
                Long id = recuperacion.getId();
                if (findRecuperacion(id) == null) {
                    throw new Exception("La recuperación con id " + id + " ya no existe.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Elimina un TbRecuperacion de la base de datos.
     *
     * @param id El ID del TbRecuperacion a eliminar.
     * @throws Exception si el TbRecuperacion no existe o si ocurre un error durante la eliminación.
     */
    public void destroy(Long id) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TbRecuperacion recuperacion;
            try {
                recuperacion = em.getReference(TbRecuperacion.class, id);
                recuperacion.getId();
            } catch (EntityNotFoundException enfe) {
                throw new Exception("La recuperación con id " + id + " ya no existe.", enfe);
            }
            em.remove(recuperacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Busca un TbRecuperacion en la base de datos utilizando su ID.
     *
     * @param id ID del TbRecuperacion a buscar.
     * @return TbRecuperacion con el ID especificado o null si no se encuentra.
     */
    public TbRecuperacion findRecuperacion(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TbRecuperacion.class, id);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    /**
     * Busca un TbRecuperacion en la base de datos utilizando un token de recuperación.
     *
     * @param token Token de recuperación para buscar el TbRecuperacion.
     * @return TbRecuperacion con el token especificado o null si no se encuentra.
     */
    public TbRecuperacion buscarRecuperacionToken(String token) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<TbRecuperacion> query = em.createQuery(
                    "SELECT r FROM TbRecuperacion r WHERE r.tokenRecuperacion = :token",
                    TbRecuperacion.class
            );
            query.setParameter("token", token);

            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    /**
     * Obtiene una lista de TbRecuperacion que están activas (no expiradas y no usadas).
     *
     * @return Lista de TbRecuperacion activas, o null si no se encuentran resultados.
     */
    public List<TbRecuperacion> findRecuperacionActivas() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<TbRecuperacion> query = em.createQuery(
                    "SELECT r FROM TbRecuperacion r WHERE r.tokenUsado = false AND r.fechaExpiracionToken > :now",
                    TbRecuperacion.class
            );
            query.setParameter("now", new Date());
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
}
