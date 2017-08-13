import modules from "../../modules";
import stateConfig from "./cash-flow-graph.state";
import cashFlowGraphComponent from "./cash-flow-graph.component";


export default modules
    .get('cashcash.cashFlowGraph', [])
    .component('cashFlowGraphComponent', cashFlowGraphComponent)
    .config(stateConfig)
    .name;

