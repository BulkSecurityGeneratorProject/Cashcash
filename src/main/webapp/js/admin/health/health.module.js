import modules from "../../modules";
import stateConfig from "./health.state";
import JhiHealthCheckController from "./health.controller";
import HealthModalController from "./health.modal.controller";
import JhiHealthService from "./health.service";

export default modules
    .get('cashcash.admin.health', [])
    .config(stateConfig)
    .controller('JhiHealthCheckController', JhiHealthCheckController)
    .controller('HealthModalController', HealthModalController)
    .factory('JhiHealthService', JhiHealthService)
    .name;

