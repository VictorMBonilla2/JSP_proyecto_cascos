import {hideLoadingSpinner, sendRequest, showLoadingSpinner} from "../ajax.js";
import {host} from "../config.js";
import {showConfirmationDialog} from "../alerts/confirm.js";
import {showSuccessAlert} from "../alerts/success.js";
import {showErrorDialog} from "../alerts/error.js";
import {validarTexto} from "../utils/validations.js";

document.addEventListener("DOMContentLoaded", async () => {
    const selectTipoVehiculo = document.querySelector("#item_selector");
    const inputNombreVehiculo = document.querySelector("#nameVehiculo_input");
    const deleteButton = document.querySelector("#delete_button");

    // Obtener la lista de tipos de vehículos
    const data = await obtenerTiposVehiculos();

    if (data.length === 0) {
        console.log('No se encontraron tipos de vehículos.');
    } else {
        console.log(data);
        data.forEach(tipo => {
            const option = document.createElement('option');
            option.value = tipo.id_Tipo;
            option.textContent = tipo.nombre_Tipo;
            selectTipoVehiculo.appendChild(option);
        });
        // Introducir nombre del primer tipo en el input
        inputNombreVehiculo.value = data[0].nombre_Tipo;
    }

    // Escuchar cambios en el selector de tipos de vehículo
    selectTipoVehiculo.addEventListener("change", (e) => {
        const id_Tipo = e.target.value;
        const dato_tipo = data.find(tipo => tipo.id_Tipo === parseInt(id_Tipo));
        inputNombreVehiculo.value = dato_tipo.nombre_Tipo;
    });

    // Manejar el botón de eliminar tipo de vehículo
    deleteButton.addEventListener("click", () => {
        const form = document.querySelector(".formulario");
        const formData = new FormData(form);
        showConfirmationDialog(
            "¿Eliminar Tipo de Vehículo?",
            "Esta acción no se puede deshacer.",
            () => eliminarTipoVehiculo(formData),
            () => console.log('Acción cancelada')
        );
    });

    // Manejar los envíos de formularios
    document.addEventListener("submit", async (e) => {
        e.preventDefault();
        const targetForm = e.target;
        const form = new FormData(targetForm);
        const tipo = form.get("formType");
        const submitButton = e.target.querySelector("button[type='submit']");
        submitButton.disabled = true;
        showLoadingSpinner()
        try{
            if (validarFormulario(form)) {
                if (tipo === "add") {
                    await  addTipoVehiculo(form);
                }
                if (tipo === "edit") {
                    await editTipoVehiculo(form);
                }
            }
        }finally {
            hideLoadingSpinner()
            submitButton.disabled = false;
        }

    });
});

// Función para obtener los tipos de vehículos
async function obtenerTiposVehiculos() {
    const response = await fetch(`${host}/tiposVehiculos`);
    if (response.status === 204) {
        return [];
    }
    return await response.json();
}

// Función para añadir un nuevo tipo de vehículo
async function addTipoVehiculo(form) {
    const nombreVehiculo = form.get("nombreVehiculo");

    const data = {
        action: "add",
        nombreVehiculo: nombreVehiculo,
    };

    const response = await sendRequest(`${host}/tiposVehiculos`, data);
    console.log(response);
    if (response.status === "success") {
        console.log("Tipo de vehículo creado correctamente");
        showSuccessAlert(response.message);
    } else {
        showErrorDialog(response.message);
    }
}

// Función para editar un tipo de vehículo
async function editTipoVehiculo(form) {
    const id_Tipo = form.get("tipoVehiculoSelect");
    const nombreVehiculo = form.get("nombreVehiculo");

    const data = {
        action: "edit",
        idTipo: id_Tipo,
        nombreVehiculo: nombreVehiculo,
    };

    const response = await sendRequest(`${host}/tiposVehiculos`, data);
    console.log(response);
    if (response.status === "success") {
        console.log("Tipo de vehículo actualizado correctamente");
        showSuccessAlert(response.message);
    } else {
        showErrorDialog(response.message);
    }
}

// Función para eliminar un tipo de vehículo
async function eliminarTipoVehiculo(form) {
    const id_Tipo = form.get("tipoVehiculoSelect");

    const data = {
        action: "delete",
        idTipo: id_Tipo,
    };

    const response = await sendRequest(`${host}/tiposVehiculos`, data);
    if (response.status === "success") {
        console.log("Tipo de vehículo eliminado correctamente");
        showSuccessAlert(response.message);
    } else {
        showErrorDialog(response.message);
    }
}

function validarFormulario(form) {
    const nombreVehiculo = form.get("nombreVehiculo");
    const idTipoVehiculo = form.get("tipoVehiculoSelect"); // Solo para el caso de editar

    // Validar que el nombre del vehículo solo contenga letras y no esté vacío
    if (!validarTexto(nombreVehiculo, 2)) {
        showErrorDialog("El nombre del vehículo debe contener solo letras y tener al menos 2 caracteres.");
        return false;
    }

    // Validar que el ID del tipo de vehículo (en caso de editar) sea un número válido
    if (idTipoVehiculo && isNaN(idTipoVehiculo)) {
        showErrorDialog("El ID del tipo de vehículo no es válido.");
        return false;
    }

    return true; // Si todo es correcto
}
