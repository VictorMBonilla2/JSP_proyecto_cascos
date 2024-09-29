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
}
