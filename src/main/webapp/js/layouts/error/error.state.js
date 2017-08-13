export default function stateConfig($stateProvider) {
    "ngInject";
    $stateProvider
        .state('error', {
            parent: 'app',
            url: '/error',
            data: {
                authorities: [],
                pageTitle: 'Error page!'
            },
            views: {
                'content@': {
                    template: require('./error.html')
                }
            }
        })
        .state('accessdenied', {
            parent: 'app',
            url: '/accessdenied',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    template: require('./accessdenied.html')
                }
            }
        });
};
