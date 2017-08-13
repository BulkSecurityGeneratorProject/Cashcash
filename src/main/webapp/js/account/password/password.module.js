import modules from "../../modules";
import stateConfig from "./password.state";
import PasswordController from "./password.controller";
import passwordStrengthBar from "./password-strength-bar.directive";
import authModule from "../../services/auth/auth.module";

export default modules
    .get('cashcash.account.password', [
        authModule
    ])
    .config(stateConfig)
    .controller('PasswordController', PasswordController)
    .directive('passwordStrengthBar', passwordStrengthBar)
    .name;

