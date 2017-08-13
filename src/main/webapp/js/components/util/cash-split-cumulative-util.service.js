import _ from "lodash";
import moment from "moment";
import d3 from "d3";

export default class CashSplitCumulativeUtils {
    constructor(CashSplitCumulative, CashAccountUtils, CashConverter) {
        "ngInject";
        this.CashSplitCumulative = CashSplitCumulative;
        this.CashAccountUtils = CashAccountUtils;
        this.CashConverter = CashConverter;
    }

    generateCashSplitCumulative(cashAccountsToRetrieve, startDate, endDate) {
        const splitSumRequest = {
            sort: ['transactionDate,asc'],
            accountIdList: _.map(cashAccountsToRetrieve, 'id'),
            startDate: startDate.toISOString(),
            endDate: endDate.toISOString(),
            currencyCode : "EUR"
        };

        return this.CashSplitCumulative.query(splitSumRequest).$promise.then(function (splitCumulativeList) {
            return this.buildData(splitCumulativeList);
        }.bind(this));
    }

    buildData(splitCumulativeList) {
        const latestTransactionDate = moment(splitCumulativeList[0].transactionDate).valueOf();
        const soonestTransactionDate = moment(splitCumulativeList[splitCumulativeList.length - 1].transactionDate).valueOf();
        const cumulativeSumAtLatestTransactionDateMap = {};
        const cumulativeSumAtSoonestTransactionDateMap = {};

        // Convert splitCumulativeList to a List<{accountId, date, amount}>
        let simpleDataList = splitCumulativeList.map(function (d) {

            // If there not yet a cumulative sum for this account at the latest date
            // We add it now
            if(!cumulativeSumAtLatestTransactionDateMap[d.accountId]){
                cumulativeSumAtLatestTransactionDateMap[d.accountId] = {
                    accountId: d.accountId,
                    cumulativeAmount: d.cumulativeAmount,
                    date: latestTransactionDate
                };
            }

            // We update the value of the cumulative sum for this account at the soonest date
            // The last update will be the correct one
            cumulativeSumAtSoonestTransactionDateMap[d.accountId] = {
                accountId: d.accountId,
                cumulativeAmount: d.cumulativeAmount,
                date: soonestTransactionDate
            };

            // Finally we return the current cumulative sum for this account
            return {
                accountId: d.accountId,
                cumulativeAmount: d.cumulativeAmount,
                date: moment(d.transactionDate).valueOf()
            };
        }.bind(this));

        const head = _.valuesIn(cumulativeSumAtLatestTransactionDateMap);
        const tail = _.valuesIn(cumulativeSumAtSoonestTransactionDateMap);
        simpleDataList = [...head, ...simpleDataList, ...tail];

        // Create a map <accoutId-currencyId, List<{date, amount}>> with unique date
        const simpleDataMapByAccountAndCurrency = d3.nest()
            .key(d => d.accountId)
            .key(d => d.date)
            .sortKeys(d3.ascending)
            .rollup(values => {
                // We just take the first one for one date because it contains the full sum
                return values[0].cumulativeAmount;
            })
            .entries(simpleDataList);
        // We get something like this
        // [
        //     {
        //         "key": "1185-7745",
        //         "values": [
        //             {
        //                 "key": "1488832085687",
        //                 "values": 3.7863674400000003
        //             }
        //         ]
        //     }
        // ]

        return simpleDataMapByAccountAndCurrency;
    }
}