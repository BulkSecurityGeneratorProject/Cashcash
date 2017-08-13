import angular from "angular";

class JhiAlertErrorController {
    constructor($scope, AlertService, $rootScope) {
        "ngInject";
        this.$scope = $scope;
        this.AlertService = AlertService;
        this.$rootScope = $rootScope;
    }

    $onInit() {
        this.alerts = this.AlertService.get();

        this.cleanHttpErrorListener = this.$rootScope.$on('cashcashApp.httpError', function (event, httpResponse) {
            let i;
            event.stopPropagation();
            switch (httpResponse.status) {
                // connection refused, server not reachable
                case 0:
                    this.addErrorAlert('Server not reachable', 'error.server.not.reachable');
                    break;

                case 400:
                    const errorHeader = httpResponse.headers('X-cashcashApp-error');
                    const entityKey = httpResponse.headers('X-cashcashApp-params');
                    if (errorHeader) {
                        const entityName = entityKey;
                        this.addErrorAlert(errorHeader, errorHeader, {entityName: entityName});
                    } else if (httpResponse.data && httpResponse.data.fieldErrors) {
                        for (i = 0; i < httpResponse.data.fieldErrors.length; i++) {
                            const fieldError = httpResponse.data.fieldErrors[i];
                            // convert 'something[14].other[4].id' to 'something[].other[].id' so translations can be written to it
                            const convertedField = fieldError.field.replace(/\[\d*\]/g, '[]');
                            const fieldName = convertedField.charAt(0).toUpperCase() + convertedField.slice(1);
                            this.addErrorAlert('Field ' + fieldName + ' cannot be empty', 'error.' + fieldError.message, {fieldName: fieldName});
                        }
                    } else if (httpResponse.data && httpResponse.data.message) {
                        this.addErrorAlert(httpResponse.data.message, httpResponse.data.message, httpResponse.data);
                    } else {
                        this.addErrorAlert(httpResponse.data);
                    }
                    break;

                case 404:
                    this.addErrorAlert('Not found', 'error.url.not.found');
                    break;

                default:
                    if (httpResponse.data && httpResponse.data.message) {
                        this.addErrorAlert(httpResponse.data.message);
                    } else {
                        this.addErrorAlert(angular.toJson(httpResponse));
                    }
            }
        }.bind(this));

        this.$scope.$on('$destroy', function () {
            if (angular.isDefined(this.cleanHttpErrorListener) && this.cleanHttpErrorListener !== null) {
                this.cleanHttpErrorListener();
                this.alerts = [];
            }
        }.bind(this));
    }

    addErrorAlert(message) {
        this.alerts.push(
            this.AlertService.add(
                {
                    type: 'danger',
                    msg: message,
                    timeout: 5000,
                    toast: this.AlertService.isToast(),
                    scoped: true
                },
                this.alerts
            )
        );
    }
}


export default {
    template: require("./alert.html"),
    controller: JhiAlertErrorController
};
