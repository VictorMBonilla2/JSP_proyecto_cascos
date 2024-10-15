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
                    <h2>Modificar Estado Espacio Individual</h2>
                </div>
            </div>
            <div class="form__side">
                <form class="formulario">
                    <div class="formulario__inputs_config">
                        <div class="input_container">
                            <label for="item_selector"> Selecciona el sector:</label>
                            <select id="item_selector" name="sector_select">
                            </select>
                        </div>
                        <div class="input_container">
                            <label for="space_selector">Selecciona el Espacio:</label>
                            <select id="space_selector" name="space_selector"></select>
                        </div>
                        <div class="input_container">
                            <label for="estado_selector">Espacios del sector:</label>
                            <select id="estado_selector" name="estado_selector">
                            <option value="Libre">Libre</option>
                            <option value="Inactivo">Inactivo</option>
                            </select>
                        </div>
                    </div>
                    <div class="button-container">
                        <button class="button_primary"
                                type="submit" id="edit_button">Actualizar</button>
                    </div>
                </form>
            </div>

        </div>
        <div class="conf_sectores__sideTwo modific_item__container">

        </div>
    </div>

</section>
<jsp:include page="resources/confirm.jsp"/>
<jsp:include page="resources/success.jsp"/>
<jsp:include page="resources/error.jsp"/>
<script type="module" src="resources/js/systemForms/Espacios.js"></script>
<jsp:include page="resources/footer.jsp" />
</body>
</html>
