export default function AuditsService($resource) {
    "ngInject";
    var service = $resource('management/jhipster/audits/:id', {}, {
        'get': {
            method: 'GET',
            isArray: true
        },
        'query': {
            method: 'GET',
            isArray: true,
            params: {fromDate: null, toDate: null}
        }
    });

    return service;
};
