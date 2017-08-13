class CashCsvConfigDialogController {
    constructor($timeout, CashCsvConfig, CashAccount, Principal, CashCurrency) {
        "ngInject";
        this.$timeout = $timeout;
        this.CashCsvConfig = CashCsvConfig;
        this.CashAccount = CashAccount;
        this.CashCurrency = CashCurrency;
        this.Principal = Principal;

        if (!this.resolve || !this.resolve.entity) {
            this.cashCsvConfig = {
                id: null,
                creationDate: null,
                modifiedDate: null,
                name: null,
                charset: null,
                hasHeader: null,
                quoteChar: null,
                delimiterChar: null,
                endOfLineSymbols: null,
                transactionDateFormat: null,
                decimalDelimiter: null,
                uniqIdColumnIndex: null,
                descriptionColumnIndex: null,
                detailDescriptionColumnIndex: null,
                accountCodeNumberColumnIndex: null,
                transactionDateColumnIndex: null,
                transactionTypeColumnIndex: null,
                amountColumnIndex: null,
                currencyColumnIndex: null
            };
        } else {
            this.cashCsvConfig = this.resolve.entity;
        }
    }

    $onInit() {
        this.cashCurrencies = this.CashCurrency.query();
        this.cashAccounts = this.CashAccount.query();

        this.Principal.identity().then(function (account) {
            this.cashCsvConfig.user = account;
        }.bind(this));

        this.endOfLineSymbolsList = [
            {value: '\\n', description: 'used in Unix/Mac OS X'},
            {value: '\\r\\n', description: 'used in Windows'}
        ];

        this.selectizeConfig = {
            closeAfterSelect: true,
            valueField: 'value',
            searchField: ['value', 'description'],
            maxItems: 1,
            render: {
                item: function (data, escape) {
                    return '<div>' + escape(data.value) + '</div>';
                },
                option: function (data, escape) {
                    return '<div><strong>' + escape(data.value) +
                        '</strong> ' + escape(data.description) + '</div>';
                }
            }
        };

        this.charsetList = require('./charsetList.json');
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
        if (this.cashCsvConfig.id !== null) {
            this.CashCsvConfig.update(this.cashCsvConfig, this.onSaveSuccess.bind(this), this.onSaveError.bind(this));
        } else {
            this.CashCsvConfig.save(this.cashCsvConfig, this.onSaveSuccess.bind(this), this.onSaveError.bind(this));
        }
    }
}

export default {
    bindings: {
        resolve: '<',
        close: '&',
        dismiss: '&'
    },
    template: require('./cash-csv-config-dialog.html'),
    controller: CashCsvConfigDialogController
};