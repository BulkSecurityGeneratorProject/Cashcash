export default function authInterceptor($localStorage, $sessionStorage) {
    "ngInject";

    function request(config) {
        /*jshint camelcase: false */
        config.headers = config.headers || {};
        var token = $localStorage.authenticationToken || $sessionStorage.authenticationToken;

        if (token) {
            config.headers.Authorization = 'Bearer ' + token;
        }

        return config;
    }

    var service = {
        request: request
    };

    return service;
};