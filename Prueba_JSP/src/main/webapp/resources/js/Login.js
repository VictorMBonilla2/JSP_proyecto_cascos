document.addEventListener("DOMContentLoaded", function () {
    const logo = document.querySelector("#logo_icon");
    const texto_logo = document.querySelector(".logo__text");
    const background = document.querySelector(".background_login");
    logo.addEventListener("click", (event) => {
        if(background.classList.contains("background_login_aprendiz")){
            background.classList.remove("background_login_aprendiz");
            texto_logo.textContent = "Colaborador";
        }else {
            background.classList.add("background_login_aprendiz");
            texto_logo.textContent = "Aprendiz";
        }
    });
    document.getElementById("Logeo").onsubmit = async function (event) {
        event.preventDefault();
        document.getElementById("Error").style.display = "none";
        document.getElementById("ErrorOnlydigitos").style.display = "none";

        // Obtener valores de los campos del formulario
        const documento = document.getElementById("documento").value;
        const tipoDocumento = document.getElementById("TipoDocumento").value;
        const password = document.getElementById("passWord").value;

        // Validar si el documento es un número
        if (isNaN(documento)) {
            // Mostrar mensaje de error si el valor no es un número
            document.getElementById("ErrorOnlydigitos").style.display = "block";
            return; // Detener el proceso de envío del formulario
        }

        try {
            // Enviar la solicitud fetch al servidor
            const response = await fetch("SvPersona", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    action: "login",
                    documento: documento,
                    TipoDocumento: tipoDocumento,
                    password: password
                })
            });

            // Verificar si la respuesta es exitosa
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }

            // Obtener el resultado de la respuesta
            const result = await response.json();

            // Verificar el resultado del inicio de sesión
            if (result.status === "success") {
                // Redireccionar al usuario a la página de inicio
                window.location.href = "Home.jsp";
            } else {
                // Mostrar mensaje de error en caso de credenciales inválidas
                document.getElementById("Error").style.display = "block";
            }
        } catch (error) {
            // Manejar cualquier error que ocurra durante la solicitud
            console.error("There was a problem with the fetch operation:", error);
            // Mostrar un mensaje de error genérico al usuario
            document.getElementById("Error").style.display = "block";
        }
    };
});
