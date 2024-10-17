export function clearFormData(formData) {
    // Obtener el formulario asociado al FormData
    const form = document.querySelector(`[name="${formData.get('formName')}"]`);

    if (form) {
        // Seleccionar todos los inputs del formulario
        const inputs = form.querySelectorAll('input, textarea, select');

        // Iterar sobre cada input y establecer su valor como vacío
        inputs.forEach(input => {
            if (input.type === 'checkbox' || input.type === 'radio') {
                input.checked = false; // Desmarcar checkbox y radio buttons
            } else {
                input.value = ''; // Limpiar el valor de los demás inputs
            }
        });
    }
}
