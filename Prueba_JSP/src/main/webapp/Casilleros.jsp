<%@ page import="Modelo.Persona" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="resources/header.jsp" />
<link rel="stylesheet" href="resources/css/casilleros.css">
<jsp:include page="resources/sidebar.jsp" />
<%
    HttpSession sesion = request.getSession(false);
    Persona user = null;
    if (sesion != null) {
        user = (Persona) sesion.getAttribute("user");
    }
    if (user == null) {
        response.sendRedirect("index.jsp");
        return;
    }
%>

        <section class="main_container__casillero" id="casillerosContainer">

        <div class="tabs">
            <ul id="tab-list">
                <!-- Aquí se generarán las pestañas (tabs) dinámicamente -->
            </ul>
        </div>

        <div class="tab-content" id="tab-content">
            <!-- Aquí se generarán los contenidos dinámicos de cada sector -->
        </div>



        </section>
</section>
</main>
<script type="module" src="resources/js/Casilleros.js"></script>
<script type="module" src="resources/js/renderCasilleros.js"></script>
<script src="resources/js/buttonListener.js"></script>

<template id="template-espacio-ocupado">
    <div class="casillero" data-entrada="{tiempoEntrada}" data-tarifa="10.0">
        <div class="casillero__title estilo__casillero">
            <h1></h1>
            <p>{nombre}</p>
            <img src="resources/imagenes/DashiconsWarning.png" class="report__img" data-report="reportmodal{espacioId}">
        </div>

        <div class="casillero__contenido estilo__contenido">
            <div class="contenido__info">
                <div class="info__casillero">
                    <h3>Documento</h3>
                    <p>{documento}</p>
                </div>
                <div class="info__tiempo">
                    <h3>Placa</h3>
                    <p>{placa}</p>
                </div>
                <div class="info__costo">
                    <h3>Cascos</h3>
                    <p>{cantCascos}</p>
                </div>
            </div>
            <div class="button-container">
                <button class="button_primary botones__pago" data-pay="paymodal{espacioId}">
                    Liberar
                </button>
                <button class="button_secundary button--edit botones__ajustar" data-edit="editmodal{espacioId}">
                    Ajustar
                </button>
            </div>
        </div>
    </div>
</template>
<template id="template-espacio-libre">
    <div class="casillero">
        <div class="casillero__title estilo__casillero">
            <h1>{nombreEspacio}</h1>
        </div>

        <div class="casillero__contenido estilo__contenido">
            <div class="contenido__info">
                <div class="info__casillero">
                    <h1>Libre</h1>
                </div>
            </div>
            <div class="button-container">
                <button class="button_primary addCasilleroBtn" data-add="addmodal{espacioId}">
                    Añadir
                </button>
            </div>
        </div>
    </div>
</template>
<template id="modal-liberar-template">
    <div class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h2 class="nameEspacio"></h2>
                <span class="close">&times;</span>
            </div>
            <div class="modal-body">
                <h2>Liberar Espacio</h2>
                <form class="formulario">
                    <input type="hidden" name="typeForm" id="typeForm" value="liberar">
                    <input type="hidden" name="idEspacio" id="idEspacio" value="">
                    <div class="input_container input_container--vertical">
                        <p class="document"></p>
                        <p class="nameAprendiz"></p>
                        <p class="Placa"></p>
                        <button type="submit" class="button_primary">Liberar</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</template>
<template id="modal-editar-template">
    <div class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h2 class="nameEspacio"></h2>
                <span class="close">&times;</span>
            </div>
            <div class="modal-body">
                <h2>Editar Espacio</h2>
                <form class="">
                    <input type="hidden" name="typeForm" id="typeForm" value="edit">
                    <input type="hidden" name="idEspacio" id="idEspacio" value="">
                    <div class="input_container input_container--vertical">
                        <label for="editdocumento">Documento Aprendiz</label>
                        <input type="text" id="editdocumento" placeholder="Documento del aprendiz" name="documento" required readonly>
                        <label for="editcant_cascos">Cantidad de Cascos</label>
                        <input type="number" id="editcant_cascos" placeholder="Cantidad de cascos" name="cant_cascos" required>
                        <button type="submit" class="button_primary">Añadir</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</template>
<template id="modal-reporte-template">
    <div class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h2 class="nameEspacio"></h2>
                <span class="close">&times;</span>
            </div>
            <div class="modal-body">
                <h2>Reportar Espacio</h2>
                <form class="formulario">
                    <input type="hidden" name="typeForm" id="typeForm" value="report">
                    <input type="hidden" name="idEspacio" id="idEspacio" value="">
                    <div class="input_container input_container--vertical">
                        <label for="reportTipo">Tipo de Reporte:</label>
                        <select id="reportTipo" name="options">
                        </select>
                        <label for="reportNombre">Nombre Reporte:</label>
                        <input type="text" id="reportNombre" name="reportNombre" placeholder="Describe lo que pasó">
                        <label for="reportDescripcion">Descripción</label>
                        <textarea id="reportDescripcion" name="description" rows="4" cols="50" placeholder="Escribe aquí tu descripción..."></textarea>
                        <button type="submit" class="button_primary">Añadir</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</template>
<template id="modal-ocupar-template">
    <div class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h2 class="nameEspacio"></h2>
                <span class="close">&times;</span>
            </div>
            <div class="modal-body">
                <h2>Nuevo Espacio</h2>
                <form class="formulario">
                    <input type="hidden" name="typeForm" id="typeForm" value="add">
                    <input type="hidden" name="idEspacio" id="idEspacio" value="">
                    <div class="input_container input_container--vertical">
                        <input type="text" placeholder="Documento del aprendiz" id="idAprendiz" name="documento" required>
                        <select id="addvehiculolist" name="options">
                            <!-- Lista de vehículos -->
                        </select>
                        <input type="number" id="addcant_cascos" placeholder="Cantidad de Cascos" name="cant_cascos">
                        <button type="submit" class="button_primary">Añadir</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</template>


<jsp:include page="resources/success.jsp"/>
<jsp:include page="resources/error.jsp"/>
<jsp:include page="resources/footer.jsp" />
</body>

</html>
