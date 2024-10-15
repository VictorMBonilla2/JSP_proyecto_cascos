import {hideLoadingSpinner, sendRequest, showLoadingSpinner} from "../ajax.js";
import {host} from "../config.js";
import {showConfirmationDialog} from "../alerts/confirm.js";
import {showSuccessAlert} from "../alerts/success.js";
import {showErrorDialog} from "../alerts/error.js";
import {validarTexto} from "../utils/validations.js";

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
            option.value = ciudad.id_Ciudad;
            option.textContent = ciudad.nombre_Ciudad;
            selectCiudad.appendChild(option);
        });
        //introducir espacios del primer sector de la lista
        inputNombreRol.value=data[0].nombre_Ciudad;

    }

    //escuchar cambios en el selector de sectores
    selectCiudad.addEventListener("change",(e)=>{
        const id_Ciudad = e.target.value;
        const dato_ciudad = data.find(rol => rol.id_Ciudad === parseInt(id_Ciudad) );
        inputNombreRol.value=dato_ciudad.nombre_Ciudad;

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

    document.addEventListener("submit", async (e)=>{
        e.preventDefault();
        const targerForm= e.target;

        const form = new FormData(targerForm);
        const tipo=form.get("formType");
        const submitButton = e.target.querySelector("button[type='submit']");
        submitButton.disabled = true;
        showLoadingSpinner()
        try{
            if (validarFormulario(form)) {
                if (tipo === "add") {
                  await  addCiudad(form);
                }
                if (tipo === "edit") {
                    await  editCiudad(form);
                }
            }
        }finally {
            hideLoadingSpinner()
            submitButton.disabled = false;
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
        idCiudad: id_ciudad,
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
function validarFormulario(form) {
    const nombreCiudad = form.get("nombreCiudad");
    const idCiudad = form.get("ciudadSelect"); // Solo para el caso de editar

    // Validar que el nombre de la ciudad solo contenga letras y no esté vacío
    if (!validarTexto(nombreCiudad, 2)) {
        showErrorDialog("El nombre de la ciudad debe contener solo letras y tener al menos 2 caracteres.");
        return false;
    }

    // Validar que el ID de la ciudad (en caso de editar) sea un número válido
    if (idCiudad && isNaN(idCiudad)) {
        showErrorDialog("El ID de la ciudad no es válido.");
        return false;
    }

    return true; // Si todo es correcto
}