import modules from "../../modules";
import treeDnd from "./angular-tree-dnd";
import compile from "./angular-tree-dnd-compile";
import compileReplace from "./angular-tree-dnd-compile-replace";
import treeDndNode from "./angular-tree-dnd-node";
import treeDndNodeHandle from "./angular-tree-dnd-node-handle";
import treeDndNodes from "./angular-tree-dnd-nodes";

export default modules
    .get('angular-tree-dnd.directive', [])
    .directive('treeDnd', treeDnd)
    .directive('compile', compile)
    .directive('compileReplace', compileReplace)
    .directive('treeDndNode', treeDndNode)
    .directive('treeDndNodeHandle', treeDndNodeHandle)
    .directive('treeDndNodes', treeDndNodes)
    .name;

