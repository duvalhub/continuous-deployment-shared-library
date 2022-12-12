var http = require('http');

http.request({
    host: 'localhost',
    port:  80,
    path: '/api/healthcheck'
//    port: Number(process.env.PORT) || 80,
//    path: process.env.HEALTHCHECK_ENDPOINT || '/api/healthcheck'
}, function (response) {
    response.on('data', () => { })
    response.on('end', function () {
        const statusCode = response.statusCode
        if (statusCode >= 400) {
            process.exit(1)
        }
    });
}).end()
