import _ from "lodash";

class CashTransactionNewListDialogController {
    constructor(CashTransactionUtils, CashCurrency, CashAccount, CashTransaction) {
        "ngInject";
        this.CashTransactionUtils = CashTransactionUtils;
        this.CashCurrency = CashCurrency;
        this.CashAccount = CashAccount;
        this.CashTransaction = CashTransaction;
    }

    $onInit() {
        this.cashCurrencies = this.CashCurrency.query();
        this.cashAccounts = this.CashAccount.query();
        this.cashTransactionList = this.resolve.cashTransactionList;
        for (var i = 0; i < this.cashTransactionList.length; i++) {
            const cashTransaction = this.cashTransactionList[i];
            cashTransaction.toImport = !cashTransaction.ofxIdAlreadyExist;
        }
        this.CashTransactionUtils.flattenTransactionInfoList(this.cashTransactionList);
    }

    clear() {
        this.dismiss({$value: 'cancel'});
    }

    onSaveSuccess(result) {
        this.close({$value: result});
        this.isSaving = false;
    }

    onSaveError() {
        this.isSaving = false;
    }

    save() {
        this.isSaving = true;

        const dataToSave = [];
        _.forEach(this.cashTransactionList, function (value) {
            if (value.toImport) {
                this.CashTransactionUtils.createSplits(value, this.cashAccounts, this.cashCurrencies);
                dataToSave.push(value);
            }
        }.bind(this));
        this.CashTransaction.save(dataToSave, this.onSaveSuccess.bind(this), this.onSaveError.bind(this));
    }

}

export default {
    bindings: {
        resolve: '<',
        close: '&',
        dismiss: '&'
    },
    template: require('./cash-transaction-new-list-dialog.html'),
    controller: CashTransactionNewListDialogController
};
