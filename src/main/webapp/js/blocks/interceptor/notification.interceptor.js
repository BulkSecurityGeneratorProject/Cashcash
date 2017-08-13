export default function notificationInterceptor(AlertService) {
    "ngInject";

    function response(response) {
        var alertKey = response.headers('X-cashcashApp-alert');
        if (angular.isString(alertKey)) {
            AlertService.success(alertKey, {param: response.headers('X-cashcashApp-params')});
        }
        return response;
    }

    var service = {
        response: response
    };

    return service;
}