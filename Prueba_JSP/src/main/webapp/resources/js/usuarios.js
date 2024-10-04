import {sendRequest} from "./ajax.js";

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
            clone.querySelector(".user_list__item").setAttribute('data-user', persona.documento);

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


            const tipodoc= clone.querySelector("#Tipo_documento_new");
            const rol= clone.querySelector("#Rol_new");
            const response = await  fetch('/Prueba_JSP_war_exploded/tipoDoc');
            const data = await response.json();
            console.log(data)
            data.forEach(tipoDocumento =>{
                const option = document.createElement("option");
                option.value=tipoDocumento.id_documento;
                option.textContent=tipoDocumento.nombre_documento;
                tipodoc.appendChild(option)
            })
            const response1 = await  fetch('/Prueba_JSP_war_exploded/listaRoles');
            const data1 = await response1.json();
            console.log(data1)
            data1.forEach(tipoDocumento =>{
                const option = document.createElement("option");
                option.value=tipoDocumento.id_rol;
                option.textContent=tipoDocumento.nombre_rol;
                rol.appendChild(option)
            })

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
                    crearUsuario();
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
            const dato_usuario = personas.find(persona => persona.documento === parseInt(data_edit));
            const clone = document.importNode(formTemplate, true);

            const formulario = clone.querySelector(".formulario");
            // Ocultamos el formulario inicialmente
            formulario.style.opacity = "0";
            formulario.style.transition = "opacity 0.5s ease";
            if (formulario) {
                // Añade el evento submit mediante addEventListener
                formulario.addEventListener("submit", (event) => {
                    event.preventDefault();
                    editUser(event, dato_usuario.documento);
                });
                formulario.setAttribute("data-user", dato_usuario.documento)
            }

            // Asignar valores a los campos del formulario
            clone.querySelector("#Nombre").value = dato_usuario.nombre;
            clone.querySelector("#Apellido").value = dato_usuario.apellido;
            clone.querySelector("#Correo").value = dato_usuario.correo;
            clone.querySelector("#numero_documento").value = dato_usuario.documento;
            clone.querySelector("#Fecha_nacimiento").value = dato_usuario.fecha_nacimineto;
            clone.querySelector("#idUser").value = dato_usuario.idUser;
            const tipodoc= clone.querySelector("#Tipo_documento");
            const rol= clone.querySelector("#Rol");
            const response = await  fetch('/Prueba_JSP_war_exploded/tipoDoc');
            const data = await response.json();
            console.log(data)
            data.forEach(tipoDocumento =>{
                const option = document.createElement("option");
                option.value=tipoDocumento.id_documento;
                option.textContent=tipoDocumento.nombre_documento;
                tipodoc.appendChild(option)
            })
            const response1 = await  fetch('/Prueba_JSP_war_exploded/listaRoles');
            const data1 = await response1.json();
            console.log(data1)
            data1.forEach(tipoDocumento =>{
                const option = document.createElement("option");
                option.value=tipoDocumento.id_rol;
                option.textContent=tipoDocumento.nombre_rol;
                rol.appendChild(option)
            })

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

async function editUser(evento, documento_user) {
    console.log(documento_user)
    const Form = document.querySelector(`form[data-user="${documento_user}"]`)
    console.log(Form)

    const nombre = Form.querySelector("#Nombre").value
    const apellido = Form.querySelector("#Apellido").value
    const correo = Form.querySelector("#Correo").value
    const tipoDocumneto = Form.querySelector("#Tipo_documento").value
    const numeroDocumento = Form.querySelector("#numero_documento").value
    const fechaNacimientoInput = document.querySelector("#Fecha_nacimiento").value;
    const fechaFormateada = formatFecha(fechaNacimientoInput);
    const rol = Form.querySelector("#Rol").value
    const idUser = Form.querySelector("#idUser").value
    const data = {
        "action": "edit",
        "documento": documento_user,
        "nombre": nombre,
        "apellido": apellido,
        "correo": correo,
        "tipoDocumneto": tipoDocumneto,
        "numeroDocumento": numeroDocumento,
        "fechaNacimiento": fechaFormateada,
        "rol": rol,
        "idUser":idUser
    }
    try {
        await sendRequest("/Prueba_JSP_war_exploded/usuarios", data);
        console.log("Solicitud enviada con éxito");
    } catch (error) {
        console.error("Error al enviar la solicitud:", error);
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
async function crearUsuario() {
    const Form = document.querySelector(".newUser");
    console.log(Form)

    const nombre = Form.querySelector("#Nombre_new").value
    const apellido = Form.querySelector("#Apellido_new").value
    const correo = Form.querySelector("#Correo_new").value
    const tipoDocumneto = Form.querySelector("#Tipo_documento_new").value
    const numeroDocumento = Form.querySelector("#numero_documento_new").value
    const fechaNacimientoInput = document.querySelector("#Fecha_nacimiento_new").value;
    const password = Form.querySelector("#password_new").value;
    const fechaFormateada = formatFecha(fechaNacimientoInput);
    const rol = Form.querySelector("#Rol_new").value
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
    try {
        await sendRequest("/Prueba_JSP_war_exploded/usuarios", data);
        console.log("Solicitud enviada con éxito");
    } catch (error) {
        console.error("Error al enviar la solicitud:", error);
    }
}


function formatFecha(inputDate) {
    const date = new Date(inputDate);
    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const year = date.getFullYear();
    return `${day}/${month}/${year}`;
}