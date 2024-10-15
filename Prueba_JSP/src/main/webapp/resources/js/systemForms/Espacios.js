import {hideLoadingSpinner, sendRequest, showLoadingSpinner} from "../ajax.js";
import {host} from "../config.js";
import {showSuccessAlert} from "../alerts/success.js";
import {showErrorDialog} from "../alerts/error.js";
import {validarCantidadEspacios, validarTextoNumeros} from "../utils/validations.js";

document.addEventListener("DOMContentLoaded",  async () => {
    const selectSector = document.querySelector("#item_selector");
    const selectEspacio = document.querySelector("#space_selector");
    const nameSector = document.querySelector("#name_input");
    const deleteButton = document.querySelector("#delete_button");

    let result = {}; // Declarar result aquí para que sea accesible globalmente

    const data = await obtenerSectores();

    if (data.length === 0) {
        console.log('No se encontraron sectores.');
    } else {
        console.log(data);
        data.forEach(sector => {
            const option = document.createElement('option');
            option.value = sector.id_sector;
            option.textContent = sector.nombre_sector;
            selectSector.appendChild(option);
        });

    }

    // Escuchar cambios en el selector de sectores
    selectSector.addEventListener("change", async (e) => {
        const id_sector = e.target.value;
        console.log(id_sector);
        try {
            // Hacer la solicitud al endpoint con el idSector seleccionado
            const response = await fetch(`${host}/configCasilleros?idSector=${id_sector}`);
            // Parsear la respuesta a JSON
            result = await response.json(); // Ahora 'result' es accesible en todo el bloque
            if (response.ok) {
                console.log(result); // Verificar la respuesta en la consola
                selectEspacio.innerHTML = ""; // Limpiar el select antes de agregar nuevas opciones
                result.forEach(espacio => {
                    const option = document.createElement("option");
                    option.value = espacio.idEspacio; // Usar el id del espacio como valor
                    option.textContent = espacio.nombreEspacio; // Usar el nombre del espacio como texto
                    selectEspacio.appendChild(option);
                });
            } else {
                console.error("Error al obtener los espacios del sector:", result.message);
            }
        } catch (error) {
            console.error("Error al hacer la solicitud:", error);
        }
    });

    // Escuchar cambios en el selector de espacios
    selectEspacio.addEventListener("change", (e) => {
        const idEspacio = e.target.value;

        // Encontrar el espacio seleccionado en los datos obtenidos de result
        const espacioSeleccionado = result.find(espacio => espacio.idEspacio === parseInt(idEspacio));

        if (espacioSeleccionado) {
            const selectEstado = document.querySelector("#estado_selector");

            // Asignar directamente el valor del estado en el select (Activo o Inactivo)
            selectEstado.value = espacioSeleccionado.estadoEspacio === "Libre" ? "Libre" : "Inactivo";
        } else {
            console.error("No se encontró el espacio seleccionado en los datos.");
        }
    });
    selectSector.dispatchEvent(new Event('change'));
});



document.addEventListener("submit",async (e)=>{
    e.preventDefault();
    const targerForm= e.target;
    const form = new FormData(targerForm);
    const submitButton = e.target.querySelector("button[type='submit']");
    submitButton.disabled = true;
    showLoadingSpinner()
    try{

        await uddateEspacio(form);

    }finally {
        hideLoadingSpinner()
        submitButton.disabled = false
    }

})



async function uddateEspacio (form) {
    const idEspacio= form.get("space_selector")
    const estadoEspacio= form.get("estado_selector")

    const data = {
        idEspacio: idEspacio,
        estadoEspacio: estadoEspacio,
    };
    const response= await sendRequest( `${host}/configCasilleros`,data)
    console.log(response)
    if(response.status === "success"){
        console.log("Se ha Actualizado el sector correctamente")
        showSuccessAlert(response.message)
    }else{
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
