import {sendRequest} from "./ajax.js";

document.addEventListener('DOMContentLoaded', function() {

    // Funciones para manejar la validación y envío
    function validateAndSubmit(event, espacioId, formType) {
        event.preventDefault(); // Previene el envío del formulario

        // Realizar la validación basada en el tipo de formulario
        if (!validarFormulario(espacioId, formType)) {
            alert('Por favor, llena todos los campos');
            return false; // Previene el envío del formulario si hay errores de validación
        }

        // Mapa de funciones para manejar los diferentes tipos de formulario
        const functionMap = {
            "add": addDataCasilleros,
            "edit": addDataCasilleros, // Reutiliza la misma función para "add" y "edit"
            "pay": payCasillero,
            "report": reportEspacio
        };

        // Ejecuta la función correspondiente si existe en el mapa
        if (functionMap[formType]) {
            const form = document.getElementById(`${formType}Casco${espacioId}`);
            return functionMap[formType](form, espacioId, formType); // Llama a la función correspondiente
        } else {
            console.error(`Tipo de formulario desconocido: ${formType}`);
            mostrarError(`Tipo de formulario desconocido: ${formType}`);
        }
    }

    function validarFormulario(espacioId, formType) {
        // Validar campos comunes
        const documento = document.getElementById(`${formType}documento${espacioId}`);
        console.log(documento)
        const idVehiculo = document.getElementById(`${formType}vehiculolist${espacioId}`);
        console.log(idVehiculo)
        const cantCascos = document.getElementById(`${formType}cant_cascos${espacioId}`);
        console.log(cantCascos)

        if (formType === "add") {
            // Validación específica para "add" y "edit"
            return documento && documento.value.trim() !== '' &&
                idVehiculo && idVehiculo.value.trim() !== '' &&
                cantCascos && cantCascos.value !== '';
        }
        if(formType === "edit"){
          return  cantCascos && cantCascos.value !== '';
        }

        if (formType === "report") {
            const tipoReporte = document.getElementById(`${formType}Tipo${espacioId}`);
            const nombreReporte = document.getElementById(`${formType}Nombre${espacioId}`);
            const DescReporte = document.getElementById(`${formType}Descripcion${espacioId}`);
            // Validación específica para "report"
            return tipoReporte && tipoReporte.value.trim() !== '' &&
                nombreReporte && nombreReporte.value.trim() !== '' &&
                DescReporte && DescReporte.value.trim() !== '';
        }

        // No se requiere validación adicional para "pay"
        return true;
    }

    async function addDataCasilleros(form, espacioId, formType) {
        const documento = document.getElementById(`${formType}documento${espacioId}`).value;
        const idVehiculo = document.getElementById(`${formType}vehiculolist${espacioId}`).value;
        console.log(idVehiculo)
        const cantcascos = document.getElementById(`${formType}cant_cascos${espacioId}`).value;

        const data = {
            espacio: espacioId,
            idVehiculo: idVehiculo,
            documento: documento,
            cantcascos: cantcascos,
            formType: formType
        };

        await sendRequest("SvCasillero", data);
    }

    async function payCasillero(form, espacioId, formType) {
        const data = {
            espacio: espacioId,
            formType: formType
        };

        await sendRequest("SvCasillero", data);
    }

    async function reportEspacio(form, espacioId, formType) {
        const tipoReporte = document.getElementById(`${formType}Tipo${espacioId}`).value;
        const nombreReporte = document.getElementById(`${formType}Nombre${espacioId}`).value;
        const DescReporte = document.getElementById(`${formType}Descripcion${espacioId}`).value;

        const data = {
            espacio: espacioId,
            tipoReporte: tipoReporte,
            nombreReporte: nombreReporte,
            DescReporte: DescReporte,
            formType: formType
        };

        await sendRequest("SvCasillero", data);
    }

    function mostrarError(mensaje) {
        const errorElement = document.getElementById("Error");
        errorElement.textContent = mensaje;
        errorElement.style.display = "block";
    }

    // Método para escuchar los formularios.
    window.addCasco = function(event, espacioId) {
        console.log("Se ejecuta Añadir casco")
        validateAndSubmit(event, espacioId, 'add');
    }

    window.editCasco = function(event, espacioId) {
        validateAndSubmit(event, espacioId, 'edit');
    }

    window.payCasco = function(event, espacioId) {
        validateAndSubmit(event, espacioId, 'pay');
    }

    window.reportCasco = function(event, espacioId) {
        validateAndSubmit(event, espacioId, 'report');
    }
});
