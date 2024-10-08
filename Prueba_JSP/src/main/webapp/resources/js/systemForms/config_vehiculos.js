import {sendRequest} from "../ajax.js";
import {showErrorDialog} from "../alerts/error.js";
import {showSuccessAlert} from "../alerts/success.js";
import {showConfirmationDialog} from "../alerts/confirm.js";
import {host} from "../config.js";
import {validarCantidadCascos, validarPlaca, validarTexto} from "../utils/validations.js";
import {cargarCiudades, cargarColores, cargarTiposDocumento, cargarTiposVehiculo} from "../utils/renderSelects.js";

document.addEventListener("DOMContentLoaded", async () => {
    const searchForm = document.getElementById('searhForm');
    const btnPlaca = document.getElementById('btn-placa');
    const btnDocumento = document.getElementById('btn-documento');
    const deleteButton= document.getElementById('deleteButton');
    const inputDocumento = document.getElementById('documento');
    const labelDocumento = document.querySelector('label[for="documento"]');
    const typeSearchInput = document.getElementById('typeSearch');
    const cardVehiculosContainer = document.querySelector(".card-Vehiculos");
    const vehiculoForm= document.querySelector("#vehiculoForm");
    const tipoVehiculoSelect= document.getElementById("tipoSelect");
    const marcaVehiculoSelect= document.getElementById("marcaSelect");
    const modeloVehiculoSelect = document.getElementById("modeloSelect")
    let vehiculosData = []; // Almacenará los datos de vehículos

    await cargarTiposVehiculo('tipoSelect')
    await cargarColores('colorSelect')
    await cargarCiudades('ciudadSelect')

    tipoVehiculoSelect.addEventListener('change', async function() {
        console.log("Tipo de vehiculo selecciondado. Buscando modelos...")
        const tipoVehiculoId = tipoVehiculoSelect.value;
        await cargarMarcas(tipoVehiculoId, 'marcaSelect');
    });

    marcaVehiculoSelect.addEventListener('change', async function() {
        console.log("Modelo seleccionado. Buscando modelos...");
        const marcaVehiculoId = marcaVehiculoSelect.value;
        const tipoVehiculoId = tipoVehiculoSelect.value;
        await cargarModelos(marcaVehiculoId, tipoVehiculoId, 'modeloSelect')
    });



    btnPlaca.addEventListener('click', () => {
        inputDocumento.placeholder = 'Placa';
        inputDocumento.name = 'placa';
        labelDocumento.textContent = 'Ingresa la placa';
        typeSearchInput.value = 'placa';
        btnPlaca.classList.remove('button--deselected');
        btnDocumento.classList.add('button--deselected');
    });

    btnDocumento.addEventListener('click', () => {
        inputDocumento.placeholder = 'Documento';
        inputDocumento.name = 'documento';
        labelDocumento.textContent = 'Ingresa el documento';
        typeSearchInput.value = 'documento';
        btnDocumento.classList.remove('button--deselected');
        btnPlaca.classList.add('button--deselected');
    });

    // Evento submit del formulario
    searchForm.addEventListener("submit", async (e) => {
        e.preventDefault();
        const form = new FormData(e.target);
        const data = await buscarVehiculo(form); // Llamada a fetch para obtener vehículos
        // Vaciar el contenedor de tarjetas antes de mostrar los resultados
        cardVehiculosContainer.innerHTML = '';
        if(data.length>0){
        vehiculosData = data;
        data.forEach(vehiculo => {
            const template = document.getElementById('vehiculoTemplate').content.cloneNode(true);

            // Llenar los datos del vehículo en el template
            template.querySelector('.vehiculo-tipo').textContent = vehiculo.tipo_vehiculo;
            template.querySelector('.vehiculo-marca').textContent = vehiculo.marca.nombre;
            template.querySelector('.vehiculo-placa').textContent = vehiculo.placa;
            template.querySelector('.vehiculo-documento').textContent = vehiculo.id_aprendiz;

            // Agregar data-attribute con el ID del vehículo
            const cardElement = template.querySelector('.itemVehiculo');
            cardElement.setAttribute('data-id', vehiculo.id_vehiculo);

            // Añadir el evento click a cada tarjeta de vehículo
            cardElement.addEventListener('click', () => {
                console.log("Se busca el vehiculo", vehiculo.id_vehiculo)
                seleccionarVehiculo(vehiculo.id_vehiculo); // Llamar función al hacer clic en el vehículo
            });

            // Añadir la tarjeta al contenedor
            cardVehiculosContainer.appendChild(template);
        });
        } else{
            showErrorDialog('No se ha conseguido vehiculos vinculados a este documento ó placa.')
        }
    });

    vehiculoForm.addEventListener("submit",(e)=>{
        e.preventDefault();
        const targerForm= e.target;

        const form = new FormData(targerForm);
        const tipo=form.get("formType");
        if(validarFormulario(form)){
            if(tipo ==="add"){
                addSector(form)
            }
            if(tipo ==="edit"){
                editVehiculo(form)
            }
        }
    })

    // Función para realizar la búsqueda con fetch
    async function buscarVehiculo(form) {
        const typeSearch = form.get("typeSearch");
        console.log(typeSearch)
        if (typeSearch === "documento") {
            try {
                const documento = form.get("documento"); // Obtener el valor del documento desde el formulario
                // Añadir el parámetro 'documento' a la URL
                const response = await fetch(`${host}/VehiculoAprendiz?documento=${documento}&typesearch=${typeSearch}`);

                if (!response.ok) {
                    throw new Error('Error en la búsqueda del vehículo');
                }

                const data = await response.json();  // Parsear la respuesta JSON
                console.log(data);
                return data;
            } catch (error) {
                console.error(error);
                return [];
            }
        } else if (typeSearch === "placa") {
            try {
                const placa = form.get("placa"); // Obtener el valor de la placa desde el formulario
                // Añadir el parámetro 'placa' a la URL
                const response = await fetch(`${host}/VehiculoAprendiz?placa=${placa}&typesearch=${typeSearch}`);
                if (!response.ok) {
                    throw new Error('Error en la búsqueda del vehículo');
                }

                const data = await response.json();  // Parsear la respuesta JSON
                console.log(data);
                return data;
            } catch (error) {
                console.error(error);
                return [];
            }
        }
    }

    // Función para seleccionar un vehículo y rellenar el formulario
    async function seleccionarVehiculo(vehiculoId) {
        // Buscar el vehículo en vehiculosData por ID
        const vehiculoSeleccionado = vehiculosData.find(vehiculo => vehiculo.id_vehiculo === vehiculoId);
        if (vehiculoSeleccionado) {
            try {
                document.getElementById('idAprendiz').value = vehiculoSeleccionado.id_aprendiz;
                document.getElementById('idVehiculo').value = vehiculoSeleccionado.id_vehiculo;

                // Seleccionar el tipo de vehículo
                document.getElementById('tipoSelect').value = vehiculoSeleccionado.tipo_vehiculo;

                // Disparar el evento `change` y esperar a que las marcas se carguen
                await cargarMarcas(vehiculoSeleccionado.tipo_vehiculo);

                // Seleccionar la marca una vez que las marcas se hayan cargado
                document.getElementById('marcaSelect').value = vehiculoSeleccionado.marca.id_marca;

                // Disparar el evento `change` para cargar los modelos y esperar a que se carguen
                await cargarModelos(vehiculoSeleccionado.marca.id_marca, vehiculoSeleccionado.tipo_vehiculo);

                // Seleccionar el modelo una vez que los modelos se hayan cargado
                document.getElementById('modeloSelect').value = vehiculoSeleccionado.modelo;

                // Rellenar el campo de la placa
                document.getElementById('placa').value = vehiculoSeleccionado.placa;

                document.getElementById('colorSelect').value = vehiculoSeleccionado.color_vehiculo

                document.getElementById('cantidadCasco').value = vehiculoSeleccionado.cantidad_cascos

                document.getElementById('ciudadSelect').value = vehiculoSeleccionado.ciudad

                console.log('Vehículo seleccionado:', vehiculoSeleccionado);

            } catch (error) {
                console.error("Error al seleccionar el vehículo:", error);
            }
        }
    }
// Función para cargar marcas según el tipo de vehículo
    async function cargarMarcas(tipoVehiculoId) {
        const response = await fetch(`${host}/listaMarcas?id_Tipo=${tipoVehiculoId}`);
        const data = await response.json();

        const marcaVehiculoSelect = document.getElementById('marcaSelect');
        marcaVehiculoSelect.innerHTML = ''; // Limpiar el select

        // Rellenar el select con las marcas recibidas
        data.forEach(marca => {
            const option = document.createElement('option');
            option.value = marca.id_Marca;
            option.textContent = marca.nombre_Marca;
            marcaVehiculoSelect.appendChild(option);
        });
    }
// Función para cargar modelos según la marca y el tipo de vehículo
    async function cargarModelos(marcaVehiculoId, tipoVehiculoId) {
        const response = await fetch(`${host}/listaModelo?id_Marca=${marcaVehiculoId}&id_Tipo=${tipoVehiculoId}`);
        const data = await response.json();

        const modeloVehiculoSelect = document.getElementById('modeloSelect');
        modeloVehiculoSelect.innerHTML = ''; // Limpiar el select

        // Rellenar el select con los modelos recibidos
        data.forEach(modelo => {
            const option = document.createElement('option');
            option.value = modelo.id_Modelo;
            option.textContent = modelo.nombre_Modelo;
            modeloVehiculoSelect.appendChild(option);
        });
    }
    deleteButton.addEventListener("click",(e)=>{
        const form = document.querySelector("#vehiculoForm");
        const formData = new FormData(form);
        const idVehiculo= formData.get("idVehiculo")
        if(idVehiculo && idVehiculo.trim() !== ""){
            showConfirmationDialog(
                "Eliminar Vehiculo?",
                "El sistema no permitira la eliminacion de vehiculos en uso. Esta acción es irreversible",
                ()=>eliminarVehiculo(formData),
                () => console.log('Acción cancelada')
            )
        }else{
            showErrorDialog('¡No ha seleccionado ningun vehiculo para eliminar!')
        }

    })

});
async function editVehiculo(form) {
    const idVehiculo=       form.get("idVehiculo")
    const idAprendiz=       form.get("idAprendiz")
    const tipoVehiculo=     form.get("tipoVehiculo");
    const placaVehiculo=    form.get("placaVehiculo")
    const marcaVehiculo =   form.get ("marcaVehiculo");
    const modeloVehiculo=   form.get("modeloVehiculo");
    const cantCascoVehiculo=form.get("cantCasco");
    const colorVehiculo =   form.get("colorVehiculo")
    const ciudadVehiculo=   form.get("ciudadVehiculo")

    const data= {
        action: "edit",
        idVehiculo: idVehiculo,
        idAprendiz:idAprendiz,
        tipoVehiculo: tipoVehiculo,
        placaVehiculo: placaVehiculo,
        marcaVehiculo: marcaVehiculo,
        modeloVehiculo: modeloVehiculo,
        cantCascoVehiculo: cantCascoVehiculo,
        colorVehiculo: colorVehiculo,
        ciudadVehiculo: ciudadVehiculo
    }
    console.log(data)
    const response= await sendRequest(`${host}/VehiculoAprendiz`,data)

    console.log(response)
    if(response.status === "success"){
        console.log("Se ha Actualizado el vehiculo correctamente")
        showSuccessAlert(response.message)
    }else{
        showErrorDialog(response.message)
    }
}

