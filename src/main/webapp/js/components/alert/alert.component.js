class JhiAlertController {
    constructor($scope, AlertService) {
        "ngInject";
        this.$scope = $scope;
        this.AlertService = AlertService;
    }

    $onInit() {
        this.alerts = this.AlertService.get();
        this.$scope.$on('$destroy', function () {
            this.alerts = [];
        }.bind(this));
    }
}

export default {
    template: require("./alert.html"),
    controller: JhiAlertController
};