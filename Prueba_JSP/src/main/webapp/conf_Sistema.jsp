<%@ page import="Modelo.Persona" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:include page="resources/header.jsp" />

<jsp:include page="resources/sidebar.jsp" />

<%
    HttpSession sesion = request.getSession();
    Persona user = (Persona) session.getAttribute("user");
%>
<section class="main_container__conf_Sistema">
    <div class="info_box__container">
        <div class="info_box config_box">
            <div class="info_data">
                <h1>Sectores Y Espacios</h1>
                <hr>
                <p>Podras modificar los sectores y los espacios de los mismos hQue Gestionaran los Colaboradores.</p>
            </div>
            <div class="info_button">
                <button class="formulario_login__button info_user_container_side2__button">Editar Sectores</button>
            </div>
        </div>
        <div class="info_box config_box">
            <div class="info_data">
                <h1>Vehiculos</h1>
                <hr>
                <p>Podras gestionar los vehiculos de los aprendices  registrados en el sistema.</p>
            </div>
            <div class="info_button">
                <button class="formulario_login__button info_user_container_side2__button">Editar Vehiculos</button>
            </div>
        </div>
        <div class="info_box config_box">
            <div class="info_data">
                <h1>Tipos de Documentos</h1>
                <hr>
                <p>Podras gestionar los tipos de documentos presentes en el Sistema</p>
            </div>
            <div class="info_button">
                <button class="formulario_login__button info_user_container_side2__button">Editar Documentos</button>
            </div>
        </div>
        <div class="info_box config_box"v>
            <div class="info_data">
                <h1>Tipos de Vehiculos</h1>
                <hr>
                <p>Podras gestionar los tipos, marcas y modelos de los vehiculos disponibles en el sistema</p>
            </div>
            <div class="info_button">
                <button class="formulario_login__button info_user_container_side2__button">Editar Vehiculos</button>
            </div>
        </div>
        <div class="info_box config_box">
            <div class="info_data">
                <h1>Roles</h1>
                <hr>
                <p>Podras gestionar los roles que se usan en el sistema</p>
            </div>
            <div class="info_button">
                <button class="formulario_login__button info_user_container_side2__button">Editar Roles</button>
            </div>
        </div>
    </div>
</section>

</body>
</html>
