import modules from "../../modules";
import stateConfig from "./settings.state";
import SettingsController from "./settings.controller";
import authModule from "../../services/auth/auth.module";

export default modules
    .get('cashcash.account.settings', [
        authModule
    ])
    .config(stateConfig)
    .controller('SettingsController', SettingsController)
    .name;

