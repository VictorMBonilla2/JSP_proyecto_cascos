package Logica;

import Controlador.PersistenciaController;
import DTO.LoginDTO;
import Modelo.Persona;

import java.util.List;


public class Logica_Persona {
    PersistenciaController controladora = new PersistenciaController();
    public  boolean validarIngreso(int documento, String tipoDocumento, String clave, String rol) {

        List<LoginDTO> lista = controladora.login(documento);
        int rolInt = Integer.parseInt(rol);

        for (LoginDTO login : lista) {
            System.out.println("La coincidencia con el Tipo de documento es: "+ login.getTipoDocumento().equals(tipoDocumento));
            System.out.println("La coincidencia con la clave es: "+ login.getClave().equals(clave));
            boolean resultado = login.getRolId() == rolInt;
            System.out.println("La coincidencia con el rol es : " + resultado);
            if (login.getTipoDocumento().equals(tipoDocumento) && login.getClave().equals(clave) && login.getRolId() == rolInt) {
                return true;
            }
        }
        return false;
    }
    //Proceso Registro.
    public boolean crearPersona(Persona perso){
        try {
            controladora.CrearPersona(perso);
            return true; // La creación fue exitosa
        } catch (Exception e) {
            // Maneja la excepción, por ejemplo, registrando el error
            System.err.println("Error al crear la persona: " + e.getMessage());
            e.printStackTrace();
            return false; // Se produjo un error
        }
    }

    public boolean actualizarPersona(Persona user)  {
        try {
            controladora.EditarPersona(user);
            return true; // La actualización fue exitosa
        } catch (Exception e) {
            // Manejo del error
            System.err.println("Error al actualizar la persona: " + e.getMessage());
            e.printStackTrace(); // O puedes optar por loggear la excepción en lugar de imprimirla
            return false; // La actualización falló
        }
    }

    public Persona buscarpersona(int documento) {

        Persona lista = controladora.buscarpersona(documento);

        return lista;

    }

    public void borrarUsuario(int documneto) throws Exception {
        controladora.eliminarUsuario(documneto);

    }

    public List<Persona> ObtenerUsuariosPorPagina(int numeroPagina) {
        int tamanioPagina = 10; // Tamaño de página fijo, ajusta según tus necesidades
        int data_inicio = (numeroPagina - 1) * tamanioPagina; // Índice inicial basado en la página solicitada
        int data_fin = tamanioPagina; // Número de resultados por página

        return controladora.TraerPersonasPorPagina(data_inicio, data_fin);
    }
    public Persona obtenerColaborador(int documento) {

        Persona Colaborador = buscarpersona(documento);

        if (Colaborador != null && Colaborador.getRol().getId() == 1) {

            return Colaborador;
        }

        return null;
    }


}
