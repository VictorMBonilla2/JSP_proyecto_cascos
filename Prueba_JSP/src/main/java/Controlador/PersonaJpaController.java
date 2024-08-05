package Controlador;

import Modelo.LoginDTO;
import Modelo.Persona;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaQuery;
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

    private List<Persona> findPersonaEntities(boolean all, int maxResults, int firstResult){
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Persona.class));
            Query q = em.createQuery(cq);
            if(!all){
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            if(em != null && em.isOpen() ){
                em.close();
            }
        }
    }
    public List<LoginDTO> login(int documento){
        EntityManager em = getEntityManager();
        try{
            TypedQuery<Object[]> query= em.createQuery(
                    "SELECT p.documento, p.tipoDocumento, p.clave, p.rol FROM tb_persona p WHERE p.documento = :NumeroDoc", Object[].class);
            query.setParameter("NumeroDoc", documento);
            List<Object[]> resultados = query.getResultList();
            List<LoginDTO> loginDTOs = new ArrayList<>();
            for (Object[] resultado : resultados) {
                int numDoc = (int) resultado[0];
                String TipDocument = (String) resultado[1];
                String clave = (String) resultado[2];
                String Rol = (String) resultado[3];
                loginDTOs.add(new LoginDTO(numDoc, TipDocument, clave, Rol));
            }
            return loginDTOs; // Devolver la lista de LoginDTOs

        }catch (NoResultException e) {
            // Se maneja el caso en que no se encuentren resultados
            System.out.println("No se encontraron resultados para el documento: " + documento);
            return new ArrayList<>(); // Devolver una lista vacía en este caso
        }finally {
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


