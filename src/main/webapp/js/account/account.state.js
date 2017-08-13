export default function stateConfig($stateProvider) {
    "ngInject";
    $stateProvider.state('account', {
        abstract: true,
        parent: 'app'
    });
};
