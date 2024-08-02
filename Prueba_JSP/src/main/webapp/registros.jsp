<%@ page import="java.util.List" %>
<%@ page import="Modelo.TbRegistro" %>
<%@ page import="Modelo.TbEspacio" %>
<%@ page import="java.lang.reflect.Field" %>
<%@ page import="Servlets.Anotaciones" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="resources/header.jsp" />

<jsp:include page="resources/sidebar.jsp" />
<%
    List<TbRegistro> DatosRegistros = (List<TbRegistro>) request.getAttribute("Registros");


%>
<section class="main_container__registros">
    <div class="tabs">
        <button>hoy</button>
        <button>Semana</button>
        <button>Mes</button>
        <button>Año</button>
        <input type="text" placeholder="Buscar">
    </div>
    <table>
        <thead>
        <tr>
            <%if (DatosRegistros != null) {

                Class<?> clazz = TbRegistro.class;

                Field[] fields = clazz.getDeclaredFields();
                int contador = 0;
                for (Field field : fields) {
                    if(contador ==0){
                        contador = 1;
                        continue;
                    }
                    Anotaciones.PropertyName annotation = field.getAnnotation(Anotaciones.PropertyName.class);
                    if (annotation != null) {
                        System.out.println("Nombre de la propiedad: " + annotation.value());
                    }


            %>
            <th><%=annotation.value()%></th>
            <%}
            %>
            </tr>
            </thead>
            <tbody>

        <%
        for(TbRegistro registro : DatosRegistros){
            %>
        <tr>
            <td> <%=registro.getFecha_registro()%></td>
            <td><%=registro.getEspacio().getId_espacio()%></td>
            <td><%=registro.getVehiculo().getPlaca_vehiculo()%></td>
            <td><%=registro.getAprendiz().getDocumento()%></td>
            <td><%=registro.getColaborador().getDocumento()%></td>
        </tr>

            <%


        }

    }
            %>

        </tbody>
    </table>
    <div class="pagination">
        <span>15 de 350</span>
        <span>&lt;&lt;Pag 1 &gt;&gt;</span>
    </div>
</section>




</main>
<script src="resources/js/Registros.js"></script>
<jsp:include page="resources/footer.jsp" />
</body>
</html>
