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
            <img src="resources/imagenes/Logo.png" alt="" class="logo__img">
        </div>
        <h1 class="Container__title">Recuperar contraseña</h1>

        <div id="Error" class="Container__error" style="display:none;">
            <p id="errorMessage"></p>
        </div>

        <div class="Inputs_Container">
            <form id="recovery" class="formulario_login">

                <div class="input_container">
                    <input type="email" id="documento" placeholder="Ingresa tu correo" name="correo" required>
                </div>
                <div class="button-container">
                    <button  type="submit" class="button_primary ">Recuperar contraseña</button>
                </div>

            </form>
        </div>
        <a href="index.jsp"> <p class="Container__forget">Volver al inicio</p></a>


    </section>
    <jsp:include page="resources/confirm.jsp"/>
    <jsp:include page="resources/success.jsp"/>
    <jsp:include page="resources/error.jsp"/>
</main>
<script type="module" src="resources/js/recoveryPassword.js"></script>
</body>

</html>
