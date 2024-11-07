package Controlador;

import Modelo.TbEspacio;
import Modelo.enums.EstadoEspacio;
import Utilidades.JPAUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;

import java.io.Serializable;
import java.util.List;

/**
 * Controlador JPA para la entidad TbEspacio. Proporciona operaciones CRUD
 * y métodos para la gestión de objetos TbEspacio en la base de datos.
 */
public class EspacioJPAController implements Serializable {

    private EntityManagerFactory fabricaEntidades;

    /**
     * Constructor que inicializa el EntityManagerFactory.
     */
    public EspacioJPAController() {
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
     * Crea y persiste un nuevo TbEspacio en la base de datos.
     *
     * @param espacio El objeto TbEspacio que se desea crear.
     */
    public void create(TbEspacio espacio) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(espacio);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Edita un TbEspacio existente en la base de datos.
     *
     * @param espacio El objeto TbEspacio que se desea editar.
     * @throws Exception si ocurre un error al editar o si el TbEspacio no existe.
     */
    public void edit(TbEspacio espacio) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            espacio = em.merge(espacio);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.isEmpty()) {
                int id = espacio.getId_espacio();
                if (findTbEspacio(id) == null) {
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
     * Elimina un TbEspacio de la base de datos usando una consulta SQL nativa.
     *
     * @param id El ID del TbEspacio a eliminar.
     * @throws Exception si el TbEspacio no existe o si ocurre un error durante la eliminación.
     */
    public void destroy(int id) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Query query = em.createNativeQuery("DELETE FROM tb_espacio WHERE id_espacio = ?");
            query.setParameter(1, id);

            int result = query.executeUpdate();

            if (result == 0) {
                throw new Exception("El espacio con id " + id + " no fue encontrado o no pudo ser eliminado.");
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
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
     * Obtiene una lista de TbEspacio disponibles (no inactivos).
     *
     * @return Lista de TbEspacio disponibles.
     */
    public List<TbEspacio> findTbEspacioDisponibles() {
        EntityManager em = null;
        try {
            em = getEntityManager();

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<TbEspacio> cq = cb.createQuery(TbEspacio.class);
            Root<TbEspacio> root = cq.from(TbEspacio.class);

            root.fetch("vehiculo", JoinType.LEFT);

            cq.select(root).where(cb.notEqual(root.get("estado_espacio"), EstadoEspacio.Inactivo));

            Query query = em.createQuery(cq);

            return query.getResultList();
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    /**
     * Método privado para obtener TbEspacio con límites de resultados.
     *
     * @param all Si es true, devuelve todos los resultados.
     * @param maxResults Número máximo de resultados a devolver.
     * @param firstResult Primer resultado a devolver.
     * @return Lista de TbEspacio en el rango especificado o todos si all es true.
     */
    private List<TbEspacio> findTbEspacioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();

            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
            }

            CriteriaQuery<TbEspacio> cq = em.getCriteriaBuilder().createQuery(TbEspacio.class);
            Root<TbEspacio> root = cq.from(TbEspacio.class);

            root.fetch("vehiculo", JoinType.LEFT);
            root.fetch("persona", JoinType.LEFT);

            cq.select(root);
            Query query = em.createQuery(cq);

            if (!all) {
                if (maxResults > 0) {
                    query.setMaxResults(maxResults);
                }
                if (firstResult >= 0) {
                    query.setFirstResult(firstResult);
                }
            }

            List<TbEspacio> resultados = query.getResultList();

            if (em.getTransaction().isActive()) {
                em.getTransaction().commit();
            }

            return resultados;
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    /**
     * Encuentra un TbEspacio por su ID, incluyendo su vehículo asociado.
     *
     * @param id ID del TbEspacio a buscar.
     * @return TbEspacio con el ID especificado y su vehículo asociado, o null si no se encuentra.
     * @throws Exception si ocurre un error durante la búsqueda.
     */
    public TbEspacio findTbEspacio(int id) {
        EntityManager em = null;
        try {
            em = getEntityManager();

            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
            }

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<TbEspacio> cq = cb.createQuery(TbEspacio.class);
            Root<TbEspacio> root = cq.from(TbEspacio.class);

            root.fetch("vehiculo", JoinType.LEFT);

            cq.select(root).where(cb.equal(root.get("id_espacio"), id));

            TbEspacio espacio = em.createQuery(cq).getSingleResult();

            if (em.getTransaction().isActive()) {
                em.getTransaction().commit();
            }

            return espacio;
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    /**
     * Obtiene los espacios de un sector específico usando una consulta SQL nativa.
     *
     * @param idSector ID del sector para obtener sus espacios.
     * @return Lista de TbEspacio en el sector especificado.
     */
    public List<TbEspacio> obtenerEspaciosPorSectorNativo(int idSector) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            String sql = "SELECT * FROM tb_espacio WHERE id_sector = ?";
            Query query = em.createNativeQuery(sql, TbEspacio.class);
            query.setParameter(1, idSector);

            return query.getResultList();
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }
}


