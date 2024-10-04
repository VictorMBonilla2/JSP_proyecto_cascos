import {host} from "./config.js";

document.addEventListener("DOMContentLoaded", async () => {
    const tabla = document.querySelector(".reportes__content");
    const reporteTemplate = document.getElementById('reporte-template').content;
    const modalTemplate = document.getElementById('modal-template').content;
    const documentoUser = document.querySelector("#idUsuario").value;
    let modalCounter = 0;  // Añade un contador global

    try {
        const responseDocument = await fetch(`${host}/SvReportes?iduser=${documentoUser}`);
        const reportes = await responseDocument.json();
        console.log(reportes);

        if (!Array.isArray(reportes)) {
            throw new Error("El JSON recibido no es un array.");
        }

        const reportesPorFecha = reportes.reduce((acc, reporte) => {
            const fecha = reporte.fecha_reporte.split(" ")[0];
            if (!acc[fecha]) {
                acc[fecha] = [];
            }
            acc[fecha].push(reporte);
            return acc;
        }, {});

        for (const [fecha, reportes] of Object.entries(reportesPorFecha)) {
            const fechaDiv = document.createElement("div");
            fechaDiv.classList.add("reportes__fecha", "estilo__casillero");
            fechaDiv.innerHTML = `<p>${fecha}</p>`;
            tabla.appendChild(fechaDiv);

            reportes.forEach((reporte) => {
                const reporteItem = document.importNode(reporteTemplate, true);
                const modalId = `report${modalCounter++}`;  // Usa un contador único para el ID

                // Actualiza el contenido del reporte
                reporteItem.querySelector('.item__casillero').textContent = reporte.documento_aprendiz;
                reporteItem.querySelector('.item__type').textContent = reporte.tipo_reporte;
                reporteItem.querySelector('.item__placa').textContent = reporte.placa_vehiculo;
                reporteItem.querySelector('.report__button').setAttribute('data-modal-id', modalId);

                tabla.appendChild(reporteItem);

                // Clona y actualiza el contenido del modal
                const modalDiv = document.importNode(modalTemplate, true);
                modalDiv.querySelector('.modal').id = modalId;
                modalDiv.querySelector('.close').setAttribute('data-modal-id', modalId);
                modalDiv.querySelector('.modal-body h3').textContent = reporte.descripcion_reporte;
                modalDiv.querySelector('.modal-body h3').textContent = reporte.descripcion_reporte;

                document.body.appendChild(modalDiv);
            });
        }

        // Manejo de eventos para abrir y cerrar modales
        document.addEventListener('click', (event) => {
            if (event.target.matches('.report__button')) {
                const modalId = event.target.getAttribute('data-modal-id');
                document.getElementById(modalId).style.display = 'flex';
            } else if (event.target.matches('.close')) {
                const modalId = event.target.getAttribute('data-modal-id');
                document.getElementById(modalId).style.display = 'none';
            }
        });

        // Cerrar modales al hacer clic fuera de ellos
        window.addEventListener('click', (event) => {
            if (event.target.classList.contains('modal')) {
                event.target.style.display = 'none';
            }
        });

    } catch (error) {
        console.error('Error fetching or parsing JSON:', error);
    }
});
