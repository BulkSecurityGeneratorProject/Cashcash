export default function stateConfig($stateProvider) {
    "ngInject";
    $stateProvider.state('entity', {
        abstract: true,
        parent: 'app'
    });
};
