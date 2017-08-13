export default function errorHandlerInterceptor($q, $rootScope) {
    "ngInject";

    function responseError(response) {
        if (!(response.status === 401 && (response.data === '' || (response.data.path && response.data.path.indexOf('/api/account') === 0 )))) {
            $rootScope.$emit('cashcashApp.httpError', response);
        }
        return $q.reject(response);
    }

    var service = {
        responseError: responseError
    };

    return service;
};