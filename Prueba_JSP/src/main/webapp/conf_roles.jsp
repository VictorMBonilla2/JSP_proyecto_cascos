<%@ page import="Modelo.Persona" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="resources/header.jsp" />
<link rel="stylesheet" href="resources/css/conf_Sistema.css">
<jsp:include page="resources/sidebar.jsp" />

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
    <div class="conf__container">
        <div class="conf_container__sideOne modific_item__container">
            <div class="title__side">
                <div class="back_button"><span> &lt; </span></div>
                <div class="title__container">
                    <h2>Modificar Roles</h2>
                </div>

            </div>
            <div class="form__side">
                <form class="formulario">
                    <input type="hidden" name="formType" value="edit">
                    <div class="formulario__inputs_config">
                        <div class="input_container">
                            <label for="item_selector"> Selecciona el Rol:</label>
                            <select id="item_selector" name="rolSelect"></select>
                        </div>
                        <div class="input_container">
                            <label for="nameRol_input">Nombre del Rol:</label>
                            <input class="config_input" name="nombreRol" id="nameRol_input">
                        </div>
                    </div>
                    <div class="button-container">
                        <button class="sector_button_edit button_primary " id="edit_button" type="submit">Editar</button>
                        <button class="sector_button_cancel button_secundary" id="cancel_edit__button" type="reset">Cancelar</button>
                        <button class="sector_button_delete button_secundary button--delete info_user_container_side2__button" id="delete_button" type="button">Eliminar</button>
                    </div>
                </form>

            </div>

        </div>
        <div class="conf_sectores__sideTwo modific_item__container">
            <div class="title__side">
                <div class="title__container">
                    <h2>Crear Nuevo Sector</h2>
                </div>

            </div>
            <div class="form__side">
                <form class="formulario">
                    <input type="hidden" name="formType" value="add">
                    <div class="formulario__inputs ">
                        <div class="input_container">
                            <label for="sector_name"> Nombre del Rol:</label>
                            <input id="sector_name" name="nombreRol">
                        </div>
                    </div>
                    <div class="button-container">
                        <button class="sector_button_edit button_primary " id="create_button" type="submit">Editar</button>
                        <button class="sector_button_cancel button_secundary" type="reset">Cancelar</button>
                    </div>
                </form>

            </div>

        </div>
    </div>

</section>
<jsp:include page="resources/confirm.jsp"/>
<jsp:include page="resources/success.jsp"/>
<jsp:include page="resources/error.jsp"/>
<script type="module" src="resources/js/systemForms/roles.js"></script>
<jsp:include page="resources/footer.jsp" />
</body>
</html>
