<%-- Created by IntelliJ IDEA. User: Propietario Date: 3/05/2024 Time: 11:07 a. m. To change this template use File |
  Settings | File Templates. --%>
  <%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="resources/header.jsp" />

<jsp:include page="resources/sidebar.jsp" />


                <section class="main_container__home">
                    <div class="info_user">
                      <h1>(nombre)</h1>
                      <p>Has iniciado sesíon en: (lugar)</p>
                      <p>Ult. sesión activa: (fecha)<br> (hora)</p>
                    </div>

                    <div class="Graph1" >
                      <div class="bloque__user">
                          <div class="info2-user__img">
                              <img src="" alt="icono">
                          </div>

                          <div class="info2-user__container">
                              <div class="info2-user__text">
                                  <h1>Nombre</h1>
                                  <p> Jose</p>
                              </div>
                              <div class="info2-user__text">
                                  <h1>Apellido</h1>
                                  <p> Antonio G.</p>
                              </div>
                              <div class="info2-user__text">
                                  <h1>Tipo Doc</h1>
                                  <p> ???</p>
                              </div>
                              <div class="info2-user__text">
                                  <h1>num Doc</h1>
                                  <p> 0485485</p>
                              </div>
                              <div class="info2-user__text">
                                  <h1>fecha nac</h1>
                                  <p> 17/35/1995</p>
                              </div>
                          </div>
                      </div>
                    </div>
                    <div class="Graph2" >
                      <div class="bloque__sesion">s</div>
                    </div>
                </section>
            </section>
        </main>
<jsp:include page="resources/footer.jsp" />
    </body>

</html>