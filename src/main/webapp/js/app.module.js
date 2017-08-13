import "./visualize/utils/lodash-mixins/index";
import "bootstrap/dist/css/bootstrap.css";
import "font-awesome/css/font-awesome.css";
import "nvd3/build/nv.d3.css"
import "angular-loading-bar/build/loading-bar.css";
import "../resources/css/main.css";
import "../resources/css/ng-tree-dnd.css";
import "../resources/css/selectize.bootstrap3.css";
import "../resources/favicon.ico";
import "jquery";
import "selectize";
import ngStorageWebpack from "ngstorage-webpack";
import angularResource from "angular-resource";
import angularCookies from "angular-cookies";
import angularUiRouter from "@uirouter/angularjs";
import angularAnimate from "angular-animate";
import angularMoment from "angular-moment";
import angularLoadingBar from "angular-loading-bar";
import angularFileSaver from "angular-file-saver";
import appConstant from "./app.constants";
import appState from "./app.state";
import blocks from "./blocks/blocks.module";
import angularCache from "angular-cache";
import angularBootstrap from "angular-ui-bootstrap";
import components from "./components/components.module";
import services from "./services/services.module";
import angularAria from "angular-aria";
import ngFileUpload from "ng-file-upload";
import angularSelectize2 from "angular-selectize2/dist/selectize";
import uiBootstrapDatetimepicker from "bootstrap-ui-datetime-picker";
import angularTreeDnd from "./angular-tree-dnd/angular-tree-dnd.module";
import ngInfiniteScroll from "ng-infinite-scroll";
import admin from "./admin/admin.module";
import account from "./account/account.module";
import error from "./layouts/error/error.module";
import navbar from "./layouts/navbar/navbar.module";
import home from "./home/home.module";
import entities from "./entities/entities.module";
import modules from "./modules";
import privateHelper from "./private";
import "./visualize/index";

// We manually assign the missing names
angularSelectize2.name = "selectize";

const cashcashApp = modules
    .get('cashcashApp', [
        ngStorageWebpack,
        angularResource,
        angularCookies,
        angularAria,
        angularCache,
        ngFileUpload,
        angularBootstrap,
        uiBootstrapDatetimepicker,
        angularUiRouter,
        ngInfiniteScroll,
        angularAnimate,
        angularMoment,
        angularLoadingBar,
        angularFileSaver,
        angularSelectize2.name,
        /////////////////
        //cashcash module
        /////////////////
        angularTreeDnd,
        appConstant,
        services,
        navbar,
        error,
        home,
        entities,
        components,
        blocks,
        admin,
        account,
        privateHelper
    ])
    .config(appState)
    .run(run);

function run(stateHandler) {
    "ngInject";
    stateHandler.initialize();
}

modules.link(cashcashApp);
