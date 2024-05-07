<%-- Created by IntelliJ IDEA. User: Propietario Date: 3/05/2024 Time: 11:07 a. m. To change this template use File |
  Settings | File Templates. --%>
  <%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <html>

    <head>
      <title>Inicio</title>
      <link rel="stylesheet" href="resources/css/home.css">
      <link rel="preconnect" href="https://fonts.googleapis.com">
      <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
      <link
        href="https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&display=swap"
        rel="stylesheet">
    </head>

    <body>
      <header class="hea_container">
        <div class="hea_container__logo">
          <img src="resources/imagenes/Logo.png" alt="" class="logo__img">
        </div>
        <div class="hea_container__list">
          
          <ul class="list__buttons">
            <li>Inicio</li>
            <li>Casilleros</li>
            <li>Soporte</li>
          </ul>
      </div>
      </header>



      <main>
        <section class="main_container">

          <section class="main_container__aside">
            <aside class="BarraIzq">
              <img src="resources/imagenes/perfilIcon.svg" alt="perfil" class="BarraIzq__img">
              <img src="resources/imagenes/bookIcoin.svg" alt="libros" class="BarraIzq__img">
              <img src="resources/imagenes/warningIcon.svg" alt="bugs" class="BarraIzq__img">
              <img src="resources/imagenes/salirIcon.svg" alt="log out" class="BarraIzq__img">
            </aside>
          </section>

          <section class="main_container__home">
            <div class="info_user">
              <h1>(nombre)</h1>
              <p>Has iniciado sesíon en: (lugar)</p>
              <p>Ult. sesión activa: (fecha)<br> (hora)</p>
            </div>

            <div class="Graph Graph1" >
              <P> (GRAFICO1)</P>
              <div class="bloque">s</div>
            </div>
            <div class="Graph Graph2" >
              <P> (GRAFICO2)</P>
              <div class="bloque">s</div>
            </div>
            <div class="Graph Graph3">
              <P> (GRAFICO3)</P>
              <div class="bloque">s</div>
            </div>
          </section>
        </section>
      </main>

    </body>

    </html>