import {host} from "../config.js";

// Función para cargar tipos de vehículos
export async function cargarTiposVehiculo(selectElementId) {
    try {
        const response = await fetch(`${host}/tiposVehiculos`);
        const tiposVehiculo = await response.json();
        const selectElement = document.getElementById(selectElementId);
        selectElement.innerHTML = ''; // Limpiar el select

        // Rellenar el select con los tipos de vehículos recibidos
        tiposVehiculo.forEach(tipo => {
            const option = document.createElement('option');
            option.value = tipo.id_Tipo;
            option.textContent = tipo.nombre_Tipo;
            selectElement.appendChild(option);
        });

    } catch (error) {
        console.error('Error al cargar los tipos de vehículos:', error);
    }
}

// Función para cargar marcas según el tipo de vehículo
export async function cargarMarcas(tipoVehiculoId, selectElementId) {
    try {
        const response = await fetch(`${host}/listaMarcas?id_Tipo=${tipoVehiculoId}`);
        const marcas = await response.json();
        const selectElement = document.getElementById(selectElementId);
        selectElement.innerHTML = ''; // Limpiar el select

        // Rellenar el select con las marcas recibidas
        marcas.forEach((marca, index)=> {
            const option = document.createElement('option');
            option.value = marca.id_Marca;
            option.textContent = marca.nombre_Marca;
            selectElement.appendChild(option);
            if (index === 0) {
                option.selected = true;
            }
        });

    } catch (error) {
        console.error('Error al cargar las marcas:', error);
    }
}

// Función para cargar modelos según la marca y el tipo de vehículo
export async function cargarModelos(marcaVehiculoId, tipoVehiculoId, selectElementId) {
    try {
        const response = await fetch(`${host}/listaModelo?id_Marca=${marcaVehiculoId}&id_Tipo=${tipoVehiculoId}`);
        const modelos = await response.json();
        const selectElement = document.getElementById(selectElementId);
        selectElement.innerHTML = ''; // Limpiar el select

        // Rellenar el select con los modelos recibidos
        modelos.forEach(modelo => {
            const option = document.createElement('option');
            option.value = modelo.id_Modelo;
            option.textContent = modelo.nombre_Modelo;
            selectElement.appendChild(option);
        });

    } catch (error) {
        console.error('Error al cargar los modelos:', error);
    }
}
export async function cargarTiposDocumento(selectElementId) {
    try {
        const documentoSelect = document.getElementById(selectElementId); // ID del select
        const response = await fetch(`${host}/tipoDoc`);  // Reutiliza la variable 'host' para construir la URL
        const data = await response.json();

        // Limpiar el select antes de rellenarlo
        documentoSelect.innerHTML = '';

        // Rellenar el select con los tipos de documentos recibidos
        data.forEach(tipoDocumento => {
            const option = document.createElement('option');
            option.value = tipoDocumento.id_documento;
            option.textContent = tipoDocumento.nombre_documento;
            documentoSelect.appendChild(option);
        });

    } catch (error) {
        console.error('Error al cargar los tipos de documento:', error);
    }
}
// Función para cargar los colores
export async function cargarColores(selectElementId) {
    try {
        const response = await fetch(`${host}/listaColores`);
        if (!response.ok) {
            throw new Error('Error al cargar los colores');
        }

        const colores = await response.json();

        const colorVehiculoSelect = document.getElementById(selectElementId);
        colorVehiculoSelect.innerHTML = ''; // Limpiar el select

        // Rellenar el select con los colores recibidos
        colores.forEach(color => {
            const option = document.createElement('option');
            option.value = color;
            option.textContent = color.charAt(0) + color.slice(1).toLowerCase(); // Formato legible
            colorVehiculoSelect.appendChild(option);
        });
    } catch (error) {
        console.error('Error al cargar los colores:', error);
    }
}
// Función para cargar las ciudades
export async function cargarCiudades(selectElementId) {
    try {
        const response = await fetch(`${host}/listaCiudades`);
        if (!response.ok) {
            throw new Error('Error al cargar las ciudades');
        }

        const ciudades = await response.json();

        // Obtener el select donde se cargarán las ciudades
        const ciudadVehiculoSelect = document.getElementById(selectElementId);
        ciudadVehiculoSelect.innerHTML = ''; // Limpiar el select

        // Rellenar el select con las ciudades recibidas
        ciudades.forEach(ciudad => {
            const option = document.createElement('option');
            option.value = ciudad.id_Ciudad;
            option.textContent = ciudad.nombre_Ciudad;
            ciudadVehiculoSelect.appendChild(option);
        });
    } catch (error) {
        console.error('Error al cargar las ciudades:', error);
    }
}

// Función para cargar roles
export async function cargarRoles(selectElementId) {
    try {
        const response = await fetch('/Prueba_JSP_war_exploded/listaRoles');
        if (!response.ok) {
            throw new Error('Error al cargar los roles');
        }

        const roles = await response.json();

        const rolSelect = document.getElementById(selectElementId);
        rolSelect.innerHTML = ''; // Limpiar el select

        // Rellenar el select con los roles recibidos
        roles.forEach(rol => {
            const option = document.createElement('option');
            option.value = rol.id_rol;
            option.textContent = rol.nombre_rol;
            rolSelect.appendChild(option);
        });
    } catch (error) {
        console.error('Error al cargar los roles:', error);
    }
}

