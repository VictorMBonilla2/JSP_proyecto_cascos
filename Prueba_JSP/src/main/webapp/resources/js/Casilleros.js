import { sendRequest } from "./ajax.js";
import { showSuccessAlert } from "./alerts/success.js";
import { showErrorDialog } from "./alerts/error.js";
import {renderEspacios} from "./renderCasilleros.js";
import {validarDocumento, validarCantidadCascos, validarNombreReporte,validarDescripcionReporte} from "./utils/validations.js";

document.addEventListener('submit', function (e) {
    e.preventDefault();
    const form = new FormData(e.target);

    const typeForm = form.get("typeForm");

    switch (typeForm) {
        case "add":
        case "edit":
            if (validarDatosCasilleros(form)) {
                addDataCasilleros(form);
            }
            break;
        case "liberar":
            payCasillero(form);
            break;
        case "report":
            if (validarDatosReporte(form)) {
                reportEspacio(form);
            }
            break;
        default:
            console.error("Acción no identificada");
            break;
    }
});

// Función para validar datos del formulario de casilleros
function validarDatosCasilleros(form) {
    const documento = form.get("documento");
    const cantCascos = form.get("cant_cascos");
    if (!validarDocumento(documento)) {
        showErrorDialog("El documento debe contener solo números positivos.");
        return false;
    }

    if (!validarCantidadCascos(cantCascos)) {
        showErrorDialog("La cantidad de cascos debe ser entre 0 y 2.");
        return false;
    }

    return true;
}

// Función para validar datos del reporte
function validarDatosReporte(form) {
    const nombreReporte = form.get("reportNombre");
    const descripcionReporte = form.get("description");

    if (!validarNombreReporte(nombreReporte)) {
        showErrorDialog("El nombre del reporte no debe superar los 20 caracteres y no debe estar vacío.");
        return false;
    }

    if (!validarDescripcionReporte(descripcionReporte)) {
        showErrorDialog("La descripción del reporte no debe superar los 500 caracteres y no debe estar vacía.");
        return false;
    }

    return true;
}

async function addDataCasilleros(form) {
    const data = {
        espacio: form.get("idEspacio"),
        idVehiculo: form.get("options"),
        documento: form.get("documento"),
        cantcascos: form.get("cant_cascos"),
        formType: form.get("typeForm")
    };

    const response = await sendRequest("SvCasillero", data);
    if (response.status === "success") {
        showSuccessAlert(response.message);
        await renderEspacios();
        const modal = document.querySelector(".modal");
        if (modal) {
            modal.remove();
        }
    } else {
        showErrorDialog(response.message);
    }
}

async function payCasillero(form) {
    const data = {
        espacio: form.get("idEspacio"),
        formType: form.get("typeForm")
    };

    const response = await sendRequest("SvCasillero", data);
    if (response.status === "success") {
        showSuccessAlert(response.message);
        await renderEspacios();
        const modal = document.querySelector(".modal");
        if (modal) {
            modal.remove();
        }
    } else {
        showErrorDialog(response.message);
    }
}

async function reportEspacio(form) {
    const data = {
        espacio: form.get("idEspacio"),
        tipoReporte: form.get("options"),
        nombreReporte: form.get("reportNombre"),
        DescReporte: form.get("description"),
        formType: form.get("typeForm")
    };

    const response = await sendRequest("SvCasillero", data);
    if (response.status === "success") {
        showSuccessAlert(response.message);
        await renderEspacios();
        const modal = document.querySelector(".modal");
        if (modal) {
            modal.remove();
        }
    } else {
        showErrorDialog(response.message);
    }
}


