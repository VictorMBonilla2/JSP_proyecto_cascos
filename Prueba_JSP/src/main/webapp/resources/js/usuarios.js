import {sendRequest} from "./ajax.js";
import {host} from "./config.js";
import {cargarRoles, cargarTiposDocumento} from "./utils/renderSelects.js";
import {validarDocumento, validarEmail, validarFecha, validarTexto} from "./utils/validations.js";
import {showErrorDialog} from "./alerts/error.js";
import {showSuccessAlert} from "./alerts/success.js";

document.addEventListener("DOMContentLoaded", async () => {
    const editButtons = document.querySelectorAll(".user_button_edit");
    const deleteButtons = document.querySelectorAll(".user_button_delete");
    const lista = document.querySelector(".users_list__container");
    const formTemplate = document.querySelector("#template_form").content;
    const formNewTemplate = document.querySelector("#template_form_newUser").content;
    const itemTemplate = document.querySelector("#item__list").content;

    let personas = await ObtenerUsuarios("1");
    console.log(personas)
    if (personas.length > 0) {
        personas.forEach((persona, index) => {
            const clone = document.importNode(itemTemplate, true);

            // Modificar el contenido del clon
            clone.querySelector(".user_list__item").setAttribute('data-user', persona.idUser);

            clone.querySelector(".user_item_document > p").textContent = persona.documento;

            clone.querySelector(".user_item_name > p").textContent = persona.nombre;

            clone.querySelector(".user_item_rol > p").textContent = persona.rol.nombre;

            clone.querySelector(".user_button_edit").setAttribute('data-edit', persona.idUser);

            clone.querySelector(".user_button_delete").setAttribute('data-delete', persona.idUser);

            // Insertar el clon modificado en la lista
            lista.appendChild(clone);
        })
    } else {
        console.log("no hay personas")
    }
    document.addEventListener("click", async (e) => {
        console.log(e.target)

        if (e.target.matches(".new_user__item")) {
            const user_item = e.target;
            // Si ya tiene la clase form_active, la quitamos y salimos
            if (user_item.classList.contains("form_active")) {
                user_item.classList.remove("form_active");
                const formActive = user_item.querySelector('.user_list__form');
                if (formActive) {
                    formActive.remove(); // Remueve el formulario si ya está activo
                }
                return; // Salir sin continuar
            }

            const clone = document.importNode(formNewTemplate, true);

            // Retirar el formulario existente antes de agregar otro
            retirarFormsActivos();

            document.querySelectorAll('.user_list__item.form_active').forEach(form => {
                form.classList.remove('form_active');
            });
            user_item.classList.add("form_active");

            const formulario = clone.querySelector(".formulario");

            // Inicialmente ocultamos el formulario
            formulario.style.opacity = "0";
            formulario.style.transition = "opacity 0.5s ease";
            if (formulario) {
                // Añade el evento submit mediante addEventListener
                formulario.addEventListener("submit", (event) => {
                    event.preventDefault();
                    const form = document.querySelector(".newUser");
                    if(validarFormularioNew(form))
                    crearUsuario(form);
                });
                formulario.setAttribute("data_user", "createUser")
            }

            // Agregar el botón de cancelar
            const cancelButton = clone.querySelector(".cancel-button");
            if (cancelButton) {
                cancelButton.addEventListener("click", (event) => {
                    event.preventDefault();
                    const formActive = user_item.querySelector('.user_list__form');
                    if (formActive) {
                        formActive.remove(); // Remueve el formulario si ya está activo
                    }
                    user_item.classList.remove("form_active");
                });
            }
            user_item.appendChild(clone)
            setTimeout(() => {
                formulario.style.opacity = "1";

            }, 1500);

            await cargarRoles('Rol_new');
            await cargarTiposDocumento('Tipo_documento_new')
        }

        if (e.target.matches("#edit_button")) {

            let data_edit = e.target.getAttribute("data-edit");

            const user_item = document.querySelector(`[data-user="${data_edit}"]`);
            // Si ya tiene la clase form_active, la quitamos y salimos
            if (user_item.classList.contains("form_active")) {
                user_item.classList.remove("form_active");
                const formActive = user_item.querySelector('.user_list__form');
                if (formActive) {
                    formActive.remove(); // Remueve el formulario si ya está activo
                }
                return; // Salir sin continuar
            }

            // Retirar el formulario existente antes de agregar otro
            retirarFormsActivos();

            // Añadir la clase activo al form actual
            user_item.classList.add("form_active");
            const dato_usuario = personas.find(persona => persona.idUser === parseInt(data_edit));
            const clone = document.importNode(formTemplate, true);

            const formulario = clone.querySelector(".formulario");
            // Ocultamos el formulario inicialmente
            formulario.style.opacity = "0";
            formulario.style.transition = "opacity 0.5s ease";
            if (formulario) {
                // Añade el evento submit mediante addEventListener
                formulario.addEventListener("submit", (event) => {
                    event.preventDefault();
                    const form = document.querySelector(`form[data-user="${dato_usuario.idUser}"]`)
                    console.log(form)
                    if(verificarFormularioEdit(form)){
                        editUser(form);
                    }
                });
                formulario.setAttribute("data-user", dato_usuario.idUser)
            }

            // Asignar valores a los campos del formulario
            clone.querySelector("#Nombre").value = dato_usuario.nombre;
            clone.querySelector("#Apellido").value = dato_usuario.apellido;
            clone.querySelector("#Correo").value = dato_usuario.correo;
            clone.querySelector("#numero_documento").value = dato_usuario.documento;
            clone.querySelector("#Fecha_nacimiento").value = dato_usuario.fecha_nacimineto;
            clone.querySelector("#idUser").value = dato_usuario.idUser;
            clone.querySelector("#Tipo_documento").value = dato_usuario.doc.idDocumento;
            clone.querySelector("#Rol").value = dato_usuario.rol.idRol;
            // Agregar el botón de cancelar
            const cancelButton = clone.querySelector(".cancel-button");
            if (cancelButton) {
                cancelButton.addEventListener("click", (event) => {
                    event.preventDefault();
                    const formActive = user_item.querySelector('.user_list__form');
                    if (formActive) {
                        formActive.remove(); // Remueve el formulario si ya está activo
                    }
                    user_item.classList.remove("form_active");
                });
            }
            user_item.appendChild(clone);
            setTimeout(() => {
                formulario.style.opacity = "1";
            }, 2100);
            await cargarRoles('Rol');
            await cargarTiposDocumento('Tipo_documento')
        }
        if (e.target.matches("#delete_button")) {

            let data_delete = e.target.getAttribute("data-delete");

            // Filtrar la lista de personas por idUser
            const personaSeleccionada = personas.filter(persona => persona.idUser == data_delete);

            if (personaSeleccionada.length > 0) {
                let advertencia = confirm(`Estas seguro de eliminar al usuario ${personaSeleccionada[0].nombre} ${personaSeleccionada[0].apellido}`);
                if (advertencia) {
                    let response = borrarUsuario(personaSeleccionada[0]);
                    if (response) {
                        const user_item = document.querySelector(`[data-user="${data_delete}"]`);
                        user_item.innerHTML = "";
                    }
                }
            } else {
                console.log("Usuario no encontrado.");
            }
        }
    });
});

