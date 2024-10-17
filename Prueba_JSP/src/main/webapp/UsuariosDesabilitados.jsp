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
    <div class="button-container">
        <a href="Usuarios.jsp" class="button_secundary button--width45">
            Ver habilitados
        </a>
    </div>
    <div class="users_list__container" id="listaDeshabilitados">
        <!-- Usuarios inhabilitados se mostrarán aquí -->
    </div>
    <div id="paginadorInhabilitados"></div> <!-- Paginador dinámico -->
    <script type="module" src="resources/js/usuarios_inactivos.js"></script>
</section>

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
<jsp:include page="resources/confirm.jsp"/>
<jsp:include page="resources/success.jsp"/>
<jsp:include page="resources/error.jsp"/>
<jsp:include page="resources/footer.jsp" />
</body>
</html>
