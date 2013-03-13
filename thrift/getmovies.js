var thrift = require('thrift');
var ttransport = require('/thrift/transport.js');

var  QGen = require('./gen-nodejs/QGen')
    ttypes = require('./gen-nodejs/kucoo_types');


var connection = thrift.createConnection("localhost", 9090),
    client = thrift.createClient(QGen, connection);

//var connection = thrift.createConnection('localhost', 9090, {transport: ttransport.TBufferedTransport}),
//    client = thrift.createClient(QGen, connection);


console.log("here");

connection.on('error', function(err) {
  console.error(err);
});


client.getPopularFilms("1", function(err, data) {
  if (err) {
    console.err("Error : " + err);
  } else {
    console.log("Here is the response:");
    console.log(data); 
  }
  connection.end();
});


