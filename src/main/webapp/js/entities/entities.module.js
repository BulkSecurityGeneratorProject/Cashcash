import modules from "../modules";
import stateConfig from "./entity.state";
import cashTransaction from "./cash-transaction/cashTransaction.module";
import CashSplitSum from "./cash-split-sum/cash-split-sum.service";
import CashSplitCumulative from "./cash-split-cumulative/cash-split-cumulative.service";
import CashSplit from "./cash-split/cash-split.service";
import CashCurrency from "./cash-currency/cash-currency.service";
import CashRate from "./cash-rate/cash-rate.service";
import cashFlowGraph from "./cash-flow-graph/cashFlowGraph.module";
import cashCsv from "./cash-csv-config/cashCsv.module";
import cashBalanceGraph from "./cash-balance-graph/cashBalanceGraph.module";
import cashAccount from "./cash-account/cashAccount.module";

export default modules
    .get('cashcash.entities', [
        cashTransaction,
        cashFlowGraph,
        cashCsv,
        cashBalanceGraph,
        cashAccount
    ])
    .factory('CashSplitSum', CashSplitSum)
    .factory('CashSplitCumulative', CashSplitCumulative)
    .factory('CashSplit', CashSplit)
    .factory('CashCurrency', CashCurrency)
    .factory('CashRate', CashRate)
    .config(stateConfig)
    .name;

