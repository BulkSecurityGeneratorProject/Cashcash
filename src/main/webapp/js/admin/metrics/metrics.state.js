export default function stateConfig($stateProvider) {
    "ngInject";
    $stateProvider.state('jhi-metrics', {
        parent: 'admin',
        url: '/metrics',
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'Application Metrics'
        },
        views: {
            'content@': {
                template: require('./metrics.html'),
                controller: 'JhiMetricsMonitoringController',
                controllerAs: 'vm'
            }
        }
    });
};
