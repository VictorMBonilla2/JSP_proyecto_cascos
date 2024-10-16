import {host} from "./config.js";

document.addEventListener("DOMContentLoaded", async () => {
    const tabla = document.querySelector(".reportes__content__main");
    const reporteTemplate = document.getElementById('reporte-template').content;
    const modalTemplate = document.getElementById('modal-template').content;
    const documentoUser = document.querySelector("#idUsuario").value;

    let modalCounter = 0;  // Añade un contador global
    let reportes;
    try {
        const responseDocument = await fetch(`${host}/SvReportes?iduser=${documentoUser}`);
        reportes = await responseDocument.json();
        console.log(reportes);

        if (!Array.isArray(reportes)) {
            throw new Error("El JSON recibido no es un array.");
        }
    } catch (error) {
        console.error('Error fetching or parsing JSON:', error);
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
            const contenedorTabla = document.createElement("div")
            contenedorTabla.classList.add("reportes__content","estilo__contenido");

            const fechaDiv = document.createElement("div");
            fechaDiv.classList.add("reportes__fecha", "estilo__casillero");
            fechaDiv.innerHTML = `<h1>${fecha}</h1>`;
            contenedorTabla.appendChild(fechaDiv);
            let modalId = `report${modalCounter}`;
            console.log(modalId)
            reportes.forEach((reporte) => {
                console.log(modalId)
                const reporteItem = document.importNode(reporteTemplate, true);
                  // Usa un contador único para el ID

                // Actualiza el contenido del reporte
                reporteItem.querySelector('.item__casillero').textContent = reporte.documento_aprendiz;
                reporteItem.querySelector('.item__type').textContent = reporte.tipo_reporte;
                reporteItem.querySelector('.item__placa').textContent = reporte.placa_vehiculo;
                reporteItem.querySelector('.button_primary').setAttribute('data-modal-id', modalId);

                contenedorTabla.appendChild(reporteItem);

                // Clona y actualiza el contenido del modal
                const modalDiv = document.importNode(modalTemplate, true);
                modalDiv.querySelector('.modal').id = modalId;
                modalDiv.querySelector('.close').setAttribute('data-modal-id', modalId);
                modalDiv.querySelector('.nombreReporte').textContent = reporte.nombre_reporte;
                modalDiv.querySelector('.nombreReportante').textContent = reporte.nombre_colaborador;
                modalDiv.querySelector('.documentoReportante').textContent = reporte.documento_colaborador;
                modalDiv.querySelector('.nombreReportado').textContent = reporte.nombre_colaborador;
                modalDiv.querySelector('.documentoReportado').textContent = reporte.documento_aprendiz;
                modalDiv.querySelector('.placaReportado').textContent = reporte.placa_vehiculo;
                modalDiv.querySelector('.tipoReporte').textContent = reporte.tipo_reporte;
                modalDiv.querySelector('.descripcionReporte').textContent = reporte.descripcion_reporte;

                document.body.appendChild(modalDiv);

                modalCounter++
            });

            tabla.appendChild(contenedorTabla)
        }

        // Manejo de eventos para abrir y cerrar modales
        document.addEventListener('click', (event) => {
            if (event.target.matches('.button_primary')) {
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


});
