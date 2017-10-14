import modules from "../modules";
import util from "./util/util.module";
import login from "./login/login.module";
import form from "./form/form.module";
import footer from "./footer/footer.component";
import cashTransactionEdit from "./cash-transaction/cash-transaction-edit-panel.component";
import accountInteractionPanel from "./account-interaction-panel/account-interaction-panel.components";
import alert from "./alert/alert.module";
import cashTransactionEditVue from "./cash-transaction/cash-transaction-edit-panel.vue";
import cashTransactionEditListVue from "./cash-transaction/cash-transaction-edit-list.vue";

export default modules
    .get('cashcash.components', [
        util,
        login,
        form,
        alert
    ])
    .component('footer', footer)
    .component('cashTransactionEdit', cashTransactionEdit)
    .component('accountInteractionPanel', accountInteractionPanel)
    .value('CashTransactionEditVue', cashTransactionEditVue)
    .value('CashTransactionEditListVue', cashTransactionEditListVue)
    .name;

