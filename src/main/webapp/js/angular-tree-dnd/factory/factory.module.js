import modules from "../../modules";
import TreeDnDConvert from "./angular-tree-dnd-convert";
import TreeDnDHelper from "./angular-tree-dnd-helper";
import TreeDnDPlugin from "./angular-tree-dnd-plugin";
import TreeDnDTemplate from "./angular-tree-dnd-template";
import TreeDnDViewport from "./angular-tree-dnd-viewport";

export default modules
    .get('angular-tree-dnd.factory', [])
    .factory('TreeDnDConvert', TreeDnDConvert)
    .factory('TreeDnDHelper', TreeDnDHelper)
    .factory('TreeDnDPlugin', TreeDnDPlugin)
    .factory('TreeDnDTemplate', TreeDnDTemplate)
    .factory('TreeDnDViewport', TreeDnDViewport)
    .name;

