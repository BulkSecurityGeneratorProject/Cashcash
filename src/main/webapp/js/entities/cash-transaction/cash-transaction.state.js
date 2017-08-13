export default function stateConfig($stateProvider) {
    "ngInject";
    $stateProvider
        .state('cash-transaction', {
            parent: 'entity',
            url: '/cash-transaction?page&sort&search&accountIdList&startDate&endDate',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'CashTransactions'
            },
            views: {
                'content@': 'cashTransactionComponent'
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                filterParams: ['$stateParams', function ($stateParams) {
                    return {
                        accountIdList: $stateParams.accountIdList,
                        startDate: $stateParams.startDate,
                        endDate: $stateParams.endDate
                    };
                }]
            }
        })
        .state('cash-transaction-detail', {
            parent: 'entity',
            url: '/cash-transaction/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'CashTransaction'
            },
            views: {
                'content@': 'cashTransactionDetailComponent'
            },
            resolve: {
                cashTransaction: ['$stateParams', 'CashTransaction', function ($stateParams, CashTransaction) {
                    return CashTransaction.get({id: $stateParams.id}).$promise;
                }]
            }
        })
        .state('cash-transaction-new', {
            parent: 'cash-transaction',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                $uibModal.open({
                    component: 'cashTransactionDialogComponent',
                    backdrop: 'static',
                    size: 'lg'
                }).result.then(function () {
                    $state.go('cash-transaction', null, {reload: true});
                }, function () {
                    $state.go('^');
                });
            }]
        })
        .state('cash-transaction-edit', {
            parent: 'cash-transaction',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                $uibModal.open({
                    component: 'cashTransactionDialogComponent',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CashTransaction', function (CashTransaction) {
                            return CashTransaction.get({id: $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function () {
                    $state.go('cash-transaction', null, {reload: true});
                }, function () {
                    $state.go('^');
                });
            }]
        })
        .state('cash-transaction-delete', {
            parent: 'cash-transaction',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                $uibModal.open({
                    component: 'cashTransactionDeleteComponent',
                    size: 'md',
                    resolve: {
                        entity: ['CashTransaction', function (CashTransaction) {
                            return CashTransaction.get({id: $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function () {
                    $state.go('cash-transaction', null, {reload: true});
                }, function () {
                    $state.go('^');
                });
            }]
        })
        .state('cash-transaction-importFile', {
            parent: 'cash-transaction',
            url: '/upload',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                $uibModal.open({
                    component: 'cashTransactionImportComponent',
                    size: 'md'
                }).result.then(function (result) {
                    if (result) {
                        $state.go('cash-transaction-newList', result, {reload: true});
                    } else {
                        $state.go('^');
                    }
                }, function () {
                    $state.go('^');
                });
            }]
        })
        .state('cash-transaction-newList', {
            parent: 'cash-transaction',
            url: '/newList',
            params: {cashTransactionList: null},
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                $uibModal.open({
                    component: 'cashTransactionNewListDialogComponent',
                    size: 'lg',
                    backdrop: 'static',
                    keyboard: false,
                    resolve: {
                        cashTransactionList: function () {
                            return $stateParams.cashTransactionList;
                        }
                    }
                }).result.then(function (result) {
                    $state.go('cash-transaction', null, {reload: true});
                }, function () {
                    $state.go('^');
                });
            }]
        });
};