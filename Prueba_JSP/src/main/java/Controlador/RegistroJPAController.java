package Controlador;

import Modelo.TbRegistro;
import Modelo.TbVehiculo;
import Utilidades.JPAUtils;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;

import java.util.List;

public class RegistroJPAController {

    private EntityManagerFactory fabricaEntidades;

    public RegistroJPAController() {
        this.fabricaEntidades = JPAUtils.getEntityManagerFactory();
    }

    public EntityManager getEntityManager() {
        return fabricaEntidades.createEntityManager();
    }

    public void create(TbRegistro registro) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(registro);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TbRegistro registro) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            registro = em.merge(registro);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = registro.getIdRegistro();
                if (findTbRegistro(id) == null) {
                    throw new Exception("The registro with id " + id + " no longer exists.");
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
            TbRegistro registro;
            try {
                registro = em.getReference(TbRegistro.class, id);
                registro.getIdRegistro();
            } catch (EntityNotFoundException enfe) {
                throw new Exception("The registro with id " + id + " no longer exists.", enfe);
            }
            em.remove(registro);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    public List<TbRegistro> findTbRegistroEntities() {
        return findTbRegistroEntities(true, -1, -1);
    }

    public List<TbRegistro> findTbRegistroEntities(int maxResults, int firstResult) {
        return findTbRegistroEntities(false, maxResults, firstResult);
    }
    public TbRegistro findTbRegistro(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TbRegistro.class, id);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    private List<TbRegistro> findTbRegistroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TbRegistro.class));
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

    public List<TbRegistro> findRegistrosGestor(int idGestor, int dataInicio, int tamanioPagina) {
        EntityManager em = getEntityManager();
        try {
            // Crear la consulta TypedQuery con un parámetro para el ID del gestor
            TypedQuery<TbRegistro> query = em.createQuery(
                    "SELECT r FROM TbRegistro r WHERE r.gestor.id = :idGestor",
                    TbRegistro.class
            );
            query.setParameter("idGestor", idGestor);


            query.setFirstResult(dataInicio);
            query.setMaxResults(tamanioPagina);

            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<TbRegistro> findRegistrosAprendiz(int idAprendiz, int dataInicio, int tamanioPagina) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<TbRegistro> cq = cb.createQuery(TbRegistro.class);

            Root<TbRegistro> root = cq.from(TbRegistro.class);

            // JOIN FETCH para inicializar las relaciones de vehículo y persona
            Fetch<TbRegistro, TbVehiculo> vehiculoFetch = root.fetch("vehiculo", JoinType.INNER);
            vehiculoFetch.fetch("persona", JoinType.INNER);  // Cargar la persona a través del vehículo
            root.fetch("gestor", JoinType.INNER);

            // Añadir el where para filtrar por el id del aprendiz (persona)
            cq.select(root).where(cb.equal(root.get("vehiculo").get("persona").get("id"), idAprendiz));

            // Crear el TypedQuery con la lógica de paginación
            TypedQuery<TbRegistro> query = em.createQuery(cq);
            query.setFirstResult(dataInicio); // Índice del primer resultado
            query.setMaxResults(tamanioPagina); // Tamaño de la página

            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public long contarRegistrosGestor(int idGestor) {
        EntityManager em = getEntityManager();
        try {
            // Crear consulta con la función COUNT para contar los registros del gestor
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(r) FROM TbRegistro r WHERE r.gestor.id = :idGestor",
                    Long.class
            );
            query.setParameter("idGestor", idGestor);

            return query.getSingleResult();  // Devolver el total de registros
        } finally {
            em.close();
        }
    }
    public long contarRegistrosAprendiz(int idAprendiz) {
        EntityManager em = getEntityManager();
        try {
            // Crear consulta con la función COUNT para contar los registros del aprendiz
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(r) FROM TbRegistro r WHERE r.vehiculo.persona.id = :idAprendiz",
                    Long.class
            );
            query.setParameter("idAprendiz", idAprendiz);

            return query.getSingleResult();  // Devolver el total de registros
        } finally {
            em.close();
        }
    }

    public long contarRegistros() {
        EntityManager em = getEntityManager();
        try {
            // Crear consulta con la función COUNT para contar todos los registros en la tabla TbRegistro
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(r) FROM TbRegistro r",
                    Long.class
            );
            return query.getSingleResult();  // Devolver el total de registros
        } finally {
            em.close();
        }
    }
}
