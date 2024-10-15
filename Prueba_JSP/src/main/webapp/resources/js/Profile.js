import {hideLoadingSpinner, sendRequest, showLoadingSpinner} from "./ajax.js";
import {host} from "./config.js";
import {showSuccessAlert} from "./alerts/success.js";
import {showErrorDialog} from "./alerts/error.js";
import {validarCelular, validarEmail, validarFecha, validarTexto} from "./utils/validations.js";
import {showConfirmationDialog} from "./alerts/confirm.js";

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
    document.querySelector('.celularUser').textContent=data.numeroCelular
    document.querySelector('.tipoDoc').textContent= `Tipo de documento: ${data.tipoDoc.nombreTipoDoc} `
    document.querySelector('.numDoc').textContent=`Numero de documento: ${data.documento} `
    document.addEventListener("click", (ev) => {
        if (ev.target.matches(".info_user_container__button")) {
            mostrarFormulario(data);
        }
        if(ev.target.matches("#data-button")){

        }
        if(ev.target.matches("#disable-button")){
            showConfirmationDialog(
                "¿Deshabilitar Cuenta?",
                "Si decides deshabilitar tu cuenta, no podras tener" +
                "acceso a ella. El sistema no permitira la deshabilitación de la " +
                "cuenta si tiene vehiculos estacionados.",
                ()=>deshabilitarCuenta(id_usuario),
                ()=> console.log("Acción cancelada")

            )
        }
        if (ev.target.matches("#info-button")){
            generarInforme(data)
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
        clone.querySelector('#celular').value=data.numeroCelular
        // Insertar el formulario clonado en el contenedor
        infoContainer.appendChild(clone);

        // Mover la configuración del evento aquí después de que el formulario se haya agregado al DOM
        // Ahora seleccionamos los elementos desde el DOM, no desde el 'clone'
        const saveButton = infoContainer.querySelector('#editForm');
        const cancelEdit = infoContainer.querySelector('#cancelEdit');


        saveButton.addEventListener('submit', async (event) => {
            event.preventDefault();
            const submitButton = event.target.querySelector("button[type='submit']");
            submitButton.disabled = true;
            showLoadingSpinner()
            const form = new FormData(event.target)
            try{
                if (validarFormulario(form)) {
                    await enviar_formulario(form);
                }
            }finally {
                hideLoadingSpinner()
                submitButton.disabled = false;
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

    infoContainer.appendChild(clone);

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
    const celular = form.get("celular")
    const data = {
        action: "editPrimaryDAta",
        usuarioId: usuarioId,
        nombre: nombre,
        apellido: apellido,
        fecha: formatearFecha(fecha),
        correo: correo,
        numeroCelular:celular
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
    const celular = form.get("celular");
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
    if (!validarCelular(celular)) {
        showErrorDialog("Numero celular no válido.");
        return false;
    }

    if (!validarFecha(fecha)) {
        showErrorDialog("Formato de fecha no válido. Debe ser dd/MM/yyyy.");
        return false;
    }

    // Si todo está bien
    return true;
}
async function deshabilitarCuenta(idUsuario){
    const data ={
        action: "disable",
        idUsuario: idUsuario
    }
    try{
        const response = await sendRequest(`${host}/SvPersona`,data);
        if (response.status === "success") {
            console.log("Modelo creado correctamente");
            showSuccessAlert(response.message);
        } else {
            showErrorDialog(response.message);
        }
    } catch (error){
        console.log("Error  en la solicitud")
    }
}
async function generarInforme(usuario){
    const data ={
        action: "infoAccount",
        idUsuario: usuario.idUsuario
    }
    try{
        const result = await sendRequest(`${host}/SvPersona`,data);
        console.log(result)

            console.log("Modelo creado correctamente");
            showSuccessAlert(result.message);
            const response = await fetch(`${host}/descargarInforme?idInforme=${result.codigoInforme}`)
            if (response.ok) {
                const blob = await response.blob();
                const url = window.URL.createObjectURL(blob);
                const a = document.createElement('a');
                a.href = url;
                a.download = "informe_usuario.pdf"; // Nombre del archivo descargado
                document.body.appendChild(a); // Necesario para que funcione en Firefox
                a.click();
                a.remove(); // Remover el elemento de anclaje después de la descarga
            } else {
                const result = await response.json();
                showErrorDialog(result.error);  // Mostrar mensaje de error si ocurre
            }
    } catch (error){
        console.log("Error en la solicitud", error )
    }
}