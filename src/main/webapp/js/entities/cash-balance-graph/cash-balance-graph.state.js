export default function stateConfig($stateProvider) {
    "ngInject";
    $stateProvider
        .state('cash-balance-graph', {
            parent: 'entity',
            url: '/cash-balance-graph?startDate&endDate',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'CashBalanceGraph'
            },
            views: {
                'content@': 'cashBalanceGraphComponent'
            },
            resolve: {
                cashAccounts: ['CashAccount', function (CashAccount) {
                    return CashAccount.query().$promise;
                }],
                cashRates: ['CashRate', function(CashRate){
                    return CashRate.get({code: 'EUR'}).$promise
                }]
            }
        });
};
