import {sendRequest} from "./ajax.js";
import {host} from "./config.js";
import {showErrorDialog} from "./alerts/error.js"; // Mostrar mensajes de error
import {showSuccessAlert} from "./alerts/success.js";
import {showConfirmationDialog} from "./alerts/confirm.js";
import {validarCantidadCascos, validarPlaca, validarTexto} from "./utils/validations.js";
import {cargarCiudades, cargarColores, cargarMarcas, cargarModelos, cargarTiposVehiculo} from "./utils/renderSelects.js"; // Mostrar mensajes de éxito


const DocumentoAprendiz = document.querySelector("#documentoUser").value;
const ejecutor = document.querySelector(".accionador");
const vehiculoList = document.querySelector(".vehiculo__grid");

const tipoVehiculoSelect = document.getElementById("tipoVehiculo");
const marcaVehiculoSelect = document.getElementById("marcaVehiculo");
const modeloVehiculoSelect = document.getElementById("modeloVehiculo");
document.addEventListener('DOMContentLoaded', async function () {
    // Cargar opciones para los selectores al iniciar
    await cargarColores('colorVehiculo')
    await cargarCiudades('ciudadVehiculo')
    await cargarTiposVehiculo('tipoVehiculo')
    tipoVehiculoSelect.dispatchEvent(new Event('change'));
});



// Lógica para cargar las marcas cuando se selecciona un tipo de vehículo
tipoVehiculoSelect.addEventListener('change', async function () {
    const tipoVehiculoId = tipoVehiculoSelect.value;
    await cargarMarcas(tipoVehiculoId,'marcaVehiculo');
    marcaVehiculoSelect.dispatchEvent(new Event('change'));
});
// Lógica para cargar los modelos cuando se selecciona una marca
marcaVehiculoSelect.addEventListener('change', async function () {
    const marcaVehiculoId = marcaVehiculoSelect.value;
    const tipoVehiculoId = tipoVehiculoSelect.value;
    await cargarModelos(marcaVehiculoId, tipoVehiculoId, 'modeloVehiculo');
});

document.addEventListener("click", async (event) => {
    const vehiculoForm = document.querySelector("#vehiculoForm");
    const formData = new FormData(vehiculoForm);

    if (event.target.id === "sendCreate") {
        if(validarFormulario(formData)){
            await crearVehiculo(formData, DocumentoAprendiz);
        }
    }
    if (event.target.id === "sendEdit") {
        if(validarFormulario(formData)){
            await editarVehiculo(formData, DocumentoAprendiz);
        }
    }
    if(event.target.id ==="delete"){
        showConfirmationDialog("¿Eliminar Vehiculo)",
            "El sistema no permitira eliminar vehiculos en uso, Esta acción es irreversible",
            ()=>eliminarVehiculo(formData),
            () => console.log('Acción cancelada')
        );
    }
});
let vehiculos = await ObtenerVehiculos(DocumentoAprendiz);
if (vehiculos.length > 0) {
    vehiculoList.innerHTML = '';
    vehiculos.forEach((vehiculo, index) => {
        const div = document.createElement("div");
        div.classList.add("vehiculo__list__item");
        div.setAttribute('data-vehiculo', index);
        const placaP = document.createElement('p');
        placaP.textContent = vehiculo.placa;
        div.appendChild(placaP);

        const marcaP = document.createElement('p');
        marcaP.textContent = vehiculo.marca.nombre;
        div.appendChild(marcaP);

        console.log(vehiculo)
        vehiculoList.appendChild(div);

        div.addEventListener('click', () => {
            llenarFormulario(vehiculo);
            ejecutor.id = "sendEdit";
            ejecutor.textContent = "Editar";
        });
    });

    const addItem = document.createElement('div');
    addItem.classList.add('vehiculo__list__item', 'add');
    addItem.addEventListener("click", () => {
        vaciarFormulario();
        ejecutor.id = "sendCreate";
        ejecutor.textContent = "Crear";
    });
    const addText = document.createElement('p');
    addText.textContent = 'Agregar';
    addItem.appendChild(addText);
    vehiculoList.appendChild(addItem);
} else {
    console.log('No se encontraron vehículos.');
}
// Función para crear un vehículo
async function crearVehiculo(formData, DocumentoAprendiz) {
    const data ={
        "idAprendiz": DocumentoAprendiz,
        "action": "add",
        "placaVehiculo": formData.get("placaVehiculo"),
        "marcaVehiculo":formData.get("marcaVehiculo"),
        "modeloVehiculo":formData.get("modeloVehiculo"),
        "tipoVehiculo":formData.get("tipoVehiculo"),
        "cantCascoVehiculo":formData.get("cascosVehiculo"),
        "colorVehiculo":formData.get("colorVehiculo"),
        "ciudadVehiculo":formData.get("ciudadVehiculo"),
        "estadoVehiculo": formData.get("estadoVehiculo")
    }
    console.log(data)
    const response = await sendRequest(`${host}/VehiculoAprendiz`, data);

    if (response.status === 'success') {
        showSuccessAlert(response.message);
        // Actualizar la lista de vehículos después de crear
        await ObtenerVehiculos(DocumentoAprendiz);
    } else {
        showErrorDialog(response.message);
    }
}

