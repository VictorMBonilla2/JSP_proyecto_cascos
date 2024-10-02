import {sendRequest} from "../ajax.js";
import {host} from "../config.js";
import {showConfirmationDialog} from "../alerts/confirm.js";
import {showSuccessAlert} from "../alerts/success.js";
import {showErrorDialog} from "../alerts/error.js";

document.addEventListener("DOMContentLoaded", async () => {

    const selectMarca = document.querySelector("#item_selector");
    const inputNombreMarca = document.querySelector("#nameMarca_input");
    const deleteButton = document.querySelector("#delete_button");

    const data = await obtenerMarcas();

    if (data.length === 0) {
        console.log('No se encontraron marcas.');
    } else {
        console.log(data);
        data.forEach(marca => {
            const option = document.createElement('option');
            option.value = marca.id_marca;
            option.textContent = marca.nombre_marca;
            selectMarca.appendChild(option);
        });

        // Introducir el primer marca en el campo de texto
        inputNombreMarca.value = data[0].nombre_marca;
    }

    // Escuchar cambios en el selector de marcas
    selectMarca.addEventListener("change", (e) => {
        const id_marca = e.target.value;
        const dato_marca = data.find(marca => marca.id_marca === parseInt(id_marca));
        inputNombreMarca.value = dato_marca.nombre_marca;
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

    document.addEventListener("submit", (e) => {
        e.preventDefault();
        const targetForm = e.target;

        const form = new FormData(targetForm);
        const tipo = form.get("formType");

        if (tipo === "add") {
            addMarca(form);
        }
        if (tipo === "edit") {
            editMarca(form);
        }
    });
});

async function addMarca(form) {
    const nombreMarca = form.get("nombreMarca");

    const data = {
        action: "add",
        nombreMarca: nombreMarca,
    };

    const response = await sendRequest(`${host}/tipoMarca`, data);
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
    const nombreMarca = form.get("nombreMarca");

    const data = {
        action: "edit",
        idMarca: id_marca,
        nombreMarca: nombreMarca,
    };

    const response = await sendRequest(`${host}/tipoMarca`, data);
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

    const response = await sendRequest(`${host}/tipoMarca`, data);

    if (response.status === "success") {
        console.log("Marca eliminada correctamente");
        showSuccessAlert(response.message);
    } else {
        showErrorDialog(response.message);
    }
}

async function obtenerMarcas() {
    const response = await fetch(`${host}/tipoMarca`);
    if (response.status === 204) {
        return [];
    }
    return await response.json();
}
