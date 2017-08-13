import angular from "angular";

export default function JhiConfigurationService($filter, $http) {
    "ngInject";

    function get() {
        function getConfigPropsComplete(response) {
            var properties = [];
            angular.forEach(response.data, function (data) {
                properties.push(data);
            });
            var orderBy = $filter('orderBy');
            return orderBy(properties, 'prefix');
        }

        return $http.get('management/configprops').then(getConfigPropsComplete);
    }

    function getEnv() {
        function getEnvComplete(response) {
            var properties = {};
            angular.forEach(response.data, function (val, key) {
                var vals = [];
                angular.forEach(val, function (v, k) {
                    vals.push({key: k, val: v});
                });
                properties[key] = vals;
            });
            return properties;
        }

        return $http.get('management/env').then(getEnvComplete);
    }

    var service = {
        get: get,
        getEnv: getEnv
    };

    return service;
};