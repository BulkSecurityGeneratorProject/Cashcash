class CashTransactionDetailController {
    constructor(CashTransactionUtils) {
        "ngInject";
        this.CashTransactionUtils = CashTransactionUtils;
    }

    $onInit() {
    }

    getPositiveSplit() {
        return this.CashTransactionUtils.getPositiveSplit(this.cashTransaction);
    }

    getNegativeSplit() {
        return this.CashTransactionUtils.getNegativeSplit(this.cashTransaction);
    }

    increaseAssets() {
        const split = this.CashTransactionUtils.getPositiveSplit(this.cashTransaction);
        return split.account.type === "ASSET";
    }
}

export default {
    bindings: {
        cashTransaction: '<'
    },
    template: require('./cash-transaction-detail.html'),
    controller: CashTransactionDetailController
};