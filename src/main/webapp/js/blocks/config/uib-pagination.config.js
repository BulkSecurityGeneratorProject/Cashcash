export default function paginationConfig(uibPaginationConfig, paginationConstants) {
    "ngInject";
    uibPaginationConfig.itemsPerPage = paginationConstants.itemsPerPage;
    uibPaginationConfig.maxSize = 5;
    uibPaginationConfig.boundaryLinks = true;
    uibPaginationConfig.firstText = '«';
    uibPaginationConfig.previousText = '‹';
    uibPaginationConfig.nextText = '›';
    uibPaginationConfig.lastText = '»';
};