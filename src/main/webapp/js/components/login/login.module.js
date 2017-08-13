import modules from "../../modules";
import LoginService from "./login.service";
import LoginController from "./login.controller";

export default modules
    .get('cashcash.components.login', [])
    .factory('LoginService', LoginService)
    .controller('LoginController', LoginController)
    .name;

