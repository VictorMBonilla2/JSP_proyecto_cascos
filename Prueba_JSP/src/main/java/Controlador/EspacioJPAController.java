package Controlador;

import Modelo.Roles;
import Modelo.TbEspacio;
import Utilidades.JPAUtils;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;

import java.io.Serializable;
import java.util.List;

public class EspacioJPAController implements Serializable {


    private EntityManagerFactory fabricaEntidades;

    public EspacioJPAController() {
        this.fabricaEntidades = JPAUtils.getEntityManagerFactory();
    }

    public EntityManager getEntityManager() {
        return fabricaEntidades.createEntityManager();
    }

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


    public void edit(TbEspacio espacio) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            espacio = em.merge(espacio);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = espacio.getId_espacio();
                if (findTbEspacio(id) == null) {
                    throw new Exception("The espacio with id " + id + " no longer exists.");
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

            // Ejecutar la consulta SQL nativa para eliminar el espacio
            Query query = em.createNativeQuery("DELETE FROM tb_espacio WHERE id_espacio = ?");
            query.setParameter(1, id);  // Establecer el valor del parámetro

            int result = query.executeUpdate();

            // Verificar si se eliminó algún registro
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





    public List<TbEspacio> findTbEspacioEntities() {
        return findTbEspacioEntities(true, -1, -1);
    }
    public List<TbEspacio> findTbEspacioEntities(int maxResults, int firstResult) {
        return findTbEspacioEntities(false, maxResults, firstResult);
    }

    private List<TbEspacio> findTbEspacioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();

            // Iniciar una transacción si es necesaria para la lectura
            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
            }

            // Crear CriteriaQuery para TbEspacio
            CriteriaQuery<TbEspacio> cq = em.getCriteriaBuilder().createQuery(TbEspacio.class);

            // Definir el root de TbEspacio
            Root<TbEspacio> root = cq.from(TbEspacio.class);

            // Hacer fetch manual de las relaciones LAZY para vehiculo y persona con LEFT JOIN
            root.fetch("vehiculo", JoinType.LEFT);  // Realiza un LEFT JOIN con vehiculo
            root.fetch("persona", JoinType.LEFT);   // Realiza un LEFT JOIN con persona

            // Seleccionar el root en la consulta
            cq.select(root);

            // Crear la consulta
            Query query = em.createQuery(cq);

            // Aplicar el filtro de paginación si 'all' es false
            if (!all) {
                if (maxResults > 0) {
                    query.setMaxResults(maxResults); // Limitar la cantidad de resultados
                }
                if (firstResult >= 0) {
                    query.setFirstResult(firstResult); // Paginación, establecer el primer resultado
                }
            }

            // Ejecutar la consulta y devolver la lista de resultados
            List<TbEspacio> resultados = query.getResultList();

            // Confirmar la transacción solo si se inició
            if (em.getTransaction().isActive()) {
                em.getTransaction().commit();
            }

            return resultados;
        } catch (Exception e) {
            // Revertir la transacción solo si se inició
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            // Cerrar el EntityManager al final
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }




    public TbEspacio findTbEspacio(int id) {
        EntityManager em = null;
        try {
            em = getEntityManager();

            // Iniciar una transacción si es necesaria para la lectura
            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
            }

            // Usar CriteriaBuilder para crear la consulta
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<TbEspacio> cq = cb.createQuery(TbEspacio.class);

            // Definir el root de TbEspacio
            Root<TbEspacio> root = cq.from(TbEspacio.class);

            // Hacer fetch manual de las relaciones LAZY para vehiculo y persona con LEFT JOIN
            root.fetch("vehiculo", JoinType.LEFT);  // Realiza un LEFT JOIN con vehiculo
            root.fetch("persona", JoinType.LEFT);   // Realiza un LEFT JOIN con persona

            // Añadir la condición para buscar por ID
            cq.select(root).where(cb.equal(root.get("id_espacio"), id));

            // Crear la consulta y obtener el resultado
            TbEspacio espacio = em.createQuery(cq).getSingleResult();

            // Confirmar la transacción solo si se inició
            if (em.getTransaction().isActive()) {
                em.getTransaction().commit();
            }

            return espacio;

        } catch (Exception e) {
            // Revertir la transacción solo si se inició
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            // Cerrar el EntityManager al final
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }


    // Método para obtener los espacios de un sector mediante consulta SQL nativa
    public List<TbEspacio> obtenerEspaciosPorSectorNativo(int idSector) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            // Consulta SQL nativa para obtener los espacios del sector
            String sql = "SELECT * FROM tb_espacio WHERE id_sector = ?";
            Query query = em.createNativeQuery(sql, TbEspacio.class);
            query.setParameter(1, idSector);

            // Ejecutar la consulta y devolver la lista de espacios
            return query.getResultList();
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

}

