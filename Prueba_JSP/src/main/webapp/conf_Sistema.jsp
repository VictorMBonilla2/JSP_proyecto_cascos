<%@ page import="Modelo.Persona" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:include page="resources/header.jsp" />

<jsp:include page="resources/sidebar.jsp" />
<link rel="stylesheet" href="resources/css/conf_Sistema.css">
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
    <div class="info_box__container">
        <div class="info_box config_box">
            <div class="info_data">
                <h1>Sectores Y Espacios</h1>
                <hr>
                <p>Podras modificar los sectores y los espacios de los mismos hQue Gestionaran los Colaboradores.</p>
            </div>
            <div class="button-container">
                <a href="conf_sectores.jsp" class="button_primary button--width45">Editar Sectores</a>
            </div>
        </div>
        <div class="info_box config_box">
            <div class="info_data">
                <h1>Vehiculos</h1>
                <hr>
                <p>Podras gestionar los vehiculos de los aprendices  registrados en el sistema.</p>
            </div>
            <div class="button-container">
                <a href="conf_vehiculos.jsp" class="button_primary button--width45">Editar Vehiculos</a>
            </div>
        </div>
        <div class="info_box config_box">
            <div class="info_data">
                <h1>Tipos de Documentos</h1>
                <hr>
                <p>Podras gestionar los tipos de documentos presentes en el Sistema</p>
            </div>
            <div class="button-container">
                <a href="conf_documentos.jsp" class="button_primary button--width45">Editar Documentos</a>
            </div>
        </div>
        <div class="info_box config_box" v="">
            <div class="info_data">
                <h1>Tipos de Vehiculos</h1>
                <hr>
                <p>Podras gestionar los tipos, marcas y modelos de los vehiculos disponibles en el sistema</p>
            </div>
            <div class="button-container">
                <a href="conf_tipoVehiculos.jsp" class="button_primary button--width45">Editar Tipo</a>
            </div>
        </div>
        <div class="info_box config_box">
            <div class="info_data">
                <h1>Roles</h1>
                <hr>
                <p>Podras gestionar los roles que se usan en el sistema</p>
            </div>
            <div class="button-container">
                <a href="conf_roles.jsp" class="button_primary button--width45">Editar Roles</a>
            </div>
        </div>
        <div class="info_box config_box">
            <div class="info_data">
                <h1>Ciudad</h1>
                <hr>
                <p>Podras gestionar las ciudades que se usan en el sistema</p>
            </div>
            <div class="button-container">
                <a href="conf_ciudad.jsp" class="button_primary button--width45">Editar Ciudades</a>
            </div>
        </div>
        <div class="info_box config_box">
            <div class="info_data">
                <h1>Marca de Vehiculo</h1>
                <hr>
                <p>Podras gestionar las marcas de vehiculos que se usan en el sistema</p>
            </div>
            <div class="button-container">
                <a href="conf_marcavehiculo.jsp" class="button_primary button--width45">Editar Marcas</a>
            </div>
        </div>
        <div class="info_box config_box">
            <div class="info_data">
                <h1>Modelo de Vehiculo</h1>
                <hr>
                <p>Podras gestionar los modelos de vehiculos que se usan en el sistema</p>
            </div>
            <div class="button-container">
                <a href="conf_modelovehiculo.jsp" class="button_primary button--width45">Editar modelo</a>
            </div>
        </div>
        <div class="info_box config_box">
            <div class="info_data">
                <h1>Verificar Informe</h1>
                <hr>
                <p>Podras buscar informes por su codigo, asi podras verificar su validez</p>
            </div>
            <div class="button-container">
                <a href="conf_informe.jsp" class="button_primary button--width45">buscar informes</a>
            </div>
        </div>
    </div>
</section>
<jsp:include page="resources/footer.jsp" />
</body>
</html>
