const casilleros = [
    {
        placa: 'ABC123',
        lugar: 'Parqueadero 1',
        numeroCasillero: 5,
        tiempo: '2 horas',
        costo: '$10'
    },
    {
        placa: 'XYZ789',
        lugar: 'Parqueadero 2',
        numeroCasillero: 10,
        tiempo: '1 hora',
        costo: '$5'
    },
    // Agrega más casilleros según sea necesario
];

function createCasillero(casillero) {
    const casilleroDiv = document.createElement('div');
    casilleroDiv.classList.add('casillero');

    const titleDiv = document.createElement('div');
    titleDiv.classList.add('casillero__title');
    const titleH1 = document.createElement('h1');
    titleH1.textContent = casillero.placa;
    const titleP = document.createElement('p');
    titleP.textContent = casillero.lugar;
    titleDiv.appendChild(titleH1);
    titleDiv.appendChild(titleP);

    const contenidoDiv = document.createElement('div');
    contenidoDiv.classList.add('casillero__contenido');

    const infoDiv = document.createElement('div');
    infoDiv.classList.add('contenido__info');

    const infoCasilleroDiv = document.createElement('div');
    infoCasilleroDiv.classList.add('info__casillero');
    const casilleroP1 = document.createElement('p');
    casilleroP1.textContent = 'Casillero';
    const casilleroP2 = document.createElement('p');
    casilleroP2.textContent = casillero.numeroCasillero;
    infoCasilleroDiv.appendChild(casilleroP1);
    infoCasilleroDiv.appendChild(casilleroP2);

    const infoTiempoDiv = document.createElement('div');
    infoTiempoDiv.classList.add('info__tiempo');
    const tiempoP1 = document.createElement('p');
    tiempoP1.textContent = 'Tiempo';
    const tiempoP2 = document.createElement('p');
    tiempoP2.textContent = casillero.tiempo;
    infoTiempoDiv.appendChild(tiempoP1);
    infoTiempoDiv.appendChild(tiempoP2);

    const infoCostoDiv = document.createElement('div');
    infoCostoDiv.classList.add('info__costo');
    const costoP1 = document.createElement('p');
    costoP1.textContent = 'Costo';
    const costoP2 = document.createElement('p');
    costoP2.textContent = casillero.costo;
    infoCostoDiv.appendChild(costoP1);
    infoCostoDiv.appendChild(costoP2);

    infoDiv.appendChild(infoCasilleroDiv);
    infoDiv.appendChild(infoTiempoDiv);
    infoDiv.appendChild(infoCostoDiv);

    const botonesDiv = document.createElement('div');
    botonesDiv.classList.add('contenido__botones');
    const botonPago = document.createElement('button');
    botonPago.classList.add('botones', 'botones__pago');
    botonPago.textContent = 'Pagar';
    const botonAjustar = document.createElement('button');
    botonAjustar.classList.add('botones', 'botones__ajustar');
    botonAjustar.textContent = 'Ajustar';
    botonesDiv.appendChild(botonPago);
    botonesDiv.appendChild(botonAjustar);

    contenidoDiv.appendChild(infoDiv);
    contenidoDiv.appendChild(botonesDiv);

    casilleroDiv.appendChild(titleDiv);
    casilleroDiv.appendChild(contenidoDiv);

    return casilleroDiv;
}

function renderCasilleros(casilleros) {
    const container = document.getElementById('casillerosContainer'); // Asegúrate de tener un contenedor con este ID en tu HTML
    container.innerHTML = ''; // Limpiar contenido previo
    casilleros.forEach(casillero => {
        const casilleroElement = createCasillero(casillero);
        container.appendChild(casilleroElement);
    });
}

// Llamar a la función para renderizar los casilleros
renderCasilleros(casilleros);
