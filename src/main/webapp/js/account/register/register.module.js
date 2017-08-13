import modules from "../../modules";
import stateConfig from "./register.state";
import RegisterController from "./register.controller";
import authModule from "../../services/auth/auth.module";
import loginModule from "../../components/login/login.module";

export default modules
    .get('cashcash.account.register', [
        authModule,
        loginModule
    ])
    .config(stateConfig)
    .controller('RegisterController', RegisterController)
    .name;

