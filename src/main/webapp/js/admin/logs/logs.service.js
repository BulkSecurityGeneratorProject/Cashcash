export default function LogsService($resource) {
    "ngInject";
    var service = $resource('management/jhipster/logs', {}, {
        'findAll': {method: 'GET', isArray: true},
        'changeLevel': {method: 'PUT'}
    });

    return service;
};
