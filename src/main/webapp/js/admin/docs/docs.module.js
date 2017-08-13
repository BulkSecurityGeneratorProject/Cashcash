import modules from "../../modules";
import stateConfig from "./docs.state";
export default modules
    .get('cashcash.admin.docs', [])
    .config(stateConfig)
    .name;

