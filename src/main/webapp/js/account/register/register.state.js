export default function stateConfig($stateProvider) {
    "ngInject";
    $stateProvider.state('register', {
        parent: 'account',
        url: '/register',
        data: {
            authorities: [],
            pageTitle: 'Registration'
        },
        views: {
            'content@': {
                template: require('./register.html'),
                controller: 'RegisterController',
                controllerAs: 'vm'
            }
        }
    });
};