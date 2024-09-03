<%@ page import="Modelo.Persona" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="resources/header.jsp" />

<jsp:include page="resources/sidebar.jsp" />

<%
    HttpSession sesion = request.getSession();
    Persona user = (Persona) session.getAttribute("user");
%>
<section class="main_container__conf_Sistema">



</section>
</head>
<body>

</body>
</html>
