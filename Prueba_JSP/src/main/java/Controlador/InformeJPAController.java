package Controlador;

import Modelo.TbInformesUsuarios;
import Utilidades.JPAUtils;
import jakarta.persistence.*;
import org.hibernate.exception.ConstraintViolationException;

import java.io.Serializable;
import java.util.List;

public class InformeJPAController implements Serializable {

    private EntityManagerFactory fabricaEntidades;

    public InformeJPAController() {
        this.fabricaEntidades = JPAUtils.getEntityManagerFactory();
    }

    public EntityManager getEntityManager() {
        return fabricaEntidades.createEntityManager();
    }

    // Método para crear un nuevo informe
    public void create(TbInformesUsuarios informe) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(informe);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("Hubo un error al crear el pdf: " + e.getMessage());
            e.printStackTrace(); // Esto te dará más información sobre la excepción
        }finally {
            if (em != null) {
                em.close();
            }
        }
    }

    // Método para editar un informe existente
    public void edit(TbInformesUsuarios informe) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            informe = em.merge(informe);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
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

    // Método para eliminar un informe por ID
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

    // Método para encontrar un informe por ID
    public TbInformesUsuarios findInforme(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TbInformesUsuarios.class, id);
        } finally {
            em.close();
        }
    }
    public TbInformesUsuarios findInformeByCodigo(String codigoInforme) {
        EntityManager em = getEntityManager();
        try {
            // Crear la consulta utilizando JPQL para buscar el informe por código
            String jpql = "SELECT i FROM TbInformesUsuarios i WHERE i.codigoInforme = :codigoInforme";
            return em.createQuery(jpql, TbInformesUsuarios.class)
                    .setParameter("codigoInforme", codigoInforme)
                    .getSingleResult();
        } catch (NoResultException e) {
            // Manejar el caso en el que no se encuentra el informe
            return null;
        } finally {
            em.close();
        }
    }


    // Método para encontrar todos los informes
    public List<TbInformesUsuarios> findAllInformes() {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT i FROM TbInformesUsuarios i", TbInformesUsuarios.class).getResultList();
        } finally {
            em.close();
        }
    }
}

