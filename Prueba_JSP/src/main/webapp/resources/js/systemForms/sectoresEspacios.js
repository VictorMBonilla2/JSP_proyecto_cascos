import {sendRequest} from "../ajax.js";
import {host} from "../config.js";
import {showConfirmationDialog} from "../alerts/confirm.js";
import {showSuccessAlert} from "../alerts/success.js";
import {showErrorDialog} from "../alerts/error.js";

document.addEventListener("DOMContentLoaded",  async ()=>{

    const selectSector = document.querySelector("#item_selector");
    const inputEspacio = document.querySelector("#space_input");
    const nameSector= document.querySelector("#name_input");
    const deleteButton= document.querySelector("#delete_button");


    const data = await obtenerSectores();

    if (data.length === 0) {
        console.log('No se encontraron sectores.');
    } else {
        console.log(data)
        data.forEach(sector => {
            const option = document.createElement('option');
            option.value = sector.id_sector;
            option.textContent = sector.nombre_sector;
            selectSector.appendChild(option);

        });
        //introducir espacios del primer sector de la lista
        inputEspacio.value=data[0].cant_espacio;
        nameSector.value=data[0].nombre_sector;

    }

    //escuchar cambios en el selector de sectores
    selectSector.addEventListener("change",(e)=>{
        const id_sector = e.target.value;
        console.log(id_sector)
        const dato_sector = data.find(sector => sector.id_sector === parseInt(id_sector) );
        inputEspacio.value=dato_sector.cant_espacio;
        nameSector.value=dato_sector.nombre_sector;

    })

    deleteButton.addEventListener("click",(e)=>{

        const form = document.querySelector(".formulario");

        // Si necesitas obtener los datos del formulario, puedes usar FormData
        const formData = new FormData(form);
        showConfirmationDialog(
            "Eliminar Sector?",
            "Antes de proceder, verifica si todos los espacios del sector estan libres. Esta Acción es irreversible.",
            ()=>eliminarSector(formData),
            () => console.log('Acción cancelada')
        )


    })

    document.addEventListener("submit",(e)=>{
        e.preventDefault();
        const targerForm= e.target;

        const form = new FormData(targerForm);
        const tipo=form.get("formType");

        if(tipo ==="add"){
            addSector(form)
        }
        if(tipo ==="edit"){
            editSector(form)
        }
    })


})

async function addSector (form) {
    const nombreSector= form.get("nombre_sector")
    const cantidadEspacio= form.get("cantidad_espacio")

    const data = {
        action : "add",
        nombreSector: nombreSector,
        cantidadEspacio: cantidadEspacio,
    };

    const response= await sendRequest( `${host}/SvSector`,data)
    console.log(response)
    if(response.status === "success"){
        console.log("Se ha Actualizado el sector correctamente")
        showSuccessAlert(response.message)
    }else{
        showErrorDialog(response.message)
    }
}
async function editSector (form) {
    const id_sector= form.get("sector_select")
    const nombreSector= form.get("nombre_sector")
    const cantidadEspacio= form.get("cantidad_espacio")

    console.log(cantidadEspacio)
    const data = {
        action : "edit",
        idSector: id_sector,
        nombreSector: nombreSector,
        cantidadEspacio: cantidadEspacio,
    };
    const response= await sendRequest( `${host}/SvSector`,data)
    console.log(response)
    if(response.status === "success"){
        console.log("Se ha Actualizado el sector correctamente")
        showSuccessAlert(response.message)
    }else{
        showErrorDialog(response.message)
    }
}

async function eliminarSector (form){
    const id_sector= form.get("sector_select")

    const data = {
        action : "delete",
        idSector: id_sector,
    };
    const response= await sendRequest( `${host}/SvSector`,data)

    if(response.status === "success"){
        console.log("Se ha Actualizado el sector correctamente")
        showSuccessAlert(response.message)
    }else {
        showErrorDialog(response.message)
    }
}


async function obtenerSectores() {
    const response = await fetch(`${host}/SvSector`);
    if (response.status === 204) {
        return [];
    }
    return await response.json();
}


