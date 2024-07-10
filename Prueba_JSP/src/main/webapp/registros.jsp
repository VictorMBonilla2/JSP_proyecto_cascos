<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="resources/header.jsp" />

<jsp:include page="resources/sidebar.jsp" />

<section class="main_container__reportes">
    <div class="box"></div>
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
            <th>Fecha</th>
            <th>Hora</th>
            <th>Placa</th>
            <th>Casillero(s)</th>
            <th>Documento Aprendiz</th>
            <th>Colaborador</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>2024-07-01</td>
            <td>08:00</td>
            <td>ABC-1234</td>
            <td>1</td>
            <td>12345678</td>
            <td>Juan Pérez</td>
        </tr>
        <tr>
            <td>2024-07-02</td>
            <td>09:00</td>
            <td>DEF-5678</td>
            <td>2</td>
            <td>87654321</td>
            <td>María López</td>
        </tr>
        <tr>
            <td>2024-07-03</td>
            <td>10:00</td>
            <td>GHI-9101</td>
            <td>3</td>
            <td>11223344</td>
            <td>Carlos Sánchez</td>
        </tr>
        <tr>
            <td>2024-07-04</td>
            <td>11:00</td>
            <td>JKL-1213</td>
            <td>4</td>
            <td>44332211</td>
            <td>Ana Martínez</td>
        </tr>
        <tr>
            <td>2024-07-05</td>
            <td>12:00</td>
            <td>MNO-1415</td>
            <td>5</td>
            <td>55667788</td>
            <td>Pedro Gómez</td>
        </tr>
        </tbody>
    </table>
    <div class="pagination">
        <span>15 de 350</span>
        <span>&lt;&lt;Pag 1 &gt;&gt;</span>
    </div>
</section>




</main>
<jsp:include page="resources/footer.jsp" />
</body>
</html>
