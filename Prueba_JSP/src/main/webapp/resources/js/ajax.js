export async function sendRequest(url, data) {
    try {
        const response = await fetch(url, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        });

        // Parsear siempre la respuesta como JSON
        const result = await response.json();

        // Si la respuesta no es exitosa, lanzar un error con el mensaje del servidor
        if (!response.ok || result.status !== "success") {
            throw new Error(result.message || "Ocurrió un error.");
        }

        // Si todo salió bien, retornar el resultado
        return result;

    } catch (error) {
        console.error("There was a problem with the fetch operation:", error);

        return {
            status: "error",
            message: error.message || "Error desconocido."
        };
    }
}


