document.addEventListener("DOMContentLoaded", function() {
    function actualizarcasilleros() {
        const casilleros = document.querySelectorAll(".casillero");

        casilleros.forEach(casillero => {
            const tiempoEntrada = casillero.getAttribute("data-entrada");
            const tarifaPorHora = casillero.getAttribute("data-tarifa");

            // Solo procesar casilleros que tienen los atributos necesarios
            if (tiempoEntrada !== null && tarifaPorHora !== null) {
                const tiempoEntradaMs = parseInt(tiempoEntrada);
                const tarifaPorHoraFloat = parseFloat(tarifaPorHora);
                const tiempoActual = Date.now();
                const tiempoTranscurrido = tiempoActual - tiempoEntradaMs;

                // Convertir el tiempo transcurrido a horas y minutos
                const horas = Math.floor(tiempoTranscurrido / (1000 * 60 * 60));
                const minutos = Math.floor((tiempoTranscurrido / (1000 * 60)) % 60);

                // Calcular el costo basado en la tarifa por hora
                const costo = (tiempoTranscurrido / (1000 * 60 * 60)) * tarifaPorHoraFloat;

                // Verificar que los elementos existen antes de intentar modificar sus propiedades
                const tiempoTranscurridoElement = casillero.querySelector(".tiempo-transcurrido");
                const costoElement = casillero.querySelector(".costo");

                tiempoTranscurridoElement.textContent = `${horas} horas y ${minutos} minutos`;
                costoElement.textContent = `$${costo.toFixed(2)}`;

            }
        });
    }

    actualizarcasilleros();

    setInterval(actualizarcasilleros, 1000);
});
