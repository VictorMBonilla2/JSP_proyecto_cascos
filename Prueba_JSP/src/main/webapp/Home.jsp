<%@ page import="Modelo.Persona" %>
<%@ page import="Modelo.TbVehiculo" %>
<%@ page import="java.util.Set" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<jsp:include page="resources/header.jsp" />
<link rel="stylesheet" href="resources/css/home.css">
<link rel="stylesheet" href="resources/css/profile_aprendiz.css">
<jsp:include page="resources/sidebar.jsp" />

<%
    HttpSession sesion = request.getSession();
    Persona user = (Persona) session.getAttribute("user");

    boolean isGestor = user.getRol().getId()==1;
    boolean isAprendiz= user.getRol().getId()==2;
    boolean isAdmin = user.getRol().getId()==3;
%>

<section class="main_container__home">
    <div class="info_user">
        <div class="content">
            <% if (isGestor || isAdmin) { %>
            <!-- Gráfico del colaborador -->
            <div class="Mygraph">
                <canvas id="myChart" height="900" width="900"></canvas>
            </div>
            <% } else if (isAprendiz) { %>
            <!-- Información de los vehículos del aprendiz -->
            <div class="vehiculo__content_data">
                <h1>Datos del Vehículo</h1>
                <hr class="linea">
                <%
                    Set<TbVehiculo> vehiculoUser = user.getVehiculos();
                    int index = 0;

                    for (TbVehiculo vehiculo : vehiculoUser) {
                %>
                <div class="vehiculo_data" id="vehiculo<%= index %>" style="<%= index == 0 ? "" : "display:none;" %>">
                    <div class="vehiculo_data__text">
                        <h1>Placa</h1>
                        <p><%= vehiculo.getPlacaVehiculo() %></p>
                    </div>
                    <div class="vehiculo_data__text">
                        <h1>Marca</h1>
                        <p><%= vehiculo.getMarcaVehiculo() %></p>
                    </div>
                    <div class="vehiculo_data__text">
                        <h1>Modelo</h1>
                        <p><%= vehiculo.getModeloVehiculo() %></p>
                    </div>
                    <div class="adicional_vehiculo_data">
                        <h1>Datos Adicionales</h1>
                        <hr class="linea">
                        <div class="vehiculo_data__text">
                            <h1>¿Lleva casco?</h1>
                            <p><%= vehiculo.getCantCasco() == 0 ? "No" : "Sí" %></p>
                        </div>
                        <div class="vehiculo_data__text">
                            <h1>Cantidad</h1>
                            <p><%= vehiculo.getCantCasco() %></p>
                        </div>
                    </div>
                </div>
                <%index++;
                    }
                %>

                <input type="hidden" id="vehiculoCount" value="<%= vehiculoUser.size() %>" />

                <div class="vehiculo_navigation">
                    <button onclick="showPrevVehiculo()" class="button_secundary button--width45" id="prevButton" disabled="">Anterior</button>
                    <button onclick="showNextVehiculo()"  class="button_secundary button--width45" id="nextButton">Siguiente</button>
                </div>
            </div>
            <% } %>
        </div>
    </div>
    <div class="Graph1">
        <div class="bloque__user">
            <div class="info2-user__img">
                <img src="resources/imagenes/IconPerfil.png" alt="icono">
            </div>

            <div class="info2-user__container">
                <div class="info2-user__text">
                    <h1>Nombre</h1>
                    <p><%= user.getNombre() %></p>
                </div>
                <div class="info2-user__text">
                    <h1>Apellido</h1>
                    <p><%= user.getApellido() %></p>
                </div>
                <div class="info2-user__text">
                    <h1>Tipo Doc</h1>
                    <p><%= user.getTipoDocumento() %></p>
                </div>
                <div class="info2-user__text">
                    <h1>num Doc</h1>
                    <p><%= user.getId() %></p>
                </div>
                <div class="info2-user__text">
                    <h1>fecha nac</h1>
                    <p> 17/35/1995</p>
                </div>
            </div>
        </div>
    </div>

</section>

<jsp:include page="resources/footer.jsp" />

<% if (isGestor) { %>
<script src="resources/js/graficos.js"></script>
<% } else { %>
<script src="resources/js/VehiculoINFO.js"></script>
<% } %>
</body>
</html>
