<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
  <!DOCTYPE html>
  <html>

  <head>
    <title>Login</title>
    ta
    <link rel="stylesheet" href="src/main/resources/css/Login.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link
      href="https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&display=swap"
      rel="stylesheet">
  </head>

  <body>

    
    <main>
      <section class="Container">
        <div class="logo">
          <img src="" alt="" class="logo__img">
          <p class="logo__text">Helmet Masters</p>
        </div>
        <h1 class="Container__title">Iniciar Sesión</h1>

        <div class="Inputs_Container">
          <form action="" method="POST">
            <input type="text" id="documento" placeholder="documento" name="documento" required>
            
            <select id="pais" name="pais">
              <option value="A+">Cedula de Ciudadania</option>
              <option value="A-">Tarjeta de identidad</option>
              <option value="B">Pasaporte</option>
            </select>
            <input type="password" id="passWord" placeholder="contraseña" name="password" required>
            <button type="submit"></button>
          </form>

          <p>¿Has olvidado tu contraseña</p>

          <p>Nuevo Colaborador</p>

        </div>


      </section>


    </main>

    <h1>
      <%= "Hello World!" %>
    </h1>
    <br />
    <a href="hello-servlet">Hello Servlet</a>

  </body>

  </html>