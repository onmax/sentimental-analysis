google.charts.load('current', {
	'packages': ['table']
});
google.charts.setOnLoadCallback(drawSentencesTable);

function drawSentencesTable() {
	const buttons = document.querySelectorAll("button")
	var data = new google.visualization.DataTable();
	data.addColumn('string', 'Content');
	data.addColumn('number', 'Score');
	data.addColumn('number', 'Magnitude');
	rows = []
	peopleData.map((p, i) => {
		rows.push(p.sentences.map(s => [s.content, s.score, s.magnitude]))
		buttons[i].addEventListener("click", () => {
			data.removeRows(0, data.getNumberOfRows())
			data.addRows(rows[i])
			var table = new google.visualization.Table(document.getElementById('sentences'));
			table.draw(data, {
				showRowNumber: true,
				width: '100%',
			});
		})
	})

}