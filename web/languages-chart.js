google.charts.load("current", { packages: ["treemap"] });
google.charts.setOnLoadCallback(drawLanguagesChart);
function drawLanguagesChart() {
	var data = google.visualization.arrayToDataTable([
		["User", "Parent"],
		["Alex", "english"],
		["Raul", "french"],
		["Gema", "spanish"],
		["Max", "spanish"]
	]);

	tree = new google.visualization.TreeMap(
		document.getElementById("languagesChart")
	);

	tree.draw(data, {
		minColor: "#f00",
		midColor: "#ddd",
		maxColor: "#0d0",
		headerHeight: 0,
		fontColor: "black",
		showScale: false
	});
}
