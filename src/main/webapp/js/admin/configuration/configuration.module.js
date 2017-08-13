import modules from "../../modules";
import stateConfig from "./configuration.state";
import JhiConfigurationController from "./configuration.controller";
import JhiConfigurationService from "./configuration.service";

export default modules
    .get('cashcash.admin.configuration', [])
    .config(stateConfig)
    .controller('JhiConfigurationController', JhiConfigurationController)
    .factory('JhiConfigurationService', JhiConfigurationService)
    .name;

