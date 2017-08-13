export default function stateConfig($stateProvider) {
    "ngInject";
    $stateProvider.state('activate', {
        parent: 'account',
        url: '/activate?key',
        data: {
            authorities: [],
            pageTitle: 'Activation'
        },
        views: {
            'content@': {
                template: require('./activate.html'),
                controller: 'ActivationController',
                controllerAs: 'vm'
            }
        }
    });
};
