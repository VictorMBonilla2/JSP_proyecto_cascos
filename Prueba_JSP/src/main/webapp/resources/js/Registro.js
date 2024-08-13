document.addEventListener("DOMContentLoaded", function () {
    document.getElementById("registro").onsubmit = async function (event) {
        event.preventDefault();
        document.getElementById("Error").style.display = "none";
        document.getElementById("ErrorOnlydigitos").style.display = "none";

        // Obtener valores de los campos del formulario
        const nombre = document.getElementById("Nombre").value;
        const apellido = document.getElementById("Apellido").value;
        const tipoDocumento = document.getElementById("TipoDocumento").value;
        const documento = document.getElementById("documento").value;
        const correo = document.getElementById("email").value;
        const password = document.getElementById("passWord").value;
        const rol = document.getElementById("rol").value;



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
                    action: "registro",
                    nombre: nombre,
                    apellido: apellido,
                    documento: documento,
                    TipoDocumento: tipoDocumento,
                    correo: correo,
                    password: password,
                    rol:rol
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
                window.location.href = "HomeHome.jsp";
            } else {
                // Mostrar mensaje de error en caso de credenciales inválidas
                document.getElementById("Error").style.display = "block";
            }
        } catch (error) {
            // Manejar cualquier error que ocurra durante la solicitud
            console.error("Hubo un error al realizar el registro:", error);
            // Mostrar un mensaje de error genérico al usuario
            document.getElementById("Error").style.display = "block";
        }
    };
});