function retirarFormsActivos() {
    const formActive = document.querySelector('.user_list__item.form_active .user_list__form');
    // Verifica que el formulario exista antes de continuar
    if (formActive) {
        formActive.remove()
    }
    document.querySelectorAll('.user_list__item.form_active').forEach(form => {
        form.classList.remove('form_active');

    });
}


async function ObtenerUsuarios(Pagina_Actual) {

    const response = await fetch(`http://localhost:8080/Prueba_JSP_war_exploded/usuarios?Pagination=${Pagina_Actual}`);
    if (response.status === 204) {
        console.log('No se encontraron personas.');
        return [];
    }
    const data = await response.json();
    if (data.length === 0) {
        console.log('No se encontraron Personas.');
    } else {
        return data;
    }
}

async function editUser(form) {
    console.log(form)
    const nombre = form.querySelector("#Nombre").value
    const apellido = form.querySelector("#Apellido").value
    const correo = form.querySelector("#Correo").value
    const tipoDocumneto = form.querySelector("#Tipo_documento").value
    const numeroDocumento = form.querySelector("#numero_documento").value
    const fechaNacimientoInput = document.querySelector("#Fecha_nacimiento").value;
    const fechaFormateada = formatFecha(fechaNacimientoInput);
    const rol = form.querySelector("#Rol").value
    const idUser = form.querySelector("#idUser").value
    const data = {
        "action": "edit",
        "nombre": nombre,
        "apellido": apellido,
        "correo": correo,
        "tipoDocumneto": tipoDocumneto,
        "numeroDocumento": numeroDocumento,
        "fechaNacimiento": fechaFormateada,
        "rol": rol,
        "idUser":idUser
    }

        const response = await sendRequest("/Prueba_JSP_war_exploded/usuarios", data);
        console.log("Solicitud enviada con éxito");
        if(response.status === "success"){
            console.log("Se ha Actualizado el vehiculo correctamente")
            showSuccessAlert(response.message)
        }else{
            showErrorDialog(response.message)
        }
}

