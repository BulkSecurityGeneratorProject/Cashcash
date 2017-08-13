export default function Account($resource) {
    "ngInject";
    var service = $resource('api/account', {}, {
        'get': {
            method: 'GET', params: {}, isArray: false,
            interceptor: {
                response: function (response) {
                    // expose response
                    return response;
                }
            }
        }
    });

    return service;
};