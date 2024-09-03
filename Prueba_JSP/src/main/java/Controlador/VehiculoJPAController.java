package Controlador;

import Modelo.TbVehiculo;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaQuery;

import java.io.Serializable;
import java.util.List;

public class VehiculoJPAController implements Serializable {


    public VehiculoJPAController() {
        fabricaEntidades = Persistence.createEntityManagerFactory("default");
    }

    private EntityManagerFactory fabricaEntidades= null;

    public EntityManager getEntityManager() {
        return fabricaEntidades.createEntityManager();
    }

    public void create(TbVehiculo vehiculo) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(vehiculo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TbVehiculo vehiculo) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            vehiculo = em.merge(vehiculo);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = vehiculo.getId_vehiculo();
                if (findTbVehiculo(id) == null) {
                    throw new Exception("The vehiculo with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(int id) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TbVehiculo vehiculo;
            try {
                vehiculo = em.getReference(TbVehiculo.class, id);
                vehiculo.getId_vehiculo();
            } catch (EntityNotFoundException enfe) {
                throw new Exception("The vehiculo with id " + id + " no longer exists.", enfe);
            }
            em.remove(vehiculo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    public List<TbVehiculo> findTbVehiculoEntities() {
        return findTbVehiculoEntities(true, -1, -1);
    }

    public List<TbVehiculo> findTbVehiculoEntities(int maxResults, int firstResult) {
        return findTbVehiculoEntities(false, maxResults, firstResult);
    }
    public TbVehiculo findTbVehiculo(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TbVehiculo.class, id);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TbVehiculo> findVehiculosByPersona(int documentoPersona) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<TbVehiculo> query = em.createQuery(
                    "SELECT v FROM TbVehiculo v WHERE v.persona.documento = :documento", TbVehiculo.class);
            query.setParameter("documento", documentoPersona);
            return query.getResultList();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    private List<TbVehiculo> findTbVehiculoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TbVehiculo.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

}