async function borrarUsuario(persona) {
    const id_persona = persona.idUser;
    console.log(id_persona)
    const data = {
        "action": "delete",
        "id": id_persona
    }

    try {
        await sendRequest("/Prueba_JSP_war_exploded/usuarios", data);
        console.log("Solicitud enviada con éxito");
        return true
    } catch (error) {
        console.error("Error al enviar la solicitud:", error);
        return false
    }

}
async function crearUsuario(form) {

    console.log(form)

    const nombre = form.querySelector("#Nombre_new").value
    const apellido = form.querySelector("#Apellido_new").value
    const correo = form.querySelector("#Correo_new").value
    const tipoDocumneto = form.querySelector("#Tipo_documento_new").value
    const numeroDocumento = form.querySelector("#numero_documento_new").value
    const fechaNacimientoInput = document.querySelector("#Fecha_nacimiento_new").value;
    const password = form.querySelector("#password_new").value;
    const fechaFormateada = formatFecha(fechaNacimientoInput);
    const rol = form.querySelector("#Rol_new").value
    const data = {
        "action": "add",
        "nombre": nombre,
        "apellido": apellido,
        "correo": correo,
        "tipoDocumneto": tipoDocumneto,
        "numeroDocumento": numeroDocumento,
        "fechaNacimiento": fechaFormateada,
        "password": password,
        "rol": rol
    }

    const response =  await sendRequest("/Prueba_JSP_war_exploded/usuarios", data);
    if(response.status === "success"){
        console.log("Se ha Actualizado el vehiculo correctamente")
        showSuccessAlert(response.message)
    }else{
        showErrorDialog(response.message)
    }

}


function formatFecha(inputDate) {
    const date = new Date(inputDate);
    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const year = date.getFullYear();
    return `${day}/${month}/${year}`;
}
function validarFormularioNew(form) {
    console.log(form)
    const nombre = form.querySelector("#Nombre_new").value;
    const apellido = form.querySelector("#Apellido_new").value;
    const correo = form.querySelector("#Correo_new").value;
    const tipoDocumento = form.querySelector("#Tipo_documento_new").value;
    const numeroDocumento = form.querySelector("#numero_documento_new").value;
    const fechaNacimientoInput = form.querySelector("#Fecha_nacimiento_new").value;
    const password = form.querySelector("#password_new").value;
    const rol = form.querySelector("#Rol_new").value;

    // Validar nombre y apellido solo texto
    if (!validarTexto(nombre, 1)) {
        showErrorDialog("El nombre debe ser un texto válido.");
        return false;
    }
    if (!validarTexto(apellido, 1)) {
        showErrorDialog("El apellido debe ser un texto válido.");
        return false;
    }

    // Validar correo electrónico
    if (!validarEmail(correo)) {
        showErrorDialog("El correo debe ser un email válido.");
        return false;
    }

    // Validar tipo de documento
    if (!tipoDocumento) {
        showErrorDialog("Debe seleccionar un tipo de documento.");
        return false;
    }

    // Validar número de documento
    if (!validarDocumento(numeroDocumento)) {
        showErrorDialog("El número de documento debe ser un número positivo.");
        return false;
    }

    // Validar fecha de nacimiento (en formato yyyy-mm-dd)
    if (!validarFecha(fechaNacimientoInput)) {
        showErrorDialog("La fecha de nacimiento debe estar en el formato yyyy-mm-dd.");
        return false;
    }

    // Validar contraseña no vacía
    if (password.trim() === '') {
        showErrorDialog("La contraseña no puede estar vacía.");
        return false;
    }

    // Validar rol
    if (!rol) {
        showErrorDialog("Debe seleccionar un rol.");
        return false;
    }

    return true; // Si todo es válido
}

function verificarFormularioEdit(form) {
    const nombre = form.querySelector("#Nombre").value;
    const apellido = form.querySelector("#Apellido").value;
    const correo = form.querySelector("#Correo").value;
    const tipoDocumento = form.querySelector("#Tipo_documento").value;
    const numeroDocumento = form.querySelector("#numero_documento").value;
    const fechaNacimientoInput = form.querySelector("#Fecha_nacimiento").value;
    const rol = form.querySelector("#Rol").value;

    // Validar que el nombre y apellido solo contengan letras
    if (!validarTexto(nombre, 1)) {
        showErrorDialog("El nombre debe ser un texto válido.");
        return false;
    }
    if (!validarTexto(apellido, 1)) {
        showErrorDialog("El apellido debe ser un texto válido.");
        return false;
    }

    // Validar el correo electrónico
    if (!validarEmail(correo)) {
        showErrorDialog("El correo electrónico no es válido.");
        return false;
    }

    // Validar que el tipo de documento no esté vacío
    if (!tipoDocumento) {
        showErrorDialog("Debe seleccionar un tipo de documento.");
        return false;
    }

    // Validar que el número de documento sea un número válido y positivo
    if (!validarDocumento(numeroDocumento)) {
        showErrorDialog("El número de documento debe ser un número positivo válido.");
        return false;
    }

    // Validar que la fecha de nacimiento tenga el formato correcto (yyyy-MM-dd)
    if (!validarFecha(fechaNacimientoInput)) {
        showErrorDialog("La fecha de nacimiento debe estar en el formato yyyy-MM-dd.");
        return false;
    }

    // Validar que el rol no esté vacío
    if (!rol) {
        showErrorDialog("Debe seleccionar un rol.");
        return false;
    }

    return true; // Si todo es válido
}

