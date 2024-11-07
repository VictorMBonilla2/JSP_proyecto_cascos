package Controlador;

import Modelo.TbTipovehiculo;
import Modelo.Tb_MarcaVehiculo;
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
 * Controlador JPA para la entidad Tb_MarcaVehiculo. Proporciona operaciones CRUD
 * y métodos para la gestión de objetos Tb_MarcaVehiculo en la base de datos.
 */
public class MarcaVehiculoJPAController implements Serializable {

    private EntityManagerFactory fabricaEntidades;

    /**
     * Constructor que inicializa el EntityManagerFactory.
     */
    public MarcaVehiculoJPAController() {
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
     * Crea y persiste un nuevo Tb_MarcaVehiculo en la base de datos.
     *
     * @param marcaVehiculo El objeto Tb_MarcaVehiculo que se desea crear.
     */
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

    /**
     * Edita un Tb_MarcaVehiculo existente en la base de datos.
     *
     * @param marcaVehiculo El objeto Tb_MarcaVehiculo que se desea editar.
     * @throws Exception si ocurre un error al editar o si el Tb_MarcaVehiculo no existe.
     */
    public void edit(Tb_MarcaVehiculo marcaVehiculo) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            marcaVehiculo = em.merge(marcaVehiculo);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.isEmpty()) {
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

    /**
     * Elimina un Tb_MarcaVehiculo de la base de datos.
     *
     * @param id El ID del Tb_MarcaVehiculo a eliminar.
     * @throws PersistenceException si el Tb_MarcaVehiculo está en uso y no se puede eliminar.
     * @throws Exception si el Tb_MarcaVehiculo no existe o si ocurre cualquier otro error inesperado.
     */
    public void destroy(int id) throws PersistenceException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();

            Tb_MarcaVehiculo marcaVehiculo;
            try {
                marcaVehiculo = em.getReference(Tb_MarcaVehiculo.class, id);
                marcaVehiculo.getId();  // Verificar que la marca existe
            } catch (EntityNotFoundException enfe) {
                throw new Exception("La marca con id " + id + " ya no existe.", enfe);
            }

            em.remove(marcaVehiculo);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw new PersistenceException("No se puede eliminar la marca porque está en uso.", e);
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
     * Encuentra un Tb_MarcaVehiculo por su ID.
     *
     * @param id ID del Tb_MarcaVehiculo a buscar.
     * @return Tb_MarcaVehiculo con el ID especificado o null si no se encuentra.
     */
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

    /**
     * Encuentra un Tb_MarcaVehiculo por su ID de marca y tipo de vehículo.
     *
     * @param marcaVehiculoId ID de la marca del vehículo.
     * @param tipoVehiculoId ID del tipo de vehículo.
     * @return Tb_MarcaVehiculo correspondiente a los criterios o null si no se encuentra.
     */
    public Tb_MarcaVehiculo findMarcaEntitiesForType(int marcaVehiculoId, int tipoVehiculoId) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Tb_MarcaVehiculo> cq = cb.createQuery(Tb_MarcaVehiculo.class);
            Root<Tb_MarcaVehiculo> marcaVehiculoRoot = cq.from(Tb_MarcaVehiculo.class);
            Join<Tb_MarcaVehiculo, TbTipovehiculo> tipoVehiculoJoin = marcaVehiculoRoot.join("tipoVehiculo");

            cq.select(marcaVehiculoRoot)
                    .where(cb.equal(marcaVehiculoRoot.get("id"), marcaVehiculoId),
                            cb.equal(tipoVehiculoJoin.get("id"), tipoVehiculoId));

            TypedQuery<Tb_MarcaVehiculo> query = em.createQuery(cq);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Encuentra una lista de marcas para un tipo de vehículo específico.
     *
     * @param idTipoVehiculo ID del tipo de vehículo para buscar marcas asociadas.
     * @return Lista de Tb_MarcaVehiculo correspondientes al tipo de vehículo especificado.
     */
    public List<Tb_MarcaVehiculo> findMarcasForType(int idTipoVehiculo) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT m FROM Tb_MarcaVehiculo m WHERE m.tipoVehiculo.id = :idTipoVehiculo";
            TypedQuery<Tb_MarcaVehiculo> query = em.createQuery(jpql, Tb_MarcaVehiculo.class);
            query.setParameter("idTipoVehiculo", idTipoVehiculo);

            return query.getResultList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}

