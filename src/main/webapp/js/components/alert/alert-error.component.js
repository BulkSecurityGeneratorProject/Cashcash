import angular from "angular";

class JhiAlertErrorController {
    constructor($scope, AlertService, $rootScope, toastr) {
        "ngInject";
        this.$scope = $scope;
        this.AlertService = AlertService;
        this.$rootScope = $rootScope;
        this.toastr = toastr;
    }

    $onInit() {
        this.cleanHttpErrorListener = this.$rootScope.$on('cashcashApp.httpError', function (event, httpResponse) {
            let i;
            event.stopPropagation();
            switch (httpResponse.status) {
                // connection refused, server not reachable
                case 0:
                    this.toastr.error('Server not reachable', 'Error');
                    break;

                case 400:
                    const errorHeader = httpResponse.headers('X-cashcashApp-error');
                    const entityKey = httpResponse.headers('X-cashcashApp-params');
                    if (errorHeader) {
                        const entityName = entityKey;
                        this.toastr.error(errorHeader + 'entity name=' + entityName, 'Error');
                    } else if (httpResponse.data && httpResponse.data.fieldErrors) {
                        for (i = 0; i < httpResponse.data.fieldErrors.length; i++) {
                            const fieldError = httpResponse.data.fieldErrors[i];
                            // convert 'something[14].other[4].id' to 'something[].other[].id' so translations can be written to it
                            const convertedField = fieldError.field.replace(/\[\d*\]/g, '[]');
                            const fieldName = convertedField.charAt(0).toUpperCase() + convertedField.slice(1);
                            this.toastr.error('Field ' + fieldName + ' cannot be empty', 'error.' + fieldError.message, 'Error');
                        }
                    } else if (httpResponse.data && httpResponse.data.message) {
                        this.toastr.error(httpResponse.data.message, 'Error');
                    } else {
                        this.toastr.error(httpResponse.data, 'Error');
                    }
                    break;

                case 404:
                    this.toastr.error('Not found', 'Error');
                    break;

                default:
                    if (httpResponse.data && httpResponse.data.message) {
                        this.toastr.error(httpResponse.data.message, 'Error');
                    } else {
                        this.toastr.error(httpResponse.data, 'Error');
                    }
            }
        }.bind(this));

        this.$scope.$on('$destroy', function () {
            if (angular.isDefined(this.cleanHttpErrorListener) && this.cleanHttpErrorListener !== null) {
                this.cleanHttpErrorListener();
            }
        }.bind(this));
    }
}


export default {
    controller: JhiAlertErrorController
};
