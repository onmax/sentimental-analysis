google.charts.load('current', {
    'packages': ['corechart']
});
google.charts.setOnLoadCallback(drawChart);

function drawChart() {
    const data = new google.visualization.DataTable();
    data.addColumn('number', 'score');
    data.addColumn('number', 'magnitude');
    data.addColumn({
        type: 'string',
        role: 'tooltip'
    });
    data.addColumn({
        type: 'string',
        role: 'style'
    });
    peopleData.map(p => {
        data.addRows(p.sentences.map(s => {
            return [s.score, s.magnitude, s.content, `point { fill-color: ${p.color}; }`]
        }))
    })

    const options = {
        title: 'Scores and magnitudes',
        hAxis: {
            title: 'Score',
            maxValue: 1,
            minValue: -1
        },
        vAxis: {
            title: 'Magnitude',
        },
        legend: 'none'
    };

    const chart = new google.visualization.ScatterChart(document.getElementById('scatter-chart'));

    chart.draw(data, options);
}