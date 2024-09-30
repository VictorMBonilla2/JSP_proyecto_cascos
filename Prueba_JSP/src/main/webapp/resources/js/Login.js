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
    logo.addEventListener("dblclick", ()=>{
        console.log("has presionado 2 veces")

        const isAprendiz = background.classList.toggle("background_login_Admin");
        texto_logo.textContent = "Administrador"
    });
    selectDocumento();
    form.onsubmit = async (event) => {
        event.preventDefault();
        errorDiv.style.display = "none";
        errorOnlyDigitsDiv.style.display = "none";

        const documento = document.getElementById("documento").value;
        const tipoDocumento = document.getElementById("TipoDocumento").value;
        const password = document.getElementById("passWord").value;
        let rol = background.classList.contains("background_login_aprendiz") ? "2" : "1";
        if(background.classList.contains("background_login_Admin")){
            rol = "3";
        }
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
                    window.location.href = "Home.jsp";
            } else {
                errorDiv.style.display = "block";
                console.log(result)
            }
        } catch (error) {
            console.error("There was a problem with the fetch operation:", error);
            errorDiv.style.display = "block";
        }
    };
});

async function selectDocumento(){
    const documentoSelect = document.getElementById("TipoDocumento");
    const response = await  fetch('/Prueba_JSP_war_exploded/tipoDoc');
    const data = await response.json();
    console.log(data)
    data.forEach(tipoDocumento =>{
        const option = document.createElement("option");
        option.value=tipoDocumento.id_documento;
        option.textContent=tipoDocumento.nombre_documento;
        documentoSelect.appendChild(option)
    })
}

