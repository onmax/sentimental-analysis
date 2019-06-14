google.charts.load("current", {
	packages: ["corechart"]
});
google.charts.setOnLoadCallback(drawPeopleSentiments);

function drawPeopleSentiments() {
	var data = google.visualization.arrayToDataTable([
		["Time", "Alex", "Gema"],
		[1, 0.08, -0.1],
		[2, 0.1, -0.5],
		[3, -0.65, 0.3],
		[4, -0.1, 0.6]
	]);

	var options = {
		title: "People's sentiments",
		legend: {
			position: "bottom"
		}
	};

	var chart = new google.visualization.LineChart(
		document.getElementById("peopleSentimentsChart")
	);

	chart.draw(data, options);
}
