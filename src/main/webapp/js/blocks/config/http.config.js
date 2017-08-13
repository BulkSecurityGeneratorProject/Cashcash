import angular from "angular";

export default function httpConfig($urlRouterProvider, CacheFactoryProvider, $httpProvider, $urlMatcherFactoryProvider) {
    "ngInject";

    angular.extend(CacheFactoryProvider.defaults, { maxAge: 15 * 60 * 1000 });

    $urlRouterProvider.otherwise('/');

    $httpProvider.interceptors.push('errorHandlerInterceptor');
    $httpProvider.interceptors.push('authExpiredInterceptor');
    $httpProvider.interceptors.push('authInterceptor');
    $httpProvider.interceptors.push('notificationInterceptor');

    $urlMatcherFactoryProvider.type('boolean', {
        name: 'boolean',
        decode: function (val) {
            return val === true || val === 'true';
        },
        encode: function (val) {
            return val ? 1 : 0;
        },
        equals: function (a, b) {
            return this.is(a) && a === b;
        },
        is: function (val) {
            return [true, false, 0, 1].indexOf(val) >= 0;
        },
        pattern: /bool|true|0|1/
    });
};