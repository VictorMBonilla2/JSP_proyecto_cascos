<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:include page="resources/header_aprendiz.jsp" />

<jsp:include page="resources/sidebar_aprendiz.jsp" />
            <section class="main_container__vehiculo">

               <div class="container__vehiculo">
                    <div class="container__vehiculo__form">

                        <form class="vehiculo__form">
                            <h1>Datos del vehiculo</h1>
                            <hr>
                            <div class="formulario__inputs">
                                <label for="tipoVehiculo">Tipo:</label>
                                <select id="tipoVehiculo" name="options">
                                    <option value="option1">Opción 1</option>
                                    <option value="option2">Opción 2</option>
                                    <option value="option3">Opción 3</option>
                                </select>
                                <label for="placaVehiculo">Tipo:</label>
                                <input type="text" id="placaVehiculo" name="placa" placeholder="Describe lo que paso">
                                <label for="marcaVehiculo">Tipo:</label>
                                <input type="text" id="marcaVehiculo" name="marca" placeholder="Describe lo que paso">
                                <label for="modeloVehiculo">Tipo:</label>
                                <input type="text" id="modeloVehiculo" name="modelo" placeholder="Describe lo que paso">
                                <hr>
                                <label for="cascoConfirm">¿Lleva Casco?</label>
                                <input type="checkbox" id="cascoConfirm" name="placa">
                                <label for="cantCasco">Tipo:</label>
                                <input type="text" id="cantCasco" name="marca" placeholder="Describe lo que paso">
                            </div>

                        </form>
                    </div>


                    <div class="vehiculo__list">
                        <h1>Datos del vehiculo</h1>
                        <hr>
                        <div class="vehiculo__grid">
                            <div class="vehiculo__list__item">
                                <p>1</p>
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
    <jsp:include page="resources/footer.jsp" />
    </body>
</html>
