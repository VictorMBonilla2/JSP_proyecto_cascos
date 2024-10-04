<%@ page import="Modelo.Persona" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="resources/header.jsp" />
<link rel="stylesheet" href="resources/css/reports.css">
<jsp:include page="resources/sidebar.jsp" />
<%
    HttpSession sesion = request.getSession(false);
    Persona user = null;
    if (sesion != null) {
        user = (Persona) sesion.getAttribute("user");
    }
    if (user == null) {
        response.sendRedirect("index.jsp");
        return;
    }
%>
<input type="hidden" id="idUsuario" value="<%=user.getId()%>">

            <section class="main_container__reportes">

                <div class="reportes__title">
                    <h1>Reportes</h1>
                    <p>Daños, extravios o cualquier irregularidad relacionada al casilleros o cascos se reportara a esta sección. </p>
                </div>
                <div class="reportes__content estilo__contenido">

                </div>

                <!-- MODAL-->

                <script type="module" src="resources/js/reportesCOntent.js"></script>

            </section>

            <!-- Plantilla para los ítems de reporte -->
            <template id="reporte-template">
                <div class="report__item">
                    <p>Documento</p>
                    <p class="item__casillero"></p>
                    <p>Tipo</p>
                    <p class="item__type"></p>
                    <p>Placa</p>
                    <p class="item__placa"></p>
                    <button class="report__button" data-modal-id="">Detalles</button>
                </div>
                <hr class="linea">
            </template>

            <!-- Plantilla para los modales -->
            <template id="modal-template">
                <div class="modal">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h2>Detalles Reporte</h2>
                            <span class="close" data-modal-id="">×</span>
                        </div>
                        <div class="modal-body">
                            <h2></h2>
                            <h3></h3>

                        </div>
                    </div>
                </div>
            </template>


        </main>

<jsp:include page="resources/footer.jsp" />
    </body>
</html>
