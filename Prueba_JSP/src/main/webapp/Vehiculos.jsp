<%@ page import="Modelo.TbVehiculo" %>
<%@ page import="java.util.Set" %>
<%@ page import="Modelo.Persona" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:include page="resources/header_aprendiz.jsp" />
<%
    HttpSession sesion = request.getSession();
    Persona user = (Persona) session.getAttribute("user");
    Set<TbVehiculo> vehiculoUser = user.getVehiculos();
    int index = 0;
%>


<jsp:include page="resources/sidebar_aprendiz.jsp" />
            <section class="main_container__vehiculo">

               <div class="container__vehiculo">
                    <div class="container__vehiculo__form">

                        <form class="vehiculo__form" id="formVehiculos">
                            <input type="hidden" id="documentoUser" value="<%=user.getDocumento()%>">
                            <input type="hidden" id="idVehiculo" value="">
                            <h1>Datos del vehiculo</h1>
                            <hr>
                            <div class="formulario__inputs">
                                <label for="tipoVehiculo">Tipo:</label>
                                <select id="tipoVehiculo" name="options">
                                </select>
                                <label for="placaVehiculo">Placa:</label>
                                <input type="text" id="placaVehiculo" name="placa" placeholder="Placa de tu vehiculo">
                                <label for="marcaVehiculo">Marca:</label>
                                <input type="text" id="marcaVehiculo" name="marca" placeholder="Marca de tu vehiculo">
                                <label for="modeloVehiculo">Modelo:</label>
                                <input type="text" id="modeloVehiculo" name="modelo" placeholder="Modelo de tu Vehiculo">
                                <label for="ciudadVehiculo">Modelo:</label>
                                <input type="text" id="ciudadVehiculo" name="ciudad" placeholder="Ciudad de tu Vehiculo">
                                <label for="colorVehiculo">Color:</label>
                                <input type="text" id="colorVehiculo" name="ciudad" placeholder="Color de tu Vehiculo">
                                <hr>
                                <label for="cascoConfirm">¿Lleva Casco?</label>
                                <input type="checkbox" id="cascoConfirm" name="placa">
                                <label for="cantCasco">Tipo:</label>
                                <input type="text" id="cantCasco" name="marca" placeholder="Describe lo que paso">
                            </div>

                        </form>
                        <div class="button_vehiculo">
                            <button class="formulario_login__button accionador" type="submit" id="sendCreate">Crear</button>
                            <button class="formulario_login__button" type="button" id="cancelEdit">Cancelar</button>
                        </div>
                    </div>



                    <div class="vehiculo__list">
                        <h1>Datos del vehiculo</h1>
                        <hr>
                        <div class="vehiculo__grid">
                            <div class="vehiculo__list__item">
                                <p>HDJ-278</p>
                                <p>Toyota</p>
                                <p>Moto</p>
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
