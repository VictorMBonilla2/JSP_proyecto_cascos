package Controlador;

import Modelo.Roles;
import Utilidades.JPAUtils;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaQuery;
import org.hibernate.exception.ConstraintViolationException;

import java.io.Serializable;
import java.util.List;

public class RolesJPAController implements Serializable {

    private EntityManagerFactory fabricaEntidades;

    public RolesJPAController() {
        this.fabricaEntidades = JPAUtils.getEntityManagerFactory();
    }

    public EntityManager getEntityManager() {
        return fabricaEntidades.createEntityManager();
    }

    public void create(Roles rol) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(rol);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Roles rol) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            rol = em.merge(rol);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = rol.getId();
                if (findRol(id) == null) {
                    throw new Exception("El rol con id " + id + " ya no existe.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(int id) throws PersistenceException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Roles rol;
            try {
                rol = em.getReference(Roles.class, id);
                rol.getId(); // Verificar que el rol exista
            } catch (EntityNotFoundException enfe) {
                throw new Exception("El rol con id " + id + " ya no existe.", enfe);
            }

            em.remove(rol);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            // Verificamos si la excepción es de tipo ConstraintViolationException
            if (e.getCause() instanceof ConstraintViolationException) {
                throw new PersistenceException("No se puede eliminar el rol porque está en uso.", e);
            }
            throw e; // Propagar cualquier otra PersistenceException
        } catch (Exception e) {
            // Capturar cualquier otra excepción inesperada
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback(); // Hacer rollback si ocurre un error
            }
            throw e; // Propagar la excepción
        } finally {
            if (em != null) {
                em.close(); // Cerrar el EntityManager
            }
        }
    }


    public List<Roles> findRolesEntities() {
        return findRolesEntities(true, -1, -1);
    }

    public List<Roles> findRolesEntities(int maxResults, int firstResult) {
        return findRolesEntities(false, maxResults, firstResult);
    }

    public Roles findRol(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Roles.class, id);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    private List<Roles> findRolesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Roles.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}
