<%@ page import="Modelo.Persona" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:include page="resources/header.jsp" />

<jsp:include page="resources/sidebar.jsp" />
<%
    HttpSession sesion = request.getSession();
    Persona user = (Persona) session.getAttribute("user");
%>
                <section class="main_container__home">
                    <div class="info_user">
                          <h1>Bienvenido <%=user.getNombre()%></h1>
                        <section class="content">
                            <div class="Mygraph">
                                <canvas id="myChart" height="900" width="900"></canvas>
                            </div>
                        </section>
                    </div>
                    <div class="Graph1" >
                      <div class="bloque__user">
                          <div class="info2-user__img">
                              <img src="resources/imagenes/IconPerfil.png" alt="icono">
                          </div>

                          <div class="info2-user__container">
                              <div class="info2-user__text">
                                  <h1>Nombre</h1>
                                  <p> <%=user.getNombre()%></p>
                              </div>
                              <div class="info2-user__text">
                                  <h1>Apellido</h1>
                                  <p> <%=user.getApellido()%></p>
                              </div>
                              <div class="info2-user__text">
                                  <h1>Tipo Doc</h1>
                                  <p> <%=user.getTipoDocumento()%></p>
                              </div>
                              <div class="info2-user__text">
                                  <h1>num Doc</h1>
                                  <p> <%=user.getDocumento()%></p>
                              </div>
                              <div class="info2-user__text">
                                  <h1>fecha nac</h1>
                                  <p> 17/35/1995</p>
                              </div>
                          </div>
                      </div>
                    </div>
                    <div class="Graph2" >
                      <div class="bloque__sesion">
                          <h1>Has iniciado sesíon en: (lugar)</h1>
                          <p>Ult. sesión activa: (fecha)<br> (hora)</p>
                      </div>
                    </div>
                </section>
                </section>
        </main>
<jsp:include page="resources/footer.jsp" />
<script src="resources/js/graficos.js"></script>


    </body>

</html>