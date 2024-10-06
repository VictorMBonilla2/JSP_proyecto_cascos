package Controlador;

import Modelo.Tb_CiudadVehiculo;
import Utilidades.JPAUtils;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaQuery;
import org.hibernate.exception.ConstraintViolationException;

import java.io.Serializable;
import java.util.List;

public class CiudadVehiculosJPAController implements Serializable {

    private EntityManagerFactory fabricaEntidades;

    public CiudadVehiculosJPAController() {
        this.fabricaEntidades = JPAUtils.getEntityManagerFactory();
    }

    public EntityManager getEntityManager() {
        return fabricaEntidades.createEntityManager();
    }

    public void create(Tb_CiudadVehiculo ciudadVehiculo) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(ciudadVehiculo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tb_CiudadVehiculo ciudadVehiculo) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ciudadVehiculo = em.merge(ciudadVehiculo);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = ciudadVehiculo.getId();
                if (findCiudadVehiculo(id) == null) {
                    throw new Exception("El ciudadVehiculo con id " + id + " ya no existe.");
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
            Tb_CiudadVehiculo ciudadVehiculo;
            try {
                ciudadVehiculo = em.getReference(Tb_CiudadVehiculo.class, id);
                ciudadVehiculo.getId();  // Verificar que la ciudad existe
            } catch (EntityNotFoundException enfe) {
                throw new Exception("La ciudad con id " + id + " ya no existe.", enfe);
            }

            em.remove(ciudadVehiculo);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            // Verificar si la excepción es de tipo ConstraintViolationException
            if (e.getCause() instanceof ConstraintViolationException) {
                throw new PersistenceException("No se puede eliminar la ciudad porque está en uso.", e);
            }
            throw e;  // Propagar cualquier otra PersistenceException
        } catch (Exception e) {
            // Capturar cualquier otra excepción inesperada
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();  // Hacer rollback si ocurre un error
            }
            throw e;  // Propagar la excepción hacia la capa superior
        } finally {
            if (em != null) {
                em.close();  // Cerrar el EntityManager
            }
        }
    }


    public List<Tb_CiudadVehiculo> findCiudadVehiculoEntities() {
        return findCiudadVehiculoEntities(true, -1, -1);
    }

    public List<Tb_CiudadVehiculo> findCiudadVehiculoEntities(int maxResults, int firstResult) {
        return findCiudadVehiculoEntities(false, maxResults, firstResult);
    }

    public Tb_CiudadVehiculo findCiudadVehiculo(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tb_CiudadVehiculo.class, id);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    private List<Tb_CiudadVehiculo> findCiudadVehiculoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tb_CiudadVehiculo.class));
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
