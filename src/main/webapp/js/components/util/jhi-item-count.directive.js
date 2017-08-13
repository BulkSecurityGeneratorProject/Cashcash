class JhiItemCountController {
    constructor() {
    }

    count() {
        if ((this.page - 1) * this.itemsPerPage === 0) {
            return 1;
        } else {
            return (this.page - 1) * this.itemsPerPage + 1
        }
    };

    count2() {
        if (this.page * this.itemsPerPage < this.queryCount) {
            return this.page * this.itemsPerPage;
        } else {
            return this.queryCount;
        }
    }
}

export default {
    template: require("./jhi-item-count.directive.html"),
    controller: JhiItemCountController,
    bindings: {
        page: '<',
        queryCount: '<',
        itemsPerPage: '<'
    }
};
