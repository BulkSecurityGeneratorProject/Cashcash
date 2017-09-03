import modules from "./modules";

export default modules
    .get('cashcash.appConstant', [])
    .constant('VERSION', '0.5.0-SNAPSHOT')
    .constant('DEBUG_INFO_ENABLED', true)
    .name;

