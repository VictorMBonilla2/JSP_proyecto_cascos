package Controlador;



import Modelo.TbTipovehiculo;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.servlet.annotation.WebServlet;

import java.io.Serializable;
import java.util.List;

@WebServlet
public class TiposVehiculosJPAController  implements Serializable {
    private EntityManagerFactory fabricaEntidades = null;
    public TiposVehiculosJPAController(){
        fabricaEntidades = Persistence.createEntityManagerFactory("default");
    }
    public EntityManager getEntityManager() {
        return fabricaEntidades.createEntityManager();
    }

    public void create(TbTipovehiculo tipovehiculo) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(tipovehiculo);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            // Maneja la excepción de persistencia, por ejemplo, registrando el error
            System.err.println("Error de persistencia: " + e.getMessage());
            e.printStackTrace();
            // Lanza una excepción personalizada si es necesario
            throw new RuntimeException("Error al persistir la entidad", e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TbTipovehiculo tipovehiculo) throws  Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            tipovehiculo = em.merge(tipovehiculo);
            em.getTransaction().commit();
        }catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = tipovehiculo.getId();
                if(findtipovehiculo(id)==null){
                    throw new Exception("The persona with id "+"no longer exists.");
                }
            }
            throw ex;
        }finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(int id) throws Exception {
        EntityManager em = null;
        try{
            em = getEntityManager();
            em.getTransaction().begin();
            TbTipovehiculo tipovehiculo;
            try {
                tipovehiculo = em.getReference(TbTipovehiculo.class, id);
                tipovehiculo.getId();
            } catch (EntityNotFoundException enfe) {
                throw new Exception("The persona with id "+id+" no longer exists.", enfe);
            }
            em.remove(tipovehiculo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TbTipovehiculo> findtipovehiculoEntities() {
        return findtipovehiculoEntities(true, -1,-1);
    }

    public List<TbTipovehiculo> findtipovehiculoEntities(int maxResults, int firstResult) {
        return findtipovehiculoEntities(false,maxResults,firstResult);
    }
    private List<TbTipovehiculo> findtipovehiculoEntities(boolean all, int maxResults, int firstResult){
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TbTipovehiculo.class));
            Query q = em.createQuery(cq);
            if(!all){
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            if(em != null && em.isOpen() ){
                em.close();
            }
        }
    }

    public TbTipovehiculo findtipovehiculo(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TbTipovehiculo.class, id);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

}
