package Controlador;


import Modelo.Persona;
import Modelo.TbEspacio;
import Modelo.TbVehiculo;
import Modelo.enums.EstadoUsuario;
import Utilidades.JPAUtils;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;

import java.io.Serializable;
import java.util.List;

/**
 * Controlador JPA para la entidad Persona. Proporciona operaciones CRUD
 * y métodos para la gestión de objetos Persona en la base de datos.
 */
public class PersonaJpaController implements Serializable {

    private EntityManagerFactory fabricaEntidades;

    /**
     * Constructor que inicializa el EntityManagerFactory.
     */
    public PersonaJpaController() {
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
     * Crea y persiste un nuevo Persona en la base de datos.
     *
     * @param persona El objeto Persona que se desea crear.
     * @throws Exception si ocurre un error durante la persistencia.
     */
    public void create(Persona persona) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(persona);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error de persistencia: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al persistir la entidad", e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Edita un Persona existente en la base de datos.
     *
     * @param persona El objeto Persona que se desea editar.
     * @throws Exception si ocurre un error al editar o si el Persona no existe.
     */
    public void edit(Persona persona) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            persona = em.merge(persona);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = persona.getId();
                if (findPersona(id) == null) {
                    throw new Exception("La persona con id " + id + " ya no existe.");
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
     * Elimina un Persona de la base de datos.
     *
     * @param id El ID del Persona a eliminar.
     * @throws Exception si el Persona no existe o si ocurre un error durante la eliminación.
     */
    public void destroy(int id) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Persona persona;
            try {
                persona = em.getReference(Persona.class, id);
                persona.getId();
            } catch (EntityNotFoundException enfe) {
                throw new Exception("La persona con id " + id + " ya no existe.", enfe);
            }
            em.remove(persona);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Encuentra un Persona por su ID.
     *
     * @param id ID del Persona a buscar.
     * @return Persona con el ID especificado o null si no se encuentra.
     */
    public Persona findPersona(int id) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Persona> cq = cb.createQuery(Persona.class);
            Root<Persona> root = cq.from(Persona.class);
            root.fetch("vehiculos", JoinType.LEFT);

            cq.select(root).where(cb.equal(root.get("id"), id));
            TypedQuery<Persona> query = em.createQuery(cq);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }


    /**
     * Busca una Persona en el espacio según el ID del usuario.
     *
     * @param iduser ID del usuario cuya persona se desea buscar en el espacio.
     * @return La Persona asociada a un vehículo en el espacio, o null si no se encuentra.
     */
    public Persona buscarPersonaEnEspacio(int iduser) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Persona> query = em.createQuery(
                    "SELECT v.persona FROM TbEspacio e JOIN e.vehiculo v WHERE v.persona.id = :iduser",
                    Persona.class
            );
            query.setParameter("iduser", iduser);

            return query.getSingleResult();
        } catch (NoResultException e) {
            System.out.println("No se encontró un vehículo en el espacio asociado a la persona con ID: " + iduser);
            return null;
        } finally {
            em.close();
        }
    }

    /**
     * Busca una Persona en la base de datos utilizando su documento.
     *
     * @param documento Número de documento de la persona a buscar.
     * @return La Persona con el documento especificado, o null si no se encuentra.
     */
    public Persona buscarPersonaDocumento(int documento) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Persona> cq = cb.createQuery(Persona.class);
            Root<Persona> personaRoot = cq.from(Persona.class);

            personaRoot.fetch("vehiculos", JoinType.LEFT);
            cq.where(cb.equal(personaRoot.get("documento"), documento));

            return em.createQuery(cq).getSingleResult();
        } catch (NoResultException e) {
            System.out.println("No se encontró la persona con documento: " + documento);
            return null;
        } finally {
            em.close();
        }
    }

    public List<Persona> findPersonaEntities() {
        return findPersonaEntities(true, -1,-1);
    }
    public List<Persona> findPersonaEntities(int maxResults, int firstResult) {
        return findPersonaEntities(false,maxResults,firstResult);
    }


    /**
     * Obtiene una lista de entidades Persona, con la opción de cargar todas las entidades o aplicar paginación.
     * Las entidades Persona incluyen las relaciones `rol` y `tipoDocumento` cargadas mediante `JOIN FETCH`.
     *
     * @param all Si es true, se obtienen todas las entidades; si es false, se aplica la paginación.
     * @param maxResults El número máximo de resultados a devolver cuando se aplica la paginación.
     * @param firstResult El índice del primer resultado a devolver cuando se aplica la paginación.
     * @return Lista de entidades Persona con las relaciones `rol` y `tipoDocumento` cargadas.
     */
    private List<Persona> findPersonaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT p FROM tb_persona p " +
                    "LEFT JOIN FETCH p.rol " +
                    "LEFT JOIN FETCH p.tipoDocumento";

            TypedQuery<Persona> query = em.createQuery(jpql, Persona.class);

            if (!all) {
                query.setMaxResults(maxResults);
                query.setFirstResult(firstResult);
            }

            return query.getResultList();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }


    /**
     * Obtiene una lista de entidades Persona filtradas por estado de usuario y cargando las relaciones `rol`
     * y `tipoDocumento` mediante `JOIN FETCH`.
     *
     * @param estado El estado de usuario para filtrar las entidades (ACTIVO o INACTIVO).
     * @param maxResults El número máximo de resultados a devolver.
     * @param firstResult El índice del primer resultado a devolver.
     * @return Lista de entidades Persona que coinciden con el estado especificado, con las relaciones `rol` y `tipoDocumento` cargadas.
     */
    private List<Persona> findUsuariosPorEstado(EstadoUsuario estado, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT p FROM tb_persona p " +
                    "LEFT JOIN FETCH p.rol " +
                    "LEFT JOIN FETCH p.tipoDocumento " +
                    "WHERE p.estadoUsuario = :estado";

            TypedQuery<Persona> query = em.createQuery(jpql, Persona.class);
            query.setParameter("estado", estado);

            query.setMaxResults(maxResults);
            query.setFirstResult(firstResult);

            return query.getResultList();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public List<Persona> findUsuariosActivos(int maxResults, int firstResult) {
        return findUsuariosPorEstado(EstadoUsuario.ACTIVO, maxResults, firstResult);
    }
    public List<Persona> findUsuariosInactivos(int maxResults, int firstResult) {
        return findUsuariosPorEstado(EstadoUsuario.INACTIVO, maxResults, firstResult);
    }

    /**
     * Cuenta el número total de usuarios que tienen el estado ACTIVO en la base de datos.
     *
     * @return Número total de usuarios activos.
     */
    public int contarUsuariosActivos() {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT COUNT(p) FROM tb_persona p WHERE p.estadoUsuario = :estado";
            Query query = em.createQuery(jpql);
            query.setParameter("estado", EstadoUsuario.ACTIVO);
            return ((Long) query.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }


    public int contarUsuariosInactivos() {
        return 0;
    }

    /**
     * Actualiza el estado de una Persona en la base de datos.
     *
     * @param persona La entidad Persona cuyo estado se desea actualizar.
     * @param nuevoEstado El nuevo estado que se establecerá para la Persona.
     * @throws Exception si ocurre un error durante la actualización.
     */
    public void actualizarEstado(Persona persona, EstadoUsuario nuevoEstado) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();

            persona.setEstadoUsuario(nuevoEstado);
            em.merge(persona);

            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Cuenta el número total de usuarios en la base de datos.
     *
     * @return Número total de entidades Persona.
     */
    public long contarUsuarios() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery("SELECT COUNT(p) FROM tb_persona p", Long.class);
            return query.getSingleResult();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    /**
     * Busca una Persona en la base de datos utilizando su correo electrónico.
     *
     * @param email Correo electrónico de la persona a buscar.
     * @return La entidad Persona con el correo electrónico especificado, o null si no se encuentra.
     */
    public Persona buscarPersonaEmail(String email) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Persona> cq = cb.createQuery(Persona.class);
            Root<Persona> personaRoot = cq.from(Persona.class);

            personaRoot.fetch("vehiculos", JoinType.LEFT);
            personaRoot.fetch("rol", JoinType.LEFT);
            personaRoot.fetch("tipoDocumento", JoinType.LEFT);

            cq.where(cb.equal(personaRoot.get("correo"), email));

            return em.createQuery(cq).getSingleResult();
        } catch (NoResultException e) {
            System.out.println("No se encontró la persona con email: " + email);
            return null;
        } finally {
            em.close();
        }
    }

}


