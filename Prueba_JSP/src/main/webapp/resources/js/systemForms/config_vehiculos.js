import { sendRequest } from "../ajax";
import {showErrorDialog} from "../alerts/error";
import {showSuccessAlert} from "../alerts/success";
import {host} from "../config";

document.addEventListener("DOMContentLoaded", () => {
    const searchForm = document.getElementById('searhForm');
    const btnPlaca = document.getElementById('btn-placa');
    const btnDocumento = document.getElementById('btn-documento');
    const inputDocumento = document.getElementById('documento');
    const labelDocumento = document.querySelector('label[for="documento"]');
    const typeSearchInput = document.getElementById('typeSearch');
    const cardVehiculosContainer = document.querySelector(".card-Vehiculos");
    const vehiculoForm= document.querySelector("#vehiculoForm")
    let vehiculosData = []; // Almacenará los datos de vehículos

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

        vehiculosData = data;
        data.forEach(vehiculo => {
            const template = document.getElementById('vehiculoTemplate').content.cloneNode(true);

            // Llenar los datos del vehículo en el template
            template.querySelector('.vehiculo-tipo').textContent = vehiculo.tipo;
            template.querySelector('.vehiculo-marca').textContent = vehiculo.marca;
            template.querySelector('.vehiculo-placa').textContent = vehiculo.placa;
            template.querySelector('.vehiculo-documento').textContent = vehiculo.documento;

            // Agregar data-attribute con el ID del vehículo
            const cardElement = template.querySelector('.itemVehiculo');
            cardElement.setAttribute('data-id', vehiculo.id);

            // Añadir el evento click a cada tarjeta de vehículo
            cardElement.addEventListener('click', () => {
                seleccionarVehiculo(vehiculo.id); // Llamar función al hacer clic en el vehículo
            });

            // Añadir la tarjeta al contenedor
            cardVehiculosContainer.appendChild(template);
        });
    });

    vehiculoForm.addEventListener("submit",(e)=>{
        e.preventDefault();
        const targerForm= e.target;

        const form = new FormData(targerForm);
        const tipo=form.get("formType");

        if(tipo ==="add"){
            addSector(form)
        }
        if(tipo ==="edit"){
            editVehiculo(form)
        }
    })

    // Función para realizar la búsqueda con fetch
    async function buscarVehiculo(form) {
        try {
            const response = await fetch('URL_DEL_SERVIDOR', {
                method: 'POST',
                body: form
            });
            if (!response.ok) {
                throw new Error('Error en la búsqueda del vehículo');
            }
            const data = await response.json();
            return data.vehiculos; // Suponiendo que la respuesta tiene un array de vehículos
        } catch (error) {
            console.error(error);
            return [];
        }
    }

    // Función para seleccionar un vehículo y rellenar el formulario
    function seleccionarVehiculo(vehiculoId) {
        // Buscar el vehículo en vehiculosData por ID
        const vehiculoSeleccionado = vehiculosData.find(vehiculo => vehiculo.id === vehiculoId);

        if (vehiculoSeleccionado) {
            // Rellenar el formulario con los datos del vehículo
            document.getElementById('vehiculoTipo').value = vehiculoSeleccionado.tipo;
            document.getElementById('vehiculoMarca').value = vehiculoSeleccionado.marca;
            document.getElementById('vehiculoPlaca').value = vehiculoSeleccionado.placa;
            document.getElementById('vehiculoDocumento').value = vehiculoSeleccionado.documento;
            console.log('Vehículo seleccionado:', vehiculoSeleccionado);
        }
    }
    deleteButton.addEventListener("click",(e)=>{
        const form = document.querySelector(".vehiculoForm");
        const formData = new FormData(form);
        const idVehiculo= formData.get("idVehiculo")
        if(idVehiculo != null){
            showConfirmationDialog(
                "Eliminar Tipo de Documento?",
                "El sistema no permitira la eliminación de tipos de documentos en uso. Esta Acción es irreversible.",
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
    const tipoVehiculo=     form.get("tipoVehiculo");
    const placaVehiculo=    form.get("placaVehiculo")
    const marcaVehiculo =   form.get ("marcaVehiculo");
    const modeloVehiculo=   form.get("modeloVehiculo");
    const cantCascoVehiculo=form.get("cantCasco");

    const data= {
        action: "edit",
        idVehiculo: idVehiculo,
        tipoVehiculo: tipoVehiculo,
        placaVehiculo: placaVehiculo,
        marcaVehiculo: marcaVehiculo,
        modeloVehiculo: modeloVehiculo,
        cantCascoVehiculo: cantCascoVehiculo
    }

    const response= await sendRequest(`${host}/vehiculo`)

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
        id_vehiculo: id_vehiculo,
    };
    const response= await sendRequest( `${host}/tipoDoc`,data)

    if(response.status === "success"){
        console.log("Se ha Eliminado el vehiculo correctamente")
        showSuccessAlert(response.message)
    }else {
        showErrorDialog(response.message)
    }
}
