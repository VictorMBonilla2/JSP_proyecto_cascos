document.addEventListener("DOMContentLoaded", function() {
    document.getElementById("logout").onsubmit = async function(event) {
        event.preventDefault();
        try {
            // Enviar la solicitud fetch al servidor
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
            // Verificar el resultado del cierre de sesión
            if (result.status === "success") {
                window.location.href = "index.jsp";
            }
        } catch (error) {
            // Manejar cualquier error que ocurra durante la solicitud
            console.error("There was a problem with the fetch operation:", error);
        }
    };
});