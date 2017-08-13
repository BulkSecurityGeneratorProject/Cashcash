import modules from "../modules";
import authInterceptor from "./interceptor/auth.interceptor";
import authExpiredInterceptor from "./interceptor/auth-expired.interceptor";
import errorHandlerInterceptor from "./interceptor/errorhandler.interceptor";
import notificationInterceptor from "./interceptor/notification.interceptor";
import stateHandler from "./handlers/state.handler";
import compileServiceConfig from "./config/compile.config";
import httpConfig from "./config/http.config";
import localStorageConfig from "./config/localstorage.config";
import pagerConfig from "./config/uib-pager.config";
import paginationConfig from "./config/uib-pagination.config";

export default modules
    .get('cashcash.blocks', [])
    .factory('authInterceptor', authInterceptor)
    .factory('authExpiredInterceptor', authExpiredInterceptor)
    .factory('errorHandlerInterceptor', errorHandlerInterceptor)
    .factory('notificationInterceptor', notificationInterceptor)
    .factory('stateHandler', stateHandler)
    .config(compileServiceConfig)
    .config(httpConfig)
    .config(localStorageConfig)
    .config(pagerConfig)
    .config(paginationConfig)
    .name;

