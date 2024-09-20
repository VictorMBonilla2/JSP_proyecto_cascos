document.addEventListener('DOMContentLoaded', function() {
    // Selecciona todos los inputs cuyo ID comienza con 'adddocumento'
    const documentoInputs = document.querySelectorAll('input[id^="adddocumento"]');

    // Itera sobre cada input y añade el evento 'blur'
    documentoInputs.forEach(input => {
        // Extrae el espacioId del ID del input
        const espacioId = input.id.replace('adddocumento', '');

        // Añade el evento 'blur'
        input.addEventListener('blur', function() {
            fetchVehiculos(espacioId);
        });
    });
});

async function fetchVehiculos(espacioId) {
    const documento = document.getElementById(`adddocumento${espacioId}`).value;
    const selectVehiculo = document.getElementById(`addvehiculolist${espacioId}`);
    const cantidadCascosInput = document.getElementById(`addcant_cascos${espacioId}`);
    // Verifica que el documento no esté vacío
    if (documento.trim() === '') {
        return;
    }

    // Realiza una solicitud AJAX (fetch) al servidor
    const response= await fetch(`listaVehiculos?documento=${documento}`);
    if (response.status === 204) {
        console.log('No se encontraron vehículos.');
        return []; // o alguna otra acción
    }
    const data = await response.json();
    if (data.length === 0) {
        console.log('No se encontraron vehículos.');
    } else {
        // Procesar la lista de vehículos
        let contador = 1; // Inicializar contador fuera del forEach

    data.forEach(vehiculo => {
            const option = document.createElement('option');
            option.value = vehiculo.id;
            option.textContent = `${contador}. ${vehiculo.marca} - ${vehiculo.placa}`;
            option.dataset.cantidadCascos = vehiculo.cantidad_cascos; // Guardar cantidad de cascos como un atributo de datos
            selectVehiculo.appendChild(option);
            contador++;
    });

        // Escuchar cambios en la selección del vehículo
        selectVehiculo.addEventListener('change', function() {
            const selectedOption = selectVehiculo.options[selectVehiculo.selectedIndex]; // Corregir acceso al objeto de opción
            cantidadCascosInput.value = selectedOption.dataset.cantidadCascos || 0; // Actualizar el valor del input
        });

        // Actualizar el input de cantidad de cascos al cargar la página con el primer vehículo
        if (selectVehiculo.options.length > 0) {
            const firstOption = selectVehiculo.options[0];
            cantidadCascosInput.value = firstOption.dataset.cantidadCascos || 0;
        }
    }


}
