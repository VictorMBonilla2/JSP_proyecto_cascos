<%@ page import="Modelo.Persona" %>

<%
    HttpSession sesion = request.getSession();
    Persona user = (Persona) session.getAttribute("user");
    boolean sesionStatus = user != null;
    boolean isGestor = user.getRol().getId()==1;
    boolean isAprendiz= user.getRol().getId()==2;
    boolean isAdmin = user.getRol().getId()==3;
%>
<section class="main_container__aside">
    <aside class="BarraIzq">

        <a href="Home.jsp" class="imgContainer">

            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 256 256">
                <path d="M128 24a104 104 0 1 0 104 104A104.11 104.11 0 0 0 128 24M74.08 197.5a64 64 0 0 1 107.84 0a87.83 87.83 0 0 1-107.84 0M96 120a32 32 0 1 1 32 32a32 32 0 0 1-32-32m97.76 66.41a79.66 79.66 0 0 0-36.06-28.75a48 48 0 1 0-59.4 0a79.66 79.66 0 0 0-36.06 28.75a88 88 0 1 1 131.52 0" />
            </svg>
            <p>Home</p>
        </a>

        <% if (isGestor) { %>
        <a href="SvCasillero" class="imgContainer">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 2048 2048"><path d="M29 1459q-29 64-29 133v72q0 76 41 139t110 94q31 69 94 110t139 41q69 0 128-34t94-94h708q35 60 94 94t128 34t128-34t94-94h162q27 0 50-10t40-27t28-41t10-50v-256q0-80-30-150t-82-122t-122-82t-150-30h-37l-328-328q-27-27-62-41t-74-15H256v128h29zm739-563v256H309l99-219q8-17 24-27t35-10zm395 0q26 0 45 19l237 237H896V896zm373 1024q-27 0-50-10t-40-27t-28-41t-10-50q0-27 10-50t27-40t41-28t50-10q27 0 50 10t40 27t28 41t10 50q0 27-10 50t-27 40t-41 28t-50 10m0-384q-53 0-99 20t-82 55t-55 81t-20 100H896v-512h768q53 0 99 20t82 55t55 81t20 100v256h-128q0-53-20-99t-55-82t-81-55t-100-20M384 1920q-27 0-50-10t-40-27t-28-41t-10-50q0-27 10-50t27-40t41-28t50-10q27 0 50 10t40 27t28 41t10 50q0 27-10 50t-27 40t-41 28t-50 10m-256-328q0-41 17-80l106-232h517v512H640q0-53-20-99t-55-82t-81-55t-100-20q-42 0-81 13t-71 37t-56 57t-37 74q-11-27-11-53z"/></svg>
            <p>Estacionamiento</p>
        </a>
        <%}%>
        <% if (isAdmin) { %>
        <a href="Usuarios.jsp" class="imgContainer">
            <svg xmlns="http://www.w3.org/2000/svg"  viewBox="0 0 256 256">
                <path d="M198.13 194.85A8 8 0 0 1 192 208H24a8 8 0 0 1-6.12-13.15c14.94-17.78 33.52-30.41 54.17-37.17a68 68 0 1 1 71.9 0c20.65 6.76 39.23 19.39 54.18 37.17M255.18 154a8 8 0 0 1-6.94 4a7.9 7.9 0 0 1-4-1.07l-4.67-2.7a23.9 23.9 0 0 1-7.58 4.39V164a8 8 0 0 1-16 0v-5.38a23.9 23.9 0 0 1-7.58-4.39l-4.67 2.7a7.9 7.9 0 0 1-4 1.07a8 8 0 0 1-4-14.93l4.66-2.69a23.6 23.6 0 0 1 0-8.76l-4.66-2.69a8 8 0 1 1 8-13.86l4.67 2.7a23.9 23.9 0 0 1 7.58-4.39V108a8 8 0 0 1 16 0v5.38a23.9 23.9 0 0 1 7.58 4.39l4.67-2.7a8 8 0 1 1 8 13.86l-4.66 2.69a23.6 23.6 0 0 1 0 8.76l4.66 2.69a8 8 0 0 1 2.94 10.93M224 144a8 8 0 1 0-8-8a8 8 0 0 0 8 8" />
            </svg>
            <p>Usuarios</p>
        </a>
        <%}%>
        <% if(isGestor || isAprendiz){%>
        <a href="registros.jsp" class="imgContainer">
            <svg xmlns="http://www.w3.org/2000/svg"  viewBox="0 0 16 16"><path fill-rule="evenodd" d="M4.806.665A8 8 0 1 1 .612 11.07a.75.75 0 1 1 1.385-.575A6.5 6.5 0 1 0 2.523 4.5H4.25a.75.75 0 0 1 0 1.5H0V1.75a.75.75 0 0 1 1.5 0v1.586A8 8 0 0 1 4.806.666ZM8 3a.75.75 0 0 1 .75.75v3.94l2.034 2.034a.75.75 0 1 1-1.06 1.06L7.47 8.53l-.22-.22V3.75A.75.75 0 0 1 8 3" clip-rule="evenodd"/></svg>
            <p>Historial</p>
        </a>
        <% }%>
        <a href="reportes.jsp" class="imgContainer">
            <svg xmlns="http://www.w3.org/2000/svg"  viewBox="0 0 24 24">
                <path d="M12 17q.425 0 .713-.288T13 16t-.288-.712T12 15t-.712.288T11 16t.288.713T12 17m-1-4h2V7h-2zm-2.75 8L3 15.75v-7.5L8.25 3h7.5L21 8.25v7.5L15.75 21z" />
            </svg>
            <p>Reportes</p>
        </a>
        <% if (isAdmin){
        %>
        <a href="conf_Sistema.jsp" class="imgContainer">
            <svg xmlns="http://www.w3.org/2000/svg"  viewBox="0 0 48 48">
                <defs>
                    <mask id="IconifyId1922a7554cd195db71">
                        <g fill="none" stroke-linejoin="round" stroke-width="4">
                            <path fill="#fff" stroke="#fff"
                                  d="M36.686 15.171a15.4 15.4 0 0 1 2.529 6.102H44v5.454h-4.785a15.4 15.4 0 0 1-2.529 6.102l3.385 3.385l-3.857 3.857l-3.385-3.385a15.4 15.4 0 0 1-6.102 2.529V44h-5.454v-4.785a15.4 15.4 0 0 1-6.102-2.529l-3.385 3.385l-3.857-3.857l3.385-3.385a15.4 15.4 0 0 1-2.529-6.102H4v-5.454h4.785a15.4 15.4 0 0 1 2.529-6.102l-3.385-3.385l3.857-3.857l3.385 3.385a15.4 15.4 0 0 1 6.102-2.529V4h5.454v4.785a15.4 15.4 0 0 1 6.102 2.529l3.385-3.385l3.857 3.857z" />
                            <path  stroke="#000" d="M24 29a5 5 0 1 0 0-10a5 5 0 0 0 0 10Z" />
                        </g>
                    </mask>
                </defs>
                <path  d="M0 0h48v48H0z" mask="url(#IconifyId1922a7554cd195db71)" />
            </svg>
            <p>Conf. Sistema</p>
        </a>

        <% }
        %>
    </aside>
</section>
