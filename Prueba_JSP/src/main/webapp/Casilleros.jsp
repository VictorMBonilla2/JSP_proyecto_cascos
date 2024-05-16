
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="resources/header.jsp" />

<jsp:include page="resources/sidebar.jsp" />


            <section class="main_container__casillero" id="casillerosContainer">
                <% Integer cantidadCasilleros = (Integer) request.getAttribute("Casilleros");
                        System.out.println(cantidadCasilleros);;
                    int Cantidad = cantidadCasilleros != null ? cantidadCasilleros.intValue() : 0;
                    for (int i=1; i<=Cantidad; i++) { %>

                <div class="casillero">
                    <div class="casillero__title">
                        <h1>(PLACA)</h1>
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
