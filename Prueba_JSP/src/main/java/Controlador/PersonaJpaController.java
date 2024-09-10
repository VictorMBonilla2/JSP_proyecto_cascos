package Controlador;

import DTO.LoginDTO;
import Modelo.Persona;
import Modelo.Roles;
import jakarta.persistence.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@WebServlet("/PersonaJpaController")//Se le da nombre al controlador de persistencia de la clase.
public class PersonaJpaController implements Serializable {

    public PersonaJpaController() {
        fabricaEntidades = Persistence.createEntityManagerFactory("default");
        //Se crea un manager de entidad, haciendo referencia a la persistencia del proyecto.
    }
    //Se instacia la variable con valor null
    private EntityManagerFactory fabricaEntidades = null;

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
                int id = persona.getDocumento();
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
                persona.getDocumento();
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
            String jpql = "SELECT p FROM tb_persona p LEFT JOIN FETCH p.rol";

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
            TypedQuery<Object[]> query = em.createQuery(
                    "SELECT p.documento, p.tipoDocumento, p.clave, p.rol FROM tb_persona p WHERE p.documento = :NumeroDoc", Object[].class);
            query.setParameter("NumeroDoc", documento);
            List<Object[]> resultados = query.getResultList();
            List<LoginDTO> loginDTOs = new ArrayList<>();
            for (Object[] resultado : resultados) {
                int numDoc = (int) resultado[0];
                String TipDocument = (String) resultado[1];
                String clave = (String) resultado[2];
                Roles rol = (Roles) resultado[3]; // Cambiado de String a Roles
                loginDTOs.add(new LoginDTO(numDoc, TipDocument, clave, rol.getId())); // Usar el ID del rol
            }
            return loginDTOs; // Devolver la lista de LoginDTOs

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


