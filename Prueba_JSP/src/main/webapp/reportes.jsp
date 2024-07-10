<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="resources/header.jsp" />

<jsp:include page="resources/sidebar.jsp" />

            <section class="main_container__reportes">

                <div class="reportes__title">
                    <h1>Reportes</h1>
                    <p>Daños, extravios o cualquier irregularidad relacionada al casilleros o cascos se reportara a esta sección. </p>
                </div>
                <div class="reportes__content estilo__contenido">
                    <div class="reportes__fecha estilo__casillero">
                        <p>(FECHA)</p>
                    </div>
                    <div class="report__item">
                        <p>Casillero</p>
                        <p class="item__casillero">(id_casillero)</p>
                        <p>Tipo</p>
                        <p class="item__type">(tipo__reporte)</p>
                        <p>Placa</p>
                        <p class="item__placa">(tipo__reporte)</p>
                        <button class="report__button" report="report1">Detalles</button>
                    </div>
                    <hr class="linea">
                    <div class="report__item">
                        <p>Casillero</p>
                        <p class="item__casillero">(id_casillero)</p>
                        <p>Tipo</p>
                        <p class="item__type">(tipo__reporte)</p>
                        <p>Placa</p>
                        <p class="item__placa">(tipo__reporte)</p>
                        <button class="report__button"  report="report2">Detalles</button>
                    </div>
                    <hr class="linea">
                </div>


                <!-- MODAL-->
                <div id="report1" class="modal">
                    <div class="modal-content">
                        <div class="modal-header">

                            <h2>Detalles Reporte 1</h2>
                            <span class="close" data-modal-id="report1">&times;</span>
                        </div>
                        <div class="modal-body">

                            <h3>ESTE ES LA DESCRIPCION DEL REPORTE 1</h3>
                        </div>
                    </div>
                </div>
                <div id="report2" class="modal">
                    <div class="modal-content">
                        <div class="modal-header">

                            <h2>Detalles Reporte 1</h2>
                            <span class="close" data-modal-id="report2">&times;</span>
                        </div>
                        <div class="modal-body">

                            <h3>ESTE ES LA DESCRIPCION DEL REPORTE 2</h3>
                        </div>
                    </div>
                </div>
                <script src="resources/js/reportes.js"></script>
            </section>




        </main>
<jsp:include page="resources/footer.jsp" />
    </body>
</html>
