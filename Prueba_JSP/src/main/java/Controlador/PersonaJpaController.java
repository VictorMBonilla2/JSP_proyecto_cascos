package Controlador;


import Modelo.Persona;
import Modelo.enums.EstadoUsuario;
import Utilidades.JPAUtils;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class PersonaJpaController implements Serializable {

    private EntityManagerFactory fabricaEntidades;

    public PersonaJpaController() {
        this.fabricaEntidades = JPAUtils.getEntityManagerFactory();
    }

    public EntityManager getEntityManager() {
        return fabricaEntidades.createEntityManager();
    }

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
            // Maneja la excepción de persistencia, por ejemplo, registrando el error
            System.err.println("Error de persistencia: " + e.getMessage());
            e.printStackTrace();
            // Lanza una excepción personalizada si es necesario
            throw new RuntimeException("Error al persistir la entidad", e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Persona persona) throws  Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            persona = em.merge(persona);
            em.getTransaction().commit();
        }catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = persona.getId();
                if(findPersona(id)==null){
                    throw new Exception("The persona with id "+"no longer exists.");
                }
            }
            throw ex;
        }finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(int id) throws Exception {
        EntityManager em = null;
        try{
            em = getEntityManager();
            em.getTransaction().begin();
            Persona persona;
            try {
                persona = em.getReference(Persona.class, id);
                persona.getId();
            } catch (EntityNotFoundException enfe) {
                throw new Exception("The persona with id "+id+" no longer exists.", enfe);
            }
            em.remove(persona);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public Persona findPersona(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Persona.class, id);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    public Persona buscarPersonaEnEspacio(int iduser) {
        EntityManager em = getEntityManager();
        try {
            // Crear la consulta con TypedQuery para buscar el usuario en la tabla de espacios
            TypedQuery<Persona> query = em.createQuery(
                    "SELECT e.persona FROM TbEspacio e WHERE e.persona.id = :iduser",
                    Persona.class
            );
            query.setParameter("iduser", iduser);

            // Retornar el resultado de la consulta
            return query.getSingleResult();
        } catch (NoResultException e) {
            System.out.println("No se encontró un usuario en el espacio con el ID: " + iduser);
            return null; // Si no se encuentra la persona, retorna null
        } finally {
            em.close();
        }
    }
    public Persona buscarPersonaDocumento(int documento) {
        EntityManager em = getEntityManager();
        try {
            // Crear el constructor de la consulta
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Persona> cq = cb.createQuery(Persona.class);

            // Definir la raíz de la consulta (tabla principal)
            Root<Persona> personaRoot = cq.from(Persona.class);

            // Realizar los LEFT JOINs para permitir la existencia de registros nulos en las relaciones
            personaRoot.fetch("vehiculos", JoinType.LEFT);
            personaRoot.fetch("rol", JoinType.LEFT);
            personaRoot.fetch("tipoDocumento", JoinType.LEFT);

            // Definir la condición de la consulta (WHERE)
            cq.where(cb.equal(personaRoot.get("documento"), documento));

            // Ejecutar la consulta y devolver el resultado
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
    private List<Persona> findPersonaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            // Crear la consulta JPQL con JOIN FETCH para cargar la relación `rol`
            String jpql = "SELECT p FROM tb_persona p " +
                    "LEFT JOIN FETCH p.rol " +
                    "LEFT JOIN FETCH p.tipoDocumento";


            // Crear la consulta TypedQuery basada en la consulta JPQL y se espera un resultado de tipo Persona
            TypedQuery<Persona> query = em.createQuery(jpql, Persona.class);

            // Aplicar paginación si no se deben traer todas las entidades
            if (!all) {
                query.setMaxResults(maxResults);
                query.setFirstResult(firstResult);
            }

            // Ejecutar la consulta y obtener la lista de resultados

            return query.getResultList();
        } finally {
            if (em != null && em.isOpen()) {
                em.close(); // Cerrar el EntityManager después de completar las operaciones
            }
        }
    }

    private List<Persona> findUsuariosPorEstado(EstadoUsuario estado, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            // Crear la consulta JPQL con JOIN FETCH para cargar la relación `rol` y filtrado por estado
            String jpql = "SELECT p FROM tb_persona p " +
                    "LEFT JOIN FETCH p.rol " +
                    "LEFT JOIN FETCH p.tipoDocumento " +
                    "WHERE p.estadoUsuario = :estado";

            // Crear la consulta TypedQuery basada en la consulta JPQL
            TypedQuery<Persona> query = em.createQuery(jpql, Persona.class);
            query.setParameter("estado", estado); // Establecer el estado (ACTIVO o INACTIVO)

            // Aplicar paginación
            query.setMaxResults(maxResults);
            query.setFirstResult(firstResult);

            // Ejecutar la consulta y obtener la lista de resultados
            return query.getResultList();
        } finally {
            if (em != null && em.isOpen()) {
                em.close(); // Cerrar el EntityManager
            }
        }
    }
    public List<Persona> findUsuariosActivos(int maxResults, int firstResult) {
        return findUsuariosPorEstado(EstadoUsuario.ACTIVO, maxResults, firstResult);
    }
    public List<Persona> findUsuariosInactivos(int maxResults, int firstResult) {
        return findUsuariosPorEstado(EstadoUsuario.INACTIVO, maxResults, firstResult);
    }

    public int contarUsuariosActivos() {
        // Realiza una consulta para contar cuántos usuarios tienen estado ACTIVO
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT COUNT(p) FROM tb_persona p WHERE p.estadoUsuario = :estado";
            Query query = em.createQuery(jpql);
            query.setParameter("estado", EstadoUsuario.ACTIVO);
            return ((Long) query.getSingleResult()).intValue(); // Devuelve el total de usuarios activos
        } finally {
            em.close();
        }
    }

    public int contarUsuariosInactivos() {
        return 0;
    }






    public void actualizarEstado(Persona persona, EstadoUsuario nuevoEstado) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();

            // Actualizar el estado del usuario directamente
            persona.setEstadoUsuario(nuevoEstado);

            // Guardar los cambios en la base de datos
            em.merge(persona);

            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }


    public long contarUsuarios() {
        EntityManager em = getEntityManager();
        try {
            // Crear la consulta JPQL para contar todas las entidades de Persona
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(p) FROM tb_persona p", Long.class
            );
            // Ejecutar la consulta y devolver el resultado
            return query.getSingleResult();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public Persona buscarPersonaEmail(String email) {
        EntityManager em = getEntityManager();
        try {
            // Crear la consulta para buscar por email
            TypedQuery<Persona> query = em.createQuery("SELECT p FROM tb_persona p WHERE p.correo = :email", Persona.class);
            query.setParameter("email", email);

            // Obtener una lista de resultados, y retornar el primero si existe
            List<Persona> resultados = query.getResultList();
            if (resultados.isEmpty()) {
                return null;  // Retorna null si no se encuentra ningún resultado
            } else {
                return resultados.get(0);  // Retorna el primer resultado encontrado
            }
        } finally {
            em.close();
        }
    }


    public Persona buscarPersonaToken(String token) {
        EntityManager em = getEntityManager();
        try {
            // Crear la consulta para buscar por token
            TypedQuery<Persona> query = em.createQuery("SELECT p FROM tb_persona p WHERE p.tokenRecuperacion = :token", Persona.class);
            query.setParameter("token", token);
            return query.getSingleResult(); // Asume que siempre se encontrará un resultado
        } catch (NoResultException e) {
            // Retorna null si no se encuentra ningún resultado
            return null;
        } finally {
            em.close();
        }
    }

}


