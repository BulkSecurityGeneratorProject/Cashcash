export default function stateConfig($stateProvider) {
    "ngInject";
    $stateProvider.state('audits', {
        parent: 'admin',
        url: '/audits',
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'Audits'
        },
        views: {
            'content@': {
                template: require('./audits.html'),
                controller: 'AuditsController',
                controllerAs: 'vm'
            }
        }
    });
};