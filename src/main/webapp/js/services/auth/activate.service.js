export default function Activate($resource) {
    "ngInject";
    var service = $resource('api/activate', {}, {
        'get': {method: 'GET', params: {}, isArray: false}
    });

    return service;
};
