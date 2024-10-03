import {sendRequest} from "./ajax.js";
import {host} from "./config.js";
import {showErrorDialog} from "./alerts/error.js"; // Mostrar mensajes de error
import {showSuccessAlert} from "./alerts/success.js"; // Mostrar mensajes de éxito

document.addEventListener('DOMContentLoaded', async function () {
    const DocumentoAprendiz = document.querySelector("#documentoUser").value;
    const ejecutor = document.querySelector(".accionador");
    const vehiculoList = document.querySelector(".vehiculo__grid");

    const tipoVehiculoSelect = document.getElementById("tipoVehiculo");
    const marcaVehiculoSelect = document.getElementById("marcaVehiculo");
    const modeloVehiculoSelect = document.getElementById("modeloVehiculo");

    // Cargar opciones para los selectores al iniciar
    await cargarTiposVehiculo();
    cargarColores()
    cargarCiudades()
    // Lógica para cargar las marcas cuando se selecciona un tipo de vehículo
    tipoVehiculoSelect.addEventListener('change', async function () {
        const tipoVehiculoId = tipoVehiculoSelect.value;
        await cargarMarcas(tipoVehiculoId);
    });

    // Lógica para cargar los modelos cuando se selecciona una marca
    marcaVehiculoSelect.addEventListener('change', async function () {
        const marcaVehiculoId = marcaVehiculoSelect.value;
        const tipoVehiculoId = tipoVehiculoSelect.value;
        await cargarModelos(marcaVehiculoId, tipoVehiculoId);
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
            marcaP.textContent = vehiculo.marca;
            div.appendChild(marcaP);

            const tipoP = document.createElement('p');
            tipoP.textContent = vehiculo.tipo;
            div.appendChild(tipoP);
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

    document.addEventListener("click", async (event) => {
        const vehiculoForm = document.querySelector("#vehiculoForm");
        const formData = new FormData(vehiculoForm);

        if (event.target.id === "sendCreate") {
            await crearVehiculo(formData, DocumentoAprendiz);
        }
        if (event.target.id === "sendEdit") {
            await editarVehiculo(formData, DocumentoAprendiz);
        }
        if(event.target.id ==="delete"){
            await eliminarVehiculo(formData)
        }
    });
});



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
        "ciudadVehiculo":formData.get("ciudadVehiculo")
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
        "idAprendiz": DocumentoAprendiz,
        "action": "edit",
        "idVehiculo":formData.get("idVehiculo"),
        "placaVehiculo": formData.get("placaVehiculo"),
        "marcaVehiculo":formData.get("marcaVehiculo"),
        "modeloVehiculo":formData.get("modeloVehiculo"),
        "tipoVehiculo":formData.get("tipoVehiculo"),
        "cantCascoVehiculo":formData.get("cascosVehiculo"),
        "colorVehiculo":formData.get("colorVehiculo"),
        "ciudadVehiculo":formData.get("ciudadVehiculo")
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
    await cargarMarcas(vehiculo.tipo_vehiculo);
    form.querySelector("#marcaVehiculo").value = vehiculo.marca.id_marca;

    await cargarModelos(vehiculo.marca.id_marca, vehiculo.tipo_vehiculo);
    form.querySelector("#modeloVehiculo").value = vehiculo.modelo;

    form.querySelector("#idVehiculo").value = vehiculo.id_vehiculo;
    form.querySelector("#placaVehiculo").value = vehiculo.placa;
    form.querySelector("#ciudadVehiculo").value = vehiculo.ciudad;
    form.querySelector("#colorVehiculo").value = vehiculo.color_vehiculo;

    form.querySelector("#cantCasco").value = vehiculo.cantidad_cascos;

    const cascoCantidad = vehiculo.cantidad_cascos;
    form.querySelector("#cascoConfirm").checked = cascoCantidad > 0;
}

// Función para vaciar el formulario
function vaciarFormulario() {
    const form = document.querySelector("#vehiculoForm");
    form.reset();
}

// Función para cargar tipos de vehículos
async function cargarTiposVehiculo() {
    try {
        const response = await fetch(`${host}/tiposVehiculos`);
        const tiposVehiculo = await response.json();

        const tipoVehiculoSelect = document.getElementById('tipoVehiculo');
        tipoVehiculoSelect.innerHTML = ''; // Limpiar el select

        // Rellenar el select con los tipos de vehículos recibidos
        tiposVehiculo.forEach(tipo => {
            const option = document.createElement('option');
            option.value = tipo.id_Tipo;
            option.textContent = tipo.nombre_Tipo;
            tipoVehiculoSelect.appendChild(option);
        });

        if (tiposVehiculo.length > 0) {
            tipoVehiculoSelect.selectedIndex = 0;
            tipoVehiculoSelect.dispatchEvent(new Event('change')); // Disparar el evento change
        }
    } catch (error) {
        console.error('Error al cargar los tipos de vehículos:', error);
    }
}

// Función para cargar marcas según el tipo de vehículo
async function cargarMarcas(tipoVehiculoId) {
    try {
        const response = await fetch(`${host}/listaMarcas?id_Tipo=${tipoVehiculoId}`);
        const marcas = await response.json();

        const marcaVehiculoSelect = document.getElementById('marcaVehiculo');
        marcaVehiculoSelect.innerHTML = ''; // Limpiar el select

        // Rellenar el select con las marcas recibidas
        marcas.forEach(marca => {
            const option = document.createElement('option');
            option.value = marca.id_Marca;
            option.textContent = marca.nombre_Marca;
            marcaVehiculoSelect.appendChild(option);
        });

        if (marcas.length > 0) {
            marcaVehiculoSelect.selectedIndex = 0;
            marcaVehiculoSelect.dispatchEvent(new Event('change')); // Disparar el evento change
        }
    } catch (error) {
        console.error('Error al cargar las marcas:', error);
    }
}

// Función para cargar modelos según la marca y el tipo de vehículo
async function cargarModelos(marcaVehiculoId, tipoVehiculoId) {
    try {
        const response = await fetch(`${host}/listaModelo?id_Marca=${marcaVehiculoId}&id_Tipo=${tipoVehiculoId}`);
        const modelos = await response.json();

        const modeloVehiculoSelect = document.getElementById('modeloVehiculo');
        modeloVehiculoSelect.innerHTML = ''; // Limpiar el select

        // Rellenar el select con los modelos recibidos
        modelos.forEach(modelo => {
            const option = document.createElement('option');
            option.value = modelo.id_Modelo;
            option.textContent = modelo.nombre_Modelo;
            modeloVehiculoSelect.appendChild(option);
        });
    } catch (error) {
        console.error('Error al cargar los modelos:', error);
    }
}
async function cargarColores() {
    const response = await fetch(`${host}/listaColores`);
    const colores = await response.json();

    const colorVehiculoSelect = document.getElementById('colorVehiculo');
    colorVehiculoSelect.innerHTML = ''; // Limpiar el select

    colores.forEach(color => {
        const option = document.createElement('option');
        option.value = color;
        option.textContent = color.charAt(0) + color.slice(1).toLowerCase(); // Formato legible
        colorVehiculoSelect.appendChild(option);
    });
}
async function cargarCiudades() {
    try {
        const response = await fetch(`${host}/listaCiudades`);
        if (!response.ok) {
            throw new Error('Error al cargar las ciudades');
        }

        const ciudades = await response.json();

        // Obtener el select donde se cargarán las ciudades
        const ciudadVehiculoSelect = document.getElementById('ciudadVehiculo');
        ciudadVehiculoSelect.innerHTML = ''; // Limpiar el select

        // Rellenar el select con las ciudades recibidas
        ciudades.forEach(ciudad => {
            const option = document.createElement('option');
            option.value = ciudad.id_Ciudad;
            option.textContent = ciudad.nombre_Ciudad;
            ciudadVehiculoSelect.appendChild(option);
        });
    } catch (error) {
        console.error('Error al cargar las ciudades:', error);
    }
}