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

// Valida que un campo no esté vacío y tenga un mínimo de caracteres
export function validarTexto(valor, longitudMinima = 1) {
    const soloLetras = /^[A-Za-z\s]+$/; // Expresión regular para permitir solo letras y espacios
    return valor && valor.length >= longitudMinima && soloLetras.test(valor);
}

// Valida el formato de un correo electrónico
export function validarEmail(email) {
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailPattern.test(email);
}

// Valida que una fecha esté en el formato yyyymmdd
export function validarFecha(fecha) {
    const fechaPattern = /^\d{4}-\d{2}-\d{2}$/; // Patrón para yyyy-MM-dd
    return fechaPattern.test(fecha);
}


export function validarTextoNumeros(valor, longitudMinima = 1) {
    const soloTextoNumeros = /^[A-Za-z0-9\s]+$/; // Expresión regular para letras, números y espacios
    return valor && valor.length >= longitudMinima && soloTextoNumeros.test(valor);
}

// Valida que la cantidad de espacios sea un número positivo mayor a 0
export function validarCantidadEspacios(cantidad) {
    const cantidadInt = parseInt(cantidad, 10);
    return !isNaN(cantidadInt) && cantidadInt > 0;
}
// Validar que la placa tenga el formato AAA-111 o que sea un número
export function validarPlaca(placa) {
    // Expresión regular para el formato AAA-111 o solo números
    const placaPattern = /^[A-Z]{3}-\d{3}$|^\d+$/;
    return placaPattern.test(placa);
}