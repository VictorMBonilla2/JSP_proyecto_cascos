<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="resources/header.jsp" />

<jsp:include page="resources/sidebar.jsp" />

<section class="main_container__registros">
    <div class="tabs">
        <button>hoy</button>
        <button>Semana</button>
        <button>Mes</button>
        <button>Año</button>
        <input type="text" placeholder="Buscar">
    </div>
    <table>
        <script src="resources/js/Registros.js"></script>
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
