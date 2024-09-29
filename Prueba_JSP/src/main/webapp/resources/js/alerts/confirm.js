// Función para mostrar el diálogo de confirmación
export function showConfirmationDialog(message, description, onConfirm, onCancel) {
    // Clonar el template
    const overlay = document.getElementById('overlay'); // Obtener el overlay directamente del DOM
    const template = document.getElementById('confirmTemplate');
    const clone = template.content.cloneNode(true);

    // Configurar el contenido de la card
    const heading = clone.querySelector('.card-heading');
    const desc = clone.querySelector('.card-description');
    heading.textContent = message;  // Cambiar el título de la card
    desc.textContent = description;  // Cambiar la descripción de la card

    // Añadir funcionalidad al botón de "Cancelar"
    const cancelButton = clone.querySelector('.secondary');
    cancelButton.addEventListener('click', (e) => {
        e.preventDefault();
        onCancel();  // Ejecutar la acción de cancelación
        document.body.removeChild(dialog);  // Eliminar el dialog del DOM
        overlay.style.display = 'none';  // Ocultar el overlay
    });

    // Añadir funcionalidad al botón de "Eliminar"
    const deleteButton = clone.querySelector('.primary');
    deleteButton.addEventListener('click', (e) => {
        e.preventDefault();
        onConfirm();  // Ejecutar la acción de confirmación
        document.body.removeChild(dialog);  // Eliminar el dialog del DOM
        overlay.style.display = 'none';  // Ocultar el overlay
    });

    // Añadir funcionalidad al botón de "Cerrar" (SVG)
    const exitButton = clone.querySelector('.exit-button');
    exitButton.addEventListener('click', (e) => {
        e.preventDefault();
        onCancel();  // Ejecutar la acción de cancelación al cerrar
        document.body.removeChild(dialog);  // Eliminar el dialog del DOM
        overlay.style.display = 'none';  // Ocultar el overlay
    });

    // Crear un contenedor para agregar al DOM
    const dialog = document.createElement('div');
    dialog.classList.add('dialog-container'); // Puedes añadir estilos aquí
    dialog.appendChild(clone);

    // Mostrar el overlay
    overlay.style.display = 'block';

    // Agregar el contenedor al body del documento
    document.body.appendChild(dialog);
}
