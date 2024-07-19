package Controlador;

import Modelo.TbVehiculo;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaQuery;

import java.io.Serializable;
import java.util.List;

public class CascosJPAController implements Serializable {

    public CascosJPAController() {
        fabricaEntidades = Persistence.createEntityManagerFactory("default");
    }
    private EntityManagerFactory fabricaEntidades= null;

    public EntityManager getEntityManager() {
        return fabricaEntidades.createEntityManager();
    }

    public void create(TbVehiculo casco) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(casco);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TbVehiculo casco) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            casco = em.merge(casco);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.isEmpty()) {
                int id = casco.getId_vehiculo();
                if (findTbCasco(id) == null) {
                    throw new Exception("The espacio with id " + id + " no longer exists.");
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
                throw new Exception("The espacio with id " + id + " no longer exists.", enfe);
            }
            em.remove(vehiculo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TbVehiculo> findTbCascoEntities() {
        return findTbCascoEntities(true, -1, -1);
    }

    public List<TbVehiculo> findTbCascoEntities(int maxResults, int firstResult) {
        return findTbCascoEntities(false, maxResults, firstResult);
    }

    private List<TbVehiculo> findTbCascoEntities(boolean all, int maxResults, int firstResult) {
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

    public TbVehiculo findTbCasco(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TbVehiculo.class, id);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    public TbVehiculo buscarCascoPorPlaca(String placa) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<TbVehiculo> query = em.createQuery("SELECT c FROM TbVehiculo c WHERE c.placa_vehiculo = :placa", TbVehiculo.class);
            query.setParameter("placa", placa);
            List<TbVehiculo> results = query.getResultList();
            return results.isEmpty() ? null : results.get(0);
        } finally {
            em.close();
        }
    }

}
