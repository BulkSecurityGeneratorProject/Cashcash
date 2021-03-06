export default function JhiMetricsMonitoringController($scope, JhiMetricsService, $uibModal) {
    "ngInject";
    var vm = this;

    function refresh() {
        vm.updatingMetrics = true;
        JhiMetricsService.getMetrics().then(function (promise) {
            vm.metrics = promise;
            vm.updatingMetrics = false;
        }, function (promise) {
            vm.metrics = promise.data;
            vm.updatingMetrics = false;
        });
    }

    function refreshThreadDumpData() {
        JhiMetricsService.threadDump().then(function (data) {
            $uibModal.open({
                template: require('./metrics.modal.html'),
                controller: 'JhiMetricsMonitoringModalController',
                controllerAs: 'vm',
                size: 'lg',
                resolve: {
                    threadDump: function () {
                        return data;
                    }

                }
            });
        });
    }

    vm.cachesStats = {};
    vm.metrics = {};
    vm.refresh = refresh;
    vm.refreshThreadDumpData = refreshThreadDumpData;
    vm.servicesStats = {};
    vm.updatingMetrics = true;

    vm.refresh();

    $scope.$watch('vm.metrics', function (newValue) {
        vm.servicesStats = {};
        vm.cachesStats = {};
        angular.forEach(newValue.timers, function (value, key) {
            if (key.indexOf('web.rest') !== -1 || key.indexOf('service') !== -1) {
                vm.servicesStats[key] = value;
            }
            if (key.indexOf('net.sf.ehcache.Cache') !== -1) {
                // remove gets or puts
                var index = key.lastIndexOf('.');
                var newKey = key.substr(0, index);

                // Keep the name of the domain
                index = newKey.lastIndexOf('.');
                vm.cachesStats[newKey] = {
                    'name': newKey.substr(index + 1),
                    'value': value
                };
            }
        });
    });
};