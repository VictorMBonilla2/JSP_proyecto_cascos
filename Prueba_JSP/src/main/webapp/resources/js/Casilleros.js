document.addEventListener('DOMContentLoaded', function () {
    // Obtén todos los botones con la clase addCasilleroBtn
    let buttons = document.getElementsByClassName("addCasilleroBtn");

    // Añade un event listener a cada botón
    for (let i = 0; i < buttons.length; i++) {
        buttons[i].onclick = function () {
            let modalId = this.getAttribute("data-add");
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

    // Encuentra todos los formularios que empiezan con "addCasco"
    const forms = document.querySelectorAll('form[id^="addCasco"]');

    // Asigna el manejador de eventos a cada formulario
    forms.forEach(form => {
        form.onsubmit = function (event) {
            // Extrae el espacioId del ID del formulario
            const espacioId = this.id.replace('addCasco', '');
            return validateAndSubmit(event, espacioId);
        };
    });
});

function validateAndSubmit(event, espacioId) {
    event.preventDefault(); // Previene el envío del formulario

    // Obtén los valores de los inputs
    const documento = document.getElementById(`placa${espacioId}`).value;
    const ciudad = document.getElementById(`ciudad${espacioId}`).value;
    const cantCascos = document.getElementById(`cant_cascos${espacioId}`).value;

    // Realiza la validación
    if (documento.trim() === '' || ciudad.trim() === '' || cantCascos.trim() === '') {
        alert('Por favor, llena todos los campos');
        return false; // Previene el envío del formulario si hay errores de validación
    }

    // Si la validación es exitosa, puedes proceder a enviar el formulario
    const form = document.getElementById(`addCasco${espacioId}`);
     return  addCasilleroBtn(form,espacioId); // Envía el formulario




}
async function addCasilleroBtn(form) {
    const placa = document.getElementById(`placa${espacioId}`).value;
    const ciudad = document.getElementById(`ciudad${espacioId}`).value;
    const cantcascos = document.getElementById(`cant_cascos${espacioId}`).value;
    try {


        // Enviar la solicitud fetch al servidor
        const response = await fetch("SvCasillero", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                placa: placa,
                ciudad: ciudad,
                cantcascos: cantcascos
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