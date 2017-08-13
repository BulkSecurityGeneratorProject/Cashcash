export default function stateConfig($stateProvider) {
    "ngInject";
    $stateProvider
        .state('cash-csv-config', {
            parent: 'entity',
            url: '/cash-csv-config?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'CashCsvConfigs'
            },
            views: {
                'content@': 'cashCsvConfigComponent'
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
            }
        })
        .state('cash-csv-config-detail', {
            parent: 'entity',
            url: '/cash-csv-config/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'CashCsvConfig'
            },
            views: {
                'content@': 'cashCsvConfigDetailComponent'
            },
            resolve: {
                cashCsvConfig: ['$stateParams', 'CashCsvConfig', function ($stateParams, CashCsvConfig) {
                    return CashCsvConfig.get({id: $stateParams.id}).$promise;
                }]
            }
        })
        .state('cash-csv-config-new', {
            parent: 'cash-csv-config',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                $uibModal.open({
                    component: 'cashCsvConfigDialogComponent',
                    backdrop: 'static',
                    size: 'lg'
                }).result.then(function () {
                    $state.go('cash-csv-config', null, {reload: true});
                }, function () {
                    $state.go('cash-csv-config');
                });
            }]
        })
        .state('cash-csv-config-edit', {
            parent: 'cash-csv-config',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                $uibModal.open({
                    component: 'cashCsvConfigDialogComponent',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CashCsvConfig', function (CashCsvConfig) {
                            return CashCsvConfig.get({id: $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function () {
                    $state.go('cash-csv-config', null, {reload: true});
                }, function () {
                    $state.go('^');
                });
            }]
        })
        .state('cash-csv-config-delete', {
            parent: 'cash-csv-config',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                $uibModal.open({
                    component: 'cashCsvConfigDeleteComponent',
                    size: 'md',
                    resolve: {
                        entity: ['CashCsvConfig', function (CashCsvConfig) {
                            return CashCsvConfig.get({id: $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function () {
                    $state.go('cash-csv-config', null, {reload: true});
                }, function () {
                    $state.go('^');
                });
            }]
        });
};
