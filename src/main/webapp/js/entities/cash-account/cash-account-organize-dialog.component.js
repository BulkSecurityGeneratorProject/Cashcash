import _ from "lodash";

class CashAccountOrganizeDialogController {
    constructor(CashAccount, CashAccountUtils) {
        "ngInject";
        this.CashAccount = CashAccount;
        this.CashAccountUtils = CashAccountUtils;
    }

    $onInit() {
        this.tree_data = {};
        this.my_tree = {};
        this.expanding_property = {
            field: 'name',
            titleClass: 'text-center',
            cellClass: 'v-middle',
            displayName: 'Name'
        };
        this.col_defs = [{
            field: 'code',
            displayName: 'Code'
        }, {
            field: 'type',
            displayName: 'Type'
        }];
        this.loadAll();
    }

    loadAll() {
        this.CashAccount.query({}, function (result, headers) {
            this.cashAccountList = result;
            this.tree_data = this.CashAccountUtils.lineToTree(result, 'id', 'parentAccountId');
            _.forEach(this.cashAccountList, function (value) {
                value.__expanded__ = true;
                value.currencyCode = value.currency.code;
            });
        }.bind(this));
    }

    onSaveSuccess(result) {
        this.close({$value: result});
        this.isSaving = false;
    }

    onSaveError(result) {
        this.isSaving = false;
    }

    updateChildrenAccounts(parentCashAccount) {
        _.forEach(parentCashAccount.__children__, function (value) {
            value.parentAccount = {
                id: parentCashAccount.id,
                level: parentCashAccount.level,
                type: parentCashAccount.type
            };
        });
    }

    save() {
        this.isSaving = true;
        _.forEach(this.cashAccountList, function (value) {
            value.level = value.__level__;
            this.updateChildrenAccounts(value);
        }.bind(this));
        this.CashAccount.update(this.cashAccountList, this.onSaveSuccess.bind(this), this.onSaveError.bind(this));
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
    template: require('./cash-account-organize-dialog.html'),
    controller: CashAccountOrganizeDialogController
};