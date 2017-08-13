class CashAccountImportController {
    constructor(CashAccount) {
        "ngInject";
        this.CashAccount = CashAccount;
    }

    $onInit() {
        this.isReadyToUpload = false;
    }

    importAccount(fileContent) {
        this.cashAccounts = fileContent;
        this.isReadyToUpload = true;
    }

    onSaveSuccess(result) {
        this.close({$value: result});
        this.isReadyToUpload = false;
    }

    onSaveError() {
        this.isReadyToUpload = false;
    }

    save() {
        if (this.cashAccounts) {
            this.CashAccount.save(this.cashAccounts, this.onSaveSuccess.bind(this), this.onSaveError.bind(this));
        }
    }

    clear() {
        this.dismiss({$value: 'cancel'});
    }
}

export default {
    bindings: {
        close: '&',
        dismiss: '&'
    },
    template: require('./cash-account-import-dialog.html'),
    controller: CashAccountImportController
};