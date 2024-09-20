<%@ page import="Modelo.Persona" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<jsp:include page="resources/header.jsp"/>
<jsp:include page="resources/sidebar.jsp"/>

<%
    HttpSession sesion = request.getSession();
    Persona user = (Persona) session.getAttribute("user");
%>
<section class="main_container__conf_Sistema">
    <div class="users_list__container">
        <div class="users_list__item new_user__item">
            Crear Nuevo Usuario
        </div>
    </div>

    <script type="module" src="resources/js/usuarios.js"></script>
</section>

<template id="item__list">
    <div class="user_list__item" >
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
                <button class="user_button_edit formulario_login__button data_button" id="edit_button">Editar</button>
                <button class="user_button_delete formulario_login__button data_button info_user_container_side2__button" id="delete_button">Eliminar</button>
            </div>
        </div>

    </div>
</template>

<template id="template_form">
    <div class="user_list__form">
        <form class="formulario ">
            <div class="form_persona">
                <div class="formulario__inputs">
                    <label for="Nombre">Nombre</label>
                    <input type="text" id="Nombre">
                </div>
                <div class="formulario__inputs">
                    <label for="Apellido"> Apellido</label>
                    <input type="text" id="Apellido">
                </div>
                <div class="formulario__inputs">
                    <label for="Correo">Correo Electronico</label>
                    <input type="text" id="Correo">
                </div>
                <div class="formulario__inputs">
                    <label for="Tipo_documento"> Tipo de documento</label>
                    <input type="text" id="Tipo_documento">
                </div>

                <div class="formulario__inputs">
                    <label for="numero_documento">Numero de documento</label>
                    <input type="text" id="numero_documento">
                </div>
                <div class="formulario__inputs">
                    <label for="Fecha_nacimiento">Fecha de nacimiento</label>
                    <input type="date" id="Fecha_nacimiento">
                </div>
                <div class="formulario__inputs">
                    <label for="Rol">Rol del usuario</label>
                    <input type="text" id="Rol">
                </div>

            </div>
            <div class="button_miranose" style="display: flex;">
                <button class="formulario_login__button" type="submit">Guardar</button>
                <button class="formulario_login__button" type="button" id="cancelEdit">Cancelar</button>
            </div>
        </form>

    </div>
</template>


<template id="template_form_newUser">
    <div class="user_list__form">
        <form class="formulario newUser">
            <div class="form_persona">
                <div class="formulario__inputs">
                    <label for="Nombre_new">Nombre</label>
                    <input type="text" id="Nombre_new">
                </div>
                <div class="formulario__inputs">
                    <label for="Apellido_new"> Apellido</label>
                    <input type="text" id="Apellido_new">
                </div>
                <div class="formulario__inputs">
                    <label for="Correo_new">Correo Electronico</label>
                    <input type="text" id="Correo_new">
                </div>
                <div class="formulario__inputs">
                    <label for="Tipo_documento_new"> Tipo de documento</label>
                    <input type="text" id="Tipo_documento_new">
                </div>

                <div class="formulario__inputs">
                    <label for="numero_documento_new">Numero de documento</label>
                    <input type="text" id="numero_documento_new">
                </div>
                <div class="formulario__inputs">
                    <label for="password_new">Numero de documento</label>
                    <input type="password" id="password_new">
                </div>
                <div class="formulario__inputs">
                    <label for="Fecha_nacimiento_new">Fecha de nacimiento</label>
                    <input type="date" id="Fecha_nacimiento_new">
                </div>
                <div class="formulario__inputs">
                    <label for="Rol_new">Rol del usuario</label>
                    <input type="text" id="Rol_new">
                </div>

            </div>
            <div class="button_miranose" style="display: flex;">
                <button class="formulario_login__button" type="submit">Guardar</button>
                <button class="formulario_login__button" type="button" id="cancelEdit_new">Cancelar</button>
            </div>
        </form>

    </div>
</template>
<jsp:include page="resources/footer.jsp" />
</body>
</html>
