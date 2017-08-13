export default function stateConfig($stateProvider) {
    "ngInject";
    $stateProvider.state('admin', {
        abstract: true,
        parent: 'app'
    });
}