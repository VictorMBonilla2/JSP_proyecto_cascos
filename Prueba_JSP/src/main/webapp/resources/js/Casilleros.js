document.addEventListener("DOMContentLoaded", function () {
  // Obtén todos los botones con la clase addCasilleroBtn
  let buttonsadd = document.getElementsByClassName("addCasilleroBtn");
  let buttonedit = document.getElementsByClassName("botones__ajustar");
  let buttonpay = document.getElementsByClassName("botones__pago");

  // Añade un event listener a cada botón pagar
  for (let i = 0; i < buttonpay.length; i++) {
    buttonpay[i].onclick = function () {
      let modalId = this.getAttribute("data-pay");
      let modal = document.getElementById(modalId);
      modal.style.display = "flex";
    };
  }
  // Añade un event listener a cada botón añadir
  for (let i = 0; i < buttonsadd.length; i++) {
    buttonsadd[i].onclick = function () {
      let modalId = this.getAttribute("data-add");
      let modal = document.getElementById(modalId);
      modal.style.display = "flex";
    };
  }
  // Añade un event listener a cada botón editar
  for (let i = 0; i < buttonedit.length; i++) {
    buttonedit[i].onclick = function () {
      let modalId = this.getAttribute("data-edit");
      let modal = document.getElementById(modalId);
      modal.style.display = "flex";
    };
  }

  // Obtén todos los elementos <span> que cierran los modales
  let closeButtons = document.getElementsByClassName("close");

  // Añade un event listener a cada botón de cierre
  for (let i = 0; i < closeButtons.length; i++) {
    closeButtons[i].onclick = function () {
      let modalId = this.getAttribute("data-modal-id");
      let modal = document.getElementById(modalId);
      modal.style.display = "none";
    };
  }

  // Cuando el usuario hace clic fuera del modal, cierra el modal
  window.onclick = function (event) {
    if (event.target.classList.contains("modal")) {
      event.target.style.display = "none";
    }
  };

  //Metodo uno para escuchar a los form

  // // Encuentra todos los formularios que empiezan con "addCasco"
  // const addforms = document.querySelectorAll('form[id^="addCasco"]');
  // const editforms = document.querySelectorAll('form[id^="editCasco"]');
  // const payforms = document.querySelectorAll('form[id^="payCasco"]');
  //
  // // Asigna el manejador de eventos a cada formulario
  // addforms.forEach(form => {
  //     form.onsubmit = function (addevent) {
  //         const espacioId = this.id.replace('addCasco', '');
  //         return validateAndSubmit(addevent, espacioId, 'add');
  //     };
  // });
  // editforms.forEach(form => {
  //     form.onsubmit = function (editevent) {
  //         const espacioId = this.id.replace('editCasco', '');
  //         return validateAndSubmit(editevent, espacioId, 'edit');
  //     };
  // });
  // payforms.forEach(form => {
  //     form.onsubmit = function (payevent) {
  //         const espacioId = this.id.replace('payCasco', '');
  //         return validateAndSubmit(payevent, espacioId, 'pay');
  //     };
  // });
});

