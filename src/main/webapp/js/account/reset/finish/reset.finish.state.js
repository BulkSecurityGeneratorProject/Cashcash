export default function stateConfig($stateProvider) {
    "ngInject";
    $stateProvider.state('finishReset', {
        parent: 'account',
        url: '/reset/finish?key',
        data: {
            authorities: []
        },
        views: {
            'content@': {
                template: require('./reset.finish.html'),
                controller: 'ResetFinishController',
                controllerAs: 'vm'
            }
        }
    });
};