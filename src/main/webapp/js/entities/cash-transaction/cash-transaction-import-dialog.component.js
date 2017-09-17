import angular from "angular";
import _ from "lodash";

class CashTransactionImportController {
    constructor(Upload, DateUtils, CashTransaction, CashAccount) {
        "ngInject";
        this.Upload = Upload;
        this.DateUtils = DateUtils;
        this.CashTransaction = CashTransaction;
        this.CashAccount = CashAccount;
    }

    $onInit() {
        this.manualDetection = false;
        this.cashAccounts = this.CashAccount.query();
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


    transformResponse(data) {
        data = angular.fromJson(data);
        data.transactionDate = this.DateUtils.convertDateTimeFromServer(data.transactionDate);
        return new this.CashTransaction(data);
    }

    transformResponseList(data) {
        const transformedList = [];
        _.forEach(data, function (value) {
            const tranformedObj = this.transformResponse(value);
            transformedList.push(tranformedObj);
        }.bind(this));
        return transformedList;
    }

    onImportSuccess(resp) {
        console.log('Success ' + resp.config.data.file.name + ' uploaded. Response: ' + resp.data);
        this.isSuccess = true;
        this.isImporting = false;

        const cashTransactionList = this.transformResponseList(resp.data);
        this.close({$value: cashTransactionList});
    }

    onImportError(resp) {
        console.log('Error status: ' + resp.status);
        this.isError = true;
        this.isImporting = false;
    }

    onImportProgress(evt) {
        const progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
        this.progressPercentage = progressPercentage;
        console.log('progress: ' + progressPercentage + '% ' + evt.config.data.file.name);
    }

    upload(file) {
        this.progressPercentage = 0;
        this.isSuccess = false;
        this.isError = false;
        this.isImporting = true;
        if (this.manualDetection) {
            this.accountId = this.selectedAccountId;
        } else {
            this.accountId = "";
        }
        this.Upload.upload({
            url: 'api/cash-transactions/upload',
            data: {file: file, 'accountId': this.accountId}
        }).then(this.onImportSuccess.bind(this), this.onImportError.bind(this), this.onImportProgress.bind(this));
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
    template: require('./cash-transaction-import-dialog.html'),
    controller: CashTransactionImportController
};
