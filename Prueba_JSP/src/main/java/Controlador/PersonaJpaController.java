package Controlador;

import DTO.LoginDTO;
import Modelo.Persona;
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
    public List<LoginDTO> login(int documento) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<LoginDTO> query = em.createQuery(
                    "SELECT new DTO.LoginDTO(p.id, p.documento, p.tipoDocumento.id, p.clave, p.rol.id) " +
                            "FROM tb_persona p WHERE p.documento = :NumeroDoc", LoginDTO.class);
            query.setParameter("NumeroDoc", documento);
            List<LoginDTO> resultados = query.getResultList();
            return resultados; // Devolver la lista de LoginDTOs
        } catch (NoResultException e) {
            System.out.println("No se encontraron resultados para el documento: " + documento);
            return new ArrayList<>(); // Devolver una lista vacía en este caso
        } finally {
            em.close();
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


