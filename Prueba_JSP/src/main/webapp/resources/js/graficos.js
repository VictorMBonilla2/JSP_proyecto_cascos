document.addEventListener("DOMContentLoaded", async () => {
    const ctx = document.getElementById("myChart").getContext("2d"); // Asegúrate de obtener el contexto 2D

    try {
        // Realizar la solicitud fetch
        const response = await fetch("SvHome"); // Ruta del servlet
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const jsonResponse = await response.json(); // Parsear el JSON

        // Asegúrate de que jsonResponse contiene la propiedad 'data'
        if (!jsonResponse.data) {
            console.error('No data property found in JSON response');
            return;
        }

        // Extraer la propiedad 'data' del objeto JSON
        const data = jsonResponse.data;

        // Verifica los datos recibidos
        console.log('Data received:', data);

        // Convertir fechas a nombres de días
        const dateToDayName = date => {
            const daysOfWeek = [ "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado","Domingo",];
            const dayIndex = new Date(date).getDay();
            return daysOfWeek[dayIndex];
        };

        // Ordenar las fechas y mapear a nombres de días
        const sortedLabels = Object.keys(data).sort((a, b) => new Date(a) - new Date(b));
        const sortedDayNames = sortedLabels.map(date => dateToDayName(date));
        const sortedValues = sortedLabels.map(label => data[label]);

        // Crear el gráfico
        const myChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: sortedDayNames,
                datasets: [{
                    label: 'Cascos',
                    data: sortedValues,
                    backgroundColor: 'rgba(75, 192, 192, 0.2)',
                    borderColor: 'rgba(75, 192, 192, 1)',
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
                        display: true,
                        position: 'top'
                    }
                }
            }
        });
    } catch (error) {
        console.error('Error fetching data:', error);
    }
});

