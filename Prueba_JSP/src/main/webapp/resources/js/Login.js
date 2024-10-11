import {host} from "./config.js";
import {cargarTiposDocumento} from "./utils/renderSelects.js";
import {sendRequest} from "./ajax.js";
import {validarDocumento, validarTextoNumeros} from "./utils/validations.js";

const logo = document.querySelector("#logo_icon");
const texto_logo = document.querySelector(".logo__text");
const background = document.querySelector(".background_login");
const form = document.getElementById("Logeo");

document.addEventListener("DOMContentLoaded",  async () => {
    // Manejador del click en el logo para alternar el rol
    logo.addEventListener("click", () => {
        const isAprendiz = background.classList.toggle("background_login_aprendiz");
        texto_logo.textContent = isAprendiz ? "Aprendiz" : "Colaborador";
    });
    logo.addEventListener("dblclick", ()=>{
        console.log("has presionado 2 veces")
        const isAprendiz = background.classList.toggle("background_login_Admin");
        texto_logo.textContent = "Administrador"
    });
    await cargarTiposDocumento("TipoDocumento")
    form.addEventListener("submit", (e)=>{
        e.preventDefault();
        const form = new FormData(e.target)
        if(validarFormulario(form)){
            validarIngreso(form);
        }

    })


});

async function validarIngreso(form){
    let rol = background.classList.contains("background_login_aprendiz") ? "2" : "1";
    if(background.classList.contains("background_login_Admin")){
        rol = "3";
    }
    const data = {
        action: "login",
        documento: form.get("documento"),
        tipoDocumento: form.get("TipoDocumento"),
        password : form.get("password"),
        rol: rol
   }
    const respuesta= await sendRequest(`${host}/SvPersona`,data)
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


