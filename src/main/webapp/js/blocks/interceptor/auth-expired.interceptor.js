export default function authExpiredInterceptor($q, $injector, $localStorage, $sessionStorage) {
    "ngInject";

    function responseError(response) {
        if (response.status === 401) {
            delete $localStorage.authenticationToken;
            delete $sessionStorage.authenticationToken;
            var Principal = $injector.get('Principal');
            if (Principal.isAuthenticated()) {
                var Auth = $injector.get('Auth');
                Auth.authorize(true);
            }
        }
        return $q.reject(response);
    }

    var service = {
        responseError: responseError
    };

    return service;
};