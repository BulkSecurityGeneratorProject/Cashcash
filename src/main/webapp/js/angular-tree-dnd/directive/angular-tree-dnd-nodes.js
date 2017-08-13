export default function treeDndNodes() {
    return {
        restrict: 'A',
        replace: true,
        link: function (scope, element) {
            scope.$type = 'TreeDnDNodes';

            if (scope.$class.nodes) {
                element.addClass(scope.$class.nodes);
                scope.$nodes_class = scope.$class.nodes;
            } else {
                scope.$nodes_class = '';
            }
        }
    };
};