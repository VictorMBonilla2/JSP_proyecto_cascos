document.getElementById("logout-link").addEventListener("click", async function(event) {
    event.preventDefault();
    try {
        const response = await fetch("SvPersona", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                action: "logout"
            })
        });

        const result = await response.json();
        if (result.status === "success") {
            window.location.href = "index.jsp";
        }
    } catch (error) {
        console.error("Hubo un problema con la operación de solicitud:", error);
    }
});