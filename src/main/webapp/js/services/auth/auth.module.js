import modules from "../../modules";
import Account from "./account.service";
import Activate from "./activate.service";
import AuthServerProvider from "./auth.jwt.service";
import Auth from "./auth.service";
import hasAnyAuthority from "./has-any-authority.directive";
import hasAuthority from "./has-authority.directive";
import Password from "./password.service";
import PasswordResetFinish from "./password-reset-finish.service";
import PasswordResetInit from "./password-reset-init.service";
import Principal from "./principal.service";
import Register from "./register.service";

export default modules
    .get('cashcash.service.auth', [])
    .factory('Account', Account)
    .factory('Activate', Activate)
    .factory('AuthServerProvider', AuthServerProvider)
    .factory('Auth', Auth)
    .directive('hasAnyAuthority', hasAnyAuthority)
    .directive('hasAuthority', hasAuthority)
    .factory('Password', Password)
    .factory('PasswordResetFinish', PasswordResetFinish)
    .factory('PasswordResetInit', PasswordResetInit)
    .factory('Principal', Principal)
    .factory('Register', Register)
    .name;

