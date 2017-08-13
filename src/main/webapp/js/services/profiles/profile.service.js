import angular from "angular";

export default function ProfileService($http) {
    "ngInject";

    let dataPromise;

    function getProfileInfo() {
        if (angular.isUndefined(dataPromise)) {
            dataPromise = $http.get('api/profile-info').then(function (result) {
                if (result.data.activeProfiles) {
                    const response = {};
                    response.activeProfiles = result.data.activeProfiles;
                    response.ribbonEnv = result.data.ribbonEnv;
                    response.inProduction = result.data.activeProfiles.indexOf("prod") !== -1;
                    response.swaggerDisabled = result.data.activeProfiles.indexOf("no-swagger") !== -1;
                    return response;
                }
            });
        }
        return dataPromise;
    }

    const service = {
        getProfileInfo: getProfileInfo
    };

    return service;
};