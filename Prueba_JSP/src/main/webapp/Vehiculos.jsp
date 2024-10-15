<%@ page import="Modelo.TbVehiculo" %>
<%@ page import="java.util.Set" %>
<%@ page import="Modelo.Persona" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:include page="resources/header.jsp" />
<link rel="stylesheet" href="resources/css/vehiculo.css">
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
    Set<TbVehiculo> vehiculoUser = user.getVehiculos();
    int index = 0;
%>

<jsp:include page="resources/sidebar.jsp" />
<section class="main_container__vehiculo">
    <input type="hidden" id="documentoUser" value="<%=user.getDocumento()%>">
    <div class="container__vehiculo">
        <div class="container__vehiculo__form">
            <form class="vehiculo__form" id="vehiculoForm">
                <input type="hidden" id="typeForm" name="typeForm" value="create">
                <input type="hidden" id="idUser" name="idUser" value="<%=user.getId()%>">
                <input type="hidden" id="idVehiculo" name="idVehiculo" value="">
                <div class="vehiculo__form__header">

                </div>
                <div class="vehiculo__form__header">
                    <h1>Datos del vehiculo</h1>
                    <hr>
                </div>
                <div class="input_grid input_container--vertical">
                    <div class="input_container">
                        <label for="tipoVehiculo">Tipo:</label>
                        <select id="tipoVehiculo" name="tipoVehiculo">
                        </select>
                    </div>
                    <div class="input_container">
                        <label for="marcaVehiculo">Marca:</label>
                        <select type="text" id="marcaVehiculo" name="marcaVehiculo"
                                placeholder="Marca de tu vehiculo"></select>
                    </div>
                    <div class="input_container">
                        <label for="modeloVehiculo">Modelo:</label>
                        <select type="text" id="modeloVehiculo" name="modeloVehiculo"
                                placeholder="Modelo de tu Vehiculo"></select>
                    </div>
                    <div class="input_container">
                        <label for="placaVehiculo">Placa:</label>
                        <input type="text" id="placaVehiculo" name="placaVehiculo"
                                placeholder="Placa de tu vehiculo">
                    </div>
                    <div class="input_container">
                        <label for="ciudadVehiculo">Ciudad:</label>
                        <select type="text" id="ciudadVehiculo" name="ciudadVehiculo"
                                placeholder="Ciudad de tu Vehiculo"></select>
                    </div>
                    <div class="input_container">
                        <label for="colorVehiculo">Color:</label>
                        <select type="text" id="colorVehiculo" name="colorVehiculo"></select>
                    </div>
                    <div class="input_container">
                        <label for="estadoVehiculo">Estado:</label>
                        <select type="text" id="estadoVehiculo" name="estadoVehiculo">
                            <option value="ACTIVO">ACTIVO</option>
                            <option value="INACTIVO">INACTIVO</option>
                        </select>
                    </div>
                </div>
                <hr>
                <div class="input_container input_container--vertical">
                    <label for="cascoConfirm">¿Lleva Casco?</label>
                    <input type="checkbox" id="cascoConfirm" name="placa">
                    <label for="cantCasco">Cantidad:</label>
                    <input type="text" id="cantCasco" name="cascosVehiculo" placeholder="Describe lo que paso">
                </div>
                <div class="button-container child-end">
                    <button class="button_primary accionador" type="submit"
                            id="sendCreate">Crear</button>
                    <button class="button_secundary" type="button" id="cancelEdit">Cancelar</button>
                    <button class="button_secundary button--delete" type="button" id="delete">Eliminar</button>
                </div>
            </form>
        </div>

        <div class="vehiculo__list">
            <div class="vehiculo__list__header">
                <h1>Datos del vehiculo</h1>
                <hr>
            </div>
            <div class="vehiculo__grid">
                <div class="vehiculo__list__item add">
                    <p>Add</p>
                </div>
            </div>
        </div>
    </div>




</section>



</main>
<jsp:include page="resources/confirm.jsp"/>
<jsp:include page="resources/success.jsp"/>
<jsp:include page="resources/error.jsp"/>
    <script type="module" src="resources/js/Vehiculo.js"></script>
    <jsp:include page="resources/footer.jsp" />
    </body>
</html>
