import {sendRequest} from "../ajax.js";
import {host} from "../config.js";
import {showConfirmationDialog} from "../alerts/confirm.js";
import {showSuccessAlert} from "../alerts/success.js";
import {showErrorDialog} from "../alerts/error.js";
import {validarTexto} from "../utils/validations.js";

document.addEventListener("DOMContentLoaded",  async ()=>{

    const selectCiudad = document.querySelector("#item_selector");
    const inputNombreRol = document.querySelector("#nameCiudad_input");
    const deleteButton= document.querySelector("#delete_button");


})
document.addEventListener("submit",async (e)=>{
    e.preventDefault();
    const targerForm= e.target;

    const form = new FormData(targerForm);

    await buscarYDescargar(form)

})
async function buscarYDescargar(form){
    const idInforme= form.get("codeinforme");

    const response = await fetch(`${host}/descargarInforme?idInforme=${idInforme}`)

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
}
