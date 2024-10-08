import {sendRequest} from "./ajax.js";
import {host} from "./config.js";
import {showSuccessAlert} from "./alerts/success.js";
import {showErrorDialog} from "./alerts/error.js";
import {validarEmail, validarFecha, validarTexto} from "./utils/validations.js";

const divPerfil = document.querySelector(".main_container__profile");
const divDetalles = document.querySelector(".detalles_user_container");
const infoContainer = document.querySelector("#userinfo");
const infoTemplate = document.querySelector("#infoTemplate").content;
const formTemplate = document.querySelector("#formTemplate").content;
const cancelButtonInfoContainer = document.querySelector("#cancelEdit");
const id_usuario = document.querySelector('#idUsuario').value
document.addEventListener('DOMContentLoaded',  async ()=>{
    const data = await ObtenerUsuario(id_usuario)
    console.log(data)
    document.querySelector('.nameUser').textContent=data.nombreUsuario
    document.querySelector('.rolUser').textContent=data.rol.namerolUsuario
    document.querySelector('.correoUser').textContent=data.correoUsuario
    document.querySelector('.fechaNacUser').textContent=data.fechaNacimiento
    document.querySelector('.tipoDoc').textContent= `Tipo de documento: ${data.tipoDoc.nombreTipoDoc} `
    document.querySelector('.numDoc').textContent=`Numero de documento: ${data.documento} `
    document.addEventListener("click", (ev) => {
        if (ev.target.matches(".info_user_container__button")) {
            mostrarFormulario(data);
        }
    });

})
function mostrarFormulario(data) {
    divPerfil.classList.add("modificado");
    divDetalles.classList.add("oculto");
    setTimeout(() => {
        divDetalles.style.display = 'none';
        infoContainer.innerHTML = ""; // Limpiar el contenedor antes de agregar el formulario

        // Clonar el contenido del template
        const clone = document.importNode(formTemplate, true); // Necesitas pasar 'true' para hacer una clonación profunda
        clone.querySelector('.info_user_container__header h1').textContent=data.nombreUsuario
        clone.querySelector('.info_user_container__header h3').textContent=data.rol.nombreRol
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
            event.preventDefault();
            const form = new FormData(event.target)
            if (validarFormulario(form)) {
                enviar_formulario(form);
            }
        });

        cancelEdit.addEventListener('click',()=>{
            mostrarInformacion(data)
        })

    }, 2100);
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
function mostrarInformacion(data) {
    console.log(data)
    divPerfil.classList.remove("modificado");
    divDetalles.classList.remove("oculto");
    divDetalles.style.opacity = '100%';
    divDetalles.style.display = 'flex';

    // Limpiar el contenedor antes de agregar la información
    infoContainer.innerHTML = "";

    // Clonar el contenido del template de información
    const clone = document.importNode(infoTemplate, true);
    clone.querySelector('.nameUser').textContent=data.nombreUsuario
    clone.querySelector('.rolUser').textContent=data.rol.namerolUsuario
    clone.querySelector('.correoUser').textContent=data.correoUsuario
    clone.querySelector('.fechaNacUser').textContent=data.fechaNacimiento

    // Insertar el template clonado en el contenedor
    infoContainer.appendChild(clone);

    // Si tienes botones en el template de información que requieren eventos,
    // puedes seleccionarlos aquí y asignarles los event listeners necesarios.
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
function validarFormulario(form) {
    const nombre = form.get("nombre");
    const apellido = form.get("apellido");
    const fecha = form.get("fecha_nacimiento");
    const correo = form.get("correo");
    console.log(fecha);
    // Usar las funciones de validación importadas
    if (!validarTexto(nombre, 2)) {
        showErrorDialog("El nombre debe tener al menos 2 caracteres y ser solo letras.");
        return false;
    }

    if (!validarTexto(apellido, 2)) {
        showErrorDialog("El apellido debe tener al menos 2 caracteres y ser solo letras.");
        return false;
    }

    if (!validarEmail(correo)) {
        showErrorDialog("Correo electrónico no válido.");
        return false;
    }

    if (!validarFecha(fecha)) {
        showErrorDialog("Formato de fecha no válido. Debe ser dd/MM/yyyy.");
        return false;
    }

    // Si todo está bien
    return true;
}
