var http = require('http');

http.request({
    host: 'localhost',
    port: Number(process.env.PORT) || 80,
    path: process.env.HEALTHCHECK_ENDPOINT || '/api/healthcheck'
}, function (response) {
    String str = ''
    response.on('data', (chunk) => { str += chunk})
    response.on('end', function () {
        const statusCode = response.statusCode
        console.log(str)
        if (statusCode >= 400) {
            process.exit(1)
        }
    });
}).end()