// Función para editar un vehículo
async function editarVehiculo(formData, DocumentoAprendiz) {
    const data ={
        "idAprendiz": formData.get("idUser"),
        "action": "edit",
        "idVehiculo":formData.get("idVehiculo"),
        "placaVehiculo": formData.get("placaVehiculo"),
        "marcaVehiculo":formData.get("marcaVehiculo"),
        "modeloVehiculo":formData.get("modeloVehiculo"),
        "tipoVehiculo":formData.get("tipoVehiculo"),
        "cantCascoVehiculo":formData.get("cascosVehiculo"),
        "colorVehiculo":formData.get("colorVehiculo"),
        "ciudadVehiculo":formData.get("ciudadVehiculo"),
        "estadoVehiculo": formData.get("estadoVehiculo")
    }
    const response = await sendRequest(`${host}/VehiculoAprendiz`, data);

    if (response.status === 'success') {
        showSuccessAlert(response.message);
        // Actualizar la lista de vehículos después de editar
        await ObtenerVehiculos(DocumentoAprendiz);
    } else {
        showErrorDialog(response.message);
    }
}
async function eliminarVehiculo(formData){
    const idVehiculo = formData.get("idVehiculo")
    const data={
        action : "delete",
        idVehiculo: idVehiculo
    }
    const response = await sendRequest(`${host}/VehiculoAprendiz`, data);
    if (response.status === 'success') {
        showSuccessAlert(response.message);
        // Actualizar la lista de vehículos después de editar
        await ObtenerVehiculos(DocumentoAprendiz);
    } else {
        showErrorDialog(response.message);
    }
}
// Función para cargar vehículos
async function ObtenerVehiculos(DocumentoAprendiz) {
    const response = await fetch(`${host}/VehiculoAprendiz?documento=${DocumentoAprendiz}`);

    if (response.status === 204) {
        console.log('No se encontraron vehículos.');
        return []; // Retorna un array vacío si no hay vehículos
    }

    const data = await response.json();

    if (!data || data.length === 0) {
        console.log('No se encontraron vehículos.');
        return []; // Asegúrate de retornar un array vacío si no hay datos
    } else {
        return data; // Retorna los datos de los vehículos si existen
    }
}
// Función para llenar el formulario con un vehículo
async function llenarFormulario(vehiculo) {
    const form = document.querySelector("#vehiculoForm");
    form.querySelector("#tipoVehiculo").value = vehiculo.tipo_vehiculo;
    await cargarMarcas(vehiculo.tipo_vehiculo,'marcaVehiculo');
    form.querySelector("#marcaVehiculo").value = vehiculo.marca.id_marca;

    await cargarModelos(vehiculo.marca.id_marca, vehiculo.tipo_vehiculo,'modeloVehiculo');
    form.querySelector("#modeloVehiculo").value = vehiculo.modelo;

    form.querySelector("#idVehiculo").value = vehiculo.id_vehiculo;
    form.querySelector("#placaVehiculo").value = vehiculo.placa;
    form.querySelector("#ciudadVehiculo").value = vehiculo.ciudad;
    form.querySelector("#colorVehiculo").value = vehiculo.color_vehiculo;
    form.querySelector("#estadoVehiculo").value = vehiculo.estadoVehiculo;
    form.querySelector("#cantCasco").value = vehiculo.cantidad_cascos;

    const cascoCantidad = vehiculo.cantidad_cascos;
    form.querySelector("#cascoConfirm").checked = cascoCantidad > 0;
}

// Función para vaciar el formulario
function vaciarFormulario() {
    const form = document.querySelector("#vehiculoForm");
    form.reset();
}
function validarFormulario(formData){
    const idVehiculo        =formData.get("idVehiculo")
    const placaVehiculo     =formData.get("placaVehiculo")
    const marcaVehiculo     =formData.get("marcaVehiculo")
    const modeloVehiculo    =formData.get("modeloVehiculo")
    const tipoVehiculo      =formData.get("tipoVehiculo")
    const cascosVehiculo    =formData.get("cascosVehiculo")
    const colorVehiculo     =formData.get("colorVehiculo")
    const ciudadVehiculo    =formData.get("ciudadVehiculo")
    if (!validarPlaca(placaVehiculo)){
        showErrorDialog("La placa del vehículo debe contener solo letras y números.");
        return false;
    }
    if (isNaN(tipoVehiculo) || isNaN(marcaVehiculo) || isNaN(modeloVehiculo) ||isNaN(ciudadVehiculo) ) {
        showErrorDialog("El tipo, marca, modelo y ciudad del vehículo deben ser números válidos.");
        return false;
    }
    if (!validarCantidadCascos(cascosVehiculo)) {
        showErrorDialog("La cantidad de cascos debe ser un número entre 0 y 2.");
        return false;
    }
    if (!validarTexto(colorVehiculo, 1)) {
        showErrorDialog("El color del vehículo debe ser un texto válido.");
        return false;
    }

    return  true
}
