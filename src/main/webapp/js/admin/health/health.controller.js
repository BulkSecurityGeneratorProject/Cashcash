export default function JhiHealthCheckController(JhiHealthService, $uibModal) {
    "ngInject";
    var vm = this;

    function getLabelClass(statusState) {
        if (statusState === 'UP') {
            return 'label-success';
        } else {
            return 'label-danger';
        }
    }

    function refresh() {
        vm.updatingHealth = true;
        JhiHealthService.checkHealth().then(function (response) {
            vm.healthData = JhiHealthService.transformHealthData(response);
            vm.updatingHealth = false;
        }, function (response) {
            vm.healthData = JhiHealthService.transformHealthData(response.data);
            vm.updatingHealth = false;
        });
    }

    function showHealth(health) {
        $uibModal.open({
            template: require('./health.modal.html'),
            controller: 'HealthModalController',
            controllerAs: 'vm',
            size: 'lg',
            resolve: {
                currentHealth: function () {
                    return health;
                },
                baseName: function () {
                    return vm.baseName;
                },
                subSystemName: function () {
                    return vm.subSystemName;
                }

            }
        });
    }

    vm.updatingHealth = true;
    vm.getLabelClass = getLabelClass;
    vm.refresh = refresh;
    vm.showHealth = showHealth;
    vm.baseName = JhiHealthService.getBaseName;
    vm.subSystemName = JhiHealthService.getSubSystemName;

    vm.refresh();
};
