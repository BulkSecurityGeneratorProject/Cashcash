import modules from "../modules";
import "../../resources/css/ng-tree-dnd.css";
import treeDndTemplate from "./module/angular-tree-dnd-template.append";
import factory from "./factory/factory.module";
import filter from "./filter/filter.module";
import plugin from "./plugin/plugin.module";
import directive from "./directive/directive.module";

export default modules
    .get('nttTreeDnD', [
        treeDndTemplate,
        factory,
        filter,
        plugin,
        directive
    ])
    .constant(
        'TreeDnDClass', {
            tree: 'tree-dnd',
            empty: 'tree-dnd-empty',
            hidden: 'tree-dnd-hidden',
            node: 'tree-dnd-node',
            nodes: 'tree-dnd-nodes',
            handle: 'tree-dnd-handle',
            place: 'tree-dnd-placeholder',
            drag: 'tree-dnd-drag',
            status: 'tree-dnd-status',
            icon: {
                '1': 'glyphicon glyphicon-minus',
                '0': 'glyphicon glyphicon-plus',
                '-1': 'glyphicon glyphicon-file'
            }
        })
    .name;
