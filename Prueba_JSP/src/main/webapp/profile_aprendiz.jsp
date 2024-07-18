<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:include page="resources/header_aprendiz.jsp" />

<jsp:include page="resources/sidebar_aprendiz.jsp" />

<section class="main_container__home">
    <div class="info_user">
        <h1>Bienvenido (nombre)</h1>
        <div class="content">
            <div class="vehiculo__content_data">

                <h1>Datos vehiculo</h1>
                <hr class="linea">
                <div class="vehiculo_data">
                    <div class="vehiculo_data__text">
                        <h1>Placa</h1>
                        <p>HJF-485</p>
                    </div>
                    <div class="vehiculo_data__text">
                        <h1>Hyundai</h1>
                        <p>HJF-485</p>
                    </div>
                    <div class="vehiculo_data__text">
                        <h1>Modelo</h1>
                        <p>XRZ-6000</p>
                    </div>
                </div>
                <div class="adicional_vehiculo_data">
                    <h1>Datos Adicionales</h1>
                    <hr class="linea">
                    <div class="vehiculo_data__text">
                        <h1>¿Lleva casco?</h1>
                        <p>Si</p>
                    </div>
                    <div class="vehiculo_data__text">
                        <h1>Cantidad</h1>
                        <p>1</p>
                    </div>
                </div>
            </div>

        </div>
    </div>
    <div class="Graph1" >
        <div class="bloque__user">
            <div class="info2-user__img">
                <img src="resources/imagenes/IconPerfil.png" alt="icono">
            </div>

            <div class="info2-user__container">
                <div class="info2-user__text">
                    <h1>Nombre</h1>
                    <p> Jose</p>
                </div>
                <div class="info2-user__text">
                    <h1>Apellido</h1>
                    <p> Antonio G.</p>
                </div>
                <div class="info2-user__text">
                    <h1>Tipo Doc</h1>
                    <p> ???</p>
                </div>
                <div class="info2-user__text">
                    <h1>num Doc</h1>
                    <p> 0485485</p>
                </div>
                <div class="info2-user__text">
                    <h1>fecha nac</h1>
                    <p> 17/35/1995</p>
                </div>
            </div>
        </div>
    </div>
    <div class="Graph2" >
        <div class="bloque__sesion">
            <h1>Has iniciado sesíon en: (lugar)</h1>
            <p>Ult. sesión activa: (fecha)<br> (hora)</p>
        </div>
    </div>
</section>
</section>
</main>
<jsp:include page="resources/footer.jsp" />
</body>

</html>