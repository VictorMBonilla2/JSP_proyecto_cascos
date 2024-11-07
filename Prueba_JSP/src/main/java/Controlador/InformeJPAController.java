package Controlador;

import Modelo.TbInformesUsuarios;
import Utilidades.JPAUtils;
import jakarta.persistence.*;
import org.hibernate.exception.ConstraintViolationException;

import java.io.Serializable;
import java.util.List;

/**
 * Controlador JPA para la entidad TbInformesUsuarios. Proporciona operaciones CRUD
 * y métodos para la gestión de objetos TbInformesUsuarios en la base de datos.
 */
public class InformeJPAController implements Serializable {

    private EntityManagerFactory fabricaEntidades;

    /**
     * Constructor que inicializa el EntityManagerFactory.
     */
    public InformeJPAController() {
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
     * Crea y persiste un nuevo TbInformesUsuarios en la base de datos.
     *
     * @param informe El objeto TbInformesUsuarios que se desea crear.
     */
    public void create(TbInformesUsuarios informe) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(informe);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("Hubo un error al crear el informe: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Edita un TbInformesUsuarios existente en la base de datos.
     *
     * @param informe El objeto TbInformesUsuarios que se desea editar.
     * @throws Exception si ocurre un error al editar o si el TbInformesUsuarios no existe.
     */
    public void edit(TbInformesUsuarios informe) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            informe = em.merge(informe);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.isEmpty()) {
                Long id = informe.getId();
                if (findInforme(id) == null) {
                    throw new Exception("El informe con id " + id + " ya no existe.");
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
     * Elimina un TbInformesUsuarios de la base de datos.
     *
     * @param id El ID del TbInformesUsuarios a eliminar.
     * @throws PersistenceException si el TbInformesUsuarios está en uso y no se puede eliminar.
     * @throws Exception si el TbInformesUsuarios no existe o si ocurre cualquier otro error inesperado.
     */
    public void destroy(Long id) throws PersistenceException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TbInformesUsuarios informe;
            try {
                informe = em.getReference(TbInformesUsuarios.class, id);
                informe.getId();  // Verificar que el informe existe
            } catch (EntityNotFoundException enfe) {
                throw new Exception("El informe con id " + id + " ya no existe.", enfe);
            }
            em.remove(informe);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw new PersistenceException("No se puede eliminar el informe porque está en uso.", e);
            }
            throw e;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
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
     * Encuentra un TbInformesUsuarios por su ID.
     *
     * @param id ID del TbInformesUsuarios a buscar.
     * @return TbInformesUsuarios con el ID especificado o null si no se encuentra.
     */
    public TbInformesUsuarios findInforme(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TbInformesUsuarios.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * Encuentra un TbInformesUsuarios por su código de informe.
     *
     * @param codigoInforme Código del informe a buscar.
     * @return TbInformesUsuarios con el código especificado o null si no se encuentra.
     */
    public TbInformesUsuarios findInformeByCodigo(String codigoInforme) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT i FROM TbInformesUsuarios i WHERE i.codigoInforme = :codigoInforme";
            return em.createQuery(jpql, TbInformesUsuarios.class)
                    .setParameter("codigoInforme", codigoInforme)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    /**
     * Obtiene una lista de todos los TbInformesUsuarios.
     *
     * @return Lista de todos los TbInformesUsuarios.
     */
    public List<TbInformesUsuarios> findAllInformes() {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT i FROM TbInformesUsuarios i", TbInformesUsuarios.class).getResultList();
        } finally {
            em.close();
        }
    }
}


