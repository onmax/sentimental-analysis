google.charts.load("current", {
	packages: ["corechart"]
});
google.charts.setOnLoadCallback(drawPeopleSentiments);

function drawPeopleSentiments() {

	const maxLength = Math.max.apply(Math, peopleData.map(p => p.sentences.length))
	const matrix = []
	for (let i = 0; i <= maxLength; i++) {
		matrix.push(new Array(peopleData.length * 2))
	}
	matrix[0][0] = "Time"

	for (let i = 2; i <= matrix[0].length; i += 2) {
		matrix[0][i] = {
			type: 'string',
			role: 'tooltip',
			p: {
				html: true
			}
		};
	}
	peopleData.map((person, i) => {
		matrix[0][i * 2 + 1] = person.name
		person.sentences.map((s, j) => {
			matrix[j + 1][0] = j + 1
			matrix[j + 1][i * 2 + 1] = s.score
			matrix[j + 1][i * 2 + 2] = `<div class="tooltip"><b>Score: </b> ${s.score} <br><b>Magnitude:</b> ${s.magnitude} <br><b>Content:</b> ${s.content}</div>`
		})
	})

	const data = google.visualization.arrayToDataTable(matrix);

	const options = {
		tooltip: {
			isHtml: true
		},
		title: "People's sentiments",
		legend: {
			position: "bottom"
		},
		hAxis: {
			title: 'Messages',
			format: '#'
		},
		vAxis: {
			title: 'Score',
		},
		colors: colors,
		legend: {
			position: 'right'
		}
	};

	const chart = new google.visualization.LineChart(
		document.getElementById("people-sentiments-chart")
	);

	chart.draw(data, options);
}