function validateAndSubmit(addevent, espacioId, formType) {
  event.preventDefault(); // Previene el envío del formulario

  if (formType === "pay") {
    const form = document.getElementById(`${formType}Casco${espacioId}`);
    return payCasillero(form, espacioId, formType);
  } else if (formType === "add" || formType === "edit") {
    // Obtén los valores de los inputs
    const placa = document.getElementById(`${formType}placa${espacioId}`).value;
    const ciudad = document.getElementById(`${formType}ciudad${espacioId}`).value;
    const cantCascos = document.getElementById(`${formType}cant_cascos${espacioId}`).value;

    // Realiza la validación
    if (
      placa.trim() === "" ||
      ciudad.trim() === "" ||
      cantCascos.trim() === ""
    ) {
      alert("Por favor, llena todos los campos");
      return false; // Previene el envío del formulario si hay errores de validación
    }

    // Si la validación es exitosa, puedes proceder a enviar el formulario
    const form = document.getElementById(`${formType}Casco${espacioId}`);
    return addDataCasilleros(form, espacioId, formType); // Envía el formulario
  }
}
async function payCasillero(form, espacioID, formType) {
  try {
    const response = await fetch("SvCasillero", {
      method: "POST",
      headers: {
        "content-Type": "application/json",
      },
      body: JSON.stringify({
        espacio: espacioID,
        formType: formType,
      }),
    });

    if (response.status === 405) {
      const casillero = document.querySelector(`#casillero${espacioID}`);

       if (formType === "pay") {
        casillero.innerHTML = `
            <div class="casillero__title estilo__casillero">
                <h1>Espacio ${espacioID}</h1>
            </div>
            <div class="casillero__contenido estilo__contenido">
                <div class="contenido__info">
                    <div class="info__casillero">
                        <h1>Libre</h1>
                    </div>
                </div>
                <div class="contenido__botones">
                    <button class="botones botones--largo botones__pago addCasilleroBtn" data-add="addmodal${espacioID}">
                        Añadir
                    </button>
                </div>
            </div>
        `;

        const addModalHTML = `
        <div id="addmodal${espacioID}" class="modal">
          <div class="modal-content">
            <div class="modal-header">

              <h2>Casillero ${espacioID}</h2>
              <span class="close" data-modal-id="addmodal${espacioID}">×</span>
            </div>
            <div class="modal-body">
              <h2>Nuevo Casco</h2>
              <form id="addCasco${espacioID}" onsubmit="addCasco(event, ${espacioID})" class="formulario">
                <div class="formulario__inputs">
                  <input type="text" id="addplaca${espacioID}" placeholder="Placa" name="documento" required="">
                  <input type="text" id="addciudad${espacioID}" placeholder="Ciudad" name="ciudad" required="">
                  <input type="number" id="addcant_cascos${espacioID}" placeholder="Cantidad de cascos" name="cant_cascos"
                    required="">
                  <button type="submit" class="formulario__button">Añadir</button>
                </div>
              </form>
            </div>
          </div>
        </div>
    `;
        body.insertAdjacentHTML("beforeend", addModalHTML);

        const editModal = document.getElementById(`addmodal${espacioID}`);
        if (editModal) {
          editModal.remove();
        }
        const payModal = document.getElementById(`payModal${espacioID}`);
        if (payModal) {
          payModal.remove();
        }
      }
      return;
    }

    if (!response.ok) {
      throw new Error("Network response was not ok");
    }

    // Obtener el resultado de la respuesta
    const result = await response.json();

    // Verificar el resultado del inicio de sesión
    if (result.status === "success") {
      // Redireccionar al usuario a la página de inicio
      window.location.href = "SvCasillero";
    } else {
      // Mostrar mensaje de error en caso de credenciales inválidas
      document.getElementById("Error").style.display = "block";
    }
  } catch (error) {
    // Manejar cualquier error que ocurra durante la solicitud
    console.error("There was a problem with the fetch operation:", error);
    // Mostrar un mensaje de error genérico al usuario
    document.getElementById("Error").style.display = "block";
  }
}
async function addDataCasilleros(form, espacioId, formType) {
  const placa = document.getElementById(`${formType}placa${espacioId}`).value;
  const ciudad = document.getElementById(`${formType}ciudad${espacioId}`).value;
  const cantcascos = document.getElementById(
    `${formType}cant_cascos${espacioId}`
  ).value;
  try {
    // Enviar la solicitud fetch al servidor
    const response = await fetch("SvCasillero", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        espacio: espacioId,
        placa: placa,
        ciudad: ciudad,
        cantcascos: cantcascos,
        formType: formType,
      }),
    });

    if (response.status === 405) {
      if (formType === "add") {
        const tituloCasillero = casillero.querySelector("div.casillero__title");

        // Modificar el contenido de h1
        const h1 = tituloCasillero.querySelector("h1");
        h1.textContent = placa;

        // Crear y añadir el nuevo elemento p
        const p = document.createElement("p");
        p.textContent = ciudad;
        tituloCasillero.appendChild(p);

        // Crear nuevo contenido para contenido__info usando HTML
        const nuevoContenidoInfoHTML = `
        <div class="info__casillero">
            <h3>Casillero</h3>
            <p>2</p>
        </div>
        <div class="info__tiempo">
            <h3>Tiempo</h3>
            <p class="tiempo-transcurrido">0 horas y 9 minutos</p>
        </div>
        <div class="info__costo">
            <h3>Costo</h3>
            <p class="costo">$1.56</p>
        </div>
    `;

        // Limpiar y añadir el nuevo contenido a contenido__info
        const contenidoInfo = casillero.querySelector(".contenido__info");
        contenidoInfo.innerHTML = nuevoContenidoInfoHTML;

        // Crear nuevo contenido para contenido__botones usando HTML
        const nuevoContenidoBotonesHTML = `
        <button class="botones botones__pago" data-pay="paymodal2">Pagar</button>
        <button class="botones botones__ajustar" data-edit="editmodal2">Ajustar</button>
    `;

        // Limpiar y añadir el nuevo contenido a contenido__botones
        const contenidoBotones = casillero.querySelector(".contenido__botones");
        contenidoBotones.innerHTML = nuevoContenidoBotonesHTML;

        // Eliminar el modal de añadir
        const addModal = document.getElementById("addmodal1");
        if (addModal) {
          addModal.remove();
        }

        const body = document.querySelector("body");

        const payModalHTML = `
        <div id="paymodal2" class="modal">
          <div class="modal-content">
            <div class="modal-header">
              <h2>Casillero 2</h2>
              <span class="close" data-modal-id="paymodal2">×</span>
            </div>
            <div class="modal-body">
              <h2>Pagar Casco</h2>
              <form id="payCasco2" onsubmit="payCasco(event, 2)" class="formulario">
                <div class="formulario__inputs">
                  <p>Tiempo transcurrido: riwmpo</p>
                  <p>Costo Total: $1000</p>
                  <p>Placa: JHG-785</p>
                  <button type="submit" class="formulario__button">Añadir</button>
                </div>
              </form>
            </div>
          </div>
        </div>
    `;

        const editModalHTML = `
        <div id="editmodal2" class="modal">
          <div class="modal-content">
            <div class="modal-header">
              <h2>Casillero 2</h2>
              <span class="close" data-modal-id="editmodal2">×</span>
            </div>
            <div class="modal-body">
              <h2>Editar Casco</h2>
              <form id="editCasco2" onsubmit="editCasco(event, 2)" class="formulario">
                <div class="formulario__inputs">
                  <input type="text" id="editplaca2" placeholder="Placa" name="documento" value="JHG-785" required="">
                  <input type="text" id="editciudad2" placeholder="Ciudad" name="ciudad" value="Bucaramanga" required="">
                  <input type="number" id="editcant_cascos2" placeholder="Cantidad de cascos" name="cant_cascos" value="3" required="">
                  <button type="submit" class="formulario__button">Añadir</button>
                </div>
              </form>
            </div>
          </div>
        </div>
    `;

        // Insertar los nuevos modales en el body
        body.insertAdjacentHTML("beforeend", payModalHTML);
        body.insertAdjacentHTML("beforeend", editModalHTML);
      } else if (formType === "edit") {
        const placa_casillero = casillero.querySelector(
          "div.casillero__title>h1"
        );
        placa_casillero.textContent = "JHG-DSDA";
        const lugar_casillero = casillero.querySelector(
          "div.casillero__title>p"
        );
        lugar_casillero.textContent = "BARRANCA";
      }
      return
    }
    // Verificar si la respuesta es exitosa
    if (!response.ok) {
      throw new Error("Network response was not ok");
    }

    // Obtener el resultado de la respuesta
    const result = await response.json();

    // Verificar el resultado del inicio de sesión
    if (result.status === "success") {
      // Redireccionar al usuario a la página de inicio
      window.location.href = "SvCasillero";
    } else {
      // Mostrar mensaje de error en caso de credenciales inválidas
      document.getElementById("Error").style.display = "block";
    }
  } catch (error) {
    // Manejar cualquier error que ocurra durante la solicitud
    console.error("There was a problem with the fetch operation:", error);
    // Mostrar un mensaje de error genérico al usuario
    document.getElementById("Error").style.display = "block";
  }
}
//Metodo 2 para escuchar los formularios.

function addCasco(event, espacioId) {
  validateAndSubmit(event, espacioId, "add");
}

function editCasco(event, espacioId) {
  validateAndSubmit(event, espacioId, "edit");
}

function payCasco(event, espacioId) {
  validateAndSubmit(event, espacioId, "pay");
}
