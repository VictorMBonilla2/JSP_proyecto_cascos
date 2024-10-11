<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Registro</title>
    <link rel="stylesheet" href="resources/css/Styles.css">
    <link rel="stylesheet" href="resources/css/register.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link
      href="https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&display=swap"
      rel="stylesheet">
</head>
<body>
<main class="background">
  <section class="Container">
    <h3>Registro</h3>

    <div id="Error" class="Container__error">
      <p>Credenciales incorrectas. Intente nuevamente.</p>
    </div>
    <div id="ErrorOnlydigitos" class="Container__error">
      <p>Ingrese solo números en Documento. Intente nuevamente.</p>
    </div>

    <div class="Inputs_Container">
      <form  class="formulario" id="registro">
        <div class="formulario__inputs">
          <input type="hidden" id="rol" value="Aprendiz">
          <input type="text"  placeholder="Nombres" name="Nombres" id="Nombre" required>
          <input type="text"  placeholder="Apellidos" name="Apellidos" id="Apellido" required>

          <select id="TipoDocumento" name="TipoDocumento">
          </select>

          <input type="text"  placeholder="Numero de Documento" name="documento" id="documento" required>
          <input type="date" id="fecha" placeholder="fechaNacimineto" name="fecha" required>
          <input type="text" id="numeroCelular" placeholder="numeroCelular" name="numeroCelular" required>
          <input type="email" id="email" placeholder="Correo Electronico" name="correo" required>
          <input type="password" id="passWord" placeholder="contraseña" name="password" required>
        </div>
        <button  type="submit" class="formulario__button">Registrarse</button>
      </form>


      <a href="index.jsp" class="Container__login">¿Ya tienes una cuenta?</a>
    </div>
  </section>
</main>
<jsp:include page="resources/confirm.jsp"/>
<jsp:include page="resources/success.jsp"/>
<jsp:include page="resources/error.jsp"/>
<script type="module" src="resources/js/Registro.js"></script>

</body>
</html>
