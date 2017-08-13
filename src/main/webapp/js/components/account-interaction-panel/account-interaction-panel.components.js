class AccountInteractionPanelComponentController {
    constructor() {
        "ngInject";
    }

    isDeletable() {
        return this.cashAccount.level > 1 && this.cashAccount.__children__.length === 0;
    }

    isEditable() {
        return this.cashAccount.level > 1;
    }
}

export default {
    bindings: {
        cashAccount: '<',
    },
    template: require('./account-interaction-panel.html'),
    controller: AccountInteractionPanelComponentController
};
