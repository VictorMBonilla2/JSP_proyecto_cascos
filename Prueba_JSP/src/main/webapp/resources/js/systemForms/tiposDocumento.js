import {sendRequest} from "../ajax.js";
import {host} from "../config.js";
import {showConfirmationDialog} from "../alerts/confirm.js";
import {showSuccessAlert} from "../alerts/success.js";
import {showErrorDialog} from "../alerts/error.js";
import {validarTexto} from "../utils/validations.js";

document.addEventListener("DOMContentLoaded",  async ()=>{

    const selectTipoDocumento = document.querySelector("#item_selector");
    const inputNombreDocumento = document.querySelector("#nameDocument_input");
    const deleteButton= document.querySelector("#delete_button");


    const data = await obtenerTipoDocumentos();

    if (data.length === 0) {
        console.log('No se encontraron documentos.');
    } else {
        console.log(data)
        data.forEach(documento => {
            const option = document.createElement('option');
            option.value = documento.id_documento;
            option.textContent = documento.nombre_documento;
            selectTipoDocumento.appendChild(option);
        });
        //introducir espacios del primer sector de la lista
        inputNombreDocumento.value=data[0].nombre_documento;

    }

    //escuchar cambios en el selector de sectores
    selectTipoDocumento.addEventListener("change",(e)=>{
        const id_documento = e.target.value;
        const dato_tipoDocumento = data.find(tipodocumento => tipodocumento.id_documento === parseInt(id_documento) );
        inputNombreDocumento.value=dato_tipoDocumento.nombre_documento;

    })

    deleteButton.addEventListener("click",(e)=>{
        const form = document.querySelector(".formulario");
        const formData = new FormData(form);
        showConfirmationDialog(
            "Eliminar Tipo de Documento?",
            "El sistema no permitira la eliminación de tipos de documentos en uso. Esta Acción es irreversible.",
            ()=>eliminarTipoDocumento(formData),
            () => console.log('Acción cancelada')
        )
    })

    document.addEventListener("submit",(e)=>{
        e.preventDefault();
        const targerForm= e.target;

        const form = new FormData(targerForm);
        const tipo=form.get("formType");

        if (validarFormulario(form)) {
            if (tipo === "add") {
                addTipoDocumento(form);
            }
            if (tipo === "edit") {
                editTipoDocumento(form);
            }
        }
    })


})

async function addTipoDocumento (form) {
    const nombreDocumento= form.get("nombreDocumento")

    const data = {
        action : "add",
        nombreDocumento: nombreDocumento,
    };

    const response= await sendRequest( `${host}/tipoDoc`,data)
    console.log(response)
    if(response.status === "success"){
        console.log("Se ha Actualizado el tipo de documento correctamente")
        showSuccessAlert(response.message)
    }else{
        showErrorDialog(response.message)
    }
}
async function editTipoDocumento (form) {
    const id_documento= form.get("documentoSelect")
    const nombreDocumento= form.get("nombreDocumento")
    const data = {
        action : "edit",
        idDocumento: id_documento,
        nombreDocumento: nombreDocumento,
    };
    const response= await sendRequest( `${host}/tipoDoc`,data)
    console.log(response)
    if(response.status === "success"){
        console.log("Se ha Actualizado el tipo de documento correctamente")
        showSuccessAlert(response.message)
    }else{
        showErrorDialog(response.message)
    }
}

async function eliminarTipoDocumento (form){
    const id_documento= form.get("documentoSelect")

    const data = {
        action : "delete",
        idDocumento: id_documento,
    };
    const response= await sendRequest( `${host}/tipoDoc`,data)

    if(response.status === "success"){
        console.log("Se ha Eliminado el tipo de documento correctamente")
        showSuccessAlert(response.message)
    }else {
        showErrorDialog(response.message)
    }
}


async function obtenerTipoDocumentos() {
    const response = await fetch(`${host}/tipoDoc`);
    if (response.status === 204) {
        return [];
    }
    return await response.json();
}
function validarFormulario(form) {
    const nombreDocumento = form.get("nombreDocumento");
    // Validar que el nombre del documento solo contenga letras y no esté vacío
    if (!validarTexto(nombreDocumento, 2)) {
        showErrorDialog("El nombre del documento debe contener solo letras y tener al menos 2 caracteres.");
        return false;
    }
    return true; // Si todo es correcto
}

