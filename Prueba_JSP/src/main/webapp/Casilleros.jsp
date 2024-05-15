<%--
  Created by IntelliJ IDEA.
  User: Propietario
  Date: 3/05/2024
  Time: 11:19 a. m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Casilleros</title>
    <link rel="stylesheet" href="resources/css/casilleros.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&display=swap" rel="stylesheet">

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

            <section class="main_container__casillero" id="casillerosContainer">
                <% for (int i=1; i<=4; i++) { %>
                <div class="casillero">
                    <div class="casillero__title">
                        <h1>(PLACA)</h1>
                        <p>lugar</p>
                    </div>

                    <div class="casillero__contenido">
                        <div class="contenido__info">
                            <div class="info__casillero">
                                <p>Casillero</p>
                                <p>(# Casillero)</p>
                            </div>
                            <div class="info__tiempo">
                                <p>Tiempo</p>
                                <p>(tiempo)</p>
                            </div>
                            <div class="info__costo">
                                <p>Costo</p>
                                <p>(costo)</p>
                            </div>
                        </div>
                        <div class="contenido__botones">
                            <button class="botones botones__pago">
                                Pagar
                            </button>
                            <button class="botones botones__ajustar">
                                Ajustar
                            </button>
                        </div>
                    </div>
                </div>
                <% } %>

                <div class="casillero">


                </div>
                <div class="casillero">


                </div>
                <div class="casillero">


                </div>
                <div class="casillero">


                </div>
                <div class="casillero">


                </div>
                <div class="casillero">


                </div>




            </section>
        </section>
    </main>

</body>

</html>
