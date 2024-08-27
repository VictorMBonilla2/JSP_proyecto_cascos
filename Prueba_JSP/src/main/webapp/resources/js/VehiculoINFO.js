
let currentIndex = 0;
const vehiculoCount = parseInt(document.getElementById("vehiculoCount").value);

function showVehiculo(index) {
    // Ocultar todos los vehículos
    for (let i = 0; i < vehiculoCount; i++) {
        document.getElementById('vehiculo' + i).style.display = 'none';
    }
    // Mostrar el vehículo actual
    document.getElementById('vehiculo' + index).style.display = 'block';
    // Actualizar botones
    document.getElementById('prevButton').disabled = index === 0;
    document.getElementById('nextButton').disabled = index === vehiculoCount - 1;
}

function showPrevVehiculo() {
    if (currentIndex > 0) {
        currentIndex--;
        showVehiculo(currentIndex);
    }
}

function showNextVehiculo() {
    if (currentIndex < vehiculoCount - 1) {
        currentIndex++;
        showVehiculo(currentIndex);
    }
}

// Inicializar mostrando el primer vehículo
document.addEventListener("DOMContentLoaded", function() {
    showVehiculo(0);
});