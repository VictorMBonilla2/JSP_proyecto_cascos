document.addEventListener('DOMContentLoaded', function () {
    let detallesbuttons= document.getElementsByClassName("report__button")

    for ( let i = 0; i < detallesbuttons.length; i++){
        detallesbuttons[i].onclick = function (){
            let modalId = this.getAttribute("report")
            let modal = document.getElementById(modalId);
            modal.style.display= "flex"
        };
    }
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


})