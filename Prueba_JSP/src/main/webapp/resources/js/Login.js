import {host} from "./config.js";
import {cargarTiposDocumento} from "./utils/renderSelects.js";
import {hideLoadingSpinner, sendRequest, showLoadingSpinner} from "./ajax.js";
import {validarDocumento} from "./utils/validations.js";

const logo = document.querySelector("#logo_icon");
const texto_logo = document.querySelector(".logo__text");
const background = document.querySelector(".background_login");
const form = document.getElementById("Logeo");

document.addEventListener("DOMContentLoaded",  async () => {
    await cargarTiposDocumento("TipoDocumento")
    form.addEventListener("submit", async (e)=>{
        e.preventDefault();
        const form = new FormData(e.target)
        const submitButton = e.target.querySelector("button[type='submit']");
        submitButton.disabled = true;
        showLoadingSpinner()
        try{
            if(validarFormulario(form)){
               await validarIngreso(form);
            }
        } finally {
            hideLoadingSpinner()
            submitButton.disabled = false;
        }


    })
});

async function validarIngreso(form){
    const data = {
        action: "login",
        documento: form.get("documento"),
        tipoDocumento: form.get("TipoDocumento"),
        password : form.get("password"),
   }
    const respuesta= await sendRequest(`${host}/SvPersona`,data,"submitButton")
    if (respuesta.status === "success") {
        // Redireccionar según el rol del usuario
        window.location.href = "Home.jsp";
    } else {
        showError(respuesta.message)
        console.log(respuesta)
    }
}
function showError(message) {
    // Actualizar el contenido del mensaje de error
    document.getElementById("errorMessage").textContent = message;

    // Mostrar el contenedor de error
    document.getElementById("Error").style.display = "block";
}
function validarFormulario(form) {
    const documento = form.get("documento");
    const tipoDocumento = form.get("TipoDocumento");

    // Validar que el documento solo tenga números y sea positivo
    if (!validarDocumento(documento)) {
        showError("El documento debe ser un número positivo.");
        return false;
    }

    // Validar que el tipo de documento sea un número válido
    if (isNaN(tipoDocumento) || tipoDocumento <= 0) {
        showError("El tipo de documento debe ser un número válido.");
        return false;
    }

    return true; // Si todas las validaciones pasan
}


