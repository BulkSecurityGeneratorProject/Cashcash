import modules from "../../modules";
import stateConfig from "./audits.state";
import AuditsController from "./audits.controller";
import AuditsService from "./audits.service";

export default modules
    .get('cashcash.admin.audits', [])
    .config(stateConfig)
    .controller('AuditsController', AuditsController)
    .factory('AuditsService', AuditsService)
    .name;

