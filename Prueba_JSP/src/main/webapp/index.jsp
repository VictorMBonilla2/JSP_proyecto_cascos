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
      <p class="logo__text">Colaborador</p>
    </div>
    <h1 class="Container__title">Iniciar Sesión</h1>

    <div id="Error" class="Container__error">
      <p>Credenciales incorrectas. Intente nuevamente.</p>
    </div>
    <div id="ErrorOnlydigitos" class="Container__error">
      <p>Ingrese solo números en Documento. Intente nuevamente.</p>
    </div>

    <div class="Inputs_Container">
      <form id="Logeo" class="formulario_login">
        <input type="hidden" name="action" value="login">
        <div class="formulario_login__inputs">
          <input type="text" id="documento" placeholder="Documento" name="documento" required>

          <select id="TipoDocumento" name="TipoDocumento">
          </select>

          <input type="password" id="passWord" placeholder="Contraseña" name="password" required>
        </div>
        <button  type="submit" class="formulario_login__button">Iniciar Sesión</button>
      </form>
    </div>
    <p class="Container__forget">¿Has olvidado tu contraseña?</p>

    <a href="registro_aprendices.jsp" class="Container__register">Nuevo Aprendiz</a>

  </section>

</main>
<script src="resources/js/Login.js"></script>
</body>

</html>
