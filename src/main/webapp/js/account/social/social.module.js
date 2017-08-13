import modules from "../../modules";
import stateConfig from "./social.state";
import SocialService from "./social.service";
import SocialAuthController from "./social-auth.controller";
import SocialRegisterController from "./social-register.controller";
import jhSocial from "./directive/social.directive";

export default modules
    .get('cashcash.account.social', [])
    .config(stateConfig)
    .factory('SocialService', SocialService)
    .controller('SocialAuthController', SocialAuthController)
    .controller('SocialRegisterController', SocialRegisterController)
    .directive('jhSocial', jhSocial)
    .name;

