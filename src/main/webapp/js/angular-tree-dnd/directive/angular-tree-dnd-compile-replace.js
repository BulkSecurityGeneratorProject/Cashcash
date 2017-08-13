export default function compileReplace($compile) {
    "ngInject";
    return {
        restrict: 'A',
        link: function (scope, element, attrs) {
            scope.$watch(
                attrs.compileReplace, function (new_val) {
                    if (new_val) {
                        element.replaceWith($compile(new_val)(scope));
                    }
                }
            );
        }
    };
};