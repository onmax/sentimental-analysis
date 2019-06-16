var fs = require("fs")
var express = require('express');
var app = express();
var path = require('path');

app.use(express.static('.'));
app.get('/', function (req, res) {
    res.sendFile(path.join(__dirname + '/index.html'));
});
app.get('/songs', function (req, res) {
    res.sendFile(path.join(__dirname + '/index.html'));
});
app.get('/3-little-pigs', function (req, res) {
    res.sendFile(path.join(__dirname + '/index.html'));
});
app.get('/data', function (req, res) {
    const data = fs.readFileSync("../jade/output.json", 'utf8')
    res.send(data);
});

app.get('/songs-data', function (req, res) {
    let json = []
    const songs = ['i-want-to-break-free', 'yesterday', 'yellow-submarine', 'dont-stop-me-now']
    songs.map(s => {
        const data = fs.readFileSync(path.join(__dirname + `/examples/${s}.json`), 'utf8')
        json.push(JSON.parse(data)[0])
    })
    res.send(json);
});

app.get('/3-little-pigs-data', function (req, res) {
    const data = fs.readFileSync(path.join(__dirname + '/examples/3-little-pigs.json'), 'utf8')
    res.send(data);
});

app.listen(8080)