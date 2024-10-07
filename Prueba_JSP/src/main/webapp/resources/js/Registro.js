import {host} from "./config.js";
import {showConfirmationDialog} from "./alerts/confirm.js";
import {showSuccessAlert} from "./alerts/success.js";
import {showErrorDialog} from "./alerts/error.js";
import {sendRequest} from "./ajax.js";

document.addEventListener("DOMContentLoaded", async function () {
await selectDocumento();

    const formulario = document.getElementById("registro");
    formulario.addEventListener("submit",(e)=>{
        e.preventDefault();
        const nombre = document.getElementById("Nombre").value;
        const apellido = document.getElementById("Apellido").value;
        const tipoDocumento = document.getElementById("TipoDocumento").value;
        const documento = document.getElementById("documento").value;
        const correo = document.getElementById("email").value;
        const password = document.getElementById("passWord").value;
        const rol = document.getElementById("rol").value;

        const response = enviarRegistro(new FormData(formulario));
        if(response.status === "success"){
            console.log("Se ha Actualizado el sector correctamente")
            showSuccessAlert(response.message)
        }else{
            showErrorDialog(response.message)
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