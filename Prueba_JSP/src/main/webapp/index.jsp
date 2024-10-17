<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>

<head>
  <title>Logín</title>
  <link rel="stylesheet" href="resources/css/Styles.css">
  <link rel="stylesheet" href="resources/css/login.css">
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link
          href="https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&display=swap"
          rel="stylesheet">
</head>

<body>

<main class="background_login">
  <section class="Container_login">
    <div id="logo_icon" class="Container_login__logo">
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
    <h1 class="Container__title">Iniciar Sesión</h1>

    <div id="Error" class="Container__error" style="display:none;">
      <p id="errorMessage"></p>
    </div>

  <div class="Inputs_Container">
    <form id="Logeo" class="formulario_login">
      <input type="hidden" name="action" value="login">
      <div class="formulario_login__inputs">
        <input type="text" id="documento" placeholder="Documento" name="documento" required>

        <select id="TipoDocumento" name="TipoDocumento">
        </select>

        <input type="password" id="passWord" placeholder="Contraseña" name="password" required autocomplete="current-password">

      </div>
      <div class="button_container">
        <button  type="submit" id="submitButton" class="button_primary">Iniciar Sesión</button>
      </div>

    </form>
  </div>
    <a href="forgetPassword.jsp"> <p class="Container__forget">¿Has olvidado tu contraseña?</p></a>


    <a href="registro_aprendices.jsp" class="Container__register">Nuevo Aprendiz</a>

  </section>
  <jsp:include page="resources/spinner.jsp"/>
</main>
<script type="module" src="resources/js/Login.js"></script>
</body>

</html>
