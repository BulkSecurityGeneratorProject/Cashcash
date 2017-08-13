import modules from "../../modules";
import activeLink from "./active-link.directive";
import NavbarController from "./navbar.controller";

export default modules
    .get('cashcash.layouts.navbar', [])
    .directive('activeLink', activeLink)
    .controller('NavbarController', NavbarController)
    .name;

