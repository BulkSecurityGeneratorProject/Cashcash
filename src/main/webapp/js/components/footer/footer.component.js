class FooterController {
    constructor() {
    }

    $onInit() {
        this.currentYear = new Date();
    }
}

export default {
    bindings: {
        version: '<'
    },
    template: require('./footer.html'),
    controller: FooterController
};
