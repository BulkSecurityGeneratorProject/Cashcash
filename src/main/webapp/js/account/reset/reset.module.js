import modules from "../../modules";
import stateConfigFinish from "./finish/reset.finish.state";
import stateConfigRequest from "./request/reset.request.state";
import ResetFinishController from "./finish/reset.finish.controller";
import RequestResetController from "./request/reset.request.controller";
import authModule from "../../services/auth/auth.module";
import loginModule from "../../components/login/login.module";

export default modules
    .get('cashcash.account.reset', [
        authModule,
        loginModule
    ])
    .config(stateConfigFinish)
    .config(stateConfigRequest)
    .controller('ResetFinishController', ResetFinishController)
    .controller('RequestResetController', RequestResetController)
    .name;

