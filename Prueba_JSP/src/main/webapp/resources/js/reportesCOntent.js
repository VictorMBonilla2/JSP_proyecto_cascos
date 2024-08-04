document.addEventListener("DOMContentLoaded", async () => {
    const tabla = document.querySelector(".reportes__content");

    try {
        const responseDocument = await fetch("SvReportes");
        const reportes = await responseDocument.json();

        // Verifica si reportes es un array
        if (!Array.isArray(reportes)) {
            throw new Error("El JSON recibido no es un array.");
        }

        // Agrupar reportes por fecha
        const reportesPorFecha = reportes.reduce((acc, reporte) => {
            // Extraer solo la fecha sin la hora
            const fecha = reporte.fecha_reporte.split(" ")[0];
            if (!acc[fecha]) {
                acc[fecha] = [];
            }
            acc[fecha].push(reporte);
            return acc;
        }, {});

        // Crear los elementos HTML
        for (const [fecha, reportes] of Object.entries(reportesPorFecha)) {
            // Crear el div de fecha
            const fechaDiv = document.createElement("div");
            fechaDiv.classList.add("reportes__fecha", "estilo__casillero");
            fechaDiv.innerHTML = `<p>${fecha}</p>`;
            tabla.appendChild(fechaDiv);

            // Crear los reportes para esa fecha
            reportes.forEach((reporte, index) => {
                const reporteItemDiv = document.createElement("div");
                reporteItemDiv.classList.add("report__item");

                const modalId = `report${index}`;

                reporteItemDiv.innerHTML = `
                    <p>Casillero</p>
                    <p class="item__casillero">${reporte.documento_aprendiz}</p>
                    <p>Tipo</p>
                    <p class="item__type">${reporte.tipo_reporte}</p>
                    <p>Placa</p>
                    <p class="item__placa">${reporte.placa_vehiculo}</p>
                    <button class="report__button" report="${modalId}">Detalles</button>
                `;
                tabla.appendChild(reporteItemDiv);

                const linea = document.createElement("hr");
                linea.classList.add("linea");
                tabla.appendChild(linea);

                // Crear el modal correspondiente
                const modalDiv = document.createElement("div");
                modalDiv.id = modalId;
                modalDiv.classList.add("modal");

                modalDiv.innerHTML = `
                    <div class="modal-content">
                        <div class="modal-header">
                            <h2>Detalles Reporte</h2>
                            <span class="close" data-modal-id="${modalId}">&times;</span>
                        </div>
                        <div class="modal-body">
                            <h3>${reporte.descripcion_reporte}</h3>
                        </div>
                    </div>
                `;
                document.body.appendChild(modalDiv);
            });
        }
    } catch (error) {
        console.error('Error fetching or parsing JSON:', error);
    }
    });
