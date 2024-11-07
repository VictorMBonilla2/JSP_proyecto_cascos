package Controlador;

import Modelo.TbSectores;
import Utilidades.JPAUtils;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaQuery;

import java.io.Serializable;
import java.util.List;

/**
 * Controlador JPA para la entidad TbSectores. Proporciona operaciones CRUD
 * y métodos para la gestión de objetos TbSectores en la base de datos.
 */
public class CasillerosJPAController implements Serializable {

    private EntityManagerFactory fabricaEntidades;

    /**
     * Constructor que inicializa el EntityManagerFactory.
     */
    public CasillerosJPAController() {
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
     * Crea y persiste un nuevo TbSectores en la base de datos.
     *
     * @param casillero El objeto TbSectores que se desea crear.
     */
    public void create(TbSectores casillero) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(casillero);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Edita un TbSectores existente en la base de datos.
     *
     * @param casillero El objeto TbSectores que se desea editar.
     * @throws Exception si ocurre un error al editar o si el TbSectores no existe.
     */
    public void edit(TbSectores casillero) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            casillero = em.merge(casillero);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.isEmpty()) {
                int id = casillero.getId();
                if (findTbCasillero(id) == null) {
                    throw new Exception("El casillero con id " + id + " ya no existe.");
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
     * Elimina un TbSectores de la base de datos.
     *
     * @param id El ID del TbSectores a eliminar.
     * @throws Exception si el TbSectores no existe.
     */
    public void destroy(int id) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TbSectores casillero;
            try {
                casillero = em.getReference(TbSectores.class, id);
                casillero.getId();
            } catch (EntityNotFoundException enfe) {
                throw new Exception("El casillero con id " + id + " ya no existe.", enfe);
            }
            em.remove(casillero);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Obtiene una lista de todos los TbSectores.
     *
     * @return Lista de todos los TbSectores.
     */
    public List<TbSectores> findTbCasilleroEntities() {
        return findTbCasilleroEntities(true, -1, -1);
    }

    /**
     * Obtiene una lista de TbSectores con límites de resultados.
     *
     * @param maxResults Número máximo de resultados a devolver.
     * @param firstResult Primer resultado a devolver.
     * @return Lista de TbSectores en el rango especificado.
     */
    public List<TbSectores> findTbCasilleroEntities(int maxResults, int firstResult) {
        return findTbCasilleroEntities(false, maxResults, firstResult);
    }

    /**
     * Método privado para obtener TbSectores con límites de resultados.
     *
     * @param all Si es true, devuelve todos los resultados.
     * @param maxResults Número máximo de resultados a devolver.
     * @param firstResult Primer resultado a devolver.
     * @return Lista de TbSectores en el rango especificado o todos si all es true.
     */
    private List<TbSectores> findTbCasilleroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TbSectores.class));
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
     * Encuentra un TbSectores por su ID e incluye sus espacios asociados.
     *
     * @param id ID del TbSectores a buscar.
     * @return TbSectores con el ID especificado y sus espacios asociados, o null si no se encuentra.
     */
    public TbSectores findTbCasillero(int id) {
        EntityManager em = getEntityManager();
        try {
            // Usar una consulta JPQL para obtener el sector y sus espacios
            String jpql = "SELECT s FROM TbSectores s LEFT JOIN FETCH s.espacios WHERE s.id = :id";
            Query query = em.createQuery(jpql, TbSectores.class);
            query.setParameter("id", id);

            return (TbSectores) query.getSingleResult();
        } catch (NoResultException e) {
            return null; // Manejar el caso en que no se encuentre el sector
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}


