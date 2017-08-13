import _ from "lodash";
import moment from "moment";
import d3 from "d3";

class CashFlowGraphController {
    constructor(CashSplit, CashAccountUtils, $q, CashConverter) {
        "ngInject";
        this.CashSplit = CashSplit;
        this.CashAccountUtils = CashAccountUtils;
        this.$q = $q;
        this.CashConverter = CashConverter;
    }

    $onInit() {
        const accountMap = this.CashAccountUtils.lineToMap(this.cashAccounts);
        const cashAccountToRetrieveList = this.CashAccountUtils.extractSubAccounts(this.cashAccounts, ['EXPENSE', 'INCOME']);

        this.startDate = moment().subtract(24, 'months');
        this.endDate = moment();

        const splitRequest = {
            sort: ['transactionDate,asc'],
            accountIdList: _.map(cashAccountToRetrieveList, 'id'),
            startDate: this.startDate.toISOString(),
            endDate: this.endDate.toISOString()
        };

        return this.$q.all([
            this.CashSplit.query(splitRequest).$promise
        ]).then(function (data) {
            const splitList = data[0];
            this.buildGraph(splitList, cashAccountToRetrieveList, accountMap);
        }.bind(this));
    }

    buildGraph(splitList, cashAccountToRetrieveList, accountMap) {
        this.initGraph(cashAccountToRetrieveList);
        this.populateGraph(splitList, accountMap, cashAccountToRetrieveList);
    }

    initGraph(cashAccountToRetrieveList) {
        this.expenseGraph = {
            config: undefined,
            series: [{
                key: "EXPENSE",
                values: []
            }]
        };

        this.incomeGraph = {
            config: undefined,
            series: [{
                key: "INCOME",
                values: []
            }]
        };

        this.subAccountGraphMap = new Map();
        this.subAccountGraphList = [];
        for (let cashAccountToRetrieve of cashAccountToRetrieveList) {
            let accountId = cashAccountToRetrieve.id;
            if (cashAccountToRetrieve.level <= 2) {
                const subAccountGraph = {
                    config: undefined,
                    series: []
                };
                this.subAccountGraphMap.set(accountId, subAccountGraph);
                this.subAccountGraphList.push(subAccountGraph);
            } else {
                this.subAccountGraphMap.set(accountId, this.subAccountGraphMap.get(cashAccountToRetrieve.parentAccountId));
            }
        }
    }

    populateGraph(splitList, accountMap, cashAccountToRetrieveList) {
        // Transform to simple data
        // Convert splitList to a List<{accountId, date, amount}>
        const simpleSplitList = splitList.map(function (d) {
            return {
                accountId: d.accountId,
                date: moment(d.transactionDate),
                amount: this.CashConverter.getAmount(d, "EUR"),
                type: d.account.type
            };
        }.bind(this));

        // We create the data for the bar chart EXPENSE and INCOME
        const simpleDataMapByType = d3.nest()
            .key(d => d.type)
            .key(d => d.date.startOf('month'))
            .sortKeys(d3.ascending)
            .rollup(values => {
                return d3.sum(values, d => d.amount);
            })
            .entries(simpleSplitList);

        simpleDataMapByType.forEach(function (dataForOneType) {
            const type = dataForOneType.key;
            const values = dataForOneType.values.map(function (d) {
                return {
                    x: +d.key,
                    y: +d.values
                };
            });
            if (type === "EXPENSE") {
                this.expenseGraph.series[0].values = values;
            } else if (type === "INCOME") {
                this.incomeGraph.series[0].values = values;
            }
        }.bind(this));

        // For each month we have to be sure that there is at least one value for each account
        const addedZeroValue = [];
        let currentMonth;
        for (let d of simpleSplitList) {
            const date = d.date;
            const startOfTheMonth = d.date.startOf('month');
            if (!currentMonth || date !== d.date.startOf('month')) {
                // We add zero value for each account
                for (let cashAccountToRetrieve of cashAccountToRetrieveList) {
                    addedZeroValue.push({
                        accountId: cashAccountToRetrieve.id,
                        date: startOfTheMonth,
                        amount: 0
                    });
                }
            }
        }
        const simpleSplitWithZeroList = simpleSplitList.concat(addedZeroValue);

        // We create the data for the bar chart by account
        const simpleDataMapByAccount = d3.nest()
            .key(d => d.accountId)
            .key(d => d.date.startOf('month'))
            .sortKeys(d3.ascending)
            .rollup(values => {
                return d3.sum(values, d => d.amount);
            })
            .entries(simpleSplitWithZeroList);


        simpleDataMapByAccount.forEach(function (dataForOneAccount) {
            let account = accountMap[+dataForOneAccount.key];
            const name = account.name;
            const values = dataForOneAccount.values.map(function (d) {
                return {
                    x: +d.key,
                    y: +d.values
                };
            });
            const objectLine = {key: name, values: values};

            const subAccountGraph = this.subAccountGraphMap.get(+dataForOneAccount.key);
            subAccountGraph.series.push(objectLine);
        }.bind(this));
    }
}

export default {
    bindings: {
        cashAccounts: '<'
    },
    template: require('./cash-flow-graph.html'),
    controller: CashFlowGraphController
};