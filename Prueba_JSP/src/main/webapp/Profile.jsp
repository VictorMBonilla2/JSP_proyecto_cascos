<%@ page import="Modelo.Persona" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:include page="resources/header.jsp" />
<link rel="stylesheet" href="resources/css/Profile.css">
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
<input type="hidden" id="idUsuario" value="<%=user.getId()%>">
<section class="main_container__profile">
    <div class="Miranose">
        <div class="info_user_container">
            <div class="user_container_side1" id="userinfo">
                <div class="info_user_container__header">
                    <img src="resources/imagenes/IconPerfil.png" alt="">
                    <h1 class="nameUser"></h1>
                    <h3 class="rolUser"></h3>
                </div>

                <div class="info_user_container__body">
                    <div>
                        <h1>Correo</h1>
                        <h3 class="correoUser"></h3>
                    </div>
                    <div>
                        <h1>Fecha de Nacimiento</h1>
                        <h3 class="fechaNacUser"></h3>
                    </div>
                </div>

                <button class="button_primary info_user_container__button">Editar perfil</button>
            </div>
        </div>

    </div>

    <div class="detalles_user_container" style="opacity: 1; display: flex;">
        <div class="detalles_user">
            <h1>Documentacion</h1>
            <hr>
            <p class="tipoDoc"></p>
            <p class="numDoc"></p>

            <h3>Cualquier tipo de cambio en tu documento debe ser realiado por un administrador!</h3>

        </div>
        <div class="detalles_user">
            <h1>Datos personales</h1>
            <hr>
            <p>Puedes obtener una copia tus datos que nosotros manejamos</p>

            <button
                    class="button_primary button--width45 data_button info_user_container_side2__button">Pedir
                datos</button>

        </div>
        <div class="detalles_user">
            <h1>Borrar cuenta</h1>
            <hr>
            <p>Si piensas borrar definitivamente tu cuenta, Se perderan todos tus datos.</p>

            <button
                    class="button_secundary button--width45 button--delete info_user_container_side2__button">Borrar
                cuenta</button>
        </div>
    </div>

    <template id="infoTemplate">
        <div class="info_user_container__header">
            <img src="resources/imagenes/IconPerfil.png" alt="">
            <h1>gggg</h1>
            <h3>Modelo.Roles@2f2aa57d</h3>
        </div>

        <div class="info_user_container__body">
            <div>
                <h1>Correo</h1>
                <h3>asd</h3>
            </div>
            <div>
                <h1>&gt;Celular</h1>
                <h3>hola</h3>
            </div>
            <div>
                <h1>Fecha de Nacimiento</h1>
                <h3>hola</h3>
            </div>
        </div>

        <button class="button_primary info_user_container__button">Editar perfil</button>
    </template>

    <template id="formTemplate">
        <div class="info_user_container__header">
            <img src="resources/imagenes/IconPerfil.png" alt="">
            <h1>gggg</h1>
            <h3>Modelo.Roles@2f2aa57d</h3>
        </div>
        <form class="form-container" id="editForm">
            <input type="hidden" id="usuarioId" value="1">
            <div class="input_grid">
                <div class="input_container input_container--vertical">
                    <label for="nombre">Nombre: </label>
                    <input type="text" id="nombre" name="nombre"  value="gggg" placeholder="">
                </div>
                <div class="input_container input_container--vertical">
                    <label for="apellido">Apellido: </label>
                    <input type="text" id="apellido"  name="apellido" value="asd" placeholder="">
                </div>
                <div class="input_container input_container--vertical">
                    <label for="fecha_nacimiento">Fecha de Nacimiento: </label>
                    <input type="date" id="fecha_nacimiento" name="fecha_nacimiento" value="17/03/1992" placeholder="">
                </div>

                <div class="input_container input_container--vertical">
                    <label for="coreo"> Correo Electronico:</label>
                    <input type="email" id="coreo" name="correo" placeholder="">
                </div>

            </div>
            <div class="button-container">
                <button class="button_primary" type="submit">Guardar</button>
                <button class="button_secundary" type="button" id="cancelEdit">Cancelar</button>
            </div>
        </form>
    </template>
</section>

<script type="module" src="resources/js/Profile.js"></script>

<jsp:include page="resources/success.jsp"/>
<jsp:include page="resources/error.jsp"/>
<jsp:include page="resources/footer.jsp" />
</body>
</html>
