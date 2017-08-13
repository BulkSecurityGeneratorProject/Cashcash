import _ from "lodash";

class CashTransactionNewListDialogController {
    constructor(CashTransactionUtils, cashTransactionList, CashCurrency, CashAccount, CashTransaction) {
        "ngInject";
        this.CashTransactionUtils = CashTransactionUtils;
        this.cashTransactionList = cashTransactionList;
        this.CashCurrency = CashCurrency;
        this.CashAccount = CashAccount;
        this.CashTransaction = CashTransaction;
    }

    $onInit() {
        this.cashCurrencies = this.CashCurrency.query();
        this.cashAccounts = this.CashAccount.query();
        this.cashTransactionList = this.resolve.cashTransactionList;
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
                CashTransactionUtils.createSplits(value, this.cashAccounts, this.cashCurrencies);
                dataToSave.push(value);
            }
        });
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
