export default function stateConfig($stateProvider) {
    "ngInject";
    $stateProvider
        .state('cash-flow-graph', {
            parent: 'entity',
            url: '/cash-flow-graph?startDate&endDate',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'CashFlowGraph'
            },
            views: {
                'content@': 'cashFlowGraphComponent'
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