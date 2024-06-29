
<html>
<%@ page import="Modelo.Persona" %>
<head>
    <meta charset="UTF-8">
    <title>Inicio</title>
    <link rel="stylesheet" href="resources/css/Styles.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Ubuntu:ital,wght@0,300;0,400;0,500;0,700;1,300;1,400;1,500;1,700&display=swap" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <script src="resources/js/VerificarSesion.js"></script>
    <script src="resources/js/logout.js"></script>


</head>

<body>
        <%
            HttpSession sesion = request.getSession();
            Persona user = (Persona) session.getAttribute("user");
            boolean sesionStatus = user != null;

            if (!sesionStatus) {
                response.sendRedirect("index.jsp");
                return;
            }
        %>
             <input type="hidden" id="sesionStatus" value="<%= sesionStatus%>" />

    <header class="hea_container">
        <div class="hea_container__logo">
            <img src="resources/imagenes/Logo.png" alt="" class="logo__img">
        </div>
        <div class="hea_container__list">

            <ul class="list__buttons">
                <li><a href="profile.jsp">Inicio</a></li>
                <li><a href="SvCasillero">Casilleros</a></li>
            </ul>
            <button id="botonMostrar" class="perfil-button">
                <img src="resources/imagenes/IconPerfil.png">
            </button>
        </div>
        <style>
            .perfil-button{
                border: none; /* Eliminar borde del botón */
                padding: 0; /* Eliminar padding del botón */
                background: none; /* Sin fondo para el botón */
                cursor: pointer; /* Cambiar el cursor al pasar sobre el botón */
            }

        </style>
        <% %>

        <div id="right-sidebar">
            <div id="contenedorIcono" class="escondido">
                <div class="user">
                    <img src="" class="user_class">
                    <h3><%= user.getNombre()%> </h3>
                    <p><%= user.getRol()%></p>
                    <span class="close-button" modal-id="modal">&times;</span>
                </div>
                <hr class="linea">
                <div class="ui-menu">

                    <a class="ui-menu__option">
                        <img src="">
                        <p>Cerrar Sesión</p>
                    </a>
                    <form id="logout" action="SvPersona" method="POST" class="formulario">
                        <input type="hidden" id="action" name="action" value="logout">
                        <button type="submit" class="formulario__button">Cerrar Sesión</button>
                    </form>


                </div>
            </div>
        </div>
    </header>

    <main>
        <section class="main_container">