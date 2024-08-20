<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:include page="resources/header.jsp" />

<jsp:include page="resources/sidebar.jsp" />


<section class="main_container__profile">

    <div class="info_user_container">
        <div class="user_container_side1">
            <div class="info_user_container__header">
                <img src="" alt="">
                <h1><%="Nombre"%></h1>
                <h3><%="Rol"%></h3>
            </div>

            <div class="info_user_container__body">
                <div>
                    <h1><%="hola"%></h1>
                    <h3><%="hola"%></h3>
                </div>
                <div>
                    <h1><%="hola"%></h1>
                    <h3><%="hola"%></h3>
                </div>
                <div>
                    <h1><%="hola"%></h1>
                    <h3><%="hola"%></h3>
                </div>
            </div>

            <button class="formulario_login__button info_user_container__button">Editar perfil</button>
        </div>
        <div class="user_container_side2">
            <h1>Datos de contacto</h1>
            <hr>
            <p>Correo electronico: <%="hola"%></p>
            <p>Número de celular: <%="hola"%></p>
            <button class="formulario_login__button info_user_container__button">Editar datos</button>
        </div>
        </div>

    <div class="detalles_user_container">
        <div class="detalles_user">
            <h1>Documentacion</h1>
            <hr>
            <p>Tipo de documento: <%="hola"%></p>
            <p>Número de documento: <%="hola"%></p>

            <h3>Cualquier tipo de cambio en tu documento debe ser realiado por un administrador!</h3>

        </div>
        <div class="detalles_user">
            <h1>Datos personales</h1>
            <hr>
            <p>Puedes obtener una copia tus datos que nosotros manejamos</p>

            <button class="data_button">Pedir datos</button>

        </div>
        <div class="detalles_user">
            <h1>Borrar cuenta</h1>
            <hr>
            <p>Si piensas borrar definitivamente tu cuenta,  Se perderan todos tus datos.</p>

            <button class="delete_button">Borrar cuenta</button>
        </div>
    </div>
    
</section>
</section>

<script src="resources/js/Profile.js"></script>

</head>
<body>

</body>
</html>
