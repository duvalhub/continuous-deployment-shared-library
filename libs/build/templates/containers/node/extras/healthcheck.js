var http = require('http');

http.request({
    host: 'localhost',
    port: 3001,
    path: '/api/healthcheck'
}, function (response) {
    response.on('data', () => { })
    response.on('end', function () {
        const statusCode = response.statusCode
        if (statusCode >= 400) {
            process.exit(1)
        }
    });
}).end()
