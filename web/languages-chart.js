google.charts.load('current', {
	'packages': ['table']
});
google.charts.setOnLoadCallback(drawTable);

function drawTable() {
	var data = new google.visualization.DataTable();
	data.addColumn('string', 'Name');
	data.addColumn('string', 'Language');
	data.addColumn('number', 'Score');
	data.addColumn('number', 'Magnitude');
	data.addColumn('number', 'Number of sentences');
	peopleData.map(p => {
		data.addRow([p.name, p.lang, p.score, p.magnitude, p.sentences.length])
	})

	var table = new google.visualization.Table(document.getElementById('languagesChart'));

	table.draw(data, {
		showRowNumber: true,
		width: '100%',
	});
}