export default function jhSortBy() {
    function linkFunc(scope, element, attrs, parentCtrl) {
        element.bind('click', function () {
            parentCtrl.sort(attrs.jhSortBy);
        });
    }

    var directive = {
        restrict: 'A',
        scope: false,
        require: '^jhSort',
        link: linkFunc
    };

    return directive;
};