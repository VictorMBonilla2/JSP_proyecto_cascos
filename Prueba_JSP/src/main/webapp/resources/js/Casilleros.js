document.addEventListener('DOMContentLoaded', function () {
    // Obtén todos los botones con la clase addCasilleroBtn
    let buttonsadd = document.getElementsByClassName("addCasilleroBtn");
    let buttonedit =document.getElementsByClassName("botones__ajustar");
    let buttonpay =document.getElementsByClassName("botones__pago");


    // Añade un event listener a cada botón pagar
    for (let i = 0; i < buttonpay.length; i++) {
        buttonpay[i].onclick = function () {
            let modalId = this.getAttribute("data-pay");
            let modal = document.getElementById(modalId);
            modal.style.display = "flex";
        };
    }
    // Añade un event listener a cada botón añadir
    for (let i = 0; i < buttonsadd.length; i++) {
        buttonsadd[i].onclick = function () {
            let modalId = this.getAttribute("data-add");
            let modal = document.getElementById(modalId);
            modal.style.display = "flex";
        };
    }
    // Añade un event listener a cada botón editar
    for (let i = 0; i < buttonedit.length; i++) {
        buttonedit[i].onclick = function () {
            let modalId = this.getAttribute("data-edit");
            let modal = document.getElementById(modalId);
            modal.style.display = "flex";
        };
    }


    // Obtén todos los elementos <span> que cierran los modales
    let closeButtons = document.getElementsByClassName("close");

    // Añade un event listener a cada botón de cierre
    for (let i = 0; i < closeButtons.length; i++) {
        closeButtons[i].onclick = function () {
            let modalId = this.getAttribute("data-modal-id");
            let modal = document.getElementById(modalId);
            modal.style.display = "none";
        };
    }

    // Cuando el usuario hace clic fuera del modal, cierra el modal
    window.onclick = function (event) {
        if (event.target.classList.contains('modal')) {
            event.target.style.display = "none";
        }
    }

   //Metodo uno para escuchar a los form

    // // Encuentra todos los formularios que empiezan con "addCasco"
    // const addforms = document.querySelectorAll('form[id^="addCasco"]');
    // const editforms = document.querySelectorAll('form[id^="editCasco"]');
    // const payforms = document.querySelectorAll('form[id^="payCasco"]');
    //
    // // Asigna el manejador de eventos a cada formulario
    // addforms.forEach(form => {
    //     form.onsubmit = function (addevent) {
    //         const espacioId = this.id.replace('addCasco', '');
    //         return validateAndSubmit(addevent, espacioId, 'add');
    //     };
    // });
    // editforms.forEach(form => {
    //     form.onsubmit = function (editevent) {
    //         const espacioId = this.id.replace('editCasco', '');
    //         return validateAndSubmit(editevent, espacioId, 'edit');
    //     };
    // });
    // payforms.forEach(form => {
    //     form.onsubmit = function (payevent) {
    //         const espacioId = this.id.replace('payCasco', '');
    //         return validateAndSubmit(payevent, espacioId, 'pay');
    //     };
    // });

});

function validateAndSubmit(addevent, espacioId, formType) {
    event.preventDefault(); // Previene el envío del formulario


    if (formType === "pay") {

    }else if (formType === "add" || formType === "edit") {
        // Obtén los valores de los inputs
        const documento = document.getElementById(`${formType}placa${espacioId}`).value;
        const ciudad = document.getElementById(`${formType}ciudad${espacioId}`).value;
        const cantCascos = document.getElementById(`${formType}cant_cascos${espacioId}`).value;

        // Realiza la validación
        if (documento.trim() === '' || ciudad.trim() === '' || cantCascos.trim() === '') {
            alert('Por favor, llena todos los campos');
            return false; // Previene el envío del formulario si hay errores de validación
        }

        // Si la validación es exitosa, puedes proceder a enviar el formulario
        const form = document.getElementById(`${formType}Casco${espacioId}`);
        return addDataCasilleros(form, espacioId, formType); // Envía el formulario
    }


}

async function addDataCasilleros(form,espacioId, formType) {
    const placa = document.getElementById(`${formType}placa${espacioId}`).value;
    const ciudad = document.getElementById(`${formType}ciudad${espacioId}`).value;
    const cantcascos = document.getElementById(`${formType}cant_cascos${espacioId}`).value
    try {


        // Enviar la solicitud fetch al servidor
        const response = await fetch("SvCasillero", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                espacio: espacioId,
                placa: placa,
                ciudad: ciudad,
                cantcascos: cantcascos,
                formType: formType
            })
        });
        // Verificar si la respuesta es exitosa
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }

        // Obtener el resultado de la respuesta
        const result = await response.json();

        // Verificar el resultado del inicio de sesión
        if (result.status === "success") {
            // Redireccionar al usuario a la página de inicio
            window.location.href = "SvCasillero";
        } else {
            // Mostrar mensaje de error en caso de credenciales inválidas
            document.getElementById("Error").style.display = "block";
        }
    } catch (error) {
        // Manejar cualquier error que ocurra durante la solicitud
        console.error("There was a problem with the fetch operation:", error);
        // Mostrar un mensaje de error genérico al usuario
        document.getElementById("Error").style.display = "block";
    }

}
//Metodo 2 para escuchar los formularios.

function addCasco(event, espacioId) {
    validateAndSubmit(event, espacioId, 'add');
}

function editCasco(event, espacioId) {
    validateAndSubmit(event, espacioId, 'edit');
}

function payCasco(event, espacioId) {
    validateAndSubmit(event, espacioId, 'pay');
}