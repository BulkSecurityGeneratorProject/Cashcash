export default function stateConfig($stateProvider) {
    "ngInject";
    $stateProvider.state('jhi-configuration', {
        parent: 'admin',
        url: '/configuration',
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'Configuration'
        },
        views: {
            'content@': {
                template: require('./configuration.html'),
                controller: 'JhiConfigurationController',
                controllerAs: 'vm'
            }
        }
    });
};