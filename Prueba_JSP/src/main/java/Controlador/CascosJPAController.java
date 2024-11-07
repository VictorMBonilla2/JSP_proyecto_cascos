package Controlador;

import Modelo.TbVehiculo;
import Utilidades.JPAUtils;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaQuery;

import java.io.Serializable;
import java.util.List;

/**
 * Controlador JPA para la entidad TbVehiculo. Proporciona operaciones CRUD
 * y métodos para la gestión de objetos TbVehiculo en la base de datos.
 */
public class CascosJPAController implements Serializable {

    private EntityManagerFactory fabricaEntidades;

    /**
     * Constructor que inicializa el EntityManagerFactory.
     */
    public CascosJPAController() {
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
     * Crea y persiste un nuevo TbVehiculo en la base de datos.
     *
     * @param casco El objeto TbVehiculo que se desea crear.
     */
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

    /**
     * Edita un TbVehiculo existente en la base de datos.
     *
     * @param casco El objeto TbVehiculo que se desea editar.
     * @throws Exception si ocurre un error al editar o si el TbVehiculo no existe.
     */
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
                    throw new Exception("El espacio con id " + id + " ya no existe.");
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
     * @throws Exception si el TbVehiculo no existe.
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
                throw new Exception("El espacio con id " + id + " ya no existe.", enfe);
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
     * Obtiene una lista de todos los TbVehiculo.
     *
     * @return Lista de todos los TbVehiculo.
     */
    public List<TbVehiculo> findTbCascoEntities() {
        return findTbCascoEntities(true, -1, -1);
    }

    /**
     * Obtiene una lista de TbVehiculo con límites de resultados.
     *
     * @param maxResults Número máximo de resultados a devolver.
     * @param firstResult Primer resultado a devolver.
     * @return Lista de TbVehiculo en el rango especificado.
     */
    public List<TbVehiculo> findTbCascoEntities(int maxResults, int firstResult) {
        return findTbCascoEntities(false, maxResults, firstResult);
    }

    /**
     * Método privado para obtener TbVehiculo con límites de resultados.
     *
     * @param all Si es true, devuelve todos los resultados.
     * @param maxResults Número máximo de resultados a devolver.
     * @param firstResult Primer resultado a devolver.
     * @return Lista de TbVehiculo en el rango especificado o todos si all es true.
     */
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

    /**
     * Encuentra un TbVehiculo por su ID.
     *
     * @param id ID del TbVehiculo a buscar.
     * @return TbVehiculo con el ID especificado o null si no se encuentra.
     */
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

    /**
     * Busca un TbVehiculo por su placa.
     *
     * @param placa La placa del TbVehiculo a buscar.
     * @return TbVehiculo con la placa especificada o null si no se encuentra.
     */
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

