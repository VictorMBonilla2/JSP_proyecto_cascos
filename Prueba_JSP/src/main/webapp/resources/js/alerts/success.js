// Función para mostrar la alerta de éxito
export function showSuccessAlert(title) {
    // Obtener el template
    const template = document.getElementById('successTemplate');
    const clone = template.content.cloneNode(true);

    // Personalizar el contenido de la alerta
    const successTitle = clone.querySelector('.success__title');
    successTitle.textContent = title;  // Cambiar el título de la alerta

    // Añadir funcionalidad al botón de cerrar
    const closeButton = clone.querySelector('.success__close');
    closeButton.addEventListener('click', (e) => {
        e.preventDefault();
        document.body.removeChild(successDialog);  // Eliminar la alerta del DOM
    });

    // Crear un contenedor para la alerta
    const successDialog = document.createElement('div');
    successDialog.classList.add('success-container'); // Añadir clases para personalizar el contenedor
    successDialog.appendChild(clone);

    // Agregar la alerta al body del documento
    document.body.appendChild(successDialog);

    // Opción de ocultar automáticamente después de cierto tiempo (por ejemplo, 3 segundos)
    setTimeout(() => {
        if (document.body.contains(successDialog)) {
            document.body.removeChild(successDialog);
        }
    }, 3000);  // Ocultar la alerta después de 3 segundos
}
