import {hideLoadingSpinner, sendRequest, showLoadingSpinner} from "./ajax.js";
import {cargarRoles, cargarTiposDocumento} from "./utils/renderSelects.js";
import {validarCelular, validarDocumento, validarEmail, validarFecha, validarTexto} from "./utils/validations.js";
import {showErrorDialog} from "./alerts/error.js";
import {showSuccessAlert} from "./alerts/success.js";
import {showConfirmationDialog} from "./alerts/confirm.js";

const formTemplate = document.querySelector("#template_form").content;
const formNewTemplate = document.querySelector("#template_form_newUser").content;
const itemTemplate = document.querySelector("#item__list").content;
const newitemTemplate= document.querySelector("#new-item__list").content;
document.addEventListener("DOMContentLoaded", async () => {

    let personasActivas = await ObtenerUsuariosActivos("1");
    let personas = personasActivas.usuarios;
    if (personasActivas.usuarios.length > 0) {
        actualizarListaUsuarios(personasActivas.usuarios, "listaHabilitados");
        crearPaginador(personasActivas.totalPaginas, 1);
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
                formulario.setAttribute("data-user", dato_usuario.idUser)
            }

            // Asignar valores a los campos del formulario
            clone.querySelector("#Nombre").value = dato_usuario.nombre;
            clone.querySelector("#Apellido").value = dato_usuario.apellido;
            clone.querySelector("#Correo").value = dato_usuario.correo;
            clone.querySelector("#numero_documento").value = dato_usuario.documento;
            clone.querySelector("#Fecha_nacimiento").value = dato_usuario.fecha_nacimiento;
            clone.querySelector("#idUser").value = dato_usuario.idUser;
            clone.querySelector("#numero_celular").value= dato_usuario.numeroCelular;

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

            document.querySelector("#Tipo_documento").value = dato_usuario.doc.idDocumento;
            document.querySelector("#Rol").value = dato_usuario.rol.idRol;
        }
        if (e.target.matches("#disable_button")) {

            let data_disable = parseInt(e.target.getAttribute("data-disable"));
            console.log(data_disable)
            // Filtrar la lista de personas por idUser
            const personaSeleccionada = personas.filter(persona => persona.idUser == data_disable);
            console.log(personaSeleccionada)
            if (personaSeleccionada.length > 0) {
                showConfirmationDialog("¿Deshabilitar Usuario?",
                    "Al deshabilitar al usuario impediras el acceso a este usuario. El sistema no permite " +
                    "deshabilitar usuario los cuales esten usando espacios",
                    ()=>deshabilitarUsuario(personaSeleccionada[0]),
                    ()=>console.log("Acción cancelada"))
            } else {
                console.log("Usuario no encontrado.");
            }
        }
    });

    document.addEventListener("submit",async (e)=>{
        e.preventDefault();
        const targerForm= e.target;
        const form = new FormData(targerForm);
        const tipo=form.get("formType");
        const submitButton = e.target.querySelector("button[type='submit']");
        submitButton.disabled = true;
        showLoadingSpinner()
        try{
            if(tipo === "add"){
                if(validarFormularioNew(form))
                    await crearUsuario(form)
            }

            if(tipo === "edit"){
                if(verificarFormularioEdit(form)){
                    await editUser(form);
                }
            }
        }finally {
            hideLoadingSpinner()
            submitButton.disabled = false;
        }
    })
});

function crearPaginador(totalPaginas, paginaActual) {
    const paginadorContainer =  document.getElementById('paginadorHabilitados');

    paginadorContainer.innerHTML = ''; // Limpiar el paginador antes de crear nuevos botones
    for (let i = 1; i <= totalPaginas; i++) {
        const boton = document.createElement('button');
        boton.textContent = i;
        boton.className = i === paginaActual ? 'active' : '';
        boton.addEventListener('click', () => cambiarPagina(i));
        paginadorContainer.appendChild(boton);
    }
}
async function cambiarPagina(pagina) {
    let personas;
    personas = await ObtenerUsuariosActivos(pagina);
    if (personas && personas.usuarios.length > 0) {
        actualizarListaUsuarios(personas.usuarios, 'listaHabilitados');
        crearPaginador(personas.totalPaginas, pagina);
    }
}
function actualizarListaUsuarios(usuarios, contenedorId) {
    const lista = document.getElementById(contenedorId);
    lista.innerHTML = '';// Limpiar la lista antes de agregar nuevos usuarios
    lista.appendChild(document.importNode(newitemTemplate, true));
    usuarios.forEach(persona => {
        // Seleccionar el template correcto según el estado del usuario
        const clone = document.importNode(itemTemplate, true);
        clone.querySelector(".user_list__item").setAttribute('data-user', persona.idUser);
        clone.querySelector(".user_item_document > p").textContent = persona.documento;
        clone.querySelector(".user_item_name > p").textContent = persona.nombre;
        clone.querySelector(".user_item_rol > p").textContent = persona.rol.nombre;
        clone.querySelector(".user_button_edit").setAttribute('data-edit', persona.idUser);
        clone.querySelector(".user_button_delete").setAttribute('data-disable', persona.idUser);
        lista.appendChild(clone);
    });
}

