import angular from "angular";

export default function TreeDnDViewport($window, $document, $timeout) {
    "ngInject";

    var viewport = null;
    var isUpdating = false;
    var isRender = false;
    var updateAgain = false;
    var viewportRect;
    var items = [];
    var nodeTemplate;
    var updateTimeout;
    var renderTime;
    var eWindow = angular.element($window);

    function recursivePromise() {
        if (isRender) {
            return;
        }

        var number = items.length;
        var item;

        if (number > 0) {
            item = items[0];

            isRender = true;
            renderTime = $timeout(function () {
                //item.element.html(nodeTemplate);
                //$compile(item.element.contents())(item.scope);

                items.splice(0, 1);
                isRender = false;
                number--;
                $timeout.cancel(renderTime);
                recursivePromise();
            }, 0);

        } else {
            isUpdating = false;
            if (updateAgain) {
                updateAgain = false;
                update();
            }
        }

    }

    function update() {

        viewportRect = {
            width: eWindow.prop('offsetWidth') || document.documentElement.clientWidth,
            height: eWindow.prop('offsetHeight') || document.documentElement.clientHeight,
            top: $document[0].body.scrollTop || $document[0].documentElement.scrollTop,
            left: $document[0].body.scrollLeft || $document[0].documentElement.scrollLeft
        };

        if (isUpdating || isRender) {
            updateAgain = true;
            return;
        }
        isUpdating = true;

        recursivePromise();
    }

    /**
     * @name setViewport
     * @desciption Set the viewport element
     * @param element
     */
    function setViewport(element) {
        viewport = element;
    }

    /**
     * Return the current viewport
     * @returns {*}
     */
    function getViewport() {
        return viewport;
    }

    /**
     * trigger an update
     */
    function updateDelayed() {
        $timeout.cancel(updateTimeout);
        updateTimeout = $timeout(function () {
            update();
        }, 0);
    }

    eWindow.on('load resize scroll', updateDelayed);

    /**
     * Add listener for event
     * @param element
     * @param callback
     */
    function add(scope, element) {
        updateDelayed();
        items.push({
            element: element,
            scope: scope
        });
    }

    function setTemplate(scope, template) {
        nodeTemplate = template;
    }

    /**
     * Get list of items
     * @returns {Array}
     */
    function getItems() {
        return items;
    }

    var $initViewport = {
        setViewport: setViewport,
        getViewport: getViewport,
        add: add,
        setTemplate: setTemplate,
        getItems: getItems,
        updateDelayed: updateDelayed
    };

    return $initViewport;
};