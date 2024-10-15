import {hideLoadingSpinner, sendRequest, showLoadingSpinner} from "./ajax.js";
import {host} from "./config.js";
import {showSuccessAlert} from "./alerts/success.js";
import {showErrorDialog} from "./alerts/error.js";

document.addEventListener("DOMContentLoaded",   ()=>{

    const formulario = document.querySelector("#recovery");

    formulario.addEventListener("submit",async(e)=>{
        e.preventDefault()
        const form = new FormData(e.target)
        const submitButton = e.target.querySelector("button[type='submit']");
        submitButton.disabled = true;
        showLoadingSpinner()
        try{
            await enviarCorreo(form)
        }finally {
            hideLoadingSpinner()
            submitButton.disabled = false;
        }

    })

})
async function enviarCorreo(form){
    const correo = form.get("correo")

    const data ={
        action: "recovery",
        correo: correo
    }
    const response= await sendRequest(`${host}/SvRecuperarContrasena`, data)

    if (response.status === "success") {
        console.log("Modelo creado correctamente");
        showSuccessAlert(response.message);
    } else {
        showErrorDialog(response.message);
    }

}