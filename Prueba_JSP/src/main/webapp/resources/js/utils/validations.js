// Validar que el documento solo tenga números y sea positivo
export function validarDocumento(documento) {
    const numeroDocumento = Number(documento);
    return !isNaN(numeroDocumento) && numeroDocumento > 0;
}

// Validar que la cantidad de cascos sea un número no mayor a 2 y positivo (0 es válido)
export function validarCantidadCascos(cantidad) {
    const numCascos = Number(cantidad);
    return !isNaN(numCascos) && numCascos >= 0 && numCascos <= 2;
}

// Validar que el nombre del reporte no tenga más de 20 caracteres y no esté vacío
export function validarNombreReporte(nombreReporte) {
    return nombreReporte.trim() !== '' && nombreReporte.length <= 20;
}

// Validar que la descripción del reporte no tenga más de 500 caracteres y no esté vacía
export function validarDescripcionReporte(descripcion) {
    return descripcion.trim() !== '' && descripcion.length <= 500;
}
