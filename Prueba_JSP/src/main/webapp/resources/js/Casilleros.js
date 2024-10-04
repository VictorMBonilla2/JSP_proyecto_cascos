import {sendRequest} from "./ajax.js";
import {showSuccessAlert} from "./alerts/success.js";
import {showErrorDialog} from "./alerts/error.js";
document.addEventListener('submit', function(e) {
    e.preventDefault();
    const form = new FormData(e.target);

    const typeForm = form.get("typeForm");

    switch (typeForm){
        case "add":
        case "edit":
            addDataCasilleros(form);
            break;
        case "liberar":
            payCasillero(form);
            break;
        case "report":
            reportEspacio(form)
            break;
        default:
            "Accion no identificada";
            break;
    }
});

async function addDataCasilleros(form) {
    const typeForm = form.get("typeForm");
    const documento  = form.get("documento")
    const idVehiculo = form.get("options")
    const cantcascos = form.get("cant_cascos")
    const espacioId  = form.get("idEspacio")
    const data = {
        espacio: espacioId,
        idVehiculo: idVehiculo,
        documento: documento,
        cantcascos: cantcascos,
        formType: typeForm
    };

    const response =  await sendRequest("SvCasillero", data);
    if (response.status === "success") {
        console.log("Marca creada correctamente");
        showSuccessAlert(response.message);
    } else {
        showErrorDialog(response.message);
    }

}

async function payCasillero(form) {
    const espacioId = form.get("idEspacio")
    const formType = form.get("typeForm");
    const data = {
        espacio: espacioId,
        formType: formType
    };

    const response = await sendRequest("SvCasillero", data);
    if (response.status === "success") {
        console.log("Marca creada correctamente");
        showSuccessAlert(response.message);
    } else {
        showErrorDialog(response.message);
    }
}

async function reportEspacio(form) {
    const espacioId = form.get("idEspacio");
    const tipoReporte = form.get("options");
    const nombreReporte = form.get("reportNombre");
    const DescReporte = form.get("description");
    const formType = form.get("typeForm");
    const data = {
        espacio: espacioId,
        tipoReporte: tipoReporte,
        nombreReporte: nombreReporte,
        DescReporte: DescReporte,
        formType: formType
    };

    const response = await sendRequest("SvCasillero", data);
    if (response.status === "success") {
        console.log("Marca creada correctamente");
        showSuccessAlert(response.message);
    } else {
        showErrorDialog(response.message);
    }
}



