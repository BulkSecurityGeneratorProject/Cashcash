export default function jhSocial($filter, SocialService) {
    "ngInject";

    /* private helper methods */

    function linkFunc(scope) {

        scope.label = $filter('capitalize')(scope.provider);
        scope.providerSetting = SocialService.getProviderSetting(scope.provider);
        scope.providerURL = SocialService.getProviderURL(scope.provider);
        scope.csrf = SocialService.getCSRF();
    }

    var directive = {
        restrict: 'E',
        scope: {
            provider: '@ngProvider'
        },
        template: require('./social.html'),
        link: linkFunc
    };

    return directive;
}