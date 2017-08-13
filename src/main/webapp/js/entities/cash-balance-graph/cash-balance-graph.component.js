import moment from "moment";

class CashBalanceGraphController {
    constructor(CashSplitCumulativeUtils, CashAccountUtils, $q) {
        "ngInject";
        this.CashSplitCumulativeUtils = CashSplitCumulativeUtils;
        this.CashAccountUtils = CashAccountUtils;
        this.$q = $q;
    }

    $onInit() {
        const cashAccountsToRetrieve = this.CashAccountUtils.extractAccountAndSubAccounts(this.cashAccounts, ['ASSET']);
        this.cashAccountsMap = new Map();
        for (let cashAccount of this.cashAccounts) {
            this.cashAccountsMap.set(cashAccount.id, cashAccount);
        }

        const startDate = moment().subtract(24, 'months');
        const endDate = moment();
        this.CashSplitCumulativeUtils.generateCashSplitCumulative(cashAccountsToRetrieve, startDate, endDate)
            .then(function (simpleDataMapByAccount) {
                this.updateCharts(simpleDataMapByAccount);
            }.bind(this));
    }

    updateCharts(simpleDataMapByAccount) {
        const constPieData = [];
        this.chartByAccountList = [];
        simpleDataMapByAccount.forEach(function (dataForOneAccount) {
            const name = this.cashAccountsMap.get(+dataForOneAccount.key).name;
            const listOfValues = dataForOneAccount.values;
            const chartObject = {
                config: undefined,
                series: [{key: name, values: listOfValues}]
            };
            this.chartByAccountList.push(chartObject);

            constPieData.push({
                label: name,
                value: listOfValues[listOfValues.length - 1] ? listOfValues[listOfValues.length - 1].values : 0
            });
        }.bind(this));

        this.cashSplitSumPie = {
            config: undefined,
            series: [{name: "GLOBAL", values: constPieData}]
        }
    }
}

export default {
    bindings: {
        cashAccounts: '<'
    },
    template: require('./cash-balance-graph.html'),
    controller: CashBalanceGraphController
};

