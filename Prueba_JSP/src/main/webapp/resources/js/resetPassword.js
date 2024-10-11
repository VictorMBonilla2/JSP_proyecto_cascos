import {sendRequest} from "./ajax.js";
import {host} from "./config.js";
import {showSuccessAlert} from "./alerts/success.js";
import {showErrorDialog} from "./alerts/error.js";
import {validarTexto, validarTextoNumeros} from "./utils/validations.js";

document.addEventListener("DOMContentLoaded",   ()=>{

    const formulario = document.querySelector("#recovery");

    formulario.addEventListener("submit",async(e)=>{
        e.preventDefault()
        const form = new FormData(e.target)
        if(validarFormulario(form)){
            await enviarpassword(form)
        }

    })

})
async function enviarpassword(form){
    const password = form.get("password")
    const token = form.get("token")

    const data ={
        action: "recovery",
        nuevaPassword: password,
        token: token
    }
    const response= await sendRequest(`${host}/resetPassword`, data)

    if (response.status === "success") {
        console.log("Modelo creado correctamente");
        showSuccessAlert(response.message);
    } else {
        showErrorDialog(response.message);
    }
}

function validarFormulario(form) {
    const password = form.get("password")

    if (!validarTextoNumeros(password, 7)) {
        showErrorDialog("La contraseña debe ser de al menos 8 caracteres.");
        return false;
    }
    return true

}