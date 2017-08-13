import modules from "../../modules";
import cashTransactionComponent from "./cash-transaction.component";
import CashTransaction from "./cash-transaction.service";
import stateConfig from "./cash-transaction.state";
import CashTransactionDeleteComponent from "./cash-transaction-delete-dialog.component";
import CashTransactionDetailComponent from "./cash-transaction-detail.component";
import CashTransactionDialogComponent from "./cash-transaction-dialog.component";
import CashTransactionImportComponent from "./cash-transaction-import-dialog.component";
import CashTransactionNewListDialogComponent from "./cash-transaction-new-list-dialog.component";

export default modules
    .get('cashcash.cashTransaction', [])
    .component('cashTransactionComponent', cashTransactionComponent)
    .factory('CashTransaction', CashTransaction)
    .component('cashTransactionDeleteComponent', CashTransactionDeleteComponent)
    .component('cashTransactionDetailComponent', CashTransactionDetailComponent)
    .component('cashTransactionDialogComponent', CashTransactionDialogComponent)
    .component('cashTransactionImportComponent', CashTransactionImportComponent)
    .component('cashTransactionNewListDialogComponent', CashTransactionNewListDialogComponent)
    .config(stateConfig)
    .name;

