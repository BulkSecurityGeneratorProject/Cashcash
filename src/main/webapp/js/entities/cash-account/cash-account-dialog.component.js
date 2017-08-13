import angular from "angular";
import lodash from "lodash";

class CashAccountDialogController {
    constructor($timeout, $scope, $stateParams, CashAccount, CashCurrency, Principal, CashCsvConfig) {
        "ngInject";
        this.$timeout = $timeout;
        this.$scope = $scope;
        this.$stateParams = $stateParams;
        this.CashAccount = CashAccount;
        this.CashCurrency = CashCurrency;
        this.Principal = Principal;
        this.CashCsvConfig = CashCsvConfig;

        if (this.resolve.entity) {
            this.cashAccount = this.resolve.entity;
        } else {
            this.cashAccount = {
                id: null,
                creationDate: null,
                modifiedDate: null,
                user: null,
                name: null,
                type: null,
                code: null,
                level: null,
                multiCurrency: false,
                bankId: null,
                branchId: null,
                accountNumber: null,
                accountKey: null,
                iban: null,
                bic: null,
                cashCsvConfig: null
            };
            if(this.resolve.parentAccount){
                this.cashAccount.parentAccountId = this.resolve.parentAccount.id;
                this.cashAccount.level = this.resolve.parentAccount.level + 1;
                this.cashAccount.type = this.resolve.parentAccount.type;
                this.cashAccount.multiCurrency = this.resolve.parentAccount.multiCurrency;
                this.cashAccount.currencyId = this.resolve.parentAccount.currencyId;
            }
        }
    }

    $onInit() {
        this.datePickerOpenStatus = {};
        this.cashCurrencies = this.CashCurrency.query();
        this.cashAccounts = this.CashAccount.query();
        this.csvConfigs = this.CashCsvConfig.query();

        this.Principal.identity().then(function (account) {
            this.cashAccount.user = account;
        }.bind(this));

        this.$timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });

        this.datePickerOpenStatus.creationDate = false;
        this.datePickerOpenStatus.modifiedDate = false;


        this.selectizeAccountConfig = {
            maxItems: 1,
            valueField: 'id',
            labelField: 'name',
            searchField: ['name'],
            optgroupField: 'type',
            optgroups: [{value: 'ASSET'},
                {value: 'LIABILITY'},
                {value: 'EQUITY'},
                {value: 'INCOME'},
                {value: 'EXPENSE'}],
            optgroupLabelField: 'value'
        };
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
        if (this.cashAccount.currencyId) {
            const currency = lodash.find(this.cashCurrencies, {'id': +this.cashAccount.currencyId});
            this.cashAccount.currency = currency;
        } else {
            this.cashAccount.currency = null;
        }
        if (this.cashAccount.parentAccountId) {
            const parentAccount = lodash.find(this.cashAccounts, {'id': +this.cashAccount.parentAccountId});
            this.cashAccount.parentAccount = parentAccount;
        } else {
            this.cashAccount.parentAccount = null;
        }
        if (this.cashAccount.csvConfigId) {
            const csvConfig = lodash.find(this.csvConfigs, {'id': +this.cashAccount.csvConfigId});
            this.cashAccount.csvConfig = csvConfig;
        } else {
            this.cashAccount.csvConfig = null;
        }

        if (this.cashAccount.parentAccount) {
            this.cashAccount.level = this.cashAccount.parentAccount.level + 1;
        }

        if (this.cashAccount.id !== null) {
            this.CashAccount.update([this.cashAccount], this.onSaveSuccess.bind(this), this.onSaveError.bind(this));
        } else {
            this.CashAccount.save([this.cashAccount], this.onSaveSuccess.bind(this), this.onSaveError.bind(this));
        }
    }

    openCalendar(date) {
        this.datePickerOpenStatus[date] = true;
    }
}

export default {
    bindings: {
        resolve: '<',
        close: '&',
        dismiss: '&'
    },
    template: require('./cash-account-dialog.html'),
    controller: CashAccountDialogController
};
