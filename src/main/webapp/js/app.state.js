export default function stateConfig($stateProvider) {
    "ngInject";
    $stateProvider.state('app', {
        abstract: true,
        views: {
            'navbar@': {
                template: require('./layouts/navbar/navbar.html'),
                controller: 'NavbarController',
                controllerAs: 'vm'
            }
        },
        resolve: {
            authorize: ['Auth', function (Auth) {
                return Auth.authorize();
            }]
        }
    });
};
