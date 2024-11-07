package Controlador;

import Modelo.TbVehiculo;
import Modelo.enums.EstadoVehiculo;
import Utilidades.JPAUtils;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.io.Serializable;
import java.util.List;

/**
 * Controlador JPA para la entidad TbVehiculo. Proporciona operaciones CRUD
 * y métodos adicionales para la gestión de vehículos en la base de datos.
 */
public class VehiculoJPAController implements Serializable {

    private EntityManagerFactory fabricaEntidades;

    /**
     * Constructor que inicializa el EntityManagerFactory.
     */
    public VehiculoJPAController() {
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
     * Persiste un nuevo objeto TbVehiculo en la base de datos.
     *
     * @param vehiculo El objeto TbVehiculo a crear.
     */
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

    /**
     * Actualiza un objeto TbVehiculo existente en la base de datos.
     *
     * @param vehiculo El objeto TbVehiculo a editar.
     * @throws Exception Si ocurre un error o el TbVehiculo no existe.
     */
    public void edit(TbVehiculo vehiculo) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            vehiculo = em.merge(vehiculo);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.isEmpty()) {
                int id = vehiculo.getId_vehiculo();
                if (findTbVehiculo(id) == null) {
                    throw new Exception("El vehículo con ID " + id + " no existe.");
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
     * Elimina un TbVehiculo de la base de datos.
     *
     * @param id El ID del TbVehiculo a eliminar.
     * @throws Exception Si el TbVehiculo no existe o ocurre un error inesperado.
     */
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
                throw new Exception("El vehículo con ID " + id + " no existe.", enfe);
            }
            em.remove(vehiculo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Encuentra todas las entidades TbVehiculo.
     *
     * @return Lista de todas las entidades TbVehiculo.
     */
    public List<TbVehiculo> findTbVehiculoEntities() {
        return findTbVehiculoEntities(true, -1, -1);
    }

    /**
     * Encuentra un rango de entidades TbVehiculo.
     *
     * @param maxResults Número máximo de resultados a devolver.
     * @param firstResult Primer resultado a devolver.
     * @return Lista de TbVehiculo en el rango especificado.
     */
    public List<TbVehiculo> findTbVehiculoEntities(int maxResults, int firstResult) {
        return findTbVehiculoEntities(false, maxResults, firstResult);
    }

    /**
     * Encuentra un TbVehiculo por su ID.
     *
     * @param id ID del TbVehiculo a buscar.
     * @return TbVehiculo con el ID especificado o null si no se encuentra.
     */
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

    /**
     * Encuentra vehículos asociados a una persona específica mediante su documento.
     *
     * @param documentoPersona Documento de la persona para buscar sus vehículos.
     * @return Lista de vehículos asociados a la persona especificada.
     */
    public List<TbVehiculo> findVehiculosByPersona(int documentoPersona) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<TbVehiculo> query = em.createQuery(
                    "SELECT v FROM TbVehiculo v WHERE v.persona.id = :documento", TbVehiculo.class);
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

    /**
     * Encuentra un TbVehiculo por su placa.
     *
     * @param placa Placa del TbVehiculo a buscar.
     * @return TbVehiculo con la placa especificada o null si no se encuentra.
     */
    public TbVehiculo findVehiculoByPlaca(String placa) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<TbVehiculo> cq = cb.createQuery(TbVehiculo.class);

            Root<TbVehiculo> vehiculo = cq.from(TbVehiculo.class);
            cq.select(vehiculo).where(cb.equal(vehiculo.get("placaVehiculo"), placa));

            Query q = em.createQuery(cq);
            return (TbVehiculo) q.getSingleResult();
        } catch (NoResultException e) {
            System.err.println("No se encontró un vehículo con la placa: " + placa);
            return null;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    /**
     * Actualiza el estado de un vehículo.
     *
     * @param vehiculo El objeto TbVehiculo cuyo estado se desea actualizar.
     * @param nuevoEstado El nuevo estado a establecer para el vehículo.
     */
    public void actualizarEstado(TbVehiculo vehiculo, EstadoVehiculo nuevoEstado) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            vehiculo.setEstadoVehiculo(nuevoEstado);
            em.merge(vehiculo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Busca un vehículo en la tabla de espacios usando su ID.
     *
     * @param idVehiculo ID del vehículo a buscar en la tabla de espacios.
     * @return TbVehiculo asociado al espacio o null si no se encuentra.
     */
    public TbVehiculo buscarVehiculoEnEspacio(int idVehiculo) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<TbVehiculo> query = em.createQuery(
                    "SELECT e.vehiculo FROM TbEspacio e WHERE e.vehiculo.id_vehiculo = :idvehiculo",
                    TbVehiculo.class
            );
            query.setParameter("idvehiculo", idVehiculo);
            return query.getSingleResult();
        } catch (NoResultException e) {
            System.out.println("No se encontró un usuario en el espacio con el ID: " + idVehiculo);
            return null;
        } finally {
            em.close();
        }
    }
}
