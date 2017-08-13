import modules from "../../modules";
import TreeDnDFilter from "./angular-tree-dnd-filter";
import TreeDnDOrderBy from "./angular-tree-dnd-order-by";

export default modules
    .get('angular-tree-dnd.filter', [])
    .factory('TreeDnDFilter', TreeDnDFilter)
    .factory('TreeDnDOrderBy', TreeDnDOrderBy)
    .name;

