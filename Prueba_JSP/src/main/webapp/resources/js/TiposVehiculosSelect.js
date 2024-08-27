document.addEventListener("DOMContentLoaded", async () => {
    const selectVehiculos = document.querySelector("#tipoVehiculo");

    try {
        // Realizar la solicitud fetch al servlet
        const respuestaLista = await fetch("/Prueba_JSP_war_exploded/listaTiposVehiculos"); // Asegúrate de que esta URL es la correcta
        const lista = await respuestaLista.json();

        // Verificar si el JSON es un array
        if (!Array.isArray(lista)) {
            throw new Error("El JSON no contiene un array");
        }

        // Poblar el select con las opciones recibidas
        lista.forEach(elemento => {
            const option = document.createElement("option");
            option.value = elemento.id_Tipo; // Asignar el ID como valor de la opción
            option.textContent = elemento.nombre_Tipo; // Asignar el nombre del tipo como el texto visible
            selectVehiculos.appendChild(option);
        });

    } catch (error) {
        console.error("Error al cargar la lista de tipos de vehículos:", error);
    }
});