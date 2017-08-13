export default function stateConfig($stateProvider) {
    "ngInject";
    $stateProvider.state('logs', {
        parent: 'admin',
        url: '/logs',
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'Logs'
        },
        views: {
            'content@': {
                template: require('./logs.html'),
                controller: 'LogsController',
                controllerAs: 'vm'
            }
        }
    });
};
