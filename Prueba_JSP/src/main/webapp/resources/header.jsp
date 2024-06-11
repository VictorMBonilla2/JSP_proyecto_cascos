<html>

<head>
    <meta charset="UTF-8">
    <title>Inicio</title>
    <link rel="stylesheet" href="resources/css/Styles.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Ubuntu:ital,wght@0,300;0,400;0,500;0,700;1,300;1,400;1,500;1,700&display=swap" rel="stylesheet">

</head>

<body>
    <header class="hea_container">
        <div class="hea_container__logo">
            <img src="resources/imagenes/Logo.png" alt="" class="logo__img">
        </div>
        <div class="hea_container__list">

            <ul class="list__buttons">
                <li><a href="profile.jsp">Inicio</a></li>
                <li><a href="SvCasillero">Casilleros</a></li>
                <li><a href="soporte.html">Soporte</a></li>
            </ul>
        </div>
        <button id="botonMostrar">Mostrar Sidebar</button>

        <div id="right-sidebar">
            <div id="contenedorIcono" class="escondido">
                <div class="user">
                    <img src="" class="user_class">
                    <h3>(nombre)</h3>
                    <p>(rol)</p>
                    <span class="close-button" modal-id="modal">&times;</span>
                </div>
                <hr class="linea">
                <div class="ui-menu">
                    <a class="ui-menu__option">
                        <img src="">
                        <p>Cerrar Sesión</p>
                    </a>
                </div>
            </div>
        </div>
    </header>

    <main>
        <section class="main_container">