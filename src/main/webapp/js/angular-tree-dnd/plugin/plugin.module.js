import modules from "../../modules";
import TreeDnDDrag from "./angular-tree-dnd-drag";
import TreeDnDControl from "./angular-tree-dnd-tree-control";

export default modules
    .get('angular-tree-dnd.plugin', [])
    .factory('TreeDnDDrag', TreeDnDDrag)
    .factory('TreeDnDControl', TreeDnDControl)
    .name;

