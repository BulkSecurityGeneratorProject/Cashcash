import angular from "angular";

export default function TreeDnDOrderBy($filter) {
    "ngInject";
    var _fnOrderBy = $filter('orderBy');
    var for_all_descendants = function (options, node, name, fnOrderBy) {
        var _i;
        var var_len;
        var _nodes;

        if (angular.isDefined(node[name])) {
            _nodes = node[name];
            _len = _nodes.length;
            // OrderBy children
            for (_i = 0; _i < _len; _i++) {
                _nodes[_i] = for_all_descendants(options, _nodes[_i], name, fnOrderBy);
            }

            node[name] = fnOrderBy(node[name], options);
        }
        return node;
    };
    var _fnOrder = function (list, orderBy) {
        return _fnOrderBy(list, orderBy);
    };
    var _fnMain = function (treeData, orderBy) {
        if (!angular.isArray(treeData)
            || treeData.length === 0
            || !(angular.isArray(orderBy) || angular.isObject(orderBy) || angular.isString(orderBy) || angular.isFunction(orderBy))
            || orderBy.length === 0 && !angular.isFunction(orderBy)) {
            return treeData;
        }

        var _i;
        var _len;

        for (_i = 0, _len = treeData.length; _i < _len; _i++) {
            treeData[_i] = for_all_descendants(
                orderBy,
                treeData[_i],
                '__children__',
                _fnOrder
            );
        }

        return _fnOrder(treeData, orderBy);
    };

    return _fnMain;
};