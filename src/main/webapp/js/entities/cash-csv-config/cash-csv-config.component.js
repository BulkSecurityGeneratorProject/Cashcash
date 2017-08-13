import angular from "angular";

class CashCsvConfigController {
    constructor($state, CashCsvConfig, ParseLinks, AlertService, paginationConstants, FileSaver, Blob) {
        "ngInject";
        this.$state = $state;
        this.CashCsvConfig = CashCsvConfig;
        this.ParseLinks = ParseLinks;
        this.AlertService = AlertService;
        this.paginationConstants = paginationConstants;
        this.FileSaver = FileSaver;
        this.Blob = Blob;
    }

    $onInit() {
        this.predicate = this.pagingParams.predicate;
        this.reverse = this.pagingParams.ascending;
        this.itemsPerPage = this.paginationConstants.itemsPerPage;
        this.loadAll();
    }

    onSuccess(data, headers) {
        const link = headers('link');
        this.links = this.ParseLinks.parse(link);
        this.totalItems = headers('X-Total-Count');
        this.queryCount = this.totalItems;
        this.cashCsvConfigs = data;
        this.page = this.pagingParams.page;
    }

    onError(error) {
        this.AlertService.error(error.data.message);
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    loadAll() {
        this.CashCsvConfig.query({
            page: this.pagingParams.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()
        }, this.onSuccess.bind(this), this.onError.bind(this));
    }

    loadPage(page) {
        this.page = page;
        this.transition();
    }

    transition() {
        this.$state.go(this.$state.$current, {
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc'),
            search: this.currentSearch
        });
    }

    onSuccessExportFile(data, headers) {
        const prettyJson = angular.toJson(data, true);
        const blob = new Blob([prettyJson], {type: 'text/plain;charset=utf-8'});
        this.FileSaver.saveAs(blob, 'csvConfig.json');
    }

    exportFile() {
        this.CashCsvConfig.query({}, this.onSuccessExportFile.bind(this), this.onError.bind(this));
    }
}

export default {
    bindings: {
        pagingParams: '<'
    },
    template: require('./cash-csv-configs.html'),
    controller: CashCsvConfigController
};