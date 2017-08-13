import modules from "../../modules";
import UserManagementDetailController from "./user-management-detail.controller";
import UserManagementDialogController from "./user-management-dialog.controller";
import UserManagementDeleteController from "./user-management-delete-dialog.controller";
import UserManagementController from "./user-management.controller";
import stateConfig from "./user-management.state";

export default modules
    .get('cashcash.admin.userManagement', [])
    .config(stateConfig)
    .controller('UserManagementController', UserManagementController)
    .controller('UserManagementDeleteController', UserManagementDeleteController)
    .controller('UserManagementDetailController', UserManagementDetailController)
    .controller('UserManagementDialogController', UserManagementDialogController)
    .name;

