
    let currentIndex = 0;
    const totalVehiculos = <%= vehiculoUser.size() %>;

    function showVehiculo(index) {
    // Oculta el vehículo actual
    document.getElementById(`vehiculo${currentIndex}`).style.display = "none";

    // Muestra el nuevo vehículo
    document.getElementById(`vehiculo${index}`).style.display = "block";

    // Actualiza el índice actual
    currentIndex = index;

    // Habilita o deshabilita botones según el índice actual
    document.getElementById('prevButton').disabled = currentIndex === 0;
    document.getElementById('nextButton').disabled = currentIndex === totalVehiculos - 1;
}

    function showNextVehiculo() {
    if (currentIndex < totalVehiculos - 1) {
    showVehiculo(currentIndex + 1);
}
}

    function showPrevVehiculo() {
    if (currentIndex > 0) {
    showVehiculo(currentIndex - 1);
}
}
