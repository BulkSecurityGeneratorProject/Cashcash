export default function stateConfig($stateProvider) {
    "ngInject";
    $stateProvider.state('requestReset', {
        parent: 'account',
        url: '/reset/request',
        data: {
            authorities: []
        },
        views: {
            'content@': {
                template: require('./reset.request.html'),
                controller: 'RequestResetController',
                controllerAs: 'vm'
            }
        }
    });
};
