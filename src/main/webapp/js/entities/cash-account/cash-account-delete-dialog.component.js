class CashAccountDeleteController {
    constructor(CashAccount) {
        "ngInject";
        this.CashAccount = CashAccount;
    }

    $onInit() {
    }

    clear() {
        this.dismiss({$value: 'cancel'});
    }

    confirmDelete(id) {
        this.CashAccount.delete({id: id},
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
    template: require('./cash-account-delete-dialog.html'),
    controller: CashAccountDeleteController
};