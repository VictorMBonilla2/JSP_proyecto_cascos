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
import org.hibernate.exception.ConstraintViolationException;

import java.io.Serializable;
import java.util.List;

/**
 * Controlador JPA para la entidad Tb_ModeloVehiculo. Proporciona operaciones CRUD
 * y métodos para la gestión de objetos Tb_ModeloVehiculo en la base de datos.
 */
public class ModeloVehiculoJPAController implements Serializable {

    private EntityManagerFactory fabricaEntidades;

    /**
     * Constructor que inicializa el EntityManagerFactory.
     */
    public ModeloVehiculoJPAController() {
        this.fabricaEntidades = JPAUtils.getEntityManagerFactory();
    }

    /**
     * Obtiene una instancia de EntityManager.
     *
     * @return EntityManager creado a partir de la fábrica de entidades.
     */
    public EntityManager getEntityManager() {
        return fabricaEntidades.createEntityManager();
    }

    /**
     * Crea y persiste un nuevo Tb_ModeloVehiculo en la base de datos.
     *
     * @param modeloVehiculo El objeto Tb_ModeloVehiculo que se desea crear.
     */
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

    /**
     * Edita un Tb_ModeloVehiculo existente en la base de datos.
     *
     * @param modeloVehiculo El objeto Tb_ModeloVehiculo que se desea editar.
     * @throws Exception si ocurre un error al editar o si el Tb_ModeloVehiculo no existe.
     */
    public void edit(Tb_ModeloVehiculo modeloVehiculo) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            modeloVehiculo = em.merge(modeloVehiculo);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.isEmpty()) {
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

    /**
     * Elimina un Tb_ModeloVehiculo de la base de datos.
     *
     * @param id El ID del Tb_ModeloVehiculo a eliminar.
     * @throws PersistenceException si el Tb_ModeloVehiculo está en uso y no se puede eliminar.
     * @throws Exception si el Tb_ModeloVehiculo no existe o si ocurre cualquier otro error inesperado.
     */
    public void destroy(int id) throws PersistenceException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tb_ModeloVehiculo modeloVehiculo;
            try {
                modeloVehiculo = em.getReference(Tb_ModeloVehiculo.class, id);
                modeloVehiculo.getId();  // Verificar que el modelo existe
            } catch (EntityNotFoundException enfe) {
                throw new Exception("El modelo de vehículo con id " + id + " ya no existe.", enfe);
            }
            em.remove(modeloVehiculo);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw new PersistenceException("No se puede eliminar el modelo porque está en uso.", e);
            }
            throw e;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Obtiene una lista de todos los Tb_ModeloVehiculo.
     *
     * @return Lista de todos los Tb_ModeloVehiculo.
     */
    public List<Tb_ModeloVehiculo> findModeloVehiculoEntities() {
        return findModeloVehiculoEntities(true, -1, -1);
    }

    /**
     * Obtiene una lista de Tb_ModeloVehiculo con límites de resultados.
     *
     * @param maxResults Número máximo de resultados a devolver.
     * @param firstResult Primer resultado a devolver.
     * @return Lista de Tb_ModeloVehiculo en el rango especificado.
     */
    public List<Tb_ModeloVehiculo> findModeloVehiculoEntities(int maxResults, int firstResult) {
        return findModeloVehiculoEntities(false, maxResults, firstResult);
    }

    /**
     * Encuentra un Tb_ModeloVehiculo por su ID.
     *
     * @param id ID del Tb_ModeloVehiculo a buscar.
     * @return Tb_ModeloVehiculo con el ID especificado o null si no se encuentra.
     */
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

    /**
     * Método privado para obtener Tb_ModeloVehiculo con límites de resultados.
     *
     * @param all Si es true, devuelve todos los resultados.
     * @param maxResults Número máximo de resultados a devolver.
     * @param firstResult Primer resultado a devolver.
     * @return Lista de Tb_ModeloVehiculo en el rango especificado o todos si all es true.
     */
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

    /**
     * Encuentra un Tb_ModeloVehiculo por su ID, ID de marca, y ID de tipo de vehículo.
     *
     * @param modeloVehiculoId ID del modelo de vehículo.
     * @param marcaVehiculoId ID de la marca del vehículo.
     * @param tipoVehiculoId ID del tipo de vehículo.
     * @return Tb_ModeloVehiculo correspondiente a los criterios o null si no se encuentra.
     */
    public Tb_ModeloVehiculo findModeloEntitiesForTypeAndBrand(int modeloVehiculoId, int marcaVehiculoId, int tipoVehiculoId) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Tb_ModeloVehiculo> cq = cb.createQuery(Tb_ModeloVehiculo.class);
            Root<Tb_ModeloVehiculo> modeloVehiculo = cq.from(Tb_ModeloVehiculo.class);

            Join<Tb_ModeloVehiculo, Tb_MarcaVehiculo> marcaVehiculo = modeloVehiculo.join("marcaVehiculo");
            Join<Tb_MarcaVehiculo, TbTipovehiculo> tipoVehiculo = marcaVehiculo.join("tipoVehiculo");

            cq.select(modeloVehiculo)
                    .where(
                            cb.equal(modeloVehiculo.get("id"), modeloVehiculoId),
                            cb.equal(marcaVehiculo.get("id"), marcaVehiculoId),
                            cb.equal(tipoVehiculo.get("id"), tipoVehiculoId)
                    );

            TypedQuery<Tb_ModeloVehiculo> query = em.createQuery(cq);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    /**
     * Encuentra una lista de modelos para una marca y tipo de vehículo específicos.
     *
     * @param idMarcaVehiculo ID de la marca del vehículo.
     * @param idTipoVehiculo ID del tipo de vehículo.
     * @return Lista de Tb_ModeloVehiculo correspondientes a la marca y tipo de vehículo especificados.
     */
    public List<Tb_ModeloVehiculo> findModelosPorMarcaYTipo(int idMarcaVehiculo, int idTipoVehiculo) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT mo FROM Tb_ModeloVehiculo mo WHERE mo.marcaVehiculo.id = :idMarcaVehiculo AND mo.marcaVehiculo.tipoVehiculo.id = :idTipoVehiculo";
            TypedQuery<Tb_ModeloVehiculo> query = em.createQuery(jpql, Tb_ModeloVehiculo.class);
            query.setParameter("idMarcaVehiculo", idMarcaVehiculo);
            query.setParameter("idTipoVehiculo", idTipoVehiculo);

            return query.getResultList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}

