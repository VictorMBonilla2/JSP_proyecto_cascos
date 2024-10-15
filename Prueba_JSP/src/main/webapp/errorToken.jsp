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
        <div id="logo_icon" class="Container_error__logo">
            <svg xmlns="http://www.w3.org/2000/svg"  viewBox="0 0 20 20"><path d="M11.53 2.3A1.85 1.85 0 0 0 10 1.21A1.85 1.85 0 0 0 8.48 2.3L.36 16.36C-.48 17.81.21 19 1.88 19h16.24c1.67 0 2.36-1.19 1.52-2.64zM11 16H9v-2h2zm0-4H9V6h2z"/></svg>
        </div>
        <h1 class="Container__title">Solititud Invalida</h1>

        <div class="textError">
            <p>Al parecer el enlace es inválido o ya ha sido usado.<br> Intenta crear otra solicitud.</p>
        </div>

        <a href="index.jsp"> <p class="Container__forget">Volver al inicio</p></a>
    </section>

</main>
<script type="module" src="resources/js/recoveryPassword.js"></script>
</body>

</html>
