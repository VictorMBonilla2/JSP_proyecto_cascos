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
                            <button class="botones botones__pago">
                                Pagar
                            </button>
                            <button class="botones botones__ajustar">
                                Ajustar
                            </button>
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
                            <button class="botones botones--largo botones__pago">
                                Añadir
                            </button>
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



            </section>
        </section>
    </main>

</body>

</html>
