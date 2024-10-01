package Controlador;

import Modelo.TbTipovehiculo;
import Modelo.Tb_MarcaVehiculo;
import Utilidades.JPAUtils;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;

import java.io.Serializable;
import java.util.List;

public class MarcaVehiculoJPAController implements Serializable {

    private EntityManagerFactory fabricaEntidades;

    public MarcaVehiculoJPAController() {
        this.fabricaEntidades = JPAUtils.getEntityManagerFactory();
    }

    public EntityManager getEntityManager() {
        return fabricaEntidades.createEntityManager();
    }

    public void create(Tb_MarcaVehiculo marcaVehiculo) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(marcaVehiculo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tb_MarcaVehiculo marcaVehiculo) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            marcaVehiculo = em.merge(marcaVehiculo);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = marcaVehiculo.getId();
                if (findMarcaVehiculo(id) == null) {
                    throw new Exception("La marcaVehiculo con id " + id + " ya no existe.");
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
            Tb_MarcaVehiculo marcaVehiculo;
            try {
                marcaVehiculo = em.getReference(Tb_MarcaVehiculo.class, id);
                marcaVehiculo.getId();
            } catch (EntityNotFoundException enfe) {
                throw new Exception("La marcaVehiculo con id " + id + " ya no existe.", enfe);
            }
            em.remove(marcaVehiculo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tb_MarcaVehiculo> findMarcaVehiculoEntities() {
        return findMarcaVehiculoEntities(true, -1, -1);
    }

    public List<Tb_MarcaVehiculo> findMarcaVehiculoEntities(int maxResults, int firstResult) {
        return findMarcaVehiculoEntities(false, maxResults, firstResult);
    }

    public Tb_MarcaVehiculo findMarcaVehiculo(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tb_MarcaVehiculo.class, id);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    private List<Tb_MarcaVehiculo> findMarcaVehiculoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tb_MarcaVehiculo.class));
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

    public Tb_MarcaVehiculo findMarcaEntitiesForType(int marcaVehiculoId, int tipoVehiculoId) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Tb_MarcaVehiculo> cq = cb.createQuery(Tb_MarcaVehiculo.class);
            Root<Tb_MarcaVehiculo> marcaVehiculoRoot = cq.from(Tb_MarcaVehiculo.class);
            Join<Tb_MarcaVehiculo, TbTipovehiculo> tipoVehiculoJoin = marcaVehiculoRoot.join("tipoVehiculo");

            // Definir los criterios de búsqueda
            cq.select(marcaVehiculoRoot)
                    .where(cb.equal(marcaVehiculoRoot.get("id"), marcaVehiculoId),
                            cb.equal(tipoVehiculoJoin.get("id"), tipoVehiculoId));

            // Crear y ejecutar la consulta
            TypedQuery<Tb_MarcaVehiculo> query = em.createQuery(cq);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;  // Manejar el caso donde no se encuentra el resultado
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tb_MarcaVehiculo> findMarcasForType(int idTipoVehiculo) {
        EntityManager em = getEntityManager();
        try {
            // Consulta JPQL para obtener marcas según el tipo de vehículo
            String jpql = "SELECT m FROM Tb_MarcaVehiculo m WHERE m.tipoVehiculo.id = :idTipoVehiculo";
            TypedQuery<Tb_MarcaVehiculo> query = em.createQuery(jpql, Tb_MarcaVehiculo.class);
            query.setParameter("idTipoVehiculo", idTipoVehiculo);

            // Retornar la lista de marcas
            return query.getResultList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /*
    * public Tb_MarcaVehiculo buscarMarcaPorTipo(int marcaVehiculo, int tipoVehiculo) {
    EntityManager em = getEntityManager();
    try {
        String jpql = "SELECT m FROM Tb_MarcaVehiculo m WHERE m.id = :marcaId AND m.tipoVehiculo.id = :tipoId";
        Query query = em.createQuery(jpql);
        query.setParameter("marcaId", marcaVehiculo);
        query.setParameter("tipoId", tipoVehiculo);

        return (Tb_MarcaVehiculo) query.getSingleResult();
    } catch (NoResultException e) {
        return null;
    } finally {
        if (em != null) {
            em.close();
        }
    }
}
    * */

}
