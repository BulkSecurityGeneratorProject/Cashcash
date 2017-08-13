export default function stateConfig($stateProvider) {
    "ngInject";
    $stateProvider.state('password', {
        parent: 'account',
        url: '/password',
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Password'
        },
        views: {
            'content@': {
                template: require('./password.html'),
                controller: 'PasswordController',
                controllerAs: 'vm'
            }
        }
    });
};