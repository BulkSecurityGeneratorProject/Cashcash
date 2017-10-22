import angular from "angular";
import _ from "lodash";
import moment from "moment";

class CashAccountController {
    constructor(CashAccount, CashAccountUtils, AlertService, FileSaver, Blob, CashConverter, CashSplitSum) {
        "ngInject";
        this.CashAccount = CashAccount;
        this.AlertService = AlertService;
        this.FileSaver = FileSaver;
        this.Blob = Blob;
        this.CashAccountUtils = CashAccountUtils;
        this.CashConverter = CashConverter;
        this.CashSplitSum = CashSplitSum;
    }

    $onInit() {
        this.tree_data = {};
        this.my_tree = {};
        this.expanding_property = {
            field: 'name',
            displayName: 'Name',
            cellTemplate: '<div>{{node.name}} {{node.code}}</div>'
        };

        this.col_defs = [
            {
                field: 'amount',
                displayName: 'Amount',
                cellTemplate: '<div>{{node.amount.toFixed(2)}} {{node.currencyCode}}</div>'
            }, {
                cellTemplate: '<account-interaction-panel cash-account="node"/>'
            }];

        this.loadAll();
    }

    loadAll() {
        this.CashAccount.query({}, function (result, headers) {
            this.cashAccounts = result;
            this.updateAmount();
            for (let value of this.cashAccounts) {
                value.__expanded__ = true;
                value.currencyCode = value.currency.code;
                value.amount = 0;
            }
        }.bind(this));

    }

    updateAmount() {
        const splitSumRequest = {
            sort: ['transactionDate,asc']
        };

        this.CashSplitSum.query(splitSumRequest, function (result, headers) {
            this.splitSumList = result;
            this.cashAccountMapById = this.CashAccountUtils.lineToMap(this.cashAccounts);
            for (let value of this.splitSumList) {
                const account = this.cashAccountMapById[value.accountId];
                if (account.amount) {
                    account.amount += this.CashConverter.getAmount(value, account.currencyCode);
                } else {
                    account.amount = this.CashConverter.getAmount(value, account.currencyCode);
                }
            }
            this.tree_data = this.CashAccountUtils.lineToTree(this.cashAccounts, 'id', 'parentAccountId');
        }.bind(this));
    }

    onSuccessExportFile(data, headers) {
        const prettyJson = angular.toJson(data, true);
        const blob = new Blob([prettyJson], {type: 'text/plain;charset=utf-8'});
        this.FileSaver.saveAs(blob, 'accounts.json');
    }

    onError(error) {
        this.AlertService.error(error.data.message);
    }

    exportFile() {
        this.CashAccount.query({}, this.onSuccessExportFile.bind(this), this.onError.bind(this));
    }
}

export default {
    bindings: {},
    template: require('./cash-accounts.html'),
    controller: CashAccountController
};
