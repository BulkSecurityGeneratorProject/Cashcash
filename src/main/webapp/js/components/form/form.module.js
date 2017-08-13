import modules from "../../modules";
import maxbytes from "./maxbytes.directive";
import minbytes from "./minbytes.directive";
import onReadFile from "./on-read-file.directive";
import paginationConstants from "./pagination.constants";
import showValidation from "./show-validation.directive";

export default modules
    .get('cashcash.components.form', [])
    .directive('maxbytes', maxbytes)
    .directive('minbytes', minbytes)
    .directive('onReadFile', onReadFile)
    .constant('paginationConstants', paginationConstants)
    .directive('showValidation', showValidation)
    .name;

