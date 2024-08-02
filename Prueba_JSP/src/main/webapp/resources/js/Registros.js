document.addEventListener("DOMContentLoaded", function() {
    const cuerpoTabla = document.querySelector("tbody");

    async function fetchRegistros() {
        try {
            const response = await fetch("SvRegistros");
            if (!response.ok) {
                throw new Error("Network response was not ok " + response.statusText);
            }
            const registros = await response.json();
            populateTable(registros);
        } catch (error) {
            console.error("Error fetching registros: ", error);
        }
    }

    function populateTable(registros) {
        cuerpoTabla.innerHTML = ''; // Limpiar tabla antes de llenarla
        registros.forEach(registro => {
            const row = document.createElement("tr");

            const fechaCell = document.createElement("td");
            fechaCell.textContent = registro.fecha_reporte;  // Ajusta esto según tu formato de fecha
            row.appendChild(fechaCell);

            const espacioCell = document.createElement("td");
            espacioCell.textContent = registro.id_espacio;
            row.appendChild(espacioCell);

            const vehiculoCell = document.createElement("td");
            vehiculoCell.textContent = registro.placa_vehiculo;
            row.appendChild(vehiculoCell);

            const aprendizCell = document.createElement("td");
            aprendizCell.textContent = registro.documento_aprendiz;
            row.appendChild(aprendizCell);

            const colaboradorCell = document.createElement("td");
            colaboradorCell.textContent = registro.documento_colaborador;
            row.appendChild(colaboradorCell);

            cuerpoTabla.appendChild(row);
        });
    }

    fetchRegistros();
});








