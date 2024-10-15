import { host } from "./config.js";

document.addEventListener("DOMContentLoaded", async () => {
    const tabla = document.querySelector("table");
    const documentoUser = document.querySelector("#idUsuario").value;

    // Función para cargar los registros y la paginación
    async function cargarRegistros(pagina = 1) {
        try {
            const response = await fetch(`${host}/SvRegistros?iduser=${documentoUser}&Pagination=${pagina}`);
            const result = await response.json();

            // Limpiar la tabla antes de agregar nuevos datos
            tabla.innerHTML = '';

            if (result.registros.length > 0) {
                // Crear el encabezado de la tabla
                const headerTable = document.createElement("thead");
                const headerRow = document.createElement("tr");
                const keys = Object.keys(result.registros[0]);

                // Generar las columnas dinámicamente
                keys.forEach((key) => {
                    const th = document.createElement("th");
                    th.textContent = key;
                    headerRow.appendChild(th);
                    if (key === "fecha_registro") {
                        const thHora = document.createElement("th");
                        thHora.textContent = "Hora";
                        headerRow.appendChild(thHora);
                    }
                });
                headerTable.appendChild(headerRow);
                tabla.appendChild(headerTable);

                // Crear las filas de datos
                const tbody = document.createElement("tbody");
                result.registros.forEach((registro) => {
                    const row = document.createElement("tr");
                    keys.forEach((key) => {
                        const td = document.createElement("td");
                        let value = registro[key];

                        // Formatear fecha y hora si es "fecha_registro"
                        if (key === "fecha_registro") {
                            const date = new Date(value);
                            const fecha = date.toLocaleDateString();
                            const hora = date.toLocaleTimeString();

                            // Crear celda de fecha
                            td.textContent = fecha;
                            row.appendChild(td);

                            // Crear celda de hora
                            const horaTd = document.createElement("td");
                            horaTd.textContent = hora;
                            row.appendChild(horaTd);
                        } else {
                            td.textContent = value;
                            row.appendChild(td);
                        }
                    });

                    tbody.appendChild(row);
                });
                tabla.appendChild(tbody);

                // Crear el paginador
                crearPaginador(result.totalPaginas, result.paginaActual);
            } else {
                // Si no hay registros
                const texto = document.createElement("p");
                texto.textContent = "No hay registros.";
                tabla.appendChild(texto);
            }
        } catch (error) {
            console.error('Error fetching or parsing JSON:', error);
        }
    }

    // Función para crear el paginador
    function crearPaginador(totalPaginas, paginaActual) {
        const paginadorContainer = document.getElementById('paginadorHabilitados');
        paginadorContainer.innerHTML = ''; // Limpiar el paginador antes de crear nuevos botones

        for (let i = 1; i <= totalPaginas; i++) {
            const boton = document.createElement('button');
            boton.textContent = i;
            boton.className = i === paginaActual ? 'active' : '';
            boton.addEventListener('click', () => cargarRegistros(i));
            paginadorContainer.appendChild(boton);
        }
    }

    // Cargar la primera página por defecto
    await cargarRegistros();
});






