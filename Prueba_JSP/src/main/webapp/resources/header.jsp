<%
    HttpSession sesion = request.getSession(false);
    Persona user = null;
    if (sesion != null) {
        user = (Persona) sesion.getAttribute("user");
    }
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    Roles roles= user.getRol();
    boolean isGestor = roles.getId()==1;
    boolean isAprendiz= roles.getId()==2;
    boolean isAdmin = roles.getId()==3;

%>
<html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="Modelo.Persona" %>
<%@ page import="Modelo.Roles" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<head>
    <meta charset="UTF-8">
    <title>Inicio</title>
    <link rel="stylesheet" href="resources/css/Styles.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Ubuntu:ital,wght@0,300;0,400;0,500;0,700;1,300;1,400;1,500;1,700&display=swap" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>

<body>

    <%
    if (isGestor || isAdmin){
%>


<header class="hea_container">
    <div class="hea_container__logo">
            <svg xmlns:inkscape="http://www.inkscape.org/namespaces/inkscape" xmlns:sodipodi="http://sodipodi.sourceforge.net/DTD/sodipodi-0.dtd"
                 xmlns="http://www.w3.org/2000/svg" version="1.1" id="svg12" sodipodi:docname="SENA logo.svg" x="0px" y="0px" viewBox="0 0 100 98" style="enable-background:new 0 0 100 98;" xml:space="preserve">
                <sodipodi:namedview bordercolor="#666666" borderopacity="1" gridtolerance="1" guidetolerance="1" id="namedview14" inkscape:current-layer="svg12" inkscape:cx="96.265104" inkscape:cy="56.747294" inkscape:pageopacity="0" inkscape:pageshadow="2" inkscape:showpageshadow="false" inkscape:window-height="705" inkscape:window-maximized="1" inkscape:window-width="1366" inkscape:window-x="-8" inkscape:window-y="-8" inkscape:zoom="1" objecttolerance="1" pagecolor="#ffffff" showgrid="false" units="px">
                <inkscape:grid id="grid58" type="xygrid"/>
                </sodipodi:namedview>
                <g id="_x23_ffffffff" transform="translate(0,-130.5)">
                </g>
                <path id="path47-5" inkscape:connector-curvature="0" d="M50.3,1c-5.9,0-10.6,4.8-10.6,10.6c0,5.9,4.8,10.6,10.6,10.6  c5.9,0,10.6-4.8,10.6-10.6v0C60.9,5.8,56.2,1,50.3,1z M15.3,25.6c-1.9,0-3.8,0.1-5.5,0.6c-1.2,0.3-2.3,0.8-3,1.5  c-0.9,1-1,2.2-0.6,3.3c0.4,1,1.5,1.7,2.7,2.1c2.6,0.9,5.5,1.1,8.2,1.6c0.5,0.1,1.1,0.3,1.4,0.6c0.3,0.4,0.1,1-0.4,1.2  c-0.9,0.5-2,0.5-3.1,0.4c-0.9,0-2-0.1-2.7-0.6c-0.6-0.3-0.6-0.9-0.5-1.4l-6.1,0c0,0.9,0.2,1.9,0.8,2.7c0.6,0.7,1.5,1.2,2.5,1.4  c1.6,0.5,3.3,0.6,5,0.6c2.3,0,4.6,0,6.8-0.5c1.3-0.3,2.6-0.8,3.4-1.7c1.5-1.5,1.1-3.9-0.8-5c-1-0.6-2.2-0.9-3.4-1.2  c-1.8-0.4-3.5-0.6-5.3-0.9c-0.6-0.1-1.3-0.2-1.8-0.5c-0.5-0.3-0.6-1,0-1.3c0.7-0.4,1.7-0.4,2.6-0.4c0.9,0,1.9,0.1,2.7,0.5  c0.4,0.2,0.6,0.6,0.6,1l5.8,0c0-0.7-0.2-1.5-0.7-2.1c-0.6-0.8-1.7-1.3-2.8-1.6C19,25.7,17.1,25.6,15.3,25.6L15.3,25.6z M27.8,26  l0,13.8l16.9,0l0-3H34v-2.7h9.5v-2.9H34l0-2.2l10.3,0l0-3L27.8,26z M55.7,26c0,0-5.2,0-7.8,0l0,13.8l5.9,0l0-9.3l8.1,9.3l8.1,0  L70,26l-5.9,0l0,9.2L55.7,26z M80.6,26c0,0-6.4,9.2-9.6,13.8l6.2,0l1.5-2.5h9.6l1.4,2.5l6.9,0L87.5,26L80.6,26z M83.7,29.3l3,5  l-6.1,0L83.7,29.3z M0.7,43.8l0.1,7.5l28.2-0.1c1.4,0.3,2.3,1.2,2,3.4L13.6,84.9l5.6,5.3L46,43.8L0.7,43.8z M54.4,43.8L80.8,90  l5.8-5.2L69.1,54.5c-0.3-2.1,0.5-3.1,2-3.4l28.2,0.1l0-7.4L54.4,43.8z M50,51.5L25.2,93.9l6.6,3.2l16.5-27.9  c0.6-0.5,1.1-0.7,1.7-0.7c0.6,0,1.2,0.2,1.8,0.7l16.5,28l6.8-3.5L50,51.5z"/>
                <g id="_x23_000000ff-2" transform="matrix(0.31570611,0,0,0.23560774,-391.49698,-10.601126)">
                </g>
            </svg>
    </div>
    <div class="hea_container__list">

        <ul class="list__buttons">
            <li><a href="Home.jsp">Inicio</a></li>
            <li><a href="Casilleros.jsp">Casilleros</a></li>
        </ul>
        <button id="botonMostrar" class="perfil-button">
            <img src="resources/imagenes/iconProfile.png">
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
                <span class="close-button" modal-id="modal">&times;</span>
                <div class="right-sidebar__infoUser">
                    <h3><%= user.getNombre()%> </h3>
                    <p><%= user.getRol().getNombre()%></p>
                </div>
                <hr class="linea">
            </div>

            <div class="ui-menu">
                <a href="Profile.jsp" class="ui-menu__option">
                    <svg xmlns="http://www.w3.org/2000/svg"  viewBox="0 0 24 24"><g fill="none" stroke="#000000" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"><path d="m20.35 8.923l-.366-.204l-.113-.064a2 2 0 0 1-.67-.66c-.018-.027-.034-.056-.066-.112a2 2 0 0 1-.3-1.157l.006-.425c.012-.68.018-1.022-.078-1.328a2 2 0 0 0-.417-.736c-.214-.24-.511-.412-1.106-.754l-.494-.285c-.592-.341-.889-.512-1.204-.577a2 2 0 0 0-.843.007c-.313.07-.606.246-1.191.596l-.003.002l-.354.211c-.056.034-.085.05-.113.066c-.278.155-.588.24-.907.25c-.032.002-.065.002-.13.002l-.13-.001a2 2 0 0 1-.91-.252c-.028-.015-.055-.032-.111-.066l-.357-.214c-.589-.354-.884-.53-1.199-.601a2 2 0 0 0-.846-.006c-.316.066-.612.238-1.205.582l-.003.001l-.488.283l-.005.004c-.588.34-.883.512-1.095.751a2 2 0 0 0-.415.734c-.095.307-.09.649-.078 1.333l.007.424c0 .065.003.097.002.128a2 2 0 0 1-.301 1.027c-.033.056-.048.084-.065.11a2 2 0 0 1-.675.664l-.112.063l-.361.2c-.602.333-.903.5-1.121.738a2 2 0 0 0-.43.73c-.1.307-.1.65-.099 1.338l.002.563c.001.683.003 1.024.104 1.329a2 2 0 0 0 .427.726c.218.236.516.402 1.113.734l.358.199c.061.034.092.05.121.068a2 2 0 0 1 .74.781l.067.12a2 2 0 0 1 .23 1.038l-.007.407c-.012.686-.017 1.03.079 1.337c.085.272.227.523.417.736c.214.24.512.411 1.106.754l.494.285c.593.341.889.512 1.204.577a2 2 0 0 0 .843-.007c.314-.07.607-.246 1.194-.598l.354-.212l.113-.066c.278-.154.588-.24.907-.25l.13-.001h.13c.318.01.63.097.91.252l.092.055l.376.226c.59.354.884.53 1.199.6a2 2 0 0 0 .846.008c.315-.066.613-.239 1.206-.583l.495-.287c.588-.342.883-.513 1.095-.752c.19-.213.33-.463.415-.734c.095-.305.09-.644.078-1.318l-.008-.44v-.127a2 2 0 0 1 .3-1.028l.065-.11a2 2 0 0 1 .675-.664l.11-.061l.002-.001l.361-.2c.602-.334.903-.5 1.122-.738c.194-.21.34-.46.429-.73c.1-.305.1-.647.098-1.327l-.002-.574c-.001-.683-.002-1.025-.103-1.33a2 2 0 0 0-.428-.725c-.217-.236-.515-.402-1.111-.733z"/><path d="M8 12a4 4 0 1 0 8 0a4 4 0 0 0-8 0"/></g></svg>
                    <p>Configuración</p>
                </a>
                <a href="#" id="logout-link" class="ui-menu__option">
                    <svg xmlns="http://www.w3.org/2000/svg"  viewBox="0 0 512 512"><path fill="#000000" d="M336 376V272H191a16 16 0 0 1 0-32h145V136a56.06 56.06 0 0 0-56-56H88a56.06 56.06 0 0 0-56 56v240a56.06 56.06 0 0 0 56 56h192a56.06 56.06 0 0 0 56-56m89.37-104l-52.68 52.69a16 16 0 0 0 22.62 22.62l80-80a16 16 0 0 0 0-22.62l-80-80a16 16 0 0 0-22.62 22.62L425.37 240H336v32Z"/></svg>
                    <p>Cerrar Sesión </p>
                </a>
                <script src="resources/js/logout.js"></script>
            </div>
        </div>
    </div>
</header>

<main>
    <section class="main_container">
            <%
    } else if (isAprendiz) {
        System.out.println("Es un header de Aprendiz");
%>

        <header class="hea_container">
            <div class="hea_container__logo">
                <svg xmlns:inkscape="http://www.inkscape.org/namespaces/inkscape"
                     xmlns:sodipodi="http://sodipodi.sourceforge.net/DTD/sodipodi-0.dtd"
                     xmlns="http://www.w3.org/2000/svg" version="1.1" id="svg12" sodipodi:docname="SENA logo.svg"
                     x="0px" y="0px" viewBox="0 0 100 98" style="enable-background:new 0 0 100 98;"
                     xml:space="preserve">
                <sodipodi:namedview bordercolor="#666666" borderopacity="1" gridtolerance="1" guidetolerance="1" id="namedview14" inkscape:current-layer="svg12" inkscape:cx="96.265104" inkscape:cy="56.747294" inkscape:pageopacity="0" inkscape:pageshadow="2" inkscape:showpageshadow="false" inkscape:window-height="705" inkscape:window-maximized="1" inkscape:window-width="1366" inkscape:window-x="-8" inkscape:window-y="-8" inkscape:zoom="1" objecttolerance="1" pagecolor="#ffffff" showgrid="false" units="px">
                    <inkscape:grid id="grid58" type="xygrid"/>
                </sodipodi:namedview>
                    <g id="_x23_ffffffff" transform="translate(0,-130.5)">
                    </g>
                    <path id="path47-5" inkscape:connector-curvature="0" d="M50.3,1c-5.9,0-10.6,4.8-10.6,10.6c0,5.9,4.8,10.6,10.6,10.6  c5.9,0,10.6-4.8,10.6-10.6v0C60.9,5.8,56.2,1,50.3,1z M15.3,25.6c-1.9,0-3.8,0.1-5.5,0.6c-1.2,0.3-2.3,0.8-3,1.5  c-0.9,1-1,2.2-0.6,3.3c0.4,1,1.5,1.7,2.7,2.1c2.6,0.9,5.5,1.1,8.2,1.6c0.5,0.1,1.1,0.3,1.4,0.6c0.3,0.4,0.1,1-0.4,1.2  c-0.9,0.5-2,0.5-3.1,0.4c-0.9,0-2-0.1-2.7-0.6c-0.6-0.3-0.6-0.9-0.5-1.4l-6.1,0c0,0.9,0.2,1.9,0.8,2.7c0.6,0.7,1.5,1.2,2.5,1.4  c1.6,0.5,3.3,0.6,5,0.6c2.3,0,4.6,0,6.8-0.5c1.3-0.3,2.6-0.8,3.4-1.7c1.5-1.5,1.1-3.9-0.8-5c-1-0.6-2.2-0.9-3.4-1.2  c-1.8-0.4-3.5-0.6-5.3-0.9c-0.6-0.1-1.3-0.2-1.8-0.5c-0.5-0.3-0.6-1,0-1.3c0.7-0.4,1.7-0.4,2.6-0.4c0.9,0,1.9,0.1,2.7,0.5  c0.4,0.2,0.6,0.6,0.6,1l5.8,0c0-0.7-0.2-1.5-0.7-2.1c-0.6-0.8-1.7-1.3-2.8-1.6C19,25.7,17.1,25.6,15.3,25.6L15.3,25.6z M27.8,26  l0,13.8l16.9,0l0-3H34v-2.7h9.5v-2.9H34l0-2.2l10.3,0l0-3L27.8,26z M55.7,26c0,0-5.2,0-7.8,0l0,13.8l5.9,0l0-9.3l8.1,9.3l8.1,0  L70,26l-5.9,0l0,9.2L55.7,26z M80.6,26c0,0-6.4,9.2-9.6,13.8l6.2,0l1.5-2.5h9.6l1.4,2.5l6.9,0L87.5,26L80.6,26z M83.7,29.3l3,5  l-6.1,0L83.7,29.3z M0.7,43.8l0.1,7.5l28.2-0.1c1.4,0.3,2.3,1.2,2,3.4L13.6,84.9l5.6,5.3L46,43.8L0.7,43.8z M54.4,43.8L80.8,90  l5.8-5.2L69.1,54.5c-0.3-2.1,0.5-3.1,2-3.4l28.2,0.1l0-7.4L54.4,43.8z M50,51.5L25.2,93.9l6.6,3.2l16.5-27.9  c0.6-0.5,1.1-0.7,1.7-0.7c0.6,0,1.2,0.2,1.8,0.7l16.5,28l6.8-3.5L50,51.5z"/>
                    <g id="_x23_000000ff-2" transform="matrix(0.31570611,0,0,0.23560774,-391.49698,-10.601126)">
                    </g>
            </svg>
            </div>
            <div class="hea_container__list">

                <ul class="list__buttons">
                    <li><a href="Home.jsp">Inicio</a></li>
                    <li><a href="Vehiculos.jsp">Vehiculo</a></li>
                </ul>
                <button id="botonMostrar" class="perfil-button">
                    <img src="resources/imagenes/iconProfile.png">
                </button>
            </div>
            <style>
                .perfil-button{
                    border: none;
                    padding: 0;
                    background: none;
                    cursor: pointer;
                }
            </style>

            <% %>

            <div id="right-sidebar">
                <div id="contenedorIcono" class="escondido">
                    <div class="user">
                        <span class="close-button" modal-id="modal">&times;</span>
                        <div class="right-sidebar__infoUser">
                            <h3><%= user.getNombre()%> </h3>
                            <p><%= user.getRol().getNombre()%></p>
                        </div>
                        <hr class="linea">
                    </div>

                    <div class="ui-menu">
                        <a href="Profile.jsp" class="ui-menu__option">
                            <svg xmlns="http://www.w3.org/2000/svg"  viewBox="0 0 24 24"><g fill="none" stroke="#000000" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"><path d="m20.35 8.923l-.366-.204l-.113-.064a2 2 0 0 1-.67-.66c-.018-.027-.034-.056-.066-.112a2 2 0 0 1-.3-1.157l.006-.425c.012-.68.018-1.022-.078-1.328a2 2 0 0 0-.417-.736c-.214-.24-.511-.412-1.106-.754l-.494-.285c-.592-.341-.889-.512-1.204-.577a2 2 0 0 0-.843.007c-.313.07-.606.246-1.191.596l-.003.002l-.354.211c-.056.034-.085.05-.113.066c-.278.155-.588.24-.907.25c-.032.002-.065.002-.13.002l-.13-.001a2 2 0 0 1-.91-.252c-.028-.015-.055-.032-.111-.066l-.357-.214c-.589-.354-.884-.53-1.199-.601a2 2 0 0 0-.846-.006c-.316.066-.612.238-1.205.582l-.003.001l-.488.283l-.005.004c-.588.34-.883.512-1.095.751a2 2 0 0 0-.415.734c-.095.307-.09.649-.078 1.333l.007.424c0 .065.003.097.002.128a2 2 0 0 1-.301 1.027c-.033.056-.048.084-.065.11a2 2 0 0 1-.675.664l-.112.063l-.361.2c-.602.333-.903.5-1.121.738a2 2 0 0 0-.43.73c-.1.307-.1.65-.099 1.338l.002.563c.001.683.003 1.024.104 1.329a2 2 0 0 0 .427.726c.218.236.516.402 1.113.734l.358.199c.061.034.092.05.121.068a2 2 0 0 1 .74.781l.067.12a2 2 0 0 1 .23 1.038l-.007.407c-.012.686-.017 1.03.079 1.337c.085.272.227.523.417.736c.214.24.512.411 1.106.754l.494.285c.593.341.889.512 1.204.577a2 2 0 0 0 .843-.007c.314-.07.607-.246 1.194-.598l.354-.212l.113-.066c.278-.154.588-.24.907-.25l.13-.001h.13c.318.01.63.097.91.252l.092.055l.376.226c.59.354.884.53 1.199.6a2 2 0 0 0 .846.008c.315-.066.613-.239 1.206-.583l.495-.287c.588-.342.883-.513 1.095-.752c.19-.213.33-.463.415-.734c.095-.305.09-.644.078-1.318l-.008-.44v-.127a2 2 0 0 1 .3-1.028l.065-.11a2 2 0 0 1 .675-.664l.11-.061l.002-.001l.361-.2c.602-.334.903-.5 1.122-.738c.194-.21.34-.46.429-.73c.1-.305.1-.647.098-1.327l-.002-.574c-.001-.683-.002-1.025-.103-1.33a2 2 0 0 0-.428-.725c-.217-.236-.515-.402-1.111-.733z"/><path d="M8 12a4 4 0 1 0 8 0a4 4 0 0 0-8 0"/></g></svg>
                            <p>Configuración</p>
                        </a>
                        <a href="#" id="logout-link" class="ui-menu__option">
                            <svg xmlns="http://www.w3.org/2000/svg"  viewBox="0 0 512 512"><path fill="#000000" d="M336 376V272H191a16 16 0 0 1 0-32h145V136a56.06 56.06 0 0 0-56-56H88a56.06 56.06 0 0 0-56 56v240a56.06 56.06 0 0 0 56 56h192a56.06 56.06 0 0 0 56-56m89.37-104l-52.68 52.69a16 16 0 0 0 22.62 22.62l80-80a16 16 0 0 0 0-22.62l-80-80a16 16 0 0 0-22.62 22.62L425.37 240H336v32Z"/></svg>
                            <p>Cerrar Sesión </p>
                        </a>
                        <script src="resources/js/logout.js"></script>
                    </div>
                </div>
            </div>
        </header>

        <main>
            <section class="main_container">
<%}%>