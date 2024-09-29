<%@ page import="Modelo.TbVehiculo" %>
<%@ page import="java.util.Set" %>
<%@ page import="Modelo.Persona" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:include page="resources/header.jsp" />
<%
    HttpSession sesion = request.getSession();
    Persona user = (Persona) session.getAttribute("user");
    Set<TbVehiculo> vehiculoUser = user.getVehiculos();
    int index = 0;
%>

<jsp:include page="resources/sidebar.jsp" />
<section class="main_container__vehiculo">

    <div class="container__vehiculo">
        <div class="container__vehiculo__form">

            <form class="vehiculo__form" id="formVehiculos">
                <input type="hidden" id="documentoUser" value="2">
                <input type="hidden" id="idVehiculo" value="">
                <div class="vehiculo__form__header">

                </div>
                <div class="vehiculo__form__header">

                    <h1>Datos del vehiculo</h1>
                    <hr>
                </div>
                <div class="input_grid input_container--vertical">
                    <div class="input_container">
                        <label for="tipoVehiculo">Tipo:</label>
                        <select id="tipoVehiculo" name="options">
                            <option value="1">Carro</option>
                            <option value="2">Moto</option>
                            <option value="3">Bicicleta</option>
                        </select>
                    </div>
                    <div class="input_container">
                        <label for="placaVehiculo">Placa:</label>
                        <input type="text" id="placaVehiculo" name="placa"
                               placeholder="Placa de tu vehiculo">
                    </div>
                    <div class="input_container">
                        <label for="marcaVehiculo">Marca:</label>
                        <input type="text" id="marcaVehiculo" name="marca"
                               placeholder="Marca de tu vehiculo">
                    </div>
                    <div class="input_container">
                        <label for="modeloVehiculo">Modelo:</label>
                        <input type="text" id="modeloVehiculo" name="modelo"
                               placeholder="Modelo de tu Vehiculo">
                    </div>
                    <div class="input_container">
                        <label for="ciudadVehiculo">Modelo:</label>
                        <input type="text" id="ciudadVehiculo" name="ciudad"
                               placeholder="Ciudad de tu Vehiculo">
                    </div>
                    <div class="input_container">
                        <label for="colorVehiculo">Color:</label>
                        <input type="text" id="colorVehiculo" name="ciudad"
                               placeholder="Color de tu Vehiculo">
                    </div>
                </div>
                <hr>
                <div class="input_container input_container--vertical">
                    <label for="cascoConfirm">¿Lleva Casco?</label>
                    <input type="checkbox" id="cascoConfirm" name="placa">
                    <label for="cantCasco">Cantidad:</label>
                    <input type="text" id="cantCasco" name="marca" placeholder="Describe lo que paso">
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
                <div class="vehiculo__list__item" data-vehiculo="0">
                    <p>aaaa</p>
                    <p>AAAAAA</p>
                    <p></p>
                </div>
                <div class="vehiculo__list__item" data-vehiculo="1">
                    <p>ZZZZZZZZ</p>
                    <p>ASAAS</p>
                    <p></p>
                </div>
                <div class="vehiculo__list__item" data-vehiculo="2">
                    <p>ADSAAD</p>
                    <p>ASDSADSA</p>
                    <p></p>
                </div>
                <div class="vehiculo__list__item" data-vehiculo="3">
                    <p>JSD-KSD</p>
                    <p>KSDK</p>
                    <p></p>
                </div>
                <div class="vehiculo__list__item add">
                    <p>Add</p>
                </div>
            </div>
        </div>
    </div>




</section>



        </main>
    <script src="resources/js/TiposVehiculosSelect.js"></script>
    <script type="module" src="resources/js/Vehiculo.js"></script>
    <jsp:include page="resources/footer.jsp" />
    </body>
</html>
