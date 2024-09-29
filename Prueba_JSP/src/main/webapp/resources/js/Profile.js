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
    divDetalles.classList.add("oculto");
    setTimeout(() => {
        divDetalles.style.display = 'none';
        infoContainer.innerHTML = ""; // Limpiar el contenedor antes de agregar el formulario

        // Clonar el contenido del template
        const clone = document.importNode(formTemplate, true); // Necesitas pasar 'true' para hacer una clonación profunda

        // Insertar el formulario clonado en el contenedor
        infoContainer.appendChild(clone);

        // Mover la configuración del evento aquí después de que el formulario se haya agregado al DOM
        // Ahora seleccionamos los elementos desde el DOM, no desde el 'clone'
        const saveButton = infoContainer.querySelector('#editForm');
        const cancelEdit = infoContainer.querySelector('#cancelEdit');


        saveButton.addEventListener('submit', async (event) => {
            event.preventDefault(); // Prevenir cualquier comportamiento por defecto
            enviar_formulario();
        });

        cancelEdit.addEventListener('click',()=>{
            mostrarInformacion()
        })

    }, 2100);
}

async function enviar_formulario(){
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
}

function mostrarInformacion() {
    divPerfil.classList.remove("modificado");
    divDetalles.classList.remove("oculto");
    divDetalles.style.opacity = '100%';
    divDetalles.style.display = 'flex';

    // Limpiar el contenedor antes de agregar la información
    infoContainer.innerHTML = "";

    // Clonar el contenido del template de información
    const clone = document.importNode(infoTemplate, true);

    // Insertar el template clonado en el contenedor
    infoContainer.appendChild(clone);

    // Si tienes botones en el template de información que requieren eventos,
    // puedes seleccionarlos aquí y asignarles los event listeners necesarios.

    buttonsInfoContainer.style.display = "none";
}