async function ObtenerUsuariosActivos(Pagina_Actual) {
    const response = await fetch(`http://localhost:8080/Prueba_JSP_war_exploded/usuarios?Pagination=${Pagina_Actual}&estado=activo`);

    if (response.ok) {
        const data = await response.json();

        if (data.usuarios.length === 0) {
            console.log('No se encontraron personas activas.');
            return { usuarios: [], totalPaginas: 0, paginaActual: 0, totalRegistros: 0 }; // Respuesta vacía con estructura adecuada
        } else {
            // Retorna los datos obtenidos
            return {
                usuarios: data.usuarios,
                totalPaginas: data.totalPaginas,
                paginaActual: data.paginaActual,
                totalRegistros: data.totalRegistros
            };
        }
    } else {
        console.log('Error al obtener usuarios activos.');
        return { usuarios: [], totalPaginas: 0, paginaActual: 0, totalRegistros: 0 }; // Respuesta vacía con estructura adecuada
    }
}
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


async function editUser(formData) {
    const data = {
        "action": "edit",
        "nombre": formData.get("Nombre"),
        "apellido": formData.get("Apellido"),
        "correo": formData.get("Correo"),
        "tipoDocumneto": formData.get("tipoDocumento"),
        "numeroDocumento": formData.get("numero_documento"),
        "fechaNacimiento": formatFecha(formData.get("Fecha_nacimiento")),
        "rol": formData.get("rol"),
        "idUser": formData.get("idUser"),
        "numeroCelular": formData.get("numero_celular")
    };

    const response = await sendRequest("/Prueba_JSP_war_exploded/usuarios", data);
    console.log("Solicitud enviada con éxito");
    if (response.status === "success") {
        console.log("Se ha actualizado el usuario correctamente");
        showSuccessAlert(response.message);
    } else {
        showErrorDialog(response.message);
    }
}
async function crearUsuario(formData) {
    const data = {
        "action": "add",
        "nombre": formData.get("Nombre_new"),
        "apellido": formData.get("Apellido_new"),
        "correo": formData.get("Correo_new"),
        "tipoDocumneto": formData.get("Tipo_documento_new"),
        "numeroDocumento": formData.get("numero_documento_new"),
        "fechaNacimiento": formatFecha(formData.get("Fecha_nacimiento_new")),
        "password": formData.get("password_new"),
        "rol": formData.get("Rol_new"),
        "numeroCelular": formData.get("numero_celular_new")
    };

    const response = await sendRequest("/Prueba_JSP_war_exploded/usuarios", data);
    if (response.status === "success") {
        console.log("Se ha creado el usuario correctamente");
        showSuccessAlert(response.message);
    } else {
        showErrorDialog(response.message);
    }
}
async function deshabilitarUsuario(persona) {
    console.log(persona)
    const id_persona = persona.idUser;
    console.log(id_persona)
    const data = {
        "action": "change",
        "id": id_persona
    }
    try {
        const response = await sendRequest("/Prueba_JSP_war_exploded/usuarios", data);
        if (response.status === "success") {
            const user_item = document.querySelector(`[data-user="${persona.idUser}"]`);
            user_item.innerHTML = "";
            console.log("Modelo creado correctamente");
            showSuccessAlert(response.message);
        } else {
            showErrorDialog(response.message);
        }
    } catch (error) {
        console.error("Error al enviar la solicitud:", error);
        return false
    }
}
function validarFormularioNew(formData) {
    const nombre = formData.get("Nombre_new");
    const apellido = formData.get("Apellido_new");
    const correo = formData.get("Correo_new");
    const tipoDocumento = formData.get("Tipo_documento_new");
    const numeroDocumento = formData.get("numero_documento_new");
    const fechaNacimientoInput = formData.get("Fecha_nacimiento_new");
    const password = formData.get("password_new");
    const rol = formData.get("Rol_new");

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
function verificarFormularioEdit(formData) {
    const nombre = formData.get("Nombre");
    const apellido = formData.get("Apellido");
    const correo = formData.get("Correo");
    const tipoDocumento = formData.get("tipoDocumento");
    const numeroDocumento = formData.get("numero_documento");
    const numeroCelular = formData.get("numero_celular");
    const fechaNacimientoInput = formData.get("Fecha_nacimiento");
    const rol = formData.get("rol");

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

    // Validar el número de celular
    if (!validarCelular(numeroCelular)) {
        showErrorDialog("El celular debe ser un número válido.");
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
function formatFecha(inputDate) {
    const date = new Date(inputDate);
    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const year = date.getFullYear();
    return `${day}/${month}/${year}`;
}
