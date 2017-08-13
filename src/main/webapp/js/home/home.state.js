export default function stateConfig($stateProvider) {
    "ngInject";
    $stateProvider.state('home', {
        parent: 'app',
        url: '/',
        data: {
            authorities: []
        },
        views: {
            'content@': {
                template: require('./home.html'),
                controller: 'HomeController',
                controllerAs: 'vm'
            }
        }
    });
};
