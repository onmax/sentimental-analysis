google.charts.load('current', {
    'packages': ['corechart']
});
google.charts.setOnLoadCallback(drawSeriesChart);

function drawSeriesChart() {

    var data = google.visualization.arrayToDataTable([
        ['User', 'Magnitude', 'Score', '', 'Number of messages'],
        ['Alex', 20.12, 0.4, "red", 101],
        ['Raul', 12.54, -0.23, "green", 20],
        ['Gema', 25.4, 0.75, "blue", 45],
        ['Max', 4.04, -0.01, "yellow", 70]
    ]);

    var options = {
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
    };

    var chart = new google.visualization.BubbleChart(document.getElementById('chart_div'));
    chart.draw(data, options);
}