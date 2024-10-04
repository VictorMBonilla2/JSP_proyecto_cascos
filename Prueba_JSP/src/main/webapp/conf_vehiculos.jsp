<%@ page import="Modelo.Persona" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="resources/header.jsp" />
<link rel="stylesheet" href="resources/css/conf_Sistema.css">
<link rel="stylesheet" href="resources/css/conf_Vehiculo.css">
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
<section class="main_container__conf_Sistema">
    <div class="container-Editvehiculo">
        <div class="vehiculos_container">
            <div class="search">
                <form id="searhForm">
                    <input type="hidden" id="typeSearch" name="typeSearch" value="document">
                    <div class="selectTypeSearch">
                        <h3>Buscar Vehículo por:</h3>
                        <div class="button-container">
                            <button class="toggle-button button_primary button--width45" id="btn-documento" type="button">Documento</button>
                            <button class="toggle-button button_primary button--width45 button--deselected"
                                    id="btn-placa" type="button">Placa</button>
                        </div>
                    </div>
                    <div class="bodySearhForm">
                        <div class="input_container">
                            <label for="documento">Ingresa el documento</label>
                            <input type="text" id="documento" name="documento" placeholder="Documento">
                        </div>
                        <button class="button_primary search-button" type="submit">
                            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="m19.6 21l-6.3-6.3q-.75.6-1.725.95T9.5 16q-2.725 0-4.612-1.888T3 9.5t1.888-4.612T9.5 3t4.613 1.888T16 9.5q0 1.1-.35 2.075T14.7 13.3l6.3 6.3zM9.5 14q1.875 0 3.188-1.312T14 9.5t-1.312-3.187T9.5 5T6.313 6.313T5 9.5t1.313 3.188T9.5 14"/></svg>
                        </button>
                    </div>
                </form>
            </div>
            <div class="edit-Vehiculo">
                <form id="vehiculoForm" class="formulario__body">
                    <h4>Datos del vehículo</h4>
                    <input type="hidden" name="formType" value="edit">
                    <input type="hidden" id="idVehiculo" name="idVehiculo">
                    <input type="hidden" id="idAprendiz" name="idAprendiz">

                    <div class="input_container">
                        <label for="tipoSelect">Tipo:</label>
                        <select type="text" id="tipoSelect" name="tipoVehiculo"></select>
                    </div>
                    <div class="input_container">
                        <label for="marcaSelect">Marca:</label>
                        <select type="text" id="marcaSelect" name="marcaVehiculo"></select>
                    </div>

                    <div class="input_container">
                        <label for="modeloSelect">Modelo:</label>
                        <select type="text" id="modeloSelect" name="modeloVehiculo"></select>
                    </div>

                    <div class="input_container">
                        <label for="ciudadSelect">ciudad:</label>
                        <select type="text" id="ciudadSelect" name="ciudadVehiculo"></select>
                    </div>

                    <div class="input_container">
                        <label for="placa">Placa:</label>
                        <input type="text" id="placa" name="placaVehiculo">
                    </div>

                    <div class="input_container">
                        <label for="colorSelect">Color:</label>
                        <select type="text" id="colorSelect" name="colorVehiculo" ></select>
                    </div>

                    <div class="input_container">
                        <label for="casco">¿Lleva casco?:</label>
                        <input type="checkbox" id="casco">
                    </div>

                    <div class="input_container">
                        <label for="cantidadCasco">Cantidad:</label>
                        <input type="number" id="cantidadCasco" min="1" name="cantCasco">
                    </div>
                    <div class="button-container">
                        <button class="button_primary" type="submit">Editar</button>
                        <button class="button_secundary" type="reset">cancelar</button>
                        <button class="button_secundary button--delete" id="deleteButton" type="button">Eliminar</button>
                    </div>
                </form>

                <hr/>
                <div class="card-Vehiculos">

                </div>
            </div>
        </div>
    </div>

</section>
<jsp:include page="resources/confirm.jsp"/>
<jsp:include page="resources/success.jsp"/>
<jsp:include page="resources/error.jsp"/>
<script type="module" src="resources/js/systemForms/config_vehiculos.js"></script>
<jsp:include page="resources/footer.jsp" />
</section>
</main>
<template id="vehiculoTemplate">
    <div class="itemVehiculo">
        <h4 class="vehiculo-tipo"></h4>
        <p class="vehiculo-marca"></p>
        <p class="vehiculo-placa"></p>
        <p class="vehiculo-documento"></p>
    </div>
</template>

</body>

</html>