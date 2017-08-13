import angular from "angular";

export default function treeDndNode(TreeDnDViewport) {
    "ngInject";

    function fnLink(scope, element, attrs) {

        scope.$node_class = '';

        if (scope.$class.node) {
            element.addClass(scope.$class.node);
            scope.$node_class = scope.$class.node;
        }
        var enabledDnD = typeof scope.dragEnabled === 'boolean' || typeof scope.dropEnabled === 'boolean';
        var keyNode = attrs.treeDndNode;
        var first = true;
        var childsElem;
        TreeDnDViewport.add(scope, element);

        if (enabledDnD) {
            scope.$type = 'TreeDnDNode';

            scope.getData = function () {
                return scope[keyNode];
            };
        }

        scope.$element = element;
        scope[keyNode].__inited__ = true;

        scope.getElementChilds = function () {
            return angular.element(element[0].querySelector('[tree-dnd-nodes]'));
        };

        scope.setScope(scope, scope[keyNode]);

        scope.getScopeNode = function () {
            return scope;
        };

        var objprops = [];
        var objexpr;
        var i;
        var keyO = Object.keys(scope[keyNode]);
        var lenO = keyO.length;
        var hashKey = scope[keyNode].__hashKey__;
        var skipAttr = [
            '__visible__',
            '__children__',
            '__level__',
            '__index__',
            '__index_real__',

            '__parent__',
            '__parent_real__',
            '__dept__',
            '__icon__',
            '__icon_class__'
        ];
        var keepAttr = [
            '__expanded__'
        ];
        var lenKeep = keepAttr.length;

        // skip __visible__
        for (i = 0; i < lenO + lenKeep; i++) {
            if (i < lenO) {
                if (skipAttr.indexOf(keyO[i]) === -1) {
                    objprops.push(keyNode + '.' + keyO[i]);
                }
            } else {
                if (keyO.indexOf(keepAttr[i - lenO]) === -1) {
                    objprops.push(keyNode + '.' + keepAttr[i - lenO]);
                }
            }
        }

        objexpr = '[' + objprops.join(',') + ']';

        function fnWatchNode(newVal, oldVal, scope) {

            var nodeOf = scope[keyNode];
            var _icon;

            if (first) {
                _icon = nodeOf.__icon__;
                nodeOf.__icon_class__ = scope.$class.icon[_icon];
            } else {

                var parentReal = nodeOf.__parent_real__;
                var parentNode = scope.tree_nodes[parentReal] || null;
                var _childs = nodeOf.__children__;
                var _len = _childs.length;
                var _i;

                if (!nodeOf.__inited__) {
                    nodeOf.__inited__ = true;
                }

                if (nodeOf.__hashKey__ !== hashKey) {
                    // clear scope in $globals
                    scope.deleteScope(scope, nodeOf);

                    // add new scope into $globals
                    scope.setScope(scope, nodeOf);
                    hashKey = nodeOf.__hashKey__;
                }

                if (parentNode && (!parentNode.__expanded__ || !parentNode.__visible__)) {
                    element.addClass(scope.$class.hidden);
                    nodeOf.__visible__ = false;
                } else {
                    element.removeClass(scope.$class.hidden);
                    nodeOf.__visible__ = true;
                }

                if (_len === 0) {
                    _icon = -1;
                } else {
                    if (nodeOf.__expanded__) {
                        _icon = 1;
                    } else {
                        _icon = 0;
                    }
                }

                nodeOf.__icon__ = _icon;
                nodeOf.__icon_class__ = scope.$class.icon[_icon];

                if (scope.isTable) {
                    for (_i = 0; _i < _len; _i++) {
                        scope.for_all_descendants(_childs[_i], scope.hiddenChild, nodeOf, true);
                    }
                } else {
                    if (!childsElem) {
                        childsElem = scope.getElementChilds();
                    }

                    if (nodeOf.__expanded__) {
                        childsElem.removeClass(scope.$class.hidden);
                    } else {
                        childsElem.addClass(scope.$class.hidden);
                    }
                }

            }

            first = false;

        }

        scope.$watch(objexpr, fnWatchNode, true);

        scope.$on('$destroy', function () {
            scope.deleteScope(scope, scope[keyNode]);
        });
    }

    return {
        restrict: 'A',
        replace: true,
        link: fnLink
    };
};