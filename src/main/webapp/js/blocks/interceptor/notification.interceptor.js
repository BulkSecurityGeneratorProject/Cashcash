export default function notificationInterceptor($injector) {
    "ngInject";

    function response(response) {
        var alertKey = response.headers('X-cashcashApp-alert');
        if (angular.isString(alertKey)) {
            const alertService = $injector.get('AlertService');
            alertService.success(alertKey + response.headers('X-cashcashApp-params'));
        }
        return response;
    }

    var service = {
        response: response
    };

    return service;
}