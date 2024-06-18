const ctx = document.getElementById("myChart")
const names = ["Lunes", "Martes", "Miercoles", "Jueves","Viernes","Sabado", "Domingo"];
const ages = [19, 21, 18, 33, 11, 45, 54];

const myChart = new Chart(ctx, {
    type: 'bar',
    data: {
        labels: names,
        datasets: [{
            label: 'Cascos',
            data: ages,
            backgroundColor: [
                'rgba(75, 192, 192, 0.2)',
                'rgba(54, 162, 235, 0.2)',
                'rgba(255, 206, 86, 0.2)',
                'rgba(153, 102, 255, 0.2)'
            ],
            borderColor: [
                'rgba(75, 192, 192, 1)',
                'rgba(54, 162, 235, 1)',
                'rgba(255, 206, 86, 1)',
                'rgba(153, 102, 255, 1)'
            ],
            borderWidth: 1
        }]
    },
    options: {
        responsive: true,
        maintainAspectRatio: false,
        scales: {
            y: {
                beginAtZero: true
            }
        },
        plugins: {
            legend: {
                display: true, // Mostrar la leyenda
                position: 'top', // Posición de la leyenda ('top', 'left', 'bottom', 'right')
                labels: {
                    color: 'rgb(255, 99, 132)', // Color de las etiquetas de la leyenda
                    text: "Cascos.",
                    font: {
                        size: 14 // Tamaño de la fuente de las etiquetas

                    }
                },
                title: {
                    display: true, // Mostrar el título de la leyenda
                    text: "Cascos gestionados esta semana.", // Texto del título de la leyenda
                    color: 'rgb(255, 99, 132)', // Color del título de la leyenda
                    font: {
                        size: 16 // Tamaño de la fuente del título
                    }
                }
            }
        },
    }
});