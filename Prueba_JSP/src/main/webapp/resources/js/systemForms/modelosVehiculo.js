import {sendRequest} from "../ajax.js";
import {host} from "../config.js";
import {showConfirmationDialog} from "../alerts/confirm.js";
import {showSuccessAlert} from "../alerts/success.js";
import {showErrorDialog} from "../alerts/error.js";

document.addEventListener("DOMContentLoaded", async () => {
    const tipoVehiculoSelect = document.querySelector("#tipo_selector");
    const marcaVehiculoSelect = document.querySelector("#marca_selector");
    const selectModelo = document.querySelector("#modelo_selector");
    const inputNombreModelo = document.querySelector("#nameModelo_input");
    const deleteButton = document.querySelector("#delete_button");

    const tipoVehiculoSelectnew= document.querySelector("#tipoNew_selector");
    const marcaVehiculoSelectnew = document.querySelector("#marcaNew_selector");
    cargarTiposVehiculo()
    tipoVehiculoSelectnew.addEventListener('change', async function() {
        console.log("Tipo de vehiculo selecciondado. Buscando modelos...");
        const tipoVehiculoId = tipoVehiculoSelectnew.value;
        try {
            const response = await fetch(`${host}/listaMarcas?id_Tipo=${tipoVehiculoId}`);
            const data = await response.json();

            // Limpiar el select de marcas
            marcaVehiculoSelectnew.innerHTML = '';

            // Rellenar el select con las marcas recibidas
            data.forEach(marca => {
                const option = document.createElement('option');
                option.value = marca.id_Marca; // Asegúrate de que los nombres coinciden con la respuesta del servidor
                option.textContent = marca.nombre_Marca; // Ajustar también aquí
                marcaVehiculoSelectnew.appendChild(option);
            });

        } catch (error) {
            console.error('Error al obtener las marcas:', error);
        }
    });


    tipoVehiculoSelect.addEventListener('change', async function() {
        console.log("Tipo de vehiculo seleccionado. Buscando marcas...");
        const tipoVehiculoId = tipoVehiculoSelect.value;
        inputNombreModelo.value=''
        try {
            const response = await fetch(`${host}/listaMarcas?id_Tipo=${tipoVehiculoId}`);
            const data = await response.json();

            // Limpiar el select de marcas
            marcaVehiculoSelect.innerHTML = '';

            // Rellenar el select con las marcas recibidas y añadir datos a dataset
            data.forEach(marca => {
                const option = document.createElement('option');
                option.value = marca.id_Marca;
                option.textContent = marca.nombre_Marca;
                option.dataset.nombreMarca = marca.nombre_Marca;  // Guardar el nombre de la marca en el dataset
                marcaVehiculoSelect.appendChild(option);
            });

            // Si existen marcas, selecciona la primera y dispara el evento 'change'
            if (marcaVehiculoSelect.options.length > 0) {
                marcaVehiculoSelect.selectedIndex = 0;
                marcaVehiculoSelect.dispatchEvent(new Event('change')); // Disparar el evento 'change' en marcas
            }
        } catch (error) {
            console.error('Error al obtener las marcas:', error);
        }
    });

    marcaVehiculoSelect.addEventListener('change', async function() {
        console.log("Marca seleccionada. Buscando modelos...");
        const marcaVehiculoId = marcaVehiculoSelect.value;
        const tipoVehiculoId = tipoVehiculoSelect.value;

        try {
            const response = await fetch(`${host}/listaModelo?id_Marca=${marcaVehiculoId}&id_Tipo=${tipoVehiculoId}`);

            if (!response.ok) {
                throw new Error('Error al obtener los modelos de vehículos');
            }

            const data = await response.json();
            console.log(data);
            selectModelo.innerHTML = ''; // Limpiar el select de modelos

            // Rellenar el select con los modelos recibidos y añadir datos a dataset
            data.forEach(modelo => {
                const option = document.createElement('option');
                option.value = modelo.id_Modelo;
                option.textContent = modelo.nombre_Modelo;
                option.dataset.nombreModelo = modelo.nombre_Modelo; // Guardar el nombre del modelo en el dataset
                selectModelo.appendChild(option);
            });

            // Si existen modelos, selecciona el primero y dispara el evento 'change'
            if (selectModelo.options.length > 0) {
                selectModelo.selectedIndex = 0;
                selectModelo.dispatchEvent(new Event('change')); // Disparar el evento 'change' en modelos
            }

        } catch (error) {
            console.error("Error al cargar los modelos:", error);
        }
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