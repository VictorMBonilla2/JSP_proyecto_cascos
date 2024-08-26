document.addEventListener("DOMContentLoaded", () => {
    const logo = document.querySelector("#logo_icon");
    const texto_logo = document.querySelector(".logo__text");
    const background = document.querySelector(".background_login");
    const errorDiv = document.getElementById("Error");
    const errorOnlyDigitsDiv = document.getElementById("ErrorOnlydigitos");
    const form = document.getElementById("Logeo");

    // Manejador del click en el logo para alternar el rol
    logo.addEventListener("click", () => {
        const isAprendiz = background.classList.toggle("background_login_aprendiz");
        texto_logo.textContent = isAprendiz ? "Aprendiz" : "Colaborador";
    });

    form.onsubmit = async (event) => {
        event.preventDefault();
        errorDiv.style.display = "none";
        errorOnlyDigitsDiv.style.display = "none";

        const documento = document.getElementById("documento").value;
        const tipoDocumento = document.getElementById("TipoDocumento").value;
        const password = document.getElementById("passWord").value;
        const rol = background.classList.contains("background_login_aprendiz") ? "Aprendiz" : "Colaborador";

        if (isNaN(documento)) {
            errorOnlyDigitsDiv.style.display = "block";
            return;
        }

        try {
            const response = await fetch("SvPersona", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ action: "login", documento, tipoDocumento, password, rol })
            });

            if (!response.ok) {
                throw new Error('Network response was not ok');
            }

            const result = await response.json();

            if (result.status === "success") {
                // Redireccionar según el rol del usuario
                if (rol === "Aprendiz") {
                    window.location.href = "Home_aprendiz.jsp";
                } else {
                    window.location.href = "Home.jsp";
                }
            } else {
                errorDiv.style.display = "block";
            }
        } catch (error) {
            console.error("There was a problem with the fetch operation:", error);
            errorDiv.style.display = "block";
        }
    };
});

