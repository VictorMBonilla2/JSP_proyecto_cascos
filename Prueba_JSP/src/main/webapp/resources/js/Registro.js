import {host} from "./config.js";
import {showSuccessAlert} from "./alerts/success.js";
import {showErrorDialog} from "./alerts/error.js";
import {hideLoadingSpinner, sendRequest, showLoadingSpinner} from "./ajax.js";
import {
    edadMinima,
    validarCelular,
    validarDocumento,
    validarEmail,
    validarFecha,
    validarTexto
} from "./utils/validations.js";

document.addEventListener("DOMContentLoaded", async function () {
await selectDocumento();

    const formulario = document.getElementById("registro");
    formulario.addEventListener("submit", async (e)=>{
        e.preventDefault();
        const submitButton = e.target.querySelector("button[type='submit']");
        submitButton.disabled = true;
        showLoadingSpinner()
        const form = new FormData(formulario)

        try{
            if(validarFormulario(form)){
                await enviarRegistro(form);
            }
        }finally {
            hideLoadingSpinner()
            submitButton.disabled = false;
        }


    })


});
async function selectDocumento(){
    console.log("se ejecuta la busca de doc")
    const documentoSelect = document.getElementById("TipoDocumento");
    const response = await  fetch(`${host}/tipoDoc`);
    const data = await response.json();
    console.log(data)
    data.forEach(tipoDocumento =>{
        const option = document.createElement("option");
        option.value=tipoDocumento.id_documento;
        option.textContent=tipoDocumento.nombre_documento;
        documentoSelect.appendChild(option)
    })
}


async function enviarRegistro(form){

    const data= {
        action: "registro",
        nombre: form.get("Nombres"),
        apellido: form.get("Apellidos"),
        TipoDocumento: form.get("TipoDocumento"),
        documento: form.get("documento"),
        numeroCelular:form.get("numeroCelular"),
        correo: form.get("correo"),
        password: form.get("password"),
        fechaNacimiento: form.get("fecha")
    }
    console.log(data)
    const response = await sendRequest(`${host}/SvPersona`,data)
    if(response.status === "success"){
        console.log("Se ha Actualizado el sector correctamente")
        showSuccessAlert(response.message)
    }else{
        showErrorDialog(response.message)
    }
}

function validarFormulario(form) {
    console.log(form)
    const nombre = form.get("Nombres")
    const apellido = form.get("Apellidos")
    const correo = form.get("correo")
    const tipoDocumento = form.get("TipoDocumento")
    const numeroDocumento = form.get("documento")
    const celular = form.get("numeroCelular");
    const fechaNacimientoInput = form.get("fecha")
    const password = form.get("password")
    const confirmPassword= form.get("Confirm-passWord");

    // Validar nombre y apellido solo texto
    if (!validarTexto(nombre, 1)) {
        showErrorDialog("El nombre debe ser un texto válido.");
        return false;
    }
    if (!validarTexto(apellido, 1)) {
        showErrorDialog("El apellido debe ser un texto válido.");
        return false;
    }

    if (!validarCelular(celular)){
        showErrorDialog("El celular debe ser un numero válido.");
        return false;
    }

    // Validar correo electrónico
    if (!validarEmail(correo)) {
        showErrorDialog("El correo debe ser un email válido.");
        return false;
    }

    // Validar tipo de documento
    if (!tipoDocumento) {
        showErrorDialog("Debe seleccionar un tipo de documento.");
        return false;
    }

    // Validar número de documento
    if (!validarDocumento(numeroDocumento)) {
        showErrorDialog("El número de documento debe ser un número positivo.");
        return false;
    }

    // Validar fecha de nacimiento (en formato yyyy-mm-dd)
    if (!validarFecha(fechaNacimientoInput)) {
        showErrorDialog("La fecha de nacimiento debe estar en el formato yyyy-mm-dd.");
        return false;
    }
    if(!edadMinima(fechaNacimientoInput)){
        showErrorDialog("Necesitas ser mayor a 16 años para poder registrarte.");
        return false;
    }

    // Validar contraseña no vacía
    if (password.trim() === '') {
        showErrorDialog("La contraseña no puede estar vacía.");
        return false;
    }

    if (!(password === confirmPassword)){
        showErrorDialog("La contraseña no coincide.");
        return false;
    }

    return true; // Si todo es válido
}