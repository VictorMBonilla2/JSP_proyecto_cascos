document.addEventListener("DOMContentLoaded", function() {
    function actualizarcasilleros(){
    const casilleros = document.querySelectorAll(".casillero");

    casilleros.forEach(casillero => {
        const tiempoEntrada = parseInt(casillero.getAttribute("data-entrada"));
        const tarifaPorHora = parseFloat(casillero.getAttribute("data-tarifa"));
        const tiempoActual = Date.now();
        const tiempoTranscurrido = tiempoActual - tiempoEntrada;

        // Convertir el tiempo transcurrido a horas y minutos
        const horas = Math.floor(tiempoTranscurrido / (1000 * 60 * 60));
        const minutos = Math.floor((tiempoTranscurrido / (1000 * 60)) % 60);

        // Calcular el costo basado en la tarifa por hora
        const costo = (tiempoTranscurrido / (1000 * 60 * 60)) * tarifaPorHora;

        // Mostrar el tiempo transcurrido y el costo en el HTML
        casillero.querySelector(".tiempo-transcurrido").textContent = `${horas} horas y ${minutos} minutos`;
        casillero.querySelector(".costo").textContent = `$${costo.toFixed(2)}`;
    });
    }
    actualizarcasilleros();


    setInterval(actualizarcasilleros,1000);



});