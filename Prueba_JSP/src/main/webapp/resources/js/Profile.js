import {sendRequest} from "./ajax.js";

const divPerfil = document.querySelector(".main_container__profile");
const divDetalles = document.querySelector(".detalles_user_container");
const divPerfil2 = document.querySelector(".user_container_side2");
const infoContainer = document.querySelector("#userinfo");
const infoTemplate = document.querySelector("#infoTemplate").content;
const formTemplate = document.querySelector("#formTemplate").content;
const buttonsInfoContainer = document.querySelector(".button_miranose");
const cancelButtonInfoContainer = document.querySelector("#cancelEdit");

document.addEventListener("click", (ev) => {
    if (ev.target.matches(".info_user_container__button")) {
        mostrarFormulario();
    } else if (ev.target === cancelButtonInfoContainer) {
        mostrarInformacion();
    } else {
        console.log("pues no");
    }
});

function mostrarFormulario() {
    divPerfil.classList.add("modificado");
    divPerfil.style.gridTemplateColumns = '100% 0%';
    divDetalles.style.opacity = '0%';
    setTimeout(() => {
        divDetalles.style.display = 'none';
        divPerfil2.style.display = 'flex';
        infoContainer.innerHTML = "";
        infoContainer.appendChild(formTemplate.cloneNode(true));
        buttonsInfoContainer.style.display = "flex";

        // Mover la configuración del evento aquí después de que el formulario se haya agregado al DOM
        const saveButton = document.querySelector('.button_miranose button[type="submit"]');
        const editProfileForm = document.getElementById('editPrimaryDataForm');

        saveButton.addEventListener('click', async (event) => {
            event.preventDefault(); // Prevenir cualquier comportamiento por defecto
            const usuarioId = editProfileForm.querySelector("#usuarioId").value;
            const nombre = editProfileForm.querySelector("#nombre").value;
            const apellido = editProfileForm.querySelector("#apellido").value;
            const fecha = editProfileForm.querySelector("#fecha_nacimiento").value;

            const data = {
                action: "editPrimaryDAta",
                usuarioId: usuarioId,
                nombre: nombre,
                apellido: apellido,
                fecha: fecha,
            };

            try {
                const response = await sendRequest("SvPersona", data);
                // Manejar la respuesta si es necesario
                if (response.status === "success") {
                    console.log("Datos actualizados con éxito");
                } else {
                    console.error("Error al actualizar los datos:", response.message);
                }
            } catch (error) {
                console.error("Error en la solicitud:", error);
            }
        });

    }, "2100");
}

const mostrarInformacion = () => {
    divPerfil.classList.remove("modificado");
    divPerfil.style.gridTemplateColumns = '34% 66%';
    divDetalles.style.opacity = '100%';
    divDetalles.style.display = 'flex';
    divPerfil2.style.display = 'none';
    infoContainer.innerHTML = "";
    infoContainer.appendChild(infoTemplate.cloneNode(true));
    buttonsInfoContainer.style.display = "none";
};
