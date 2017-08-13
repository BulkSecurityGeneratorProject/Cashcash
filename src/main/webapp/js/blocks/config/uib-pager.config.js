export default function pagerConfig(uibPagerConfig, paginationConstants) {
    "ngInject";
    uibPagerConfig.itemsPerPage = paginationConstants.itemsPerPage;
    uibPagerConfig.previousText = '«';
    uibPagerConfig.nextText = '»';
};