
const divPerfil = document.querySelector(".main_container__profile");

const divDetalles = document.querySelector(".detalles_user_container");

const divPerfil2 = document.querySelector(".user_container_side2");

const infoContainer= document.querySelector("#userinfo");

const infoTemplate = document.querySelector("#infoTemplate").content;

const formTemplate = document.querySelector("#formTemplate").content;

const buttonsInfoContainer = document.querySelector(".button_miranose");

const cancelButtonInfoContainer= document.querySelector("#cancelEdit");

document.addEventListener("click", (ev)=>{
    if(ev.target.matches(".info_user_container__button")){


        mostrarFormulario();
    }else if(ev.target===cancelButtonInfoContainer){

            mostrarInformacion();

    }
    else {
        console.log("pues no")
    }
})

function mostrarFormulario (){
    divPerfil.classList.add("modificado")
    divPerfil.style.gridTemplateColumns = '100% 0%';
    divDetalles.style.opacity='0%'
    setTimeout(() => {
        divDetalles.style.display='none'
        divPerfil2.style.display='flex'
        infoContainer.innerHTML="";
        infoContainer.appendChild(formTemplate.cloneNode(true));
        buttonsInfoContainer.style.display="flex"
    }, "2100");
}

const mostrarInformacion= () => {
    divPerfil.classList.remove("modificado");
    divPerfil.style.gridTemplateColumns = '34% 66%';
    divDetalles.style.opacity='100%';
    divDetalles.style.display='flex'
    divPerfil2.style.display='none'
    infoContainer.innerHTML="";
    infoContainer.appendChild(infoTemplate.cloneNode(true));
    buttonsInfoContainer.style.display="none"
}