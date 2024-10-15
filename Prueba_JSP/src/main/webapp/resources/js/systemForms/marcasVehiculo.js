import {hideLoadingSpinner, sendRequest, showLoadingSpinner} from "../ajax.js";
import {host} from "../config.js";
import {showConfirmationDialog} from "../alerts/confirm.js";
import {showSuccessAlert} from "../alerts/success.js";
import {showErrorDialog} from "../alerts/error.js";
import {cargarMarcas} from "../utils/renderSelects.js";
import {validarTexto} from "../utils/validations.js";

document.addEventListener("DOMContentLoaded", async () => {
    const tipoVehiculoSelect = document.querySelector("#tipo_selector");
    const marcaVehiculoSelect = document.querySelector("#marca_selector");
    const inputNombreMarca = document.querySelector("#nameMarca_input");
    const deleteButton = document.querySelector("#delete_button");

    await cargarTiposVehiculo()

    tipoVehiculoSelect.addEventListener('change', async function() {
        console.log("Tipo de vehiculo selecciondado. Buscando modelos...");
        const tipoVehiculoId = tipoVehiculoSelect.value;
        inputNombreMarca.value='';
        await cargarMarcas(tipoVehiculoId,'marca_selector')
    });

    // Registrar el evento change para marcaVehiculoSelect una sola vez
    marcaVehiculoSelect.addEventListener("change", (e) => {
        const id_marca = e.target.value;
        const dato_marca = marcaVehiculoSelect.dataset.marcaData; // Usar data almacenada para obtener las marcas
        const marcaEncontrada = JSON.parse(dato_marca).find(marca => marca.id_Marca === parseInt(id_marca)); // Parsear y buscar
        if (marcaEncontrada) {
            inputNombreMarca.value = marcaEncontrada.nombre_Marca; // Asegurarse de que coincida con 'nombre_Marca'
        }
    });



    deleteButton.addEventListener("click", (e) => {
        const form = document.querySelector(".formulario");
        const formData = new FormData(form);
        showConfirmationDialog(
            "Eliminar Marca?",
            "Esta acción es irreversible. Asegúrese de que la marca no esté en uso.",
            () => eliminarMarca(formData),
            () => console.log('Acción cancelada')
        );
    });

    document.addEventListener("submit", async (e) => {
        e.preventDefault();
        const targetForm = e.target;

        const form = new FormData(targetForm);
        const tipo = form.get("formType");
        const submitButton = e.target.querySelector("button[type='submit']");
        submitButton.disabled = true;
        showLoadingSpinner()
        try{
            if(validarFormulario(form)){
                if (tipo === "add") {
                   await addMarca(form);
                }
                if (tipo === "edit") {
                    await editMarca(form);
                }
            }
        }finally {
            hideLoadingSpinner()
            submitButton.disabled = false;
        }
    });
});

async function addMarca(form) {
    const tipoSelect = form.get("tipoSelect");
    const nombreMarca = form.get("nombreMarca");

    const data = {
        action: "add",
        nombreMarca: nombreMarca,
        idTipoVehiculo: tipoSelect
    };

    const response = await sendRequest(`${host}/listaMarcas`, data);
    console.log(response);
    if (response.status === "success") {
        console.log("Marca creada correctamente");
        showSuccessAlert(response.message);
    } else {
        showErrorDialog(response.message);
    }
}

async function editMarca(form) {
    const id_marca = form.get("marcaSelect");
    const tipoSelect = form.get("tipoSelect");
    const nombreMarca = form.get("nombreMarca");

    const data = {
        action: "edit",
        idMarca: id_marca,
        nombreMarca: nombreMarca,
        idTipoVehiculo:tipoSelect
    };

    const response = await sendRequest(`${host}/listaMarcas`, data);
    console.log(response);
    if (response.status === "success") {
        console.log("Marca actualizada correctamente");
        showSuccessAlert(response.message);
    } else {
        showErrorDialog(response.message);
    }
}

async function eliminarMarca(form) {
    const id_marca = form.get("marcaSelect");

    const data = {
        action: "delete",
        idMarca: id_marca,
    };

    const response = await sendRequest(`${host}/listaMarcas`, data);

    if (response.status === "success") {
        console.log("Marca eliminada correctamente");
        showSuccessAlert(response.message);
    } else {
        showErrorDialog(response.message);
    }
}

async function cargarTiposVehiculo() {
    try {
        const response = await fetch(`${host}/tiposVehiculos`);
        if (!response.ok) {
            throw new Error('Error al cargar los tipos de vehículos');
        }

        const tiposVehiculo = await response.json();

        const tipoVehiculoSelect = document.getElementById('tipo_selector');
        const tipoVehiculoNewSelect = document.querySelector("#tipoNew_selector");


        tiposVehiculo.forEach(tipo => {
            const option1 = document.createElement('option');
            option1.value = tipo.id_Tipo;
            option1.textContent = tipo.nombre_Tipo;
            tipoVehiculoSelect.appendChild(option1);

            // Clonar el option para el segundo select
            const option2 = option1.cloneNode(true);
            tipoVehiculoNewSelect.appendChild(option2);
        });

        if (tiposVehiculo.length > 0) {
            tipoVehiculoSelect.selectedIndex = 0;
            tipoVehiculoSelect.dispatchEvent(new Event('change')); // Disparar el evento change
        }
    } catch (error) {
        console.error('Error al cargar los tipos de vehículos:', error);
    }
}
function validarFormulario(form) {
    const nombreMarca = form.get("nombreMarca");
    const tipoSelect = form.get("tipoSelect");
    const idMarca = form.get("marcaSelect");

    // Validar que el nombre de la marca solo contenga letras y no esté vacío
    if (!validarTexto(nombreMarca, 2)) {
        showErrorDialog("El nombre de la marca debe contener solo letras y tener al menos 2 caracteres.");
        return false;
    }

    // Validar que el tipo de vehículo sea un número válido
    if (tipoSelect && isNaN(tipoSelect)) {
        showErrorDialog("El tipo de vehículo seleccionado no es válido.");
        return false;
    }

    // Validar que el ID de la marca (en caso de editar) sea un número válido
    if (idMarca && isNaN(idMarca)) {
        showErrorDialog("El ID de la marca no es válido.");
        return false;
    }

    return true; // Si todo es correcto
}
