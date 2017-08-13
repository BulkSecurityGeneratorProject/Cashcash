class PageRibbonComponentController {
    constructor(ProfileService) {
        "ngInject";
        this.ProfileService = ProfileService;
    }

    $onInit() {
        this.ProfileService.getProfileInfo().then(function (response) {
            if (response.ribbonEnv) {
                this.ribbonEnv = response.ribbonEnv;
            }
        }.bind(this));
    }
}

export default {
    bindings: {
    },
    template: require('./page-ribbon.html'),
    controller: PageRibbonComponentController
};
