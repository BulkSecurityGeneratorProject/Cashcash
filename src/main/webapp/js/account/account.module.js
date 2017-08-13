import modules from "../modules";
import stateConfig from "./account.state";
import social from "./social/social.module";
import settings from "./settings/settings.module";
import reset from "./reset/reset.module";
import register from "./register/register.module";
import password from "./password/password.module";
import activate from "./activate/activate.module";
import authModule from "../services/auth/auth.module";

export default modules
    .get('cashcash.account', [
        social,
        settings,
        reset,
        register,
        password,
        activate,
        authModule
    ])
    .config(stateConfig)
    .name;

