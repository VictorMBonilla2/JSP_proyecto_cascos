export async function sendRequest(url, data) {
    try {
        const response = await fetch(url, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        });

        if (!response.ok) {
            const errorText = await response.text(); // Get raw response text
            throw new Error(errorText || 'Network response was not ok');
        }

        const result = await response.json();

        if (result.status === "success") {
            // Handle success case
        } else {
            const errorElement = document.getElementById("Error");
            if (errorElement) {
                errorElement.textContent = result.message || "Ocurrió un error.";
                errorElement.style.display = "block";
            }
        }
    } catch (error) {
        console.error("There was a problem with the fetch operation:", error);
        const errorElement = document.getElementById("Error");
        if (errorElement) {
            errorElement.textContent = error.message || "Ocurrió un error al procesar la solicitud.";
            errorElement.style.display = "block";
        }
    }
}

