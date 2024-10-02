import {sendRequest} from "../ajax.js";
import {host} from "../config.js";
import {showConfirmationDialog} from "../alerts/confirm.js";
import {showSuccessAlert} from "../alerts/success.js";
import {showErrorDialog} from "../alerts/error.js";

document.addEventListener("DOMContentLoaded", async () => {

    const selectModelo = document.querySelector("#item_selector");
    const inputNombreModelo = document.querySelector("#nameModelo_input");
    const deleteButton = document.querySelector("#delete_button");

    const data = await obtenerModelos();

    if (data.length === 0) {
        console.log('No se encontraron modelos.');
    } else {
        console.log(data);
        data.forEach(modelo => {
            const option = document.createElement('option');
            option.value = modelo.id_modelo;
            option.textContent = modelo.nombre_modelo;
            selectModelo.appendChild(option);
        });

        // Introducir el primer modelo en el campo de texto
        inputNombreModelo.value = data[0].nombre_modelo;
    }

    // Escuchar cambios en el selector de modelos
    selectModelo.addEventListener("change", (e) => {
        const id_modelo = e.target.value;
        const dato_modelo = data.find(modelo => modelo.id_modelo === parseInt(id_modelo));
        inputNombreModelo.value = dato_modelo.nombre_modelo;
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

    document.addEventListener("submit", (e) => {
        e.preventDefault();
        const targetForm = e.target;

        const form = new FormData(targetForm);
        const tipo = form.get("formType");

        if (tipo === "add") {
            addModelo(form);
        }
        if (tipo === "edit") {
            editModelo(form);
        }
    });
});

async function addModelo(form) {
    const nombreModelo = form.get("nombreModelo");

    const data = {
        action: "add",
        nombreModelo: nombreModelo,
    };

    const response = await sendRequest(`${host}/tipoModelo`, data);
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
        idModelo: id_modelo,
        nombreModelo: nombreModelo,
    };

    const response = await sendRequest(`${host}/tipoModelo`, data);
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

    const data = {
        action: "delete",
        idModelo: id_modelo,
    };

    const response = await sendRequest(`${host}/tipoModelo`, data);

    if (response.status === "success") {
        console.log("Modelo eliminado correctamente");
        showSuccessAlert(response.message);
    } else {
        showErrorDialog(response.message);
    }
}

async function obtenerModelos() {
    const response = await fetch(`${host}/tipoModelo`);
    if (response.status === 204) {
        return [];
    }
    return await response.json();
}
