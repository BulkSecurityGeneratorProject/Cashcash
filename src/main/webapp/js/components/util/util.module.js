import modules from "../../modules";
import Base64 from "./base64.service";
import capitalize from "./capitalize.filter";
import CashAccountUtils from "./cash-account-util.service";
import CashTransactionUtils from "./cash-transaction-util.service";
import CashSplitCumulativeUtils from "./cash-split-cumulative-util.service";
import CashConverter from "./cash-converter";
import DataUtils from "./data-util.service";
import DateUtils from "./date-util.service";
import jhiItemCount from "./jhi-item-count.directive";
import PaginationUtil from "./pagination-util.service";
import ParseLinks from "./parse-links.service";
import jhSort from "./sort.directive";
import jhSortBy from "./sort-by.directive";
import characters from "./truncate-characters.filter";
import words from "./truncate-words.filter";

export default modules
    .get('cashcash.components.util', [])
    .factory('Base64', Base64)
    .filter('capitalize', capitalize)
    .service('CashAccountUtils', CashAccountUtils)
    .service('CashTransactionUtils', CashTransactionUtils)
    .service('CashSplitCumulativeUtils', CashSplitCumulativeUtils)
    .service('CashConverter', CashConverter)
    .factory('DataUtils', DataUtils)
    .factory('DateUtils', DateUtils)
    .component('jhiItemCount', jhiItemCount)
    .factory('PaginationUtil', PaginationUtil)
    .factory('ParseLinks', ParseLinks)
    .directive('jhSort', jhSort)
    .directive('jhSortBy', jhSortBy)
    .filter('characters', characters)
    .filter('words', words)
    .name;

