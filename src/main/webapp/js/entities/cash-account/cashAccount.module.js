import modules from "../../modules";
import stateConfig from "./cash-account.state";
import cashAccountComponent from "./cash-account.component";
import CashAccount from "./cash-account.service";
import cashAccountDeleteComponent from "./cash-account-delete-dialog.component";
import cashAccountDetailComponent from "./cash-account-detail.component";
import cashAccountDialogComponent from "./cash-account-dialog.component";
import cashAccountImportComponent from "./cash-account-import-dialog.component";
import cashAccountOrganizeDialogComponent from "./cash-account-organize-dialog.component";

export default modules
    .get('cashcash.cashAccount', [])
    .component('cashAccountComponent', cashAccountComponent)
    .factory('CashAccount', CashAccount)
    .component('cashAccountDeleteComponent', cashAccountDeleteComponent)
    .component('cashAccountDetailComponent', cashAccountDetailComponent)
    .component('cashAccountDialogComponent', cashAccountDialogComponent)
    .component('cashAccountImportComponent', cashAccountImportComponent)
    .component('cashAccountOrganizeDialogComponent', cashAccountOrganizeDialogComponent)
    .config(stateConfig)
    .name;

