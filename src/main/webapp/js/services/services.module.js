import modules from "../modules";
import userService from "./user/user.service";
import pageRibbon from "./profiles/page-ribbon.component";
import ProfileService from "./profiles/profile.service";
import auth from "./auth/auth.module";

export default modules
    .get('cashcash.service', [
        auth
    ])
    .factory('User', userService)
    .component('pageRibbon', pageRibbon)
    .factory('ProfileService', ProfileService)
    .name;

