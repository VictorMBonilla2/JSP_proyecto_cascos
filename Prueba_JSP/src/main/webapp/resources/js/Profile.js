
const divPerfil = document.querySelector(".main_container__profile")

const divDetalles = document.querySelector(".detalles_user_container")

const divPerfil2 = document.querySelector(".user_container_side2")

document.addEventListener("click", (ev)=>{
    if(ev.target.matches(".info_user_container__button")){
        if (divPerfil.classList.contains("modificado")){
        divPerfil.classList.remove("modificado");
        divPerfil.style.gridTemplateColumns = '34% 66%';
        divDetalles.style.opacity='100%';
        divDetalles.style.display='flex'
        divPerfil2.style.display='none'
        }
        else{
            divPerfil.classList.add("modificado")
            divPerfil.style.gridTemplateColumns = '100% 0%';
            divDetalles.style.opacity='0%'
            setTimeout(() => {
                divDetalles.style.display='none'
                divPerfil2.style.display='flex'
                const infoBody = document.querySelector(".info_user_container__body");
                infoBody.innerHTML = `
                <form id="userInfoForm">
                    <div>
                        <label for="nombre">Nombre:</label>
                        <input type="text" id="nombre" name="nombre" value="jose">
                    </div>
                    <div>
                        <label for="apellido">Apellido:</label>
                        <input type="text" id="apellido" name="apellido" value="juan">
                    </div>
                    <div>
                        <label for="fechaNacimiento">Fecha de nacimiento:</label>
                        <input type="date" id="fechaNacimiento" name="fechaNacimiento" value="1992-03-17">
                    </div>
                    <div>
                        <button type="button" class="formulario_login__button" id="submitForm">Enviar</button>
                        <button type="button" class="formulario_login__button" id="cancelEdit">Cancelar</button>
                    </div>
                </form>
            `;
            }, "2100");




        }
    }else {
        console.log("pues no")
    }
})