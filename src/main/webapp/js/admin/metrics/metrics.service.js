export default function JhiMetricsService($rootScope, $http) {
    "ngInject";

    function getMetrics() {
        return $http.get('management/jhipster/metrics').then(function (response) {
            return response.data;
        });
    }

    function threadDump() {
        return $http.get('management/dump').then(function (response) {
            return response.data;
        });
    }

    var service = {
        getMetrics: getMetrics,
        threadDump: threadDump
    };

    return service;
};