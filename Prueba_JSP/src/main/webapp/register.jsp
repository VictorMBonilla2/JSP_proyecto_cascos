<%--
  Created by IntelliJ IDEA.
  User: Propietario
  Date: 3/05/2024
  Time: 10:50 a. m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Registro</title>
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

    <div class="Inputs_Container">
      <form action="" method="POST" class="formulario">
        <div class="formulario__inputs">
        <input type="text" id="documento" placeholder="Nombres" name="Nombres" required>
        <input type="text" id="documento" placeholder="Apellidos" name="Apellidos" required>
        <select id="pais" name="pais">
          <option value="A+" class="CdC">Cedula de Ciudadania</option>
          <option value="A-" class="TdI">Tarjeta de identidad</option>
          <option value="B" class="Psp">Pasaporte</option>
        </select>
        <input type="text" id="documento" placeholder="Numero de Documento" name="documento" required>
        <input type="email" id="email" placeholder="Correo Electronico" name="documento" required>
        <input type="password" id="passWord" placeholder="contraseña" name="password" required>
        </div>
        <button  type="submit" class="formulario__button">Iniciar Sesión</button>
      </form>


      <a href="index.jsp" class="Container__login">¿Ya tienes una cuenta?</a>

  </section>
</main>


</body>
</html>
