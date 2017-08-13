export default function stateConfig($stateProvider) {
    "ngInject";
    $stateProvider.state('social-register', {
        parent: 'account',
        url: '/social-register/:provider?{success:boolean}',
        data: {
            authorities: [],
            pageTitle: 'Register with {{ label }}'
        },
        views: {
            'content@': {
                template: require('./social-register.html'),
                controller: 'SocialRegisterController',
                controllerAs: 'vm'
            }
        }
    })
        .state('social-auth', {
            parent: 'account',
            url: '/social-auth',
            data: {
                authorities: [],
            },
            views: {
                'content@': {
                    controller: 'SocialAuthController'
                }
            }
        });
}
