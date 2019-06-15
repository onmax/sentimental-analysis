google.charts.load('current', {
    'packages': ['corechart']
});
google.charts.setOnLoadCallback(drawSeriesChart);

function drawSeriesChart() {
    const matrix = []
    matrix.push(['User', 'Magnitude', 'Score', '', 'Number of messages'])
    peopleData.map(p => {
        matrix.push([p.name, p.magnitude, p.score, p.name, p.sentences.length])
    })
    const data = google.visualization.arrayToDataTable(matrix);
    const options = {
        title: 'Relation between score and magnitude',
        hAxis: {
            title: 'Magnitude'
        },
        vAxis: {
            title: 'Score',
            maxValue: 1,
            minValue: -1
        },
        sizeAxis: {
            minSize: 15
        },
        bubble: {
            textStyle: {
                fontSize: 11
            }
        },
        legend: 'none'
    };

    const chart = new google.visualization.BubbleChart(document.getElementById('magnitude-score-chart'));
    chart.draw(data, options);
}