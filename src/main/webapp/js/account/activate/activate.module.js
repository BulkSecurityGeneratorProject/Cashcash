import modules from "../../modules";
import stateConfig from "./activate.state";
import ActivationController from "./activate.controller";
import authModule from "../../services/auth/auth.module";
import loginModule from "../../components/login/login.module";

export default modules
    .get('cashcash.account.activate', [
        authModule,
        loginModule
    ])
    .config(stateConfig)
    .controller('ActivationController', ActivationController)
    .name;

