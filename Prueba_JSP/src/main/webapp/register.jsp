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
</head>
<body>
<main>
  <section class="Container">
    <h3>Registro</h3>

    <form action="" method="post">
      <input type="text" id="nombre" placeholder="Nombres" name="nomre" required>
      <input type="text" id="apellido" placeholder="Apellidos" name="apellido" required>
      <select id="pais" name="pais">
        <option value="CdC">Cedula de Ciudadania</option>
        <option value="TdI">Tarjeta de identidad</option>
        <option value="Psp">Pasaporte</option>
      </select>
      <input type="email" id="correo" placeholder="Correo Electrónico" name="correo" required>
      <input type="password" id="passWord" placeholder="contraseña" name="password" required>
      <button type="submit"></button>
    </form>


    <p>¿Ya tienes una cuenta?</p>
  </section>
</main>


</body>
</html>
