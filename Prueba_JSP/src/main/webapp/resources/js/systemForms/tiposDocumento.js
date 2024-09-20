import {sendRequest} from "../ajax";

document.addEventListener("DOMContentLoaded",  async ()=>{

    const selectSector = document.querySelector("#item_selector");
    const inputEspacio = document.querySelector("#space_input");


    const data = await obtenerSectores();

    if (data.length === 0) {
        console.log('No se encontraron sectores.');
    } else {
        console.log(data)
        data.forEach(sector => {
            const option = document.createElement('option');
            option.value = sector.id_sector;
            option.textContent = sector.id_sector;
            selectSector.appendChild(option);
        });
        //introducir espacios del primer sector de la lista
        inputEspacio.value=data[0].cant_espacio;
    }

    //escuchar cambios en el selector de sectores
    selectSector.addEventListener("change",(e)=>{
        const id_sector = e.target.value;
        console.log(id_sector)
        const dato_usuario = data.find(sector => sector.id_sector === parseInt(id_sector) );
        inputEspacio.value=dato_usuario.cant_espacio;

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

    await sendRequest('SvSectores',data)
}
async function editSector (form) {
    const id_sector= form.get("sector_select")
    const nombreSector= form.get("nombre_sector")
    const cantidadEspacio= form.get("cantidad_espacio")

    const data = {
        action : "edit",
        idSector: id_sector,
        nombreSector: nombreSector,
        cantidadEspacio: cantidadEspacio,
    };
    await sendRequest('SvSectores',data)
}

async function eliminarSector (form){
    const id_sector= form.get("sector_select")

    const data = {
        action : "delete",
        idSector: id_sector,
    };
    await sendRequest('SvSectores',data)
}



async function obtenerSectores () {
    const response = await fetch(`SvSectores`);
    if (response.status === 204) {
        return [];
    }
    return await response.json();
}


