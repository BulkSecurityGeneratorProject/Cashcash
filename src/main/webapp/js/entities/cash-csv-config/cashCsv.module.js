import modules from "../../modules";
import stateConfig from "./cash-csv-config.state";
import cashCsvConfigComponent from "./cash-csv-config.component";
import CashCsvConfig from "./cash-csv-config.service";
import cashCsvConfigDeleteComponent from "./cash-csv-config-delete-dialog.component";
import cashCsvConfigDetailComponent from "./cash-csv-config-detail.component";
import cashCsvConfigDialogComponent from "./cash-csv-config-dialog.component";

export default modules
    .get('cashcash.cashCsv', [])
    .component('cashCsvConfigComponent', cashCsvConfigComponent)
    .factory('CashCsvConfig', CashCsvConfig)
    .component('cashCsvConfigDeleteComponent', cashCsvConfigDeleteComponent)
    .component('cashCsvConfigDetailComponent', cashCsvConfigDetailComponent)
    .component('cashCsvConfigDialogComponent', cashCsvConfigDialogComponent)
    .config(stateConfig)
    .name;

