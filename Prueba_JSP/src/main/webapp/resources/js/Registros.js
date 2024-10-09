import {host} from "./config.js";

document.addEventListener("DOMContentLoaded", async () => {


    const tabla = document.querySelector("table");
    const documentoUser= document.querySelector("#idUsuario").value
    try {
        const responseDocument = await fetch(`${host}/SvRegistros?iduser=${documentoUser}`);
        const result1 = await responseDocument.json();
        if(result1.length>0) {

        // Crear el encabezado de la tabla
        const header_table = document.createElement("thead")
        const header_encabezado = document.createElement("tr");
        const cantproperty = Object.keys(result1[0]);
        cantproperty.forEach(elemento => {
            const lista = document.createElement("th");
            lista.textContent = elemento;
            header_encabezado.appendChild(lista);

            // Agregar un encabezado adicional para la hora si el campo es "fecha_reporte"
            if (elemento === "fecha_reporte") {
                const horaEncabezado = document.createElement("th");
                horaEncabezado.textContent = "Hora";
                header_encabezado.appendChild(horaEncabezado);
            }
            header_table.appendChild(header_encabezado)
        });
        tabla.appendChild(header_table);

        // Crear las filas de datos
        const hola = document.createElement("tbody")

            result1.forEach(element => {
                const contenido = document.createElement("tr");
                cantproperty.forEach(key => {
                    const lista = document.createElement("td");
                    let value = element[key];

                    // Transformar la fecha y hora si el campo es "fecha_reporte"
                    if (key === "fecha_reporte") {
                        const date = new Date(value);
                        const fecha = date.toLocaleDateString();
                        const hora = date.toLocaleTimeString();

                        // Crear y agregar celda de fecha
                        lista.textContent = fecha;
                        contenido.appendChild(lista);

                        // Crear y agregar celda de hora
                        const horaCelda = document.createElement("td");
                        horaCelda.textContent = hora;
                        contenido.appendChild(horaCelda);
                    } else {
                        lista.textContent = value;
                        contenido.appendChild(lista);
                    }
                });
                hola.appendChild(contenido);
            });
            tabla.appendChild(hola)
        }
        else {
            const texto = document.createElement("p");
            texto.textContent="No hay registros."
            tabla.appendChild(texto)
        }
        document.appendChild(tabla)


    } catch (error) {
        console.error('Error fetching or parsing JSON:', error);
    }
});











