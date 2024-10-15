import {hideLoadingSpinner, sendRequest, showLoadingSpinner} from "../ajax.js";
import {host} from "../config.js";
import {showConfirmationDialog} from "../alerts/confirm.js";
import {showSuccessAlert} from "../alerts/success.js";
import {showErrorDialog} from "../alerts/error.js";
import {validarTextoNumeros} from "../utils/validations.js";
import {cargarMarcas, cargarModelos} from "../utils/renderSelects.js";

document.addEventListener("DOMContentLoaded", async () => {
    const tipoVehiculoSelect = document.querySelector("#tipo_selector");
    const marcaVehiculoSelect = document.querySelector("#marca_selector");
    const selectModelo = document.querySelector("#modelo_selector");
    const inputNombreModelo = document.querySelector("#nameModelo_input");
    const deleteButton = document.querySelector("#delete_button");

    const tipoVehiculoSelectnew= document.querySelector("#tipoNew_selector");
    const marcaVehiculoSelectnew = document.querySelector("#marcaNew_selector");
    await cargarTiposVehiculo()
    tipoVehiculoSelectnew.addEventListener('change', async function() {
        console.log("Tipo de vehiculo selecciondado. Buscando modelos...");
        const tipoVehiculoId = tipoVehiculoSelectnew.value;
        await cargarMarcas(tipoVehiculoId, 'marcaNew_selector')

    });

    tipoVehiculoSelect.addEventListener('change', async function() {
        console.log("Tipo de vehiculo seleccionado. Buscando marcas...");
        const tipoVehiculoId = tipoVehiculoSelect.value;
        inputNombreModelo.value=''
        await cargarMarcas(tipoVehiculoId,'marca_selector' )
        marcaVehiculoSelect.dispatchEvent(new Event('change'));
    });

    marcaVehiculoSelect.addEventListener('change', async function() {
        console.log("Marca seleccionada. Buscando modelos...");
        const marcaVehiculoId = marcaVehiculoSelect.value;
        const tipoVehiculoId = tipoVehiculoSelect.value;
        await cargarModelos(marcaVehiculoId, tipoVehiculoId,'modelo_selector' )

    });
    // Escuchar cambios en el selector de modelos
    selectModelo.addEventListener("change", (e) => {
        const id_modelo = e.target.value;
        const option = selectModelo.options[selectModelo.selectedIndex];
        inputNombreModelo.value = option.dataset.nombreModelo || '';  // Obtener el nombre del modelo desde dataset
    });

    deleteButton.addEventListener("click", (e) => {
        const form = document.querySelector(".formulario");
        const formData = new FormData(form);
        showConfirmationDialog(
            "Eliminar Modelo?",
            "Esta acción es irreversible y eliminará el modelo seleccionado. Asegúrese de que no esté en uso.",
            () => eliminarModelo(formData),
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
            if (validarFormulario(form)) {
                if (tipo === "add") {
                    await addModelo(form);
                }
                if (tipo === "edit") {
                    await editModelo(form);
                }
            }
        }finally {
            hideLoadingSpinner()
            submitButton.disabled = false;
        }

    });
    tipoVehiculoSelect.dispatchEvent(new Event('change'));
    tipoVehiculoSelectnew.dispatchEvent(new Event('change'));
});

async function addModelo(form) {
    const tipoSelect = form.get("tipoSelect");
    const marcaSelect = form.get("marcaSelect");
    const nombreModelo = form.get("nombreModelo");

    const data = {
        action: "add",
        nombreModelo: nombreModelo,
        id_Marca:marcaSelect,
        id_Tipo:tipoSelect
    };

    const response = await sendRequest(`${host}/listaModelo`, data);
    console.log(response);
    if (response.status === "success") {
        console.log("Modelo creado correctamente");
        showSuccessAlert(response.message);
    } else {
        showErrorDialog(response.message);
    }
}

async function editModelo(form) {
    const id_modelo = form.get("modeloSelect");
    const nombreModelo = form.get("nombreModelo");

    const data = {
        action: "edit",
        id_Modelo: id_modelo,
        nombreModelo: nombreModelo,
    };

    const response = await sendRequest(`${host}/listaModelo`, data);
    console.log(response);
    if (response.status === "success") {
        console.log("Modelo actualizado correctamente");
        showSuccessAlert(response.message);
    } else {
        showErrorDialog(response.message);
    }
}

async function eliminarModelo(form) {
    const id_modelo = form.get("modeloSelect");
    console.log("id del modelo a eliminar: ", id_modelo )
    const data = {
        action: "delete",
        id_Modelo: id_modelo,
    };

    const response = await sendRequest(`${host}/listaModelo`, data);

    if (response.status === "success") {
        console.log("Modelo eliminado correctamente");
        showSuccessAlert(response.message);
    } else {
        showErrorDialog(response.message);
    }
}

async function obtenerModelos() {
    const response = await fetch(`${host}/listaModelo`);
    if (response.status === 204) {
        return [];
    }
    return await response.json();
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
        tipoVehiculoSelect.innerHTML = ''; // Limpiar el select

        // Rellenar el select con los tipos de vehículos recibidos
        tiposVehiculo.forEach(tipo => {
            const option = document.createElement('option');
            option.value = tipo.id_Tipo;
            option.textContent = tipo.nombre_Tipo;
            tipoVehiculoSelect.appendChild(option);

            const option2 = option.cloneNode(true);
            tipoVehiculoNewSelect.appendChild(option2);
        });

        if (tiposVehiculo.length > 0) {
            tipoVehiculoSelect.selectedIndex = 0;
            tipoVehiculoSelect.dispatchEvent(new Event('change')); // Disparar el evento change
            tipoVehiculoNewSelect.dispatchEvent(new Event('change'));
        }

    } catch (error) {
        console.error('Error al cargar los tipos de vehículos:', error);
    }
}
function validarFormulario(form) {
    const nombreModelo = form.get("nombreModelo");
    const tipoVehiculo = form.get("tipoSelect");
    const marcaVehiculo = form.get("marcaSelect");
    const idModelo = form.get("modeloSelect"); // Solo para la edición
    console.log(nombreModelo)
    // Validar que el nombre del modelo solo contenga letras y no esté vacío
    if (!validarTextoNumeros(nombreModelo, 2)) {
        showErrorDialog("El nombre del modelo debe contener solo letras y tener al menos 2 caracteres.");
        return false;
    }

    // Validar que el tipo y la marca sean números válidos
    if (isNaN(tipoVehiculo) || isNaN(marcaVehiculo)) {
        showErrorDialog("El tipo y la marca del vehículo deben ser números válidos.");
        return false;
    }

    // Validar que el ID del modelo (en caso de editar) sea un número válido
    if (idModelo && isNaN(idModelo)) {
        showErrorDialog("El ID del modelo no es válido.");
        return false;
    }

    return true; // Si todo es correcto
}