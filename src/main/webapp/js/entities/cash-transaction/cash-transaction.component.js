import angular from "angular";

class CashTransactionController {
    constructor($state, CashTransaction, ParseLinks, AlertService, paginationConstants, CashTransactionUtils, FileSaver, Blob, CashAccount) {
        "ngInject";
        this.$state = $state;
        this.CashTransaction = CashTransaction;
        this.ParseLinks = ParseLinks;
        this.AlertService = AlertService;
        this.paginationConstants = paginationConstants;
        this.CashTransactionUtils = CashTransactionUtils;
        this.FileSaver = FileSaver;
        this.Blob = Blob;
        this.CashAccount = CashAccount;
    }

    $onInit() {
        this.predicate = this.pagingParams.predicate;
        this.reverse = this.pagingParams.ascending;
        this.itemsPerPage = this.paginationConstants.itemsPerPage;
        this.loadAll();
    }

    sort() {
        let result;
        if (this.predicate === 'id') {
            result = ['transactionDate,desc'];
            result.push('id');
        } else {
            result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
            result.push('id');
        }
        return result;
    }

    onSuccess(data, headers) {
        const link = headers('link');
        this.links = this.ParseLinks.parse(link);
        this.totalItems = headers('X-Total-Count');
        this.queryCount = this.totalItems;
        this.cashTransactions = data;
        this.page = this.pagingParams.page;
    }

    onError(error) {
        this.AlertService.error(error.data.message);
    }

    loadAll() {
        const queryParam = {
            page: this.pagingParams.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()
        };

        if (this.filterParams.accountIdList) {
            this.filterAccount = this.CashAccount.get({id: this.filterParams.accountIdList});
            queryParam.accountIdList = +this.filterParams.accountIdList;
        }

        this.CashTransaction.query(queryParam, this.onSuccess.bind(this), this.onError.bind(this));
    }

    transition() {
        const queryParam = {
            page: this.page,
            size: this.itemsPerPage,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        };

        queryParam.accountIdList = this.filterParams.accountIdList ? +this.filterParams.accountIdList : null;

        this.$state.go(this.$state.$current, queryParam);
    }

    addAccountFilter(accountIdList) {
        this.filterParams.accountIdList = accountIdList;
        this.transition();
    }

    removeAccountFilter() {
        this.filterParams.accountIdList = null;
        this.transition();
    }

    increaseAssets(cashTransaction) {
        const split = this.CashTransactionUtils.getPositiveSplit(cashTransaction);
        return split.account.type === "ASSET";
    }

    getPositiveSplit(cashTransaction) {
        return this.CashTransactionUtils.getPositiveSplit(cashTransaction);
    }

    getNegativeSplit(cashTransaction) {
        return this.CashTransactionUtils.getNegativeSplit(cashTransaction);
    }

    onSuccessExportFile(data, headers) {
        const prettyJson = angular.toJson(data, true);
        const blob = new this.Blob([prettyJson], {type: 'text/plain;charset=utf-8'});
        this.FileSaver.saveAs(blob, 'transactions.json');
    }

    exportFile() {
        this.CashTransaction.query({}, this.onSuccessExportFile.bind(this), this.onError.bind(this));
    }

}

export default {
    bindings: {
        pagingParams: '<',
        filterParams: '<',
    },
    template: require('./cash-transactions.html'),
    controller: CashTransactionController
};