import {sendRequest} from "./ajax.js";
document.addEventListener("DOMContentLoaded",  async () => {
    const editButtons = document.querySelectorAll(".user_button_edit");
    const deleteButtons = document.querySelectorAll(".user_button_delete");
    const lista= document.querySelector(".users_list__container");
    const formTemplate = document.querySelector("#template_form").content;
    const formNewTemplate= document.querySelector("#template_form_newUser").content ;
    const itemTemplate = document.querySelector("#item__list").content;

    let personas= await ObtenerUsuarios("1");

    if(personas.length>0){
        personas.forEach((persona, index) =>{
            const clone = document.importNode(itemTemplate, true);

            // Modificar el contenido del clon
            clone.querySelector(".user_list__item").setAttribute('data-user', index);

            clone.querySelector(".user_item_document > p").textContent = persona.documento;

            clone.querySelector(".user_item_name > p").textContent = persona.nombre;

            clone.querySelector(".user_item_rol > p").textContent = persona.rol.nombre;

            clone.querySelector(".user_button_edit").setAttribute('data-edit', index);

            clone.querySelector(".user_button_delete").setAttribute('data-delete', index);

            // Insertar el clon modificado en la lista
            lista.appendChild(clone);
        })
    }else{
        console.log("no hay personas")
    }



    editButtons.forEach(button => {
        button.addEventListener("click", (event) => {
            const data_user = event.target.getAttribute("data-edit");
            console.log(data_user);


            const user_item = document.querySelector(`[data-user="${data_user}"]`);
            console.log(user_item);


            user_item.style.height = "66%";


            user_item.appendChild(formTemplate.cloneNode(true));
        });
    });

    document.addEventListener("click", async (e) => {
        console.log(e.target)

        if(e.target.matches(".new_user__item")){
            const user_item= e.target;
            const clone = document.importNode(formNewTemplate, true);
            user_item.style.height = "66%";
            const formulario = clone.querySelector(".formulario");
            if (formulario) {
                // Añade el evento submit mediante addEventListener
                formulario.addEventListener("submit", (event) => {
                    event.preventDefault();
                    crearUsuario();
                });
                formulario.setAttribute("data_user","createUser")
            }
            user_item.appendChild(clone)

        }

        if (e.target.matches("#edit_button")) {
            console.log("Se presiono edit")
            let data_edit = e.target.getAttribute("data-edit");
            const user_item = document.querySelector(`[data-user="${data_edit}"]`);
            console.log(user_item);
            user_item.style.height = "66%";
            const dato_usuario= personas[data_edit]
            const clone = document.importNode(formTemplate, true);

            const formulario = clone.querySelector(".formulario");
            if (formulario) {
                // Añade el evento submit mediante addEventListener
                formulario.addEventListener("submit", (event) => {
                    event.preventDefault();
                    editUser(event, dato_usuario.documento);
                });
                formulario.setAttribute("data_user",dato_usuario.documento)
            }


            clone.querySelector("#Nombre").value = dato_usuario.nombre;
            console.log( clone.querySelector(".Nombre"));

            clone.querySelector("#Apellido").value = dato_usuario.apellido;

            clone.querySelector("#Correo").value = dato_usuario.correo;
            clone.querySelector("#Tipo_documento").value = dato_usuario.tipo_documento;
            clone.querySelector("#numero_documento").value = dato_usuario.numero_documento;
            clone.querySelector("#Fecha_nacimiento").value = dato_usuario.fecha_nacimineto;
            clone.querySelector("#Rol").value = dato_usuario.rol.idRol;


            user_item.appendChild(clone);
        }
        if (e.target.matches("#delete_button")){

            let data_delete = e.target.getAttribute("data-delete");
            let advertencia=confirm( `Estas seguro de eliminar al usuario  ${personas[data_delete].nombre} ${personas[data_delete].apellido}`)
            if(advertencia){
                let response= borrarUsuario(personas[data_delete]);
                if(response){
                    const user_item = document.querySelector(`[data-user="${data_delete}"]`);
                    user_item.innerHTML=""
                }
            }
        }
    });




});
async function ObtenerUsuarios(Pagina_Actual){

    const response = await fetch(`/Prueba_JSP_war_exploded/usuarios?Pagination=${Pagina_Actual}`);
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

async function editUser(evento, documento_user){

    const Form = document.querySelector(`[data-user="${documento_user}"]`)


    const nombre= Form.querySelector("#Nombre").value
    const apellido= Form.querySelector("#Apellido").value
    const correo= Form.querySelector("#Correo").value
    const tipoDocumneto= Form.querySelector("#Tipo_documento").value
    const numeroDocumento= Form.querySelector("#numero_documento").value
    const fechaNacimientoInput = document.querySelector("#Fecha_nacimiento").value;
    const fechaFormateada = formatFecha(fechaNacimientoInput);
    const rol= Form.querySelector("#Rol").value
    const data={
        "action": "edit",
        "documento":documento_user,
        "nombre": nombre,
        "apellido": apellido,
        "correo":correo,
        "tipoDocumneto":tipoDocumneto,
        "numeroDocumento":numeroDocumento,
        "fechaNacimiento":fechaFormateada,
        "rol":rol
    }
    try {
        await sendRequest("/Prueba_JSP_war_exploded/usuarios", data);
        console.log("Solicitud enviada con éxito");
    } catch (error) {
        console.error("Error al enviar la solicitud:", error);
    }
}

async function borrarUsuario(persona) {
    const id_persona= persona.documento;
    console.log(id_persona)
    const data={
        "action":"delete",
        "id":id_persona
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
async function crearUsuario(){
    const Form = document.querySelector(".newUser");
    console.log(Form)

    const nombre= Form.querySelector("#Nombre_new").value
    const apellido= Form.querySelector("#Apellido_new").value
    const correo= Form.querySelector("#Correo_new").value
    const tipoDocumneto= Form.querySelector("#Tipo_documento_new").value
    const numeroDocumento= Form.querySelector("#numero_documento_new").value
    const fechaNacimientoInput = document.querySelector("#Fecha_nacimiento_new").value;
    const password = Form.querySelector("#password_new").value;
    const fechaFormateada = formatFecha(fechaNacimientoInput);
    const rol= Form.querySelector("#Rol_new").value
    const data={
        "action": "add",
        "nombre": nombre,
        "apellido": apellido,
        "correo":correo,
        "tipoDocumneto":tipoDocumneto,
        "numeroDocumento":numeroDocumento,
        "fechaNacimiento":fechaFormateada,
        "password":password,
        "rol":rol
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