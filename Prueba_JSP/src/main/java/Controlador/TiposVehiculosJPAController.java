package Controlador;



import Modelo.TbTipovehiculo;
import Utilidades.JPAUtils;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.servlet.annotation.WebServlet;
import org.hibernate.exception.ConstraintViolationException;

import java.io.Serializable;
import java.util.List;

@WebServlet
public class TiposVehiculosJPAController  implements Serializable {
    private EntityManagerFactory fabricaEntidades;

    public TiposVehiculosJPAController() {
        this.fabricaEntidades = JPAUtils.getEntityManagerFactory();
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

    public void destroy(int id) throws PersistenceException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TbTipovehiculo tipovehiculo;
            try {
                tipovehiculo = em.getReference(TbTipovehiculo.class, id);
                tipovehiculo.getId();  // Verificar que el tipo de vehículo existe
            } catch (EntityNotFoundException enfe) {
                throw new Exception("El tipo de vehículo con id " + id + " ya no existe.", enfe);
            }

            em.remove(tipovehiculo);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            // Verificar si la excepción es de tipo ConstraintViolationException
            if (e.getCause() instanceof ConstraintViolationException) {
                throw new PersistenceException("No se puede eliminar el tipo de vehículo porque está en uso.", e);
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
