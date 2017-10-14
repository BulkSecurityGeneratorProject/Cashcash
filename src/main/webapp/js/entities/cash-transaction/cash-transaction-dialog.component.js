import angular from "angular";

class CashTransactionDialogController {
    constructor(CashTransactionUtils, $timeout, CashTransaction, CashAccount, Principal, CashCurrency) {
        "ngInject";
        this.CashTransactionUtils = CashTransactionUtils;
        this.$timeout = $timeout;
        this.CashTransaction = CashTransaction;
        this.CashAccount = CashAccount;
        this.Principal = Principal;
        this.CashCurrency = CashCurrency;

        if (!this.resolve || !this.resolve.entity) {
            this.cashTransaction = {
                creationDate: null,
                modifiedDate: null,
                description: null,
                ofxId: null,
                transactionDate: null,
                type: null,
                id: null,
                splits: []
            };
        } else {
            this.cashTransaction = this.resolve.entity;
        }
    }

    $onInit() {
        this.cashCurrencies = this.CashCurrency.query();
        this.cashAccounts = this.CashAccount.query();
        this.CashTransactionUtils.flattenTransactionInfo(this.cashTransaction);

        this.Principal.identity().then(function (account) {
            this.cashTransaction.user = account;
        }.bind(this));

        this.$timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });
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
        this.CashTransactionUtils.createSplits(this.cashTransaction, this.cashAccounts, this.cashCurrencies);
        if (this.cashTransaction.id !== null) {
            this.CashTransaction.update(this.cashTransaction, this.onSaveSuccess.bind(this), this.onSaveError.bind(this));
        } else {
            this.CashTransaction.save([this.cashTransaction], this.onSaveSuccess.bind(this), this.onSaveError.bind(this));
        }
    }

}

export default {
    bindings: {
        resolve: '<',
        close: '&',
        dismiss: '&'
    },
    template: require('./cash-transaction-dialog.html'),
    controller: CashTransactionDialogController
};