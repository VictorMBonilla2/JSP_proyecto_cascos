import {sendRequest} from "./ajax.js";
import {host} from "./config.js";
import {showSuccessAlert} from "./alerts/success.js";
import {showErrorDialog} from "./alerts/error.js";

const divPerfil = document.querySelector(".main_container__profile");
const divDetalles = document.querySelector(".detalles_user_container");
const divPerfil2 = document.querySelector(".user_container_side2");
const infoContainer = document.querySelector("#userinfo");
const infoTemplate = document.querySelector("#infoTemplate").content;
const formTemplate = document.querySelector("#formTemplate").content;
const buttonsInfoContainer = document.querySelector(".button_miranose");
const cancelButtonInfoContainer = document.querySelector("#cancelEdit");
const id_usuario = document.querySelector('#idUsuario').value



document.addEventListener('DOMContentLoaded',  async ()=>{
    const data = await ObtenerUsuario(id_usuario)
    console.log(data)
    document.querySelector('.nameUser').textContent=data.nombreUsuario
    document.querySelector('.rolUser').textContent=data.rol.namerolUsuario
    document.querySelector('.correoUser').textContent=data.correoUsuario
    document.querySelector('.fechaNacUser').textContent=data.fechaNacimiento

    document.querySelector('.tipoDoc').textContent= `Tipo de documento: ${data.tipodoc.nametipodocUsuario} `
    document.querySelector('.numDoc').textContent=`Numero de documento: ${data.documento} `

async function selectDocumento(){
    const documentoSelect = document.getElementById("TipoDocumento");
    const response = await  fetch('/Prueba_JSP_war_exploded/tipoDoc');
    const data = await response.json();
    console.log(data)
    data.forEach(tipoDocumento =>{
        const option = document.createElement("option");
        option.value=tipoDocumento.id_documento;
        option.textContent=tipoDocumento.nombre_documento;
        documentoSelect.appendChild(option)
    })
}
    document.addEventListener("click", (ev) => {
        if (ev.target.matches(".info_user_container__button")) {
            mostrarFormulario();
        } else if (ev.target === cancelButtonInfoContainer) {
            mostrarInformacion();
        } else {
            console.log("pues no");
        }

    });

function mostrarFormulario() {
    divPerfil.classList.add("modificado");
    divDetalles.classList.add("oculto");
    setTimeout(() => {
        divDetalles.style.display = 'none';
        infoContainer.innerHTML = ""; // Limpiar el contenedor antes de agregar el formulario

        // Clonar el contenido del template
        const clone = document.importNode(formTemplate, true); // Necesitas pasar 'true' para hacer una clonación profunda

        clone.querySelector('#nombre').value=data.nombreUsuario
        clone.querySelector('#apellido').value=data.ApellidoUsuario
        clone.querySelector('#fecha_nacimiento').value=data.fechaNacimiento
        clone.querySelector('#coreo').value=data.correoUsuario
        // Insertar el formulario clonado en el contenedor
        infoContainer.appendChild(clone);

        // Mover la configuración del evento aquí después de que el formulario se haya agregado al DOM
        // Ahora seleccionamos los elementos desde el DOM, no desde el 'clone'
        const saveButton = infoContainer.querySelector('#editForm');
        const cancelEdit = infoContainer.querySelector('#cancelEdit');


        saveButton.addEventListener('submit', async (event) => {
            event.preventDefault(); // Prevenir cualquier comportamiento por defecto
            const form = new FormData(event.target)
            enviar_formulario(form);
        });

        cancelEdit.addEventListener('click',()=>{
            mostrarInformacion()
        })

    }, 2100);
}

async function enviar_formulario(form){

    const usuarioId = document.querySelector("#idUsuario").value;
    const nombre = form.get("nombre");
    const apellido = form.get("apellido");
    const fecha = form.get("fecha_nacimiento");
    const correo = form.get("correo");
    const data = {
        action: "editPrimaryDAta",
        usuarioId: usuarioId,
        nombre: nombre,
        apellido: apellido,
        fecha: formatearFecha(fecha),
        correo: correo,
    };
    console.log(data)
    try {
        const response = await sendRequest(`${host}/SvPersona`, data);
        // Manejar la respuesta si es necesario
        if (response.status === "success") {
            console.log("Modelo creado correctamente");
            showSuccessAlert(response.message);
        } else {
            showErrorDialog(response.message);
        }
    } catch (error) {
        console.error("Error en la solicitud:", error);
    }
}

function mostrarInformacion() {
    divPerfil.classList.remove("modificado");
    divDetalles.classList.remove("oculto");
    divDetalles.style.opacity = '100%';
    divDetalles.style.display = 'flex';

    // Limpiar el contenedor antes de agregar la información
    infoContainer.innerHTML = "";

    // Clonar el contenido del template de información
    const clone = document.importNode(infoTemplate, true);

    // Insertar el template clonado en el contenedor
    infoContainer.appendChild(clone);

    // Si tienes botones en el template de información que requieren eventos,
    // puedes seleccionarlos aquí y asignarles los event listeners necesarios.

    buttonsInfoContainer.style.display = "none";
}
async function ObtenerUsuario(id_usuario) {
    const response = await fetch(`${host}/SvPersona?id_usuario=${id_usuario}`);
    if (response.status === 204) {
        console.log('No se encontraron personas.');
        return [];
    }
    const data = await response.json();
    if (data.length === 0) {
        console.log('No se encontraron Personas.');
    } else {
        return data;
    }
}
    function formatearFecha(fechaOriginal) {
        // Convertir el string a un objeto Date
        const fecha = new Date(fechaOriginal);

        // Obtener día, mes y año
        const dia = String(fecha.getDate()).padStart(2, '0');
        const mes = String(fecha.getMonth() + 1).padStart(2, '0'); // Los meses van de 0 a 11
        const anio = fecha.getFullYear();

        // Formatear la fecha como dd/MM/yyyy
        return `${dia}/${mes}/${anio}`;
    }

})
