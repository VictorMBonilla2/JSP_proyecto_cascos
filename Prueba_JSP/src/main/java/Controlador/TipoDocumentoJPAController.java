package Controlador;

import Modelo.TbTipoDocumento;
import Modelo.TbVehiculo;
import Utilidades.JPAUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaQuery;

import java.io.Serializable;
import java.util.List;

public class TipoDocumentoJPAController implements Serializable {

    private EntityManagerFactory fabricaEntidades;
    public TipoDocumentoJPAController() {
        this.fabricaEntidades = JPAUtils.getEntityManagerFactory();
    }

    public EntityManager getEntityManager() {
        return fabricaEntidades.createEntityManager();
    }

    public void create (TbTipoDocumento tipoDocumento){
        EntityManager em= null;
        try{
            em= getEntityManager();
            em.getTransaction().begin();
            em.persist(tipoDocumento);
            em.getTransaction().commit();
        }finally {
            if(em != null){
                em.close();
            }
        }
    }
    public void edit(TbTipoDocumento tipoDocumento) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            tipoDocumento = em.merge(tipoDocumento);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.isEmpty()) {
                int id = tipoDocumento.getId();
                if (findTbTipoDocumento(id) == null) {
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
            TbTipoDocumento tipoDocumento;
            try {
                tipoDocumento = em.getReference(TbTipoDocumento.class, id);
                tipoDocumento.getId();
            } catch (EntityNotFoundException enfe) {
                throw new Exception("The espacio with id " + id + " no longer exists.", enfe);
            }
            em.remove(tipoDocumento);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }


    public TbTipoDocumento findTbTipoDocumento(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TbTipoDocumento.class, id);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TbTipoDocumento> findTbTipoDocumentoEntities() {
        return findTbTipoDocumentoEntities(true, -1, -1);
    }

    public List<TbTipoDocumento> findTbTipoDocumentoEntities(int maxResults, int firstResult) {
        return findTbTipoDocumentoEntities(false, maxResults, firstResult);
    }

    private List<TbTipoDocumento> findTbTipoDocumentoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TbTipoDocumento.class));
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
