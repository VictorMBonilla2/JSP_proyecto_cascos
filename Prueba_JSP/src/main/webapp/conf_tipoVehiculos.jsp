<%@ page import="Modelo.Persona" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="resources/header.jsp" />

<jsp:include page="resources/sidebar.jsp" />

<%
    HttpSession sesion = request.getSession();
    Persona user = (Persona) session.getAttribute("user");
%>
<section class="main_container__conf_Sistema">
    <div class="conf__container">
        <div class="conf_container__sideOne modific_item__container">
            <div class="title__side">
                <div class="back_button"><span> &lt; </span></div>
                <div class="title__container">
                    <h2>Modificar Tipos de Vehiculos</h2>
                </div>

            </div>
            <div class="form__side">
                <form class="formulario">
                    <div class="formulario__inputs_config">
                        <div class="input_form">
                            <label for="uten_selector"> Selecciona el Tipo:</label>
                            <select id="uten_selector"></select>
                        </div>
                        <div class="input_form">
                            <label id="spac_input">Nombre del Tipo:</label>
                            <input class="config_input" id="space_input">
                        </div>
                    </div>
                    <div class="contenido__botones">
                        <button class="sector_button_edit formulario_login__button data_button" id="edit_button">Editar</button>
                        <button class="sector_button_cancel formulario_login__button" id="cancel_edit__button">Cancelar</button>
                        <button class="sector_button_delete formulario_login__button data_button info_user_container_side2__button" id="delete_button">Eliminar</button>
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
                    <div class="formulario__inputs ">
                        <div class="input_form">
                            <label for="sector_name"> Nombre del Tipo:</label>
                            <input id="sector_name">
                        </div>
                    </div>
                    <div class="contenido__botones">
                        <button class="sector_button_edit formulario_login__button data_button" id="create_button">Editar</button>
                        <button class="sector_button_cancel formulario_login__button">Cancelar</button>
                    </div>
                </form>

            </div>

        </div>
    </div>

</section>

</body>
</html>
