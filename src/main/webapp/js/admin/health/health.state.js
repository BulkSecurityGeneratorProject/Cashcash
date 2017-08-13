export default function stateConfig($stateProvider) {
    "ngInject";
    $stateProvider.state('jhi-health', {
        parent: 'admin',
        url: '/health',
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'Health Checks'
        },
        views: {
            'content@': {
                template: require('./health.html'),
                controller: 'JhiHealthCheckController',
                controllerAs: 'vm'
            }
        }
    });
};