async function eliminarVehiculo(form) {

    const id_vehiculo= form.get("idVehiculo")

    const data = {
        action : "delete",
        idVehiculo: id_vehiculo,
    };
    const response= await sendRequest( `${host}/VehiculoAprendiz`,data)

    if(response.status === "success"){
        console.log("Se ha Eliminado el vehiculo correctamente")
        showSuccessAlert(response.message)
    }else {
        showErrorDialog(response.message)
    }
}
function validarFormulario(form) {
    const tipoVehiculo = form.get("tipoVehiculo");
    const marcaVehiculo = form.get("marcaVehiculo");
    const modeloVehiculo = form.get("modeloVehiculo");
    const placaVehiculo = form.get("placaVehiculo");
    const colorVehiculo = form.get("colorVehiculo");
    const cantCascoVehiculo = form.get("cantCasco");

    // Validar que tipo, marca y modelo sean números válidos
    if (isNaN(tipoVehiculo) || isNaN(marcaVehiculo) || isNaN(modeloVehiculo)) {
        showErrorDialog("El tipo, marca y modelo del vehículo deben ser números válidos.");
        return false;
    }

    // Validar que la placa del vehículo solo contenga letras, números y sea válida
    if (!validarPlaca(placaVehiculo)) {
        showErrorDialog("La placa del vehículo debe contener solo letras y números.");
        return false;
    }

    // Validar que el color del vehículo sea solo texto
    if (!validarTexto(colorVehiculo, 1)) {
        showErrorDialog("El color del vehículo debe ser un texto válido.");
        return false;
    }

    // Validar que la cantidad de cascos sea un número entre 0 y 2
    if (!validarCantidadCascos(cantCascoVehiculo)) {
        showErrorDialog("La cantidad de cascos debe ser un número entre 0 y 2.");
        return false;
    }

    return true; // Si todo es válido
}


