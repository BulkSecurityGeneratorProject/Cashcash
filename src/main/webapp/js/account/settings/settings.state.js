export default function stateConfig($stateProvider) {
    "ngInject";
    $stateProvider.state('settings', {
        parent: 'account',
        url: '/settings',
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Settings'
        },
        views: {
            'content@': {
                template: require('./settings.html'),
                controller: 'SettingsController',
                controllerAs: 'vm'
            }
        }
    });
}