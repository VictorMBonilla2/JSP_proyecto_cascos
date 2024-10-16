<%@ page import="Modelo.Persona" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<jsp:include page="resources/header.jsp"/>
<link rel="stylesheet" href="resources/css/conf_Sistema.css">
<link rel="stylesheet" href="resources/css/usuarios.css">
<jsp:include page="resources/sidebar.jsp"/>

<%
    HttpSession sesion = request.getSession(false);
    Persona user = null;
    if (sesion != null) {
        user = (Persona) sesion.getAttribute("user");
    }
    if (user == null) {
        response.sendRedirect("index.jsp");
        return;
    }
%>
<section class="main_container__conf_Sistema">

    <a href="UsuariosDesabilitados.jsp" class="button-secundary button--width45">
        Ver deshabilitados
    </a>
    <div class="users_list__container" id="listaHabilitados">
        <div class="user_list__item new_user__item">
            Crear Nuevo Usuario
        </div>
    </div>
    <div id="paginadorHabilitados"></div>
    <script type="module" src="resources/js/usuarios.js"></script>
</section>

<template id="new-item__list">
    <div class="user_list__item new_user__item">
        Crear Nuevo Usuario
    </div>
</template>

<template id="item__list">
    <div class="user_list__item">
        <div class="users_list__data">
            <div class="user_item user_item_document">
                <h2>Documento</h2>
                <p></p>
            </div>
            <div class="user_item user_item_name">
                <h2>Nombre</h2>
                <p></p>
            </div>
            <div class="user_item user_item_rol">
                <h2>Rol</h2>
                <p></p>
            </div>
            <div class="user_item user_item__buttons">
                <button class="user_button_edit button_primary button--edit"
                        id="edit_button">Editar</button>
                <button
                        class="user_button_delete button_secundary button--delete"
                        id="disable_button">Deshabilitar</button>
            </div>
        </div>

    </div>
</template>
<template id="item__list--disable">
    <div class="user_list__item">
        <div class="users_list__data">
            <div class="user_item user_item_document">
                <h2>Documento</h2>
                <p></p>
            </div>
            <div class="user_item user_item_name">
                <h2>Nombre</h2>
                <p></p>
            </div>
            <div class="user_item user_item_rol">
                <h2>Rol</h2>
                <p></p>
            </div>
            <div class="user_item user_item__buttons">
                <button
                        class="user_button_delete button_secundary button--delete"
                        id="enable_button">Habilitar</button>
            </div>
        </div>

    </div>
</template>

<template id="template_form">
    <div class="user_list__form">
        <form class="formulario ">
            <input type="hidden" id="formType" name="formType" value="edit">
            <input type="hidden" id="idUser" name="idUser" value="">
            <div class="form_persona">
                <div class="input_container">
                    <label for="Nombre">Nombre</label>
                    <input type="text" id="Nombre" name="Nombre">
                </div>
                <div class="input_container">
                    <label for="Apellido">Apellido</label>
                    <input type="text" id="Apellido" name="Apellido">
                </div>
                <div class="input_container">
                    <label for="Correo">Correo Electronico</label>
                    <input type="text" id="Correo" name="Correo">
                </div>
                <div class="input_container">
                    <label for="numero_celular">Numero Celular</label>
                    <input type="text" id="numero_celular" name="numero_celular">
                </div>
                <div class="input_container">
                    <label for="Tipo_documento">Tipo de documento</label>
                    <select name="tipoDocumento" id="Tipo_documento">
                    </select>
                </div>
                <div class="input_container">
                    <label for="numero_documento">Numero de documento</label>
                    <input type="text" id="numero_documento" name="numero_documento">
                </div>
                <div class="input_container">
                    <label for="Fecha_nacimiento">Fecha de nacimiento</label>
                    <input type="date" id="Fecha_nacimiento" name="Fecha_nacimiento">
                </div>
                <div class="input_container">
                    <label for="Rol">Rol del usuario</label>
                    <select name="rol" id="Rol"></select>
                </div>
            </div>
            <div class="button_container" style="display: flex;">
                <button class="button_primary" type="submit">Guardar</button>
                <button class="button_secundary cancel-button" type="button">Cancelar</button>
            </div>
        </form>
    </div>
</template>


<template id="template_form_newUser">
    <div class="user_list__form">
        <form class="formulario newUser">
            <input type="hidden" id="formType" name="formType" value="add">
            <div class="form_persona">
                <div class="input_container">
                    <label for="Nombre_new">Nombre</label>
                    <input type="text" id="Nombre_new" name="Nombre_new">
                </div>
                <div class="input_container">
                    <label for="Apellido_new">Apellido</label>
                    <input type="text" id="Apellido_new" name="Apellido_new">
                </div>
                <div class="input_container">
                    <label for="Correo_new">Correo Electrónico</label>
                    <input type="text" id="Correo_new" name="Correo_new">
                </div>
                <div class="input_container">
                    <label for="numero_celular_new">Número Celular</label>
                    <input type="text" id="numero_celular_new" name="numero_celular_new">
                </div>
                <div class="input_container">
                    <label for="Tipo_documento_new">Tipo de documento</label>
                    <select id="Tipo_documento_new" name="Tipo_documento_new"></select>
                </div>
                <div class="input_container">
                    <label for="numero_documento_new">Número de documento</label>
                    <input type="text" id="numero_documento_new" name="numero_documento_new">
                </div>
                <div class="input_container">
                    <label for="password_new">Contraseña</label>
                    <input type="password" id="password_new" name="password_new">
                </div>
                <div class="input_container">
                    <label for="Fecha_nacimiento_new">Fecha de nacimiento</label>
                    <input type="date" id="Fecha_nacimiento_new" name="Fecha_nacimiento_new">
                </div>
                <div class="input_container">
                    <label for="Rol_new">Rol del usuario</label>
                    <select id="Rol_new" name="Rol_new"></select>
                </div>
            </div>
            <div class="button_container" style="display: flex;">
                <button class="button_primary" type="submit">Crear</button>
                <button class="button_secundary cancel-button" type="button">Cancelar</button>
            </div>
        </form>
    </div>
</template>

<jsp:include page="resources/confirm.jsp"/>
<jsp:include page="resources/success.jsp"/>
<jsp:include page="resources/error.jsp"/>
<jsp:include page="resources/footer.jsp" />
</body>
</html>
