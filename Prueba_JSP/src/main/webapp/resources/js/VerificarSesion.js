document.addEventListener("DOMContentLoaded", function() {
    const estado_sesion = document.getElementById('sesionStatus');

    console.log(estado_sesion);

    console.log("Se verifica la sesion");
    if (estado_sesion === null || estado_sesion.value === "false") {
        console.log("Sesion invalida");
        alert("Sesion invalida. Ingrese nuevamente");
        window.location.href = "index.jsp";
    }
});