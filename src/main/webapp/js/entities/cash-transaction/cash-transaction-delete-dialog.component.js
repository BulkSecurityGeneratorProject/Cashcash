class CashTransactionDeleteController {
    constructor(CashTransaction) {
        "ngInject";
        this.CashTransaction = CashTransaction;
    }

    $onInit() {
    }

    clear() {
        this.dismiss({$value: 'cancel'});
    }

    confirmDelete(id) {
        this.CashTransaction.delete({id: id},
            function () {
                this.close({$value: true});
            }.bind(this));
    }
}

export default {
    bindings: {
        resolve: '<',
        close: '&',
        dismiss: '&'
    },
    template: require('./cash-transaction-delete-dialog.html'),
    controller: CashTransactionDeleteController
};
