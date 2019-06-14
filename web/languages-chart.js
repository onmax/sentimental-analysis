google.charts.load("current", {
	packages: ["treemap"]
});
google.charts.setOnLoadCallback(drawLanguagesChart);

function drawLanguagesChart() {
	var data = google.visualization.arrayToDataTable([
		["Language", "Number of people"],
		["English", 1],
		["Spanish", 2],
		["French", 1],
	]);

	var view = new google.visualization.DataView(data);
	view.setColumns([0, 1]);

	var options = {
		title: "Languages spoken",
	};
	var chart = new google.visualization.ColumnChart(document.getElementById("languagesChart"));
	chart.draw(view, options);
}