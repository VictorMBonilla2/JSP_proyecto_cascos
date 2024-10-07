import {host} from "./config.js";
import {fetchVehiculos} from "./vehiculoSelect.js";
export async function renderEspacios() {
    const response = await fetch(`${host}/SvCasillero`);
    const espacios = await response.json();

    const fetchTiposReportes = await fetch(`${host}/listaTipoReportes`);
    const tiporeportes = await fetchTiposReportes.json();

    const sectoresArray = agruparEspaciosPorSector(espacios);
    renderTabs(sectoresArray, tiporeportes);
}


function renderTabs(sectores, tiporeportes) {
    const tabList = document.getElementById('tab-list');
    const tabContent = document.getElementById('tab-content');
    tabList.innerHTML=''
    tabContent.innerHTML=''
    sectores.forEach((sector, index) => {
        // Crear el tab del sector
        const tab = document.createElement('li');
        tab.classList.add('tab');
        tab.textContent = sector.nombre;
        tab.dataset.sectorId = sector.id;

        // Agregar el evento para mostrar el contenido del tab
        tab.addEventListener('click', () => {
            // Ocultar todo el contenido de los otros sectores
            document.querySelectorAll('.sector-content').forEach(content => {
                content.style.display = 'none';
            });

            // Mostrar solo el contenido del sector seleccionado
            const selectedContent = document.getElementById(`sector-content-${sector.id}`);
            if (selectedContent) {
                selectedContent.style.display = 'grid';
            }
        });

        // Agregar el tab a la lista de tabs
        tabList.appendChild(tab);

        // Crear el contenido del tab (los espacios del sector)
        const sectorContent = document.createElement('div');
        sectorContent.classList.add('sector-content');
        sectorContent.id = `sector-content-${sector.id}`;

        sector.espacios.forEach(espacio => {
            let espacioDiv;
            if (espacio.persona) {
                espacioDiv = renderEspacioOcupado(espacio, tiporeportes); // Usar tu función para renderizar un espacio ocupado
            } else {
                espacioDiv = renderEspacioLibre(espacio); // Usar tu función para renderizar un espacio libre
            }
            sectorContent.appendChild(espacioDiv);
        });

        // Inicialmente ocultamos todos los sectores menos el primero
        if (index > 0) {
            sectorContent.style.display = 'none';
        }

        tabContent.appendChild(sectorContent);
    });

    // Asegurarnos de que se active el primer tab después del renderizado
    setTimeout(() => {
        const firstTab = tabList.querySelector('li');
        if (firstTab) {
            firstTab.click();
        }
    }, 0);
}

