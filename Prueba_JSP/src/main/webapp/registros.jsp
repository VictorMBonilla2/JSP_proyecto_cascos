<%@ page import="Modelo.TbEspacio" %>
<%@ page import="Modelo.Persona" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="resources/header.jsp" />
<link rel="stylesheet" href="resources/css/registros.css">
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

<section class="main_container__registros">

    <script type="module" src="resources/js/Registros.js"></script>
    <div class="table-container">
        <table>
        </table>
    </div>
    <div id="paginadorHabilitados"></div>
</section>




</main>

<jsp:include page="resources/footer.jsp" />
</body>
</html>
