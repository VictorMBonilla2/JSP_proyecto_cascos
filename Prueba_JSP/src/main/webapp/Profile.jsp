<%@ page import="Modelo.Persona" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:include page="resources/header.jsp" />

<jsp:include page="resources/sidebar.jsp" />

<%
    HttpSession sesion = request.getSession();
    Persona user = (Persona) session.getAttribute("user");
%>
<section class="main_container__profile">
    <div class="Miranose">
        <div class="info_user_container">
            <div class="user_container_side1" id="userinfo">
                <div class="info_user_container__header">
                    <img src="resources/imagenes/IconPerfil.png" alt="">
                    <h1><%=user.getNombre()%></h1>
                    <h3><%=user.getRol()%></h3>
                </div>

                <div class="info_user_container__body">
                    <div>
                        <h1>Correo</h1>
                        <h3><%=user.getCorreo()%></h3>
                    </div>
                    <div>
                        <h1>>Celular</h1>
                        <h3><%="hola"%></h3>
                    </div>
                    <div>
                        <h1>Fecha de Nacimiento</h1>
                        <h3><%="hola"%></h3>
                    </div>
                </div>

                <button class="formulario_login__button info_user_container__button">Editar perfil</button>
            </div>
            <div class="user_container_side2">
                <div class="info_box">
                    <div class="info_data">
                    <h1>Datos de contacto</h1>
                    <hr>
                    <p>Correo electronico: <%=user.getCorreo()%></p>
                    <p>Número de celular: <%="hola"%></p>
                    </div>
                    <div class="info_button">
                    <button class="formulario_login__button info_user_container_side2__button">Editar datos</button>
                    </div>
                </div>
                <div class="info_box">
                    <div class="info_data">
                    <h1>Datos Personales</h1>
                    <hr>
                    <p>Puedes obtener una copia de tus datos que nosotros manejamos.</p>
                    </div>
                    <div class="info_button">
                    <button class="formulario_login__button info_user_container_side2__button">Pedir datos datos</button>
                    </div>
                </div>
                <div class="info_box">
                    <div class="info_data">
                    <h1>Borrar cuenta</h1>
                    <hr>
                    <p>Si piensa borrar definitivamente tu cuenta, se perderan todos tus datos</p>
                    </div>
                    <div class="info_button">
                    <button class="formulario_login__button info_user_container_side2__button">Borrar Datos</button>
                    </div>
                </div>


            </div>
        </div>
        <div class="button_miranose">
            <button class="formulario_login__button" type="submit">Guardar</button>
            <button class="formulario_login__button" type="button" id="cancelEdit">Cancelar</button>
        </div>
    </div>

    <div class="detalles_user_container">
        <div class="detalles_user">
            <h1>Documentacion</h1>
            <hr>
            <p>Tipo de documento: <%=user.getTipoDocumento()%></p>
            <p>Número de documento: <%=user.getDocumento()%></p>

            <h3>Cualquier tipo de cambio en tu documento debe ser realiado por un administrador!</h3>

        </div>
        <div class="detalles_user">
            <h1>Datos personales</h1>
            <hr>
            <p>Puedes obtener una copia tus datos que nosotros manejamos</p>

            <button class="formulario_login__button data_button info_user_container_side2__button">Pedir datos</button>

        </div>
        <div class="detalles_user">
            <h1>Borrar cuenta</h1>
            <hr>
            <p>Si piensas borrar definitivamente tu cuenta,  Se perderan todos tus datos.</p>

            <button class="formulario_login__button delete_button info_user_container_side2__button">Borrar cuenta</button>
        </div>
    </div>

    <template id="infoTemplate">
        <div class="info_user_container__header">
            <img src="resources/imagenes/IconPerfil.png" alt="">
            <h1><%=user.getNombre()%></h1>
            <h3><%=user.getRol()%></h3>
        </div>

        <div class="info_user_container__body">
            <div>
                <h1>Correo</h1>
                <h3><%=user.getCorreo()%></h3>
            </div>
            <div>
                <h1>>Celular</h1>
                <h3><%="hola"%></h3>
            </div>
            <div>
                <h1>Fecha de Nacimiento</h1>
                <h3><%="hola"%></h3>
            </div>
        </div>

        <button class="formulario_login__button info_user_container__button">Editar perfil</button>
    </template>

    <template id="formTemplate">
        <div class="info_user_container__header">
            <img src="resources/imagenes/IconPerfil.png" alt="">
            <h1><%=user.getNombre()%></h1>
            <h3><%=user.getRol()%></h3>
        </div>
        <form class="form-container" id="editPrimaryDataForm">
            <input type="hidden" id="usuarioId" value="<%= user.getDocumento() %>">
            <div class="Inputs_Container" >
                <label>
                    Nombre:
                    <input type="text" id="nombre" value="<%=user.getNombre()%>">
                </label>
                <label>
                    Apellido:
                    <input type="text" id="apellido" value="<%=user.getApellido()%>">
                </label>
                <label>
                    Fecha de Nacimiento:
                    <input type="text" id="fecha_nacimiento" value="17/03/1992">
                </label>

            </div>
        </form>
    </template>
</section>
</section>

<script type="module" src="resources/js/Profile.js"></script>


<jsp:include page="resources/footer.jsp" />
</body>
</html>
