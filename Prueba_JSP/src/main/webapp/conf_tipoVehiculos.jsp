<%@ page import="Modelo.Persona" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="resources/header.jsp" />
<link rel="stylesheet" href="resources/css/conf_Sistema.css">
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
                    <h2>Modificar Tipo de Vehículo</h2>
                </div>
            </div>
            <div class="form__side">
                <form class="formulario">
                    <input type="hidden" name="formType" value="edit">
                    <div class="formulario__inputs_config">
                        <div class="input_container">
                            <label for="item_selector">Selecciona el Tipo de Vehículo:</label>
                            <select id="item_selector" name="tipoVehiculoSelect"></select>
                        </div>
                        <div class="input_container">
                            <label for="nameVehiculo_input">Nombre del Tipo:</label>
                            <input class="config_input" name="nombreVehiculo" id="nameVehiculo_input">
                        </div>
                    </div>
                    <div class="button-container">
                        <button class="sector_button_edit button_primary" id="edit_button" type="submit">Editar</button>
                        <button class="sector_button_cancel button_secundary" id="cancel_edit__button" type="reset">Cancelar</button>
                        <button class="sector_button_delete button_secundary button--delete" id="delete_button" type="button">Eliminar</button>
                    </div>
                </form>
            </div>
        </div>

        <div class="conf_sectores__sideTwo modific_item__container">
            <div class="title__side">
                <div class="title__container">
                    <h2>Crear Nuevo Tipo de Vehículo</h2>
                </div>
            </div>
            <div class="form__side">
                <form class="formulario">
                    <input type="hidden" name="formType" value="add">
                    <div class="formulario__inputs">
                        <div class="input_container">
                            <label for="tipoVehiculo_name">Nombre del Tipo de Vehículo:</label>
                            <input id="tipoVehiculo_name" name="nombreVehiculo">
                        </div>
                    </div>
                    <div class="button-container">
                        <button class="sector_button_edit button_primary" id="create_button" type="submit">Crear</button>
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
<script type="module" src="resources/js/systemForms/tipoVehiculos.js"></script>
<jsp:include page="resources/footer.jsp" />
</body>
</html>

