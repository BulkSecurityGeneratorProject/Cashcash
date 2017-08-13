import _ from "lodash";

export default class CashAccountUtils {
    constructor() {
        "ngInject";
    }

    extractAccountAndSubAccounts(sortedAccountList, parentAccountNames) {
        var extractedAccountList = [];
        var extractedAccountIdList = [];
        for (let account of sortedAccountList) {
            if (_.includes(parentAccountNames, account.name)
                || _.indexOf(extractedAccountIdList, account.parentAccountId) > -1) {
                extractedAccountList.push(account);
                extractedAccountIdList.push(account.id);
            }
        }
        return extractedAccountList;
    }

    extractSubAccounts(sortedAccountList, parentAccountNames) {
        var extractedAccountList = [];
        var extractedAccountIdList = [];
        for (let account of sortedAccountList) {
            if ((account.parentAccount && _.includes(parentAccountNames, account.parentAccount.name))
                || _.indexOf(extractedAccountIdList, account.parentAccountId) > -1) {
                extractedAccountList.push(account);
                extractedAccountIdList.push(account.id);
            }
        }
        return extractedAccountList;
    }

    extractAccount(sortedAccountList, nameAccount) {
        return _.find(sortedAccountList, {'name': nameAccount});
    }

    findAccount(id, cashAccounts) {
        return _.first(_.filter(cashAccounts, {id: Number(id)}));
    }

    lineToTree(flatData, primaryKey = "id", parentKey = "parentAccountId") {
        // create a primaryKey: node map
        const dataMap = flatData.reduce(function (map, node) {
            map[node[primaryKey]] = node;
            return map;
        }, {});

        // create the tree array
        const treeData = [];
        flatData.forEach(function (node) {
            // add to parent
            const parent = dataMap[node[parentKey]];
            if (parent) {
                // create child array if it doesn't exist
                if (!parent.__children__) {
                    parent.__children__ = [];
                }
                // add node to child array
                parent.__children__.push(node);
            } else {
                // parent is null or missing
                treeData.push(node);
            }
        });
        return treeData;
    }

    lineToMap(flatData, primaryKey = "id") {
        // create a primaryKey: node map
        const dataMap = flatData.reduce(function (map, node) {
            map[node[primaryKey]] = node;
            return map;
        }, {});

        return dataMap;
    }
}
