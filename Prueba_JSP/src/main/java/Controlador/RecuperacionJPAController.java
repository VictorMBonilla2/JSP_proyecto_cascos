package Controlador;

import Modelo.TbRecuperacion;
import Utilidades.JPAUtils;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class RecuperacionJPAController implements Serializable {

    private EntityManagerFactory fabricaEntidades;

    public RecuperacionJPAController() {
        this.fabricaEntidades = JPAUtils.getEntityManagerFactory();
    }

    public EntityManager getEntityManager() {
        return fabricaEntidades.createEntityManager();
    }

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

    public void edit(TbRecuperacion recuperacion) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            recuperacion = em.merge(recuperacion);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
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

    public void destroy(Long id) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TbRecuperacion recuperacion;
            try {
                recuperacion = em.getReference(TbRecuperacion.class, id);
                recuperacion.getId();  // Verificar si existe
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

    public TbRecuperacion buscarRecuperacionToken(String token) {
        EntityManager em = getEntityManager();
        try {
            // Crear la consulta para buscar el token
            TypedQuery<TbRecuperacion> query = em.createQuery("SELECT r FROM TbRecuperacion r WHERE r.tokenRecuperacion = :token", TbRecuperacion.class);
            query.setParameter("token", token);

            return query.getSingleResult(); // Se asume que el token es único y siempre existe uno
        } catch (NoResultException e) {
            // Retorna null si no se encuentra ningún resultado
            return null;
        } finally {
            em.close();
        }
    }


    public List<TbRecuperacion> findRecuperacionActivas() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<TbRecuperacion> query = em.createQuery(
                    "SELECT r FROM TbRecuperacion r WHERE r.tokenUsado = false AND r.fechaExpiracionToken > :now",
                    TbRecuperacion.class
            );
            query.setParameter("now", new Date()); // Solo los tokens que no han expirado
            return query.getResultList();
        }catch (NoResultException e){
            return null;
        }
        finally {
            em.close();
        }
    }
}
