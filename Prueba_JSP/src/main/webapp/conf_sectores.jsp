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

                <div class="title__container">
                    <h2>Modificar Sectores Existentes</h2>
                </div>
            </div>
            <div class="form__side">
                <form class="formulario">
                    <input type="hidden" name="formType" value="edit">
                    <div class="formulario__inputs_config">
                        <div class="input_container">
                            <label for="item_selector"> Selecciona el sector:</label>
                            <select id="item_selector" name="sector_select">
                            </select>
                        </div>
                        <div class="input_container">
                            <label for="name_input">Nombre del Sector:</label>
                            <input class="config_input"  name="nombre_sector" id="name_input" placeholder="">
                        </div>
                        <div class="input_container">
                            <label for="space_input">Espacios del sector:</label>
                            <input class="config_input"  name="cantidad_espacio" id="space_input" placeholder="">
                        </div>
                    </div>
                    <div class="button-container">
                        <button class="button_primary"
                                type="submit" id="edit_button">Editar</button>
                        <button class="button_secundary"
                                type="reset" id="cancel_edit__button">Cancelar</button>
                        <button
                                class="button_secundary button--delete"
                                type="button" id="delete_button">Eliminar</button>
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
                            <label for="sector_name"> Nombre del sector:</label>
                            <input id="sector_name" name="nombre_sector" placeholder="">
                        </div>
                        <div class="input_container">
                            <label for="space_new_input">Cantidad de Espacios:</label>
                            <input id="space_new_input" name="cantidad_espacio" placeholder="">
                        </div>
                    </div>
                    <div class="button-container">
                        <button class="button_primary"
                                id="create_button" type="submit">Crear</button>
                        <button class="button_secundary" type="reset">Cancelar</button>
                    </div>
                </form>

            </div>

        </div>
    </div>

</section>
<jsp:include page="resources/confirm.jsp"/>
<jsp:include page="resources/success.jsp"/>
<jsp:include page="resources/error.jsp"/>
<script type="module" src="resources/js/systemForms/sectoresEspacios.js"></script>
<jsp:include page="resources/footer.jsp" />
</body>
</html>
