<%@ page import="Modelo.Persona" %>

<%
    HttpSession sesion = request.getSession();
    Persona user = (Persona) session.getAttribute("user");
    boolean sesionStatus = user != null;
%>
<section class="main_container__aside">
    <aside class="BarraIzq">

        <div class="imgContainer">
            <a href="<%= user.getRol().getId()==1 ? "Home.jsp" : "#" %>">
                <img src="resources/imagenes/perfilIcon.svg" alt="perfil">
                <p>Perfil</p>
            </a>
        </div>

        <% if (user.getRol().getId()==1) { %>
        <div class="imgContainer">
            <a href="SvCasillero">
                <img src="resources/imagenes/casilleros.svg" alt="casilleros">
                <p>Casillero</p>
            </a>
        </div>
        <% } %>

        <div class="imgContainer">
            <a href="registros.jsp">
                <img src="resources/imagenes/bookIcoin.svg" alt="registros">
                <p>Registro</p>
            </a>
        </div>

        <div class="imgContainer">
            <a href="reportes.jsp">
                <img src="resources/imagenes/warningIcon.svg" alt="reportes">
                <p>Reportes</p>
            </a>
        </div>
    </aside>
</section>
