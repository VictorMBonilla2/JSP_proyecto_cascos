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
    <div class="conf__container--individual">
        <div class="conf_container__sideOne modific_item__container no-border">
            <div class="title__side no-border">
                <div class="title__container">
                    <h2>Buscar informes</h2>
                </div>

            </div>
            <div class="form__side">
                <form class="formulario">
                    <input type="hidden" name="formType" value="edit">
                    <div class="formulario__inputs_config">
                        <div class="input_container">
                            <label for="nameCiudad_input">Ingresa el codigo del informa:</label>
                            <input class="config_input" name="codeinforme" id="nameCiudad_input">
                        </div>
                    </div>
                    <div class="button-container">
                        <button class="sector_button_edit button_primary " id="edit_button" type="submit">Buscar y descargar</button>
                    </div>
                </form>

            </div>

        </div>
    </div>

</section>
<jsp:include page="resources/confirm.jsp"/>
<jsp:include page="resources/success.jsp"/>
<jsp:include page="resources/error.jsp"/>
<script type="module" src="resources/js/systemForms/informes.js"></script>
<jsp:include page="resources/footer.jsp" />
</body>
</html>
