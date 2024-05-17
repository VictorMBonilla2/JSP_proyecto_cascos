<%@ page import="Modelo.TbEspacio" %>
<%@ page import="java.util.List" %>
<%@ page import="Modelo.TbCasco" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="resources/header.jsp" />

<jsp:include page="resources/sidebar.jsp" />


            <section class="main_container__casillero" id="casillerosContainer">
                <% Integer cantidadCasilleros = (Integer) request.getAttribute("Casilleros");
                    List<TbEspacio> DatosEspacio = (List<TbEspacio>) request.getAttribute("Espacios");
                    for (TbEspacio espacio : DatosEspacio) {
                        TbCasco casco = espacio.getCasco();
                        String placaCasco = "";
                        if (casco != null) {
                            placaCasco = casco.getPlaca_casco();
                        }%>

                <div class="casillero">
                    <div class="casillero__title">
                        <h1><%=placaCasco%></h1>
                        <p>lugar</p>
                    </div>

                    <div class="casillero__contenido">
                        <div class="contenido__info">
                            <div class="info__casillero">
                                <p>Casillero</p>
                                <p>(# Casillero)</p>
                            </div>
                            <div class="info__tiempo">
                                <p>Tiempo</p>
                                <p>(tiempo)</p>
                            </div>
                            <div class="info__costo">
                                <p>Costo</p>
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
                <% } %>

                <div class="casillero">


                </div>
                <div class="casillero">


                </div>
                <div class="casillero">


                </div>
                <div class="casillero">


                </div>
                <div class="casillero">


                </div>
                <div class="casillero">


                </div>




            </section>
        </section>
    </main>

</body>

</html>