//En caso de Cambiar la Gestion de las consultas por "CriteriaQuery", Se guarda en este fragmento un ejemplo funcional de la sintaxis a usar.
// La funcion devuelve una Lista tipo Persona la cual se le hace un Join Fetch del modelo Rol.

//private List<Persona> findPersonaEntities(boolean all, int maxResults, int firstResult) {
//    EntityManager em = getEntityManager();
//    try {
//        // Crear CriteriaBuilder y CriteriaQuery
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<Persona> cq = cb.createQuery(Persona.class);
//
//        // Definir la raíz de la consulta
//        Root<Persona> root = cq.from(Persona.class);
//
//        // Agregar JOIN FETCH para inicializar la relación `rol`
//        root.fetch("rol", JoinType.LEFT); // Esto cargará la relación `rol` junto con la entidad `Persona`
//
//        // Construir la consulta
//        cq.select(root);
//
//        // Crear la consulta
//        TypedQuery<Persona> query = em.createQuery(cq);
//
//        // Aplicar paginación si no se deben traer todas las entidades
//        if (!all) {
//            query.setMaxResults(maxResults);
//            query.setFirstResult(firstResult);
//        }
//
//        // Ejecutar la consulta y obtener la lista de resultados
//        List<Persona> personas = query.getResultList();
//
//
//        return personas;
//    } finally {
//        if (em != null && em.isOpen()) {
//            em.close(); // Cerrar el EntityManager después de completar las operaciones
//        }
//    }
//}


