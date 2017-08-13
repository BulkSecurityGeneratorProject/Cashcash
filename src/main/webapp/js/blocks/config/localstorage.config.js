export default function localStorageConfig($localStorageProvider, $sessionStorageProvider) {
    "ngInject";
    $localStorageProvider.setKeyPrefix('jhi-');
    $sessionStorageProvider.setKeyPrefix('jhi-');
};
