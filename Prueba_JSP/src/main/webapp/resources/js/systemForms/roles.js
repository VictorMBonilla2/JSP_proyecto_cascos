import {hideLoadingSpinner, sendRequest, showLoadingSpinner} from "../ajax.js";
import {host} from "../config.js";
import {showConfirmationDialog} from "../alerts/confirm.js";
import {showSuccessAlert} from "../alerts/success.js";
import {showErrorDialog} from "../alerts/error.js";
import {validarTexto} from "../utils/validations.js";

document.addEventListener("DOMContentLoaded",  async ()=>{

    const selectRol = document.querySelector("#item_selector");
    const inputNombreRol = document.querySelector("#nameRol_input");
    const deleteButton= document.querySelector("#delete_button");


    const data = await obtenerTipoDocumentos();

    if (data.length === 0) {
        console.log('No se encontraron roles.');
    } else {
        console.log(data)
        data.forEach(rol => {
            const option = document.createElement('option');
            option.value = rol.id_rol;
            option.textContent = rol.nombre_rol;
            selectRol.appendChild(option);
        });
        //introducir espacios del primer sector de la lista
        inputNombreRol.value=data[0].nombre_rol;

    }

    //escuchar cambios en el selector de sectores
    selectRol.addEventListener("change",(e)=>{
        const id_rol = e.target.value;
        const dato_rol = data.find(rol => rol.id_rol === parseInt(id_rol) );
        inputNombreRol.value=dato_rol.nombre_rol;

    })

    deleteButton.addEventListener("click",(e)=>{
        const form = document.querySelector(".formulario");
        const formData = new FormData(form);
        showConfirmationDialog(
            "Eliminar rol?",
            "El sistema no permitira la eliminación de roles en uso. Esta Acción es irreversible.",
            ()=>eliminarRol(formData),
            () => console.log('Acción cancelada')
        )
    })

    document.addEventListener("submit",async (e)=>{
        e.preventDefault();
        const targerForm= e.target;

        const form = new FormData(targerForm);
        const tipo=form.get("formType");
        const submitButton = e.target.querySelector("button[type='submit']");
        submitButton.disabled = true;
        showLoadingSpinner()
        try{
            if (validarFormulario(form)) {
                if(tipo ==="add"){
                    await addRol(form)
                }
                if(tipo ==="edit"){
                    await editRol(form)
                }
            }
        }finally {
            hideLoadingSpinner()
            submitButton.disabled = false;
        }

    })


})

async function addRol (form) {
    const nombreRol= form.get("nombreRol")

    const data = {
        action : "add",
        nombreRol: nombreRol,
    };

    const response= await sendRequest( `${host}/listaRoles`,data)
    console.log(response)
    if(response.status === "success"){
        console.log("Se ha Actualizado el rol correctamente")
        showSuccessAlert(response.message)
    }else{
        showErrorDialog(response.message)
    }
}
async function editRol (form) {
    const id_rol= form.get("rolSelect")
    const nombreRol= form.get("nombreRol")
    const data = {
        action : "edit",
        idRol: id_rol,
        nombreRol: nombreRol,
    };
    const response= await sendRequest( `${host}/listaRoles`,data)
    console.log(response)
    if(response.status === "success"){
        console.log("Se ha Actualizado el rol correctamente")
        showSuccessAlert(response.message)
    }else{
        showErrorDialog(response.message)
    }
}

async function eliminarRol (form){
    const id_rol= form.get("rolSelect")

    const data = {
        action : "delete",
        idRol: id_rol,
    };
    const response= await sendRequest( `${host}/listaRoles`,data)

    if(response.status === "success"){
        console.log("Se ha Eliminado el rol correctamente")
        showSuccessAlert(response.message)
    }else {
        showErrorDialog(response.message)
    }
}
async function obtenerTipoDocumentos() {
    const response = await fetch(`${host}/listaRoles`);
    if (response.status === 204) {
        return [];
    }
    return await response.json();
}
function validarFormulario(form){
    const nombreRol= form.get("nombreRol")
    if(!validarTexto(nombreRol, 2)){
        showErrorDialog("El nombre del rol debe contener solo letras y tener al menos 2 caracteres.");
        return false;
    }
    return true
}