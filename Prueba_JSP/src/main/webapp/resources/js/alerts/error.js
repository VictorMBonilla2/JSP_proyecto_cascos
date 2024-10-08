// Función para mostrar el error
export function showErrorDialog(message) {
    const template = document.getElementById('errorTemplate');
    const clone = template.content.cloneNode(true);

    const errorTitle = clone.querySelector('.error__title');
    errorTitle.textContent = message;

    const closeButton = clone.querySelector('.error__close');
    closeButton.addEventListener('click', () => {
        const errorContainer = document.querySelector('.error-container');
        if (errorContainer) {
            errorContainer.remove();
        }
    });

    document.body.appendChild(clone);
    const contenedor= document.querySelector('#error-container')
    setTimeout(() => {
        if (document.body.contains(contenedor)) {
            document.body.removeChild(contenedor);
        }
    }, 2500);  // Ocultar la alerta después de 3 segundos
}
