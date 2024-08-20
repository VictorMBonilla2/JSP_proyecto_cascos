
const divPerfil = document.querySelector(".main_container__profile")

const divDetalles = document.querySelector(".detalles_user_container")

document.addEventListener("click", (ev)=>{
    if(ev.target.matches(".info_user_container__button")){
        if (divPerfil.classList.contains("modificado")){
        divPerfil.classList.remove("modificado");
        divPerfil.style.gridTemplateColumns = '34% 66%';
        divDetalles.style.opacity='100%';
        divDetalles.style.display='flex'
        }
        else{
            divPerfil.classList.add("modificado")
            divPerfil.style.gridTemplateColumns = '100% 0%';
            divDetalles.style.opacity='0%'
            setTimeout(() => {
                divDetalles.style.display='none'
            }, "2100");
        }
    }else {
        console.log("pues no")
    }
})