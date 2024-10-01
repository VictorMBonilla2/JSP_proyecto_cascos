package Controlador;

import Modelo.TbTipovehiculo;
import Modelo.Tb_MarcaVehiculo;
import Modelo.Tb_ModeloVehiculo;
import Utilidades.JPAUtils;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;

import java.io.Serializable;
import java.util.List;

public class ModeloVehiculoJPAController implements Serializable {

    private EntityManagerFactory fabricaEntidades;

    public ModeloVehiculoJPAController() {
        this.fabricaEntidades = JPAUtils.getEntityManagerFactory();
    }

    public EntityManager getEntityManager() {
        return fabricaEntidades.createEntityManager();
    }

    public void create(Tb_ModeloVehiculo modeloVehiculo) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(modeloVehiculo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tb_ModeloVehiculo modeloVehiculo) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            modeloVehiculo = em.merge(modeloVehiculo);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = modeloVehiculo.getId();
                if (findModeloVehiculo(id) == null) {
                    throw new Exception("El modeloVehiculo con id " + id + " ya no existe.");
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
            Tb_ModeloVehiculo modeloVehiculo;
            try {
                modeloVehiculo = em.getReference(Tb_ModeloVehiculo.class, id);
                modeloVehiculo.getId();
            } catch (EntityNotFoundException enfe) {
                throw new Exception("El modeloVehiculo con id " + id + " ya no existe.", enfe);
            }
            em.remove(modeloVehiculo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tb_ModeloVehiculo> findModeloVehiculoEntities() {
        return findModeloVehiculoEntities(true, -1, -1);
    }

    public List<Tb_ModeloVehiculo> findModeloVehiculoEntities(int maxResults, int firstResult) {
        return findModeloVehiculoEntities(false, maxResults, firstResult);
    }

    public Tb_ModeloVehiculo findModeloVehiculo(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tb_ModeloVehiculo.class, id);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    private List<Tb_ModeloVehiculo> findModeloVehiculoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tb_ModeloVehiculo.class));
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

    public Tb_ModeloVehiculo findModeloEntitiesForTypeAndBrand(int modeloVehiculoId, int marcaVehiculoId, int tipoVehiculoId) {
        EntityManager em = getEntityManager();
        try {
            // Usar CriteriaBuilder para construir la consulta
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Tb_ModeloVehiculo> cq = cb.createQuery(Tb_ModeloVehiculo.class);

            // Definir el "root" de la consulta desde la entidad Tb_ModeloVehiculo
            Root<Tb_ModeloVehiculo> modeloVehiculo = cq.from(Tb_ModeloVehiculo.class);

            // Hacer el join con la tabla MarcaVehiculo y TipoVehiculo
            Join<Tb_ModeloVehiculo, Tb_MarcaVehiculo> marcaVehiculo = modeloVehiculo.join("marcaVehiculo");
            Join<Tb_MarcaVehiculo, TbTipovehiculo> tipoVehiculo = marcaVehiculo.join("tipoVehiculo");

            // Construir las condiciones de búsqueda con CriteriaBuilder
            cq.select(modeloVehiculo)
                    .where(
                            cb.equal(modeloVehiculo.get("id"), modeloVehiculoId),
                            cb.equal(marcaVehiculo.get("id"), marcaVehiculoId),
                            cb.equal(tipoVehiculo.get("id"), tipoVehiculoId)
                    );

            // Ejecutar la consulta
            TypedQuery<Tb_ModeloVehiculo> query = em.createQuery(cq);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null; // Si no se encuentra el resultado
        } finally {
            em.close();
        }
    }

    public List<Tb_ModeloVehiculo> findModelosPorMarcaYTipo(int idMarcaVehiculo, int idTipoVehiculo) {
        EntityManager em = getEntityManager();
        try {
            // Consulta JPQL para obtener modelos según la marca y el tipo de vehículo
            String jpql = "SELECT mo FROM Tb_ModeloVehiculo mo WHERE mo.marcaVehiculo.id = :idMarcaVehiculo AND mo.marcaVehiculo.tipoVehiculo.id = :idTipoVehiculo";
            TypedQuery<Tb_ModeloVehiculo> query = em.createQuery(jpql, Tb_ModeloVehiculo.class);
            query.setParameter("idMarcaVehiculo", idMarcaVehiculo);
            query.setParameter("idTipoVehiculo", idTipoVehiculo);

            // Retornar la lista de modelos
            return query.getResultList();
        } finally {
            if (em != null) {
                em.close();
            }
        }

    }
}
