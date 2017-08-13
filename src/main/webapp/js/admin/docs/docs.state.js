export default function stateConfig($stateProvider) {
    "ngInject";
    $stateProvider.state('docs', {
        parent: 'admin',
        url: '/docs',
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'API'
        },
        views: {
            'content@': {
                template: require('./docs.html')
            }
        }
    });
};