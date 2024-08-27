import {sendRequest} from "./ajax.js";

document.addEventListener('DOMContentLoaded', async function (){
    const DocumentoAprendiz = document.querySelector("#documentoUser").value;
    const EnviarEdicion = document.querySelector("#sendEdit");
    const EnviarCreacion = document.querySelector("#sendCreate");
    const CancelarEdicion= document.querySelector("#cancelEdit");
    const formVehiculos= document.querySelector("#formVehiculos");
    const vehiculoList = document.querySelector(".vehiculo__grid");
    console.log('DocumentoAprendiz:', DocumentoAprendiz); // Verifica el valor
    EnviarEdicion.style.display="none"
    let vehiculos = await ObtenerVehiculos(DocumentoAprendiz);

     if (vehiculos.length > 0) {
        vehiculoList.innerHTML = '';

        vehiculos.forEach((vehiculo, index) => {
            const div = document.createElement("div");
            div.classList.add("vehiculo__list__item");

            div.setAttribute('data-vehiculo', index);

            const placaP = document.createElement('p');
            placaP.textContent = vehiculo.placa;
            div.appendChild(placaP);

            const marcaP = document.createElement('p');
            marcaP.textContent = vehiculo.marca;
            div.appendChild(marcaP);

            const tipoP = document.createElement('p');
            tipoP.textContent = vehiculo.tipo;
            div.appendChild(tipoP);

            vehiculoList.appendChild(div);

            div.addEventListener('click', () => {
                llenarFormulario(vehiculo);
                EnviarEdicion.style.display="block"
                EnviarCreacion.style.display="none"
            });
        });

        const addItem = document.createElement('div');
        addItem.classList.add('vehiculo__list__item', 'add');
        addItem.addEventListener("click", ()=>{
            vaciarFormulario();
            EnviarEdicion.style.display="none"
            EnviarCreacion.style.display="block"
        })
        const addText = document.createElement('p');
        addText.textContent = 'Add';
        addItem.appendChild(addText);

        vehiculoList.appendChild(addItem);
    } else {
        console.log('No se encontraron vehículos.');
    }
     document.addEventListener("click", async (event) => {
         if(event.target === EnviarCreacion){
             const placa = document.querySelector("#placaVehiculo").value ;
             const marca = document.querySelector("#marcaVehiculo").value ;
             const modelo =  document.querySelector("#modeloVehiculo").value ;
             const tipo =  document.querySelector("#tipoVehiculo").value ;
             const cant_casco =  document.querySelector("#cantCasco").value
             const color =  document.querySelector("#colorVehiculo").value ;
             const ciudad =  document.querySelector("#ciudadVehiculo").value ;
             console.log(DocumentoAprendiz)
             console.log(placa);
             console.log(marca);
             console.log(modelo)
             console.log(tipo)
             console.log(cant_casco)
             console.log(color)
             console.log(ciudad)
             const data ={
                 "user": DocumentoAprendiz,
                 "action": "add",
                 "placa_vehiculo": placa,
                 "marca_vehiculo":marca,
                 "modelo_vehiculo":modelo,
                 "tipo_vehiculo":tipo,
                 "cantidad_cascos":cant_casco,
                 "color":color,
                 "ciudad":ciudad
             }
             await sendRequest("/Prueba_JSP_war_exploded/Vehiculo",data)

         }
     })

})
async function ObtenerVehiculos(DocumentoAprendiz){

    const response = await fetch(`/Prueba_JSP_war_exploded/Vehiculo?documento=${DocumentoAprendiz}`);
    if (response.status === 204) {
        console.log('No se encontraron vehículos.');
        return []; // o alguna otra acción
    }
    const data = await response.json();
    if (data.length === 0) {
        console.log('No se encontraron vehículos.');
    } else {
        return data;
    }
}
function llenarFormulario(vehiculo) {
    // Llenar el formulario con la información del vehículo seleccionado
    document.querySelector("#placaVehiculo").value = vehiculo.placa;
    document.querySelector("#marcaVehiculo").value = vehiculo.marca;
    document.querySelector("#modeloVehiculo").value = vehiculo.modelo;
    document.querySelector("#ciudadVehiculo").value = vehiculo.ciudad;
    document.querySelector("#colorVehiculo").value = vehiculo.color_vehiculo;
    document.querySelector("#tipoVehiculo").value = vehiculo.tipo_vehiculo;

    const cascoCantidad = vehiculo.cantidad_cascos;
    if(cascoCantidad >0){
        document.querySelector("#cascoConfirm").checked = true;
    }else {
        document.querySelector("#cascoConfirm").checked = false;
    }
    document.querySelector("#cantCasco").value = vehiculo.cantidad_cascos;

}

function vaciarFormulario() {
    // Vaciar los campos del formulario
    document.querySelector("#placaVehiculo").value = '';
    document.querySelector("#marcaVehiculo").value = '';
    document.querySelector("#modeloVehiculo").value = '';
    document.querySelector("#tipoVehiculo").value = '';
    document.querySelector("#cascoConfirm").checked = false;
    document.querySelector("#cantCasco").value = '';
    document.querySelector("#colorVehiculo").value = '';
    document.querySelector("#ciudadVehiculo").value = '';
}