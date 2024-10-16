import {hideLoadingSpinner, sendRequest, showLoadingSpinner} from "./ajax.js";
import {showSuccessAlert} from "./alerts/success.js";
import {showErrorDialog} from "./alerts/error.js";
import {renderEspacios} from "./renderCasilleros.js";
import {
    validarCantidadCascos,
    validarDescripcionReporte,
    validarDocumento,
    validarNombreReporte
} from "./utils/validations.js";
import {showConfirmationDialog} from "./alerts/confirm.js";

document.addEventListener('submit', async function (e) {
    e.preventDefault();
    const form = new FormData(e.target);
    const submitButton = e.target.querySelector("button[type='submit']");
    submitButton.disabled = true;
    showLoadingSpinner()
    const typeForm = form.get("typeForm");
    try{
        switch (typeForm) {
            case "add":
            case "edit":
                if (validarDatosCasilleros(form)) {
                    await addDataCasilleros(form);
                }
                break;
            case "liberar":
                await payCasillero(form);
                break;
            case "report":
                if (validarDatosReporte(form)) {
                    showConfirmationDialog(
                        "¿Enviar Reporte?",
                        "Al realizar un reporte, el espacio se desocupara automáticamente, se le vinculará a usted y al usuario que esté usando el espacio seleccionado en el reporte. El reporte es irreversible.",
                        async () => {
                            await reportEspacio(form);
                        },
                        () => console.log("Acción cancelada")
                    )
                }
                break;
            default:
                console.error("Acción no identificada");
                break;
        }
    } finally {
        hideLoadingSpinner()
        submitButton.disabled = false;
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
        showErrorDialog("El nombre del reporte no debe superar los 40 caracteres y no debe estar vacío.");
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


