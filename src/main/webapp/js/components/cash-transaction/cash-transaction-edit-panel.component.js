class CashTransactionEditComponentController {
    constructor(CashTransactionUtils) {
        "ngInject";
        this.cashTransactionUtils = CashTransactionUtils;
    }

    $onInit() {
        this.datePickerOpenStatus = {
            transactionDate: false
        };

        if (this.isImportPanel === true) {
            value.toImport = !value.ofxIdAlreadyExist;
        }

        this.cashTransactionUtils.flattenTransactionInfo(this.cashTransaction);
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

        this.selectizeCurrencyConfig = {
            maxItems: 1,
            valueField: 'id',
            labelField: 'code',
            searchField: ['code']
        };

        this.cashTransactionTypes = [{type: 'CREDIT', text: 'Generic credit'},
            {type: 'DEBIT', text: 'Generic debit'},
            {type: 'INT', text: 'Interest earned'},
            {type: 'DIV', text: 'Dividend'},
            {type: 'FEE', text: 'Bank fee'},
            {type: 'SRVCHG', text: 'Service charge'},
            {type: 'DEP', text: 'Deposit'},
            {type: 'ATM', text: 'ATM transaction'},
            {type: 'POS', text: 'Point of sale'},
            {type: 'XFER', text: 'Transfer'},
            {type: 'CHECK', text: 'Check'},
            {type: 'PAYMENT', text: 'Electronic payment'},
            {type: 'CASH', text: 'Cash'},
            {type: 'DIRECTDEP', text: 'Direct deposit'},
            {type: 'DIRECTDEBIT', text: 'Merchant-initiated debit'},
            {type: 'REPEATPMT', text: 'Repeating payment'},
            {type: 'OTHER', text: 'Other'}];
    }

    openCalendar(date) {
        this.datePickerOpenStatus[date] = true;
    }
}

export default {
    bindings: {
        cashTransaction: '<',
        cashAccounts: '<',
        cashCurrencies: '<',
        isImportPanel: '@'
    },
    template: require('./cash-transaction-edit-panel.html'),
    controller: CashTransactionEditComponentController
};
