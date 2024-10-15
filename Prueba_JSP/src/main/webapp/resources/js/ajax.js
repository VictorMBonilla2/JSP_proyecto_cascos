export async function sendRequest(url, data) {
    try {
        const response = await fetch(url, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        });

        // Verificar si el JSON está bien formateado
        let result;
        try {
            result = await response.json();
        } catch (error) {
            throw new Error("Error al parsear la respuesta JSON.");
        }

        // Si la respuesta no es exitosa, lanzar un error con el mensaje del servidor
        if (!response.ok || result.status !== "success") {
            throw new Error(result.message || "Ocurrió un error.");
        }

        // Si todo salió bien, retornar el resultado
        return result;

    } catch (error) {
        console.error("Error en la operación fetch:", error);

        return {
            status: "error",
            message: error.message || "Error desconocido."
        };
    }
}

// Funciones para mostrar y ocultar el spinner global
export function showLoadingSpinner() {
    document.querySelector(".loader-overlay").style.display = "flex";
}

export function hideLoadingSpinner() {
    document.querySelector(".loader-overlay").style.display = "none";
}


