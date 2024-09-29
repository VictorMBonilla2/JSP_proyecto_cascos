document.addEventListener('DOMContentLoaded', function () {
    // Obtén todos los botones con la clase addCasilleroBtn
    let buttonsadd = document.getElementsByClassName("addCasilleroBtn");
    let buttonedit =document.getElementsByClassName("botones__ajustar");
    let buttonpay =document.getElementsByClassName("botones__pago");
    let buttonreport=document.querySelectorAll(".report__img");

    console.log(buttonreport)
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
    // Añade un event listener a cada botón reportar
    for(let i = 0; i <buttonreport.length; i++){
        buttonreport[i].onclick= function (){
            console.log("has presionado")
            let modalId= this.getAttribute("data-report");
            let modal= document.getElementById(modalId);
            modal.style.display= "flex";
        }
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