import {host} from "./config.js";
import {cargarTiposDocumento} from "./utils/renderSelects.js";
import {hideLoadingSpinner, sendRequest, showLoadingSpinner} from "./ajax.js";
import {validarDocumento} from "./utils/validations.js";

const logo = document.querySelector("#logo_icon");
const texto_logo = document.querySelector(".logo__text");
const background = document.querySelector(".background_login");
const form = document.getElementById("Logeo");

document.addEventListener("DOMContentLoaded",  async () => {
    const roles = ["Aprendiz", "Colaborador"];
    let isAdmin = false; // Control para saber si estamos en modo Administrador
    let currentRoleIndex = 0;

    // Función para alternar entre Aprendiz y Colaborador
    function alternarRol() {
        if (!isAdmin) { // Solo alternar si no estamos en modo Administrador
            currentRoleIndex = (currentRoleIndex + 1) % roles.length;
            const role = roles[currentRoleIndex];

            // Cambiar el fondo y el texto según el rol
            if (role === "Aprendiz") {
                background.classList.add("background_login_aprendiz");
                background.classList.remove("background_login_colaborador", "background_login_Admin");
            } else {
                background.classList.add("background_login_colaborador");
                background.classList.remove("background_login_aprendiz", "background_login_Admin");
            }
            texto_logo.textContent = role; // Actualiza el texto
        }
    }

    // Función para entrar o salir del modo Administrador
    function toggleAdministrador() {
        isAdmin = !isAdmin; // Cambia el estado de Administrador (on/off)

        if (isAdmin) {
            background.classList.add("background_login_Admin");
            background.classList.remove("background_login_aprendiz", "background_login_colaborador");
            texto_logo.textContent = "Administrador";
        } else {
            // Si salimos del modo Admin, volvemos al estado alternante
            alternarRol(); // Alternar entre Aprendiz y Colaborador al salir de Admin
        }
    }

    // Evento para alternar entre Aprendiz y Colaborador con un clic
    logo.addEventListener("click", alternarRol);

    // Evento para alternar Administrador con doble clic
    logo.addEventListener("dblclick", () => {
        toggleAdministrador();
    });

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


