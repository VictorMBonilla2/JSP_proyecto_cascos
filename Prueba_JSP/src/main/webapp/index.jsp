

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
  <!DOCTYPE html>
  <html>

  <head>
    <title>Login</title>
    <link rel="stylesheet" href="resources/css/login.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link
      href="https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&display=swap"
      rel="stylesheet">
  </head>

  <body>

    <main class="background">
      <section class="Container">
        <div class="Container__logo">
          <img src="resources/imagenes/Logo.png" alt="" class="logo__img">
          <p class="logo__text">Helmet Masters</p>
        </div>
        <h1 class="Container__title">Iniciar Sesión</h1>

        <div class="Inputs_Container">
          <form action="SvPersona" method="POST" class="formulario">
            <input type="hidden" name="action" value="login">
            <div class="formulario__inputs">

            <input type="text" id="documento" placeholder="Documento" name="documento" required>

            <select id="TipDocument" name="TipoDocumento">
              <option value="Cedula de Ciudadania" class="CdC">Cedula de Ciudadania</option>
              <option value="Tarjeta de identidad" class="TdI">Tarjeta de identidad</option>
              <option value="Pasaporte" class="Psp">Pasaporte</option>
            </select>

            <input type="password" id="passWord" placeholder="Contraseña" name="password" required>
            </div>
            <button  type="submit" class="formulario__button">Iniciar Sesión</button>
          </form>

          
        </div>
        <p class="Container__forget">¿Has olvidado tu contraseña?</p>

        <a href="register.jsp" class="Container__register">Nuevo Colaborador</a>


      </section>


    </main>

  </body>

  </html>