import {sendRequest} from "../ajax.js";
import {host} from "../config.js";
import {showConfirmationDialog} from "../alerts/confirm.js";
import {showSuccessAlert} from "../alerts/success.js";
import {showErrorDialog} from "../alerts/error.js";

document.addEventListener("DOMContentLoaded",  async ()=>{

    const selectCiudad = document.querySelector("#item_selector");
    const inputNombreRol = document.querySelector("#nameCiudad_input");
    const deleteButton= document.querySelector("#delete_button");


    const data = await obtenerCiudades();

    if (data.length === 0) {
        console.log('No se encontraron ciudades.');
    } else {
        console.log(data)
        data.forEach(ciudad => {
            const option = document.createElement('option');
            option.value = ciudad.id_ciudad;
            option.textContent = ciudad.nombre_ciudad;
            selectCiudad.appendChild(option);
        });
        //introducir espacios del primer sector de la lista
        inputNombreRol.value=data[0].nombre_ciudad;

    }

    //escuchar cambios en el selector de sectores
    selectCiudad.addEventListener("change",(e)=>{
        const id_ciudad = e.target.value;
        const dato_ciudad = data.find(rol => rol.id_ciudad === parseInt(id_ciudad) );
        inputNombreRol.value=dato_ciudad.nombre_ciudad;

    })

    deleteButton.addEventListener("click",(e)=>{
        const form = document.querySelector(".formulario");
        const formData = new FormData(form);
        showConfirmationDialog(
            "Eliminar Ciudad?",
            "El sistema no permitira la eliminación de ciudades en uso. Esta Acción es irreversible.",
            ()=>eliminarCiudad(formData),
            () => console.log('Acción cancelada')
        )
    })

    document.addEventListener("submit",(e)=>{
        e.preventDefault();
        const targerForm= e.target;

        const form = new FormData(targerForm);
        const tipo=form.get("formType");

        if(tipo ==="add"){
            addCiudad(form)
        }
        if(tipo ==="edit"){
            editCiudad(form)
        }
    })


})

async function addCiudad (form) {
    const nombreCiudad= form.get("nombreCiudad")

    const data = {
        action : "add",
        nombreCiudad: nombreCiudad,
    };

    const response= await sendRequest( `${host}/listaCiudades`,data)
    console.log(response)
    if(response.status === "success"){
        console.log("Se ha Actualizado el rol correctamente")
        showSuccessAlert(response.message)
    }else{
        showErrorDialog(response.message)
    }
}
async function editCiudad (form) {
    const id_ciudad= form.get("ciudadSelect")
    const nombreCiudad= form.get("nombreCiudad")
    const data = {
        action : "edit",
        idCiudad: id_ciudad,
        nombreCiudad: nombreCiudad,
    };
    const response= await sendRequest( `${host}/listaCiudades`,data)
    console.log(response)
    if(response.status === "success"){
        console.log("Se ha Actualizado la ciudad correctamente")
        showSuccessAlert(response.message)
    }else{
        showErrorDialog(response.message)
    }
}

async function eliminarCiudad (form){
    const id_ciudad= form.get("ciudadSelect")

    const data = {
        action : "delete",
        idRol: id_ciudad,
    };
    const response= await sendRequest( `${host}/listaCiudades`,data)

    if(response.status === "success"){
        console.log("Se ha Eliminado la ciudad correctamente")
        showSuccessAlert(response.message)
    }else {
        showErrorDialog(response.message)
    }
}


async function obtenerCiudades() {
    const response = await fetch(`${host}/listaCiudades`);
    if (response.status === 204) {
        return [];
    }
    return await response.json();
}
