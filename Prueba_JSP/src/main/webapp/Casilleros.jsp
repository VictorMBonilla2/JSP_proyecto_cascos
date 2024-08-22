<%@ page import="Modelo.TbEspacio" %>
<%@ page import="java.util.List" %>
<%@ page import="Modelo.TbVehiculo" %>
<%@ page import="java.util.Date" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="resources/header.jsp" />

<jsp:include page="resources/sidebar.jsp" />


            <section class="main_container__casillero" id="casillerosContainer">
            <%
            Integer cantidadCasilleros = (Integer) request.getAttribute("Casilleros");
            List<TbEspacio> DatosEspacio = (List<TbEspacio>) request.getAttribute("Espacios");
            if (DatosEspacio != null) {
                for (TbEspacio espacio : DatosEspacio) {
                    int espacioId = espacio.getId_espacio();

                    TbVehiculo vehiculo = espacio.getVehiculo();

                    if (vehiculo != null) {
                        Integer documento = espacio.getPersona().getDocumento();
                        String placa = vehiculo.getPlaca_vehiculo();
                        String nombre = espacio.getNombre();
                        Integer cantCascos= espacio.getCantidad_cascos();
                        Date tiempoEntrada = espacio.getHora_entrada();
                    %>

                <div class="casillero" data-entrada="<%= tiempoEntrada.getTime() %>" data-tarifa="10.0">
                    <div class="casillero__title estilo__casillero">
                        <h1>Espacio <%=espacioId%></h1>
                        <p><%=nombre%></p>
                        <img src="resources/imagenes/DashiconsWarning.png" class="report__img" data-report="reportmodal<%=espacioId%>">
                    </div>

                    <div class="casillero__contenido estilo__contenido">
                        <div class="contenido__info">
                            <div class="info__casillero">
                                <h3>Documento</h3>
                                <p><%=documento%></p>
                            </div>
                            <div class="info__tiempo">
                                <h3>Placa</h3>
                                <p> <%=placa%></p>
                            </div>
                            <div class="info__costo">
                                <h3>Cascos</h3>
                                <p> <%=cantCascos%> </p>
                            </div>
                        </div>
                        <div class="contenido__botones">
                            <button class="botones botones__pago " data-pay="paymodal<%=espacioId%>">
                                Liberar
                            </button>
                            <button class="botones botones__ajustar" data-edit="editmodal<%=espacioId%>">
                                Ajustar
                            </button>
                        </div>
                    </div>
                </div>
                <!-- ventana add Espacio-->
                <div id="paymodal<%=espacioId%>" class="modal">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h2>Espacio <%=espacioId%></h2>
                            <span class="close" data-modal-id="paymodal<%=espacioId%>">&times;</span>
                        </div>
                        <div class="modal-body">
                            <h2>Liberar Espacio</h2>
                            <form id="payCasco<%= espacioId %>" onsubmit="payCasco(event, <%= espacioId %>)" class="formulario">
                                <div class="formulario__inputs">
                                    <p>Documento: <%=documento%></p>
                                    <p>Nombre: <%=nombre%></p>
                                    <p>Placa: <%=vehiculo.getPlaca_vehiculo()%></p>

                                    <button type="submit" class="formulario__button">Liberar</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
                <!-- ventana Editar Espacio-->

                <div id="editmodal<%=espacioId%>" class="modal">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h2>Espacio <%=espacioId%></h2>
                            <span class="close" data-modal-id="editmodal<%=espacioId%>">&times;</span>
                        </div>
                        <div class="modal-body">
                            <h2>Editar Espacio</h2>
                            <form id="editCasco<%= espacioId %>" onsubmit="editCasco(event, <%= espacioId %>)" class="formulario">
                                <div class="formulario__inputs">
                                    <input type="text" id="editnombre<%=espacioId%>" placeholder="Nombre Opcional" name="nombre" value="<%=nombre%>">
                                    <input type="number" id="editcant_cascos<%=espacioId%>" placeholder="Cantidad de cascos" name="cant_cascos" value="<%=cantCascos%>" required>
                                    <button type="submit" class="formulario__button">Añadir</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>

                <!-- ventana Reportar Espacio-->

                <div id="reportmodal<%=espacioId%>" class="modal">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h2>Espacio <%=espacioId%></h2>
                            <span class="close" data-modal-id="reportmodal<%=espacioId%>">&times;</span>
                        </div>
                        <div class="modal-body">
                            <h2>Editar Espacio</h2>
                            <form id="reportCasco<%= espacioId %>" onsubmit="editCasco(event, <%= espacioId %>)" class="formulario">
                                <div class="formulario__inputs">
                                    <label for="selectOption">Tipo de Reporte:</label>
                                    <select id="selectOption" name="options">
                                        <option value="option1">Opción 1</option>
                                        <option value="option2">Opción 2</option>
                                        <option value="option3">Opción 3</option>
                                    </select>


                                    <label for="textBox">Texto:</label>
                                    <input type="text" id="textBox" name="textBox" placeholder="Describe lo que paso">

                                    <label for="description">Descripción</label>
                                    <textarea id="description" name="description" rows="4" cols="50" placeholder="Escribe aquí tu descripción..."></textarea>

                                    <button type="submit" class="formulario__button">Añadir</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>

                <%
                        }else {%>
                <div class="casillero">
                    <div class="casillero__title estilo__casillero">
                        <h1>Espacio <%=espacioId%></h1>
                    </div>

                    <div class="casillero__contenido estilo__contenido">
                        <div class="contenido__info">
                            <div class="info__casillero">
                                <h1>Libre</h1>
                            </div>
                        </div>
                        <div class="contenido__botones">
                            <button class="botones botones--largo botones__pago addCasilleroBtn" data-add="addmodal<%=espacioId%>">
                                Añadir
                            </button>
                        </div>
                    </div>

                </div>
                <!-- ventana Crear Espacio-->
                <div id="addmodal<%=espacioId%>" class="modal">
                    <div class="modal-content">
                        <div class="modal-header">

                            <h2>Espacio <%=espacioId%></h2>
                            <span class="close" data-modal-id="addmodal<%=espacioId%>">&times;</span>
                        </div>
                        <div class="modal-body">
                            <h2>Nuevo Espacio</h2>
                            <form id="addCasco<%= espacioId %>" onsubmit="addCasco(event, <%= espacioId %>)" class="formulario">
                                <div class="formulario__inputs">
                                    <input type="text" id="adddocumento<%=espacioId%>" placeholder="Documento del aprendiz" name="documento" required>
                                    <input type="text" id="addnombre<%=espacioId%>" placeholder="Nombre del aprendiz" name="nombre">
                                    <input type="number" id="addcant_cascos<%=espacioId%>" placeholder="Cantidad de Cascos" name="cant_cascos">
                                    <button type="submit" class="formulario__button">Añadir</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
                <% }
                }
            } else {
                %>
                <div class="casillero">
                    <div class="casillero__title">
                        <h1><%="vacio"%></h1>
                    </div>

                    <div class="casillero__contenido">
                        <div class="contenido__info">
                            <div class="info__casillero">
                                <h1>Libre</h1>
                            </div>
                        </div>
                        <div class="contenido__botones">
                            <button class="botones botones--largo botones__pago">
                                Liberar
                            </button>
                        </div>
                    </div>
                </div>


                <%} %>



                    <script src="resources/js/Casilleros.js"></script>
                    <script src="resources/js/tiempoCasilleros.js"></script>

            </section>
        </section>
    </main>
    <jsp:include page="resources/footer.jsp" />
</body>

</html>
