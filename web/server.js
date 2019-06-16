var fs = require("fs")
var express = require('express');
var app = express();
var path = require('path');
// viewed at http://localhost:8080
app.use(express.static('.'));
app.get('/', function(req, res) {
    res.sendFile(path.join(__dirname + '/index.html'));
});
app.get('/data', function(req, res) {
    const data = fs.readFileSync("../jade/output.json",'utf8')
    res.send(data);
});
app.listen(8080);