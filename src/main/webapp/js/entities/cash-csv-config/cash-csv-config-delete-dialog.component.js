class CashCsvConfigDeleteController {
    constructor(CashCsvConfig) {
        "ngInject";
        this.CashCsvConfig = CashCsvConfig;
    }

    $onInit() {
    }

    clear() {
        this.dismiss({$value: 'cancel'});
    }

    confirmDelete(id) {
        this.CashCsvConfig.delete({id: id},
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
    template: require('./cash-csv-config-delete-dialog.html'),
    controller: CashCsvConfigDeleteController
};
