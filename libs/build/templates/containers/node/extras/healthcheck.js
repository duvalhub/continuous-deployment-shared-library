var http = require('http');

http.request({
    host: 'localhost',
    port: Number(process.env.PORT) || 80,
    path: process.argv[2] || '/api/healthcheck'
}, function (response) {
    var str = ''
    response.on('data', (chunk) => { str += chunk})
    response.on('end', function () {
        const statusCode = response.statusCode
        console.log(str)
        if (statusCode >= 400) {
            process.exit(1)
        }
    });
}).end()