function agruparEspaciosPorSector(espacios) {
    const sectoresArray = [];

    // Agrupar los espacios por sector
    espacios.forEach(espacio => {
        let sector = sectoresArray.find(s => s.id === espacio.sector.id_sector);
        // Si no existe aún el sector en el array, agregarlo
        if (!sector) {
            sector = {
                id: espacio.sector.id_sector,
                nombre: espacio.sector.nombre_sector,
                espacios: []
            };
            sectoresArray.push(sector);
        }

        // Añadir el espacio al sector correspondiente
        sector.espacios.push(espacio);
    });

    return sectoresArray;
}
function renderEspacioLibre(espacio) {
    const template = document.getElementById('template-espacio-libre'); // Tu template de espacio libre
    const clone = template.content.cloneNode(true);
    // Asignar valores
    clone.querySelector('.casillero__title h1').textContent = `${espacio.nombre}`;
    clone.querySelector('.addCasilleroBtn').setAttribute('data-add', `addmodal${espacio.id_espacio}`);
    clone.querySelector('.addCasilleroBtn').addEventListener("click", () => {
        modalAddEspacio(espacio)
    })
    // Retornar el clone para ser insertado en el DOM
    return clone;
}
function modalAddEspacio(Espacio){
    const template = document.getElementById('modal-ocupar-template');
    const clone = template.content.cloneNode(true);
    const modal = clone.querySelector('.modal');
    const closeBtn = clone.querySelector('.close');
    const inputDocumento = clone.querySelector('#idAprendiz');
    clone.querySelector('.nameEspacio').textContent = Espacio.nombre;
    clone.querySelector('#idEspacio').value = Espacio.id_espacio;
    modal.style.display='flex'
    modal.onclick= (e)=>{
        if(e.target.classList.contains('modal')){
            modal.remove();
        }
    }
    closeBtn.onclick = ()=>{
        modal.remove(); // Eliminar el modal
    }
    inputDocumento.onchange= (e)=>{
        fetchVehiculos(e.target.value)
    }
    document.body.appendChild(clone);
}
function modalFreeEspacio(Espacio){
    const template = document.getElementById('modal-liberar-template');
    const clone = template.content.cloneNode(true);
    const modal = clone.querySelector('.modal');
    const closeBtn = clone.querySelector('.close');
    clone.querySelector('.nameEspacio').textContent = Espacio.nombre;
    clone.querySelector('#idEspacio').value = Espacio.id_espacio;
    modal.style.display='flex'
    modal.onclick= (e)=>{
        if(e.target.classList.contains('modal')){
            modal.remove();
        }
    }
    closeBtn.onclick = ()=>{
        modal.remove(); // Eliminar el modal
    }
    clone.querySelector('.document').value=Espacio.persona.documento
    clone.querySelector('.nameAprendiz').value=Espacio.persona.nombreAprendiz
    clone.querySelector('.Placa').value=Espacio.placa;

    document.body.appendChild(clone);
}
function modalEditEspacio(Espacio){
    const template = document.getElementById('modal-editar-template');
    const clone = template.content.cloneNode(true);
    const modal = clone.querySelector('.modal');
    const closeBtn = clone.querySelector('.close');
    clone.querySelector('.nameEspacio').textContent = Espacio.nombre;
    clone.querySelector('#idEspacio').value = Espacio.id_espacio;
    modal.style.display='flex'
    modal.onclick= (e)=>{
        if(e.target.classList.contains('modal')){
            modal.remove();
        }
    }
    closeBtn.onclick = ()=>{
        modal.remove(); // Eliminar el modal
    }
    clone.querySelector('#editdocumento').value=Espacio.persona.documento
    clone.querySelector('#editcant_cascos').value=Espacio.persona.nombreAprendiz

    document.body.appendChild(clone);
}
function modalReportEspacio(Espacio,tipoReportes){
    const template = document.getElementById('modal-reporte-template');
    const clone = template.content.cloneNode(true);
    const modal = clone.querySelector('.modal');
    const closeBtn = clone.querySelector('.close');
    const selectColors= clone.querySelector('#reportTipo');
    clone.querySelector('.nameEspacio').textContent = Espacio.nombre;
    clone.querySelector('#idEspacio').value = Espacio.id_espacio;
    modal.style.display='flex'
    modal.onclick= (e)=>{
        if(e.target.classList.contains('modal')){
            modal.remove();
        }
    }
    closeBtn.onclick = ()=>{
        modal.remove(); // Eliminar el modal
    }
    tipoReportes.forEach(tReporte => {
        const option = document.createElement('option');
        option.value = tReporte;
        option.textContent = tReporte.charAt(0) + tReporte.slice(1).toLowerCase(); // Formato legible
        selectColors.appendChild(option);
    });
    document.body.appendChild(clone);
}
function renderEspacioOcupado(espacio,tiporeportes) {
    const template = document.getElementById('template-espacio-ocupado'); // Tu template de espacio ocupado
    const clone = template.content.cloneNode(true);

    // Seleccionar los elementos del template y asignar los valores
    clone.querySelector('.casillero__title h1').textContent = `Espacio ${espacio.nombre}`;
    clone.querySelector('.casillero__title p').textContent = espacio.persona ? espacio.persona.documento : 'N/A';
    clone.querySelector('.info__casillero p').textContent = espacio.persona ? espacio.persona.documento : 'N/A';
    clone.querySelector('.info__tiempo p').textContent = espacio.vehiculo ? espacio.vehiculo.placa : 'N/A';
    clone.querySelector('.info__costo p').textContent = espacio.cantidad_cascos;

    // Agregar los IDs y atributos específicos
    clone.querySelector('.botones__pago').setAttribute('data-pay', `paymodal${espacio.id_espacio}`);
    clone.querySelector('.botones__pago').addEventListener("click", () => {
        modalFreeEspacio(espacio)
    })
    clone.querySelector('.botones__ajustar').setAttribute('data-edit', `editmodal${espacio.id_espacio}`);
    clone.querySelector('.botones__ajustar').addEventListener("click", () => {
        modalEditEspacio(espacio)
    })
    clone.querySelector('.report__img').setAttribute('data-report', `reportmodal${espacio.id_espacio}`);
    clone.querySelector('.report__img').addEventListener("click", () => {
        modalReportEspacio(espacio, tiporeportes)
    })
    // Retornar el clone para que luego sea insertado en el DOM
    return clone;
}

document.addEventListener("DOMContentLoaded", async ()=> {
    renderEspacios();
})