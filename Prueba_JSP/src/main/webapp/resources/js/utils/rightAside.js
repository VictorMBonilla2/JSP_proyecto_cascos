document.addEventListener("DOMContentLoaded", function() {
    const botonMostrar = document.getElementById("botonMostrar");
    const contenedorIcono = document.getElementById("contenedorIcono");
    const sidebar = document.getElementById("right-sidebar");
    const closeButton = document.querySelector(".close-button");

    botonMostrar.addEventListener("click", function() {
        sidebar.style.display = "flex";
        contenedorIcono.classList.toggle("escondido");
    });

    closeButton.addEventListener("click", function() {
        sidebar.style.display = "none";
        contenedorIcono.classList.add("escondido");
    });
    window.onclick = function (event) {
        if (event.target.id === 'right-sidebar') {
            sidebar.style.display = "none";
            contenedorIcono.classList.add("escondido");
        }
    }
});