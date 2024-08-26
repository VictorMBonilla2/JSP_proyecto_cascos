export async function sendRequest(url, data) {
    try {
        const response = await fetch(url, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        });

        const result = await response.json();

        if (!response.ok) {
            throw new Error(result.message || 'Network response was not ok');
        }

        if (result.status === "success") {
        } else {
            document.getElementById("Error").textContent = result.message || "Ocurrió un error.";
            document.getElementById("Error").style.display = "block";
        }
    } catch (error) {
        console.error("There was a problem with the fetch operation:", error);
        document.getElementById("Error").textContent = error.message || "Ocurrió un error al procesar la solicitud.";
        document.getElementById("Error").style.display = "block";
    }
}
