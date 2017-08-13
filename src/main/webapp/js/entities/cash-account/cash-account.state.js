export default function stateConfig($stateProvider) {
    "ngInject";
    $stateProvider
        .state('cash-account', {
            parent: 'entity',
            url: '/cash-account',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'CashAccounts'
            },
            views: {
                'content@': 'cashAccountComponent'
            },
            resolve: {
                cashRates: ['CashRate', function(CashRate){
                    return CashRate.get({code: 'EUR'}).$promise
                }]
            }
        })
        .state('cash-account-detail', {
            parent: 'entity',
            url: '/cash-account/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'CashAccount'
            },
            views: {
                'content@': 'cashAccountDetailComponent'
            },
            resolve: {
                cashAccount: ['$stateParams', 'CashAccount', function ($stateParams, CashAccount) {
                    return CashAccount.get({id: $stateParams.id}).$promise;
                }]
            }
        })
        .state('cash-account-new', {
            parent: 'cash-account',
            url: '/{parentId}/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                $uibModal.open({
                    component: 'cashAccountDialogComponent',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        parentAccount:  ['CashAccount', function (CashAccount) {
                            return CashAccount.get({id: $stateParams.parentId}).$promise;
                        }]
                    }
                }).result.then(function () {
                    $state.go('cash-account', null, {reload: true});
                }, function () {
                    $state.go('cash-account');
                });
            }]
        })
        .state('cash-account-edit', {
            parent: 'cash-account',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                $uibModal.open({
                    component: 'cashAccountDialogComponent',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CashAccount', function (CashAccount) {
                            return CashAccount.get({id: $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function () {
                    $state.go('cash-account', null, {reload: true});
                }, function () {
                    $state.go('^');
                });
            }]
        })
        .state('cash-account-delete', {
            parent: 'cash-account',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                $uibModal.open({
                    component: 'cashAccountDeleteComponent',
                    size: 'md',
                    resolve: {
                        entity: ['CashAccount', function (CashAccount) {
                            return CashAccount.get({id: $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function () {
                    $state.go('cash-account', null, {reload: true});
                }, function () {
                    $state.go('^');
                });
            }]
        })
        .state('cash-account-organize', {
            parent: 'cash-account',
            url: '/organize',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                $uibModal.open({
                    component: 'cashAccountOrganizeDialogComponent',
                    size: 'lg'
                }).result.then(function (result) {
                    $state.go('cash-account', null, {reload: true});
                }, function () {
                    $state.go('^');
                });
            }]
        })
        .state('cash-account-importFile', {
            parent: 'cash-account',
            url: '/upload',
            data: {
                authorities: ['ROLE_USER'],
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                $uibModal.open({
                    component: 'cashAccountImportComponent',
                    size: 'md'
                }).result.then(function (result) {
                    $state.go('cash-account', null, {reload: true});
                }, function () {
                    $state.go('^');
                })
            }]
        });
};