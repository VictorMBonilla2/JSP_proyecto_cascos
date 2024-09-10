import {sendRequest} from "./ajax.js";

document.addEventListener('DOMContentLoaded', async function (){
    const DocumentoAprendiz = document.querySelector("#documentoUser").value;
    const ejecutor = document.querySelector(".accionador");
    const vehiculoList = document.querySelector(".vehiculo__grid");
    console.log('DocumentoAprendiz:', DocumentoAprendiz); // Verifica el valor
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
                ejecutor.id="sendEdit";
                ejecutor.textContent="Editar"
            });
        });

        const addItem = document.createElement('div');
        addItem.classList.add('vehiculo__list__item', 'add');
        addItem.addEventListener("click", ()=>{
            vaciarFormulario();
            ejecutor.id="sendCreate";
            ejecutor.textContent="Crear"
        })
        const addText = document.createElement('p');
        addText.textContent = 'Add';
        addItem.appendChild(addText);

        vehiculoList.appendChild(addItem);
    } else {
        console.log('No se encontraron vehículos.');
    }
     document.addEventListener("click", async (event) => {
         if(event.target.id==="sendCreate"){
             const placa = document.querySelector("#placaVehiculo").value ;
             const marca = document.querySelector("#marcaVehiculo").value ;
             const modelo =  document.querySelector("#modeloVehiculo").value ;
             const tipo =  document.querySelector("#tipoVehiculo").value ;
             const cant_casco =  document.querySelector("#cantCasco").value
             const color =  document.querySelector("#colorVehiculo").value ;
             const ciudad =  document.querySelector("#ciudadVehiculo").value ;
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
         if(event.target.id === "sendEdit"){
             const id_vehiculo = document.querySelector("#idVehiculo").value;
             const placa = document.querySelector("#placaVehiculo").value ;
             const marca = document.querySelector("#marcaVehiculo").value ;
             const modelo =  document.querySelector("#modeloVehiculo").value ;
             const tipo =  document.querySelector("#tipoVehiculo").value ;
             const cant_casco =  document.querySelector("#cantCasco").value
             const color =  document.querySelector("#colorVehiculo").value ;
             const ciudad =  document.querySelector("#ciudadVehiculo").value ;
             const data ={
                 "user": DocumentoAprendiz,
                 "action": "edit",
                 "id_vehiculo":id_vehiculo,
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
        return [];
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
    document.querySelector("#idVehiculo").value = vehiculo.id_vehiculo;
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