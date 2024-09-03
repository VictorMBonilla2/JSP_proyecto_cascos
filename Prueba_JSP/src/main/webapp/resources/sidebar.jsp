<%@ page import="Modelo.Persona" %>

<%
    HttpSession sesion = request.getSession();
    Persona user = (Persona) session.getAttribute("user");
    boolean sesionStatus = user != null;
    boolean isGestor = user.getRol().getId()==1;
    boolean isAprendiz= user.getRol().getId()==2;
    boolean isAdmin = user.getRol().getId()==3;
%>
<section class="main_container__aside">
    <aside class="BarraIzq">

        <div class="imgContainer">
            <a href="Home.jsp">
                <img src="resources/imagenes/perfilIcon.svg" alt="perfil">
                <p>Home</p>
            </a>
        </div>

        <% if (isGestor) { %>
        <div class="imgContainer">
            <a href="SvCasillero">
                <img src="resources/imagenes/casilleros.svg" alt="casilleros">
                <p>Casillero</p>
            </a>
        </div>
        <%}%>
        <% if (isAdmin) { %>
        <div class="imgContainer">
            <a href="SvCasillero">
                <img src="resources/imagenes/BxsUser.svg" alt="casilleros">
                <p>Usuarios</p>
            </a>
        </div>
        <%}%>
        <% if(isGestor || isAprendiz){%>
        <div class="imgContainer">
            <a href="registros.jsp">
                <img src="resources/imagenes/bookIcoin.svg" alt="registros">
                <p>Registro</p>
            </a>
        </div>
        <% }%>
        <div class="imgContainer">
            <a href="reportes.jsp">
                <img src="resources/imagenes/warningIcon.svg" alt="reportes">
                <p>Reportes</p>
            </a>
        </div>

        <% if (isAdmin){
        %>
        <div class="imgContainer">
            <a href="conf_Sistema.jsp">
                <img src="resources/imagenes/Engranaje.svg" alt="reportes">
                <p>Conf. Sistema</p>
            </a>
        </div>

        <% }
        %>
    </aside>
</section>
