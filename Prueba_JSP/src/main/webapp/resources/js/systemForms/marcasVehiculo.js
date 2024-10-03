import {sendRequest} from "../ajax.js";
import {host} from "../config.js";
import {showConfirmationDialog} from "../alerts/confirm.js";
import {showSuccessAlert} from "../alerts/success.js";
import {showErrorDialog} from "../alerts/error.js";

document.addEventListener("DOMContentLoaded", async () => {
    const tipoVehiculoSelect = document.querySelector("#tipo_selector");
    const marcaVehiculoSelect = document.querySelector("#marca_selector");
    const inputNombreMarca = document.querySelector("#nameMarca_input");
    const deleteButton = document.querySelector("#delete_button");

    cargarTiposVehiculo()

    tipoVehiculoSelect.addEventListener('change', async function() {
        console.log("Tipo de vehiculo selecciondado. Buscando modelos...");
        const tipoVehiculoId = tipoVehiculoSelect.value;
        inputNombreMarca.value='';
        try {
            const response = await fetch(`${host}/listaMarcas?id_Tipo=${tipoVehiculoId}`);
            const data = await response.json();

            // Limpiar el select de marcas
            marcaVehiculoSelect.innerHTML = '';

            // Rellenar el select con las marcas recibidas
            data.forEach(marca => {
                const option = document.createElement('option');
                option.value = marca.id_Marca; // Asegúrate de que los nombres coinciden con la respuesta del servidor
                option.textContent = marca.nombre_Marca; // Ajustar también aquí
                marcaVehiculoSelect.appendChild(option);
            });

            // Guardar la data en el elemento select usando dataset (esto es clave)
            marcaVehiculoSelect.dataset.marcaData = JSON.stringify(data);

            // Seleccionar automáticamente la primera opción y disparar el evento `change`
            if (marcaVehiculoSelect.length > 0) {
                marcaVehiculoSelect.selectedIndex = 0;
                marcaVehiculoSelect.dispatchEvent(new Event('change')); // Disparar el evento change programáticamente
            }

        } catch (error) {
            console.error('Error al obtener las marcas:', error);
        }
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
