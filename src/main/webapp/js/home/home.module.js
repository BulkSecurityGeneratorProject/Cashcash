import modules from "../modules";
import stateConfig from "./home.state";
import HomeController from "./home.controller";

export default modules
    .get('cashcash.home', [])
    .config(stateConfig)
    .controller('HomeController', HomeController)
    .name;

