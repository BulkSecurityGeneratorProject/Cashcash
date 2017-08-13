import angular from "angular";

export default function AuthServerProvider($http, $localStorage, $sessionStorage, $q) {
    "ngInject";

    function getToken() {
        return $localStorage.authenticationToken || $sessionStorage.authenticationToken;
    }

    function hasValidToken() {
        var token = getToken();
        return token && token.expires && token.expires > new Date().getTime();
    }

    function storeAuthenticationToken(jwt, rememberMe) {
        if (rememberMe) {
            $localStorage.authenticationToken = jwt;
        } else {
            $sessionStorage.authenticationToken = jwt;
        }
    }

    function loginWithToken(jwt, rememberMe) {
        var deferred = $q.defer();

        if (angular.isDefined(jwt)) {
            storeAuthenticationToken(jwt, rememberMe);
            deferred.resolve(jwt);
        } else {
            deferred.reject();
        }

        return deferred.promise;
    }

    function logout() {
        delete $localStorage.authenticationToken;
        delete $sessionStorage.authenticationToken;
    }

    var service = {
        getToken: getToken,
        hasValidToken: hasValidToken,
        loginWithToken: loginWithToken,
        storeAuthenticationToken: storeAuthenticationToken,
        logout: logout
    };

    function login(credentials) {

        var data = {
            username: credentials.username,
            password: credentials.password,
            rememberMe: credentials.rememberMe
        };

        function authenticateSuccess(response) {
            var bearerToken = response.headers('Authorization');
            if (angular.isDefined(bearerToken) && bearerToken.slice(0, 7) === 'Bearer ') {
                var jwt = bearerToken.slice(7, bearerToken.length);
                service.storeAuthenticationToken(jwt, credentials.rememberMe);
                return jwt;
            }
        }

        return $http.post('api/authenticate', data).then(authenticateSuccess);
    }

    service.login = login;

    return service;
};
