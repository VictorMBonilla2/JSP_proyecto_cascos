import {sendRequest} from "./ajax.js";
import {showConfirmationDialog} from "./alerts/confirm.js";
import {showErrorDialog} from "./alerts/error.js";
import {showSuccessAlert} from "./alerts/success.js";

const itemDisableTemplate= document.querySelector("#item__list--disable").content;
document.addEventListener("DOMContentLoaded", async () => {


    let personasInactivas = await ObtenerUsuariosInactivos("1");

    if (personasInactivas.usuarios.length > 0) {
        actualizarListaUsuariosInactivos(personasInactivas.usuarios, "listaDeshabilitados");
        crearPaginadorInactivos(personasInactivas.totalPaginas, 1);
    }
    document.addEventListener("click", (e)=>{
        if (e.target.matches("#enable_button")) {

        let data_enable = parseInt(e.target.getAttribute("data-enable"));

            // Filtrar la lista de personas por idUser
            const personaSeleccionada = personasInactivas.usuarios.filter(persona => persona.idUser === data_enable);
            if (personaSeleccionada.length > 0) {
                showConfirmationDialog("¿Habilitar Usuario?",
                    "Al habilitar al usuario, retomara accesoa su cuenta y podra ser buscado en el estacionamiento",
                    ()=>habilitarUsuario(personaSeleccionada[0]),
                    ()=>console.log("Acción cancelada"))
            } else {
                console.log("Usuario no encontrado.");
            }
        }
    })
})
function crearPaginadorInactivos(totalPaginas, paginaActual) {
    const paginadorContainer = document.getElementById('paginadorInhabilitados');

    paginadorContainer.innerHTML = ''; // Limpiar el paginador antes de crear nuevos botones
    for (let i = 1; i <= totalPaginas; i++) {
        const boton = document.createElement('button');
        boton.textContent = i;
        boton.className = i === paginaActual ? 'active' : '';
        boton.addEventListener('click', () => cambiarPaginaInactivos(i));
        paginadorContainer.appendChild(boton);
    }
}

async function cambiarPaginaInactivos(pagina) {
    let personas = await ObtenerUsuariosInactivos(pagina);

    if (personas && personas.usuarios.length > 0) {
        actualizarListaUsuariosInactivos(personas.usuarios, 'listaDeshabilitados');
        crearPaginadorInactivos(personas.totalPaginas, pagina);
    }
}

function actualizarListaUsuariosInactivos(usuarios, contenedorId) {
    const lista = document.getElementById(contenedorId);
    lista.innerHTML = ''; // Limpiar la lista antes de agregar nuevos usuarios
    usuarios.forEach(persona => {
        // Seleccionar el template correcto para usuarios inactivos
        const clone = document.importNode(itemDisableTemplate, true);
        clone.querySelector(".user_list__item").setAttribute('data-user', persona.idUser);
        clone.querySelector(".user_item_document > p").textContent = persona.documento;
        clone.querySelector(".user_item_name > p").textContent = persona.nombre;
        clone.querySelector(".user_item_rol > p").textContent = persona.rol.nombre;
        clone.querySelector(".user_button_delete").setAttribute('data-enable', persona.idUser);
        lista.appendChild(clone);
    });
}

async function ObtenerUsuariosInactivos(Pagina_Actual) {
    const response = await fetch(`http://localhost:8080/Prueba_JSP_war_exploded/usuarios?Pagination=${Pagina_Actual}&estado=inactivo`);

    if (response.ok) {
        const data = await response.json();

        if (data.usuarios.length === 0) {
            console.log('No se encontraron personas inactivas.');
            return { usuarios: [], totalPaginas: 0, paginaActual: 0, totalRegistros: 0 }; // Respuesta vacía con estructura adecuada
        } else {
            // Retorna los datos obtenidos
            return {
                usuarios: data.usuarios,
                totalPaginas: data.totalPaginas,
                paginaActual: data.paginaActual,
                totalRegistros: data.totalRegistros
            };
        }
    } else {
        console.log('Error al obtener usuarios inactivos.');
        return { usuarios: [], totalPaginas: 0, paginaActual: 0, totalRegistros: 0 }; // Respuesta vacía con estructura adecuada
    }
}
async function habilitarUsuario(persona) {
    console.log(persona)
    const id_persona = persona.idUser;
    console.log(id_persona)
    const data = {
        "action": "change",
        "id": id_persona
    }
    try {
        const response=  await sendRequest("/Prueba_JSP_war_exploded/usuarios", data);
        if (response.status === "success") {
            const user_item = document.querySelector(`[data-user="${persona.idUser}"]`);
            user_item.innerHTML = "";
            console.log("Modelo creado correctamente");
            showSuccessAlert(response.message);
        } else {
            showErrorDialog(response.message);
        }
    } catch (error) {
        console.error("Error al enviar la solicitud:", error);
    }
}
