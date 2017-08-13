import angular from "angular";

export default function User($resource) {
    "ngInject";
    var service = $resource('api/users/:login', {}, {
        'query': {method: 'GET', isArray: true},
        'get': {
            method: 'GET',
            transformResponse: function (data) {
                data = angular.fromJson(data);
                return data;
            }
        },
        'save': {method: 'POST'},
        'update': {method: 'PUT'},
        'delete': {method: 'DELETE'}
    });

    return service;
};
