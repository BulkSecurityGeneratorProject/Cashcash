import modules from "../../modules";
import stateConfig from "./cash-balance-graph.state";
import cashBalanceGraphComponent from "./cash-balance-graph.component";


export default modules
    .get('cashcash.cashBalanceGraph', [])
    .component('cashBalanceGraphComponent', cashBalanceGraphComponent)
    .config(stateConfig)
    .name;

