<%@ page import="Modelo.TbEspacio" %>
<%@ page import="java.util.List" %>
<%@ page import="Modelo.TbCasco" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="resources/header.jsp" />

<jsp:include page="resources/sidebar.jsp" />


            <section class="main_container__casillero" id="casillerosContainer">
            <%
            Integer cantidadCasilleros = (Integer) request.getAttribute("Casilleros");
            List<TbEspacio> DatosEspacio = (List<TbEspacio>) request.getAttribute("Espacios");
            if (DatosEspacio != null) {
                for (TbEspacio espacio : DatosEspacio) {
                    TbCasco casco = espacio.getCasco();
                    int espacioId = espacio.getId();

                    if (casco != null) {
                        String placaCasco = casco.getPlaca_casco();
                        String ciudad = casco.getCiudad();
                        Integer cantCascos= casco.getCant_casco();
                    %>

                <div class="casillero">
                    <div class="casillero__title">
                        <h1><%=placaCasco%></h1>
                        <p><%=ciudad%></p>
                    </div>

                    <div class="casillero__contenido">
                        <div class="contenido__info">
                            <div class="info__casillero">
                                <h3>Casillero</h3>
                                <p><%=espacioId%></p>
                            </div>
                            <div class="info__tiempo">
                                <h3>Tiempo</h3>
                                <p>(tiempo)</p>
                            </div>
                            <div class="info__costo">
                                <h3>Costo</h3>
                                <p>(costo)</p>
                            </div>
                        </div>
                        <div class="contenido__botones">
                            <button class="botones botones__pago " data-pay="paymodal<%=espacioId%>">
                                Pagar
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
                            <h2>Casillero <%=espacioId%></h2>
                            <span class="close" data-modal-id="paymodal<%=espacioId%>">&times;</span>
                        </div>
                        <div class="modal-body">
                            <h2>Pagar Casco</h2>
                            <form id="payCasco<%= espacioId %>" onsubmit="payCasco(event, <%= espacioId %>)" class="formulario">
                                <div class="formulario__inputs">
                                    <p>Tiempo transcurrido: riwmpo</p>
                                    <p>Costo Total: $1000</p>
                                    <p>Placa: <%=placaCasco%></p>

                                    <button type="submit" class="formulario__button">Añadir</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
                <!-- ventana Editar Espacio-->

                <div id="editmodal<%=espacioId%>" class="modal">
                    <div class="modal-content">
                        <div class="modal-header">

                            <h2>Casillero <%=espacioId%></h2>
                            <span class="close" data-modal-id="editmodal<%=espacioId%>">&times;</span>
                        </div>
                        <div class="modal-body">
                            <h2>editar Casco</h2>
                            <form id="editCasco<%= espacioId %>" onsubmit="editCasco(event, <%= espacioId %>)" class="formulario">
                                <div class="formulario__inputs">
                                    <input type="text" id="editplaca<%=espacioId%>" placeholder="Placa" name="documento" value="<%=placaCasco%>" required>
                                    <input type="text" id="editciudad<%=espacioId%>" placeholder="Ciudad" name="ciudad" value="<%=ciudad%>" required >
                                    <input type="number" id="editcant_cascos<%=espacioId%>" placeholder="Cantidad de cascos" name="cant_cascos" value="<%=cantCascos%>" required>
                                    <button type="submit" class="formulario__button">Añadir</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>

                <%
                        }else {%>
                <div class="casillero">
                    <div class="casillero__title">
                        <h1>Espacio <%=espacioId%></h1>
                    </div>

                    <div class="casillero__contenido">
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

                            <h2>Casillero <%=espacioId%></h2>
                            <span class="close" data-modal-id="modal<%=espacioId%>">&times;</span>
                        </div>
                        <div class="modal-body">
                            <h2>Nuevo Casco</h2>
                            <form id="addCasco<%= espacioId %>" onsubmit="addCasco(event, <%= espacioId %>)" class="formulario">
                                <div class="formulario__inputs">
                                    <input type="text" id="addplaca<%=espacioId%>" placeholder="Placa" name="documento" required>
                                    <input type="text" id="addciudad<%=espacioId%>" placeholder="Ciudad" name="ciudad" required>
                                    <input type="number" id="addcant_cascos<%=espacioId%>" placeholder="Cantidad de cascos" name="cant_cascos" required>
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
                                Pagar
                            </button>
                        </div>
                    </div>
                </div>


                <%} %>



                    <script src="resources/js/Casilleros.js"></script>
            </section>
        </section>
    </main>

</body>

</html>
