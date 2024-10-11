package Controlador;

import Modelo.Persona;
import Modelo.TbVehiculo;
import Modelo.enums.EstadoVehiculo;
import Utilidades.JPAUtils;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.io.Serializable;
import java.util.List;

public class VehiculoJPAController implements Serializable {


    private EntityManagerFactory fabricaEntidades;

    public VehiculoJPAController() {
        this.fabricaEntidades = JPAUtils.getEntityManagerFactory();
    }

    public EntityManager getEntityManager() {
        return fabricaEntidades.createEntityManager();
    }

    public void create(TbVehiculo vehiculo) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(vehiculo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TbVehiculo vehiculo) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            vehiculo = em.merge(vehiculo);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = vehiculo.getId_vehiculo();
                if (findTbVehiculo(id) == null) {
                    throw new Exception("The vehiculo with id " + id + " no longer exists.");
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
            TbVehiculo vehiculo;
            try {
                vehiculo = em.getReference(TbVehiculo.class, id);
                vehiculo.getId_vehiculo();
            } catch (EntityNotFoundException enfe) {
                throw new Exception("The vehiculo with id " + id + " no longer exists.", enfe);
            }
            em.remove(vehiculo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    public List<TbVehiculo> findTbVehiculoEntities() {
        return findTbVehiculoEntities(true, -1, -1);
    }

    public List<TbVehiculo> findTbVehiculoEntities(int maxResults, int firstResult) {
        return findTbVehiculoEntities(false, maxResults, firstResult);
    }
    public TbVehiculo findTbVehiculo(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TbVehiculo.class, id);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TbVehiculo> findVehiculosByPersona(int documentoPersona) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<TbVehiculo> query = em.createQuery(
                    "SELECT v FROM TbVehiculo v WHERE v.persona.id = :documento", TbVehiculo.class);
            query.setParameter("documento", documentoPersona);
            return query.getResultList();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    private List<TbVehiculo> findTbVehiculoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TbVehiculo.class));
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

    public TbVehiculo findVehiculoByPlaca(String placa) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<TbVehiculo> cq = cb.createQuery(TbVehiculo.class);

            Root<TbVehiculo> vehiculo = cq.from(TbVehiculo.class);
            cq.select(vehiculo).where(cb.equal(vehiculo.get("placaVehiculo"), placa));

            Query q = em.createQuery(cq);

            // Obtener el resultado único
            return (TbVehiculo) q.getSingleResult();
        } catch (NoResultException e) {
            // Si no se encuentra ningún resultado, devolver null o manejar el caso de otra manera
            System.err.println("No se encontró un vehículo con la placa: " + placa);
            return null;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public void actualizarEstado(TbVehiculo vehiculo, EstadoVehiculo nuevoEstado) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();

            // Actualizar el estado del usuario directamente
            vehiculo.setEstadoVehiculo(nuevoEstado);

            // Guardar los cambios en la base de datos
            em.merge(vehiculo);

            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public TbVehiculo buscarVehiculoEnEspacio(int idVehiculo) {
        EntityManager em = getEntityManager();
        try {
            // Crear la consulta con TypedQuery para buscar el vehiuclo en la tabla de espacios
            TypedQuery<TbVehiculo> query = em.createQuery(
                    "SELECT e.vehiculo FROM TbEspacio e WHERE e.vehiculo.id_vehiculo = :idvehiculo",
                    TbVehiculo.class
            );
            query.setParameter("idvehiculo", idVehiculo);

            // Retornar el resultado de la consulta
            return query.getSingleResult();
        } catch (NoResultException e) {
            System.out.println("No se encontró un usuario en el espacio con el ID: " + idVehiculo);
            return null; // Si no se encuentra la persona, retorna null
        } finally {
            em.close();
        }
    }
}
