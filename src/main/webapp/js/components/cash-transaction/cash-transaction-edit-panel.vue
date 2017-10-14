<template>
    <div class="panel panel-default">
        <div class="panel-body" v-if="isMounted">

            <div class="row" v-if="isImportPanel">
                <div class="col-lg-1">
                    <div class="form-group">
                        <div class="input-group">
                            <div type="checkbox" class="btn btn-primary"
                                 @click="toggleToImport">
                                <i v-if="cashTransaction.toImport"
                                   class="glyphicon glyphicon-check"></i>
                                <i v-if="!cashTransaction.toImport"
                                   class="glyphicon glyphicon-unchecked"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-lg-12">
                    <div class="form-group">
                        <div class="input-group">
                            <div class="input-group-addon" uib-tooltip="description">
                                <i class="fa fa-file-text"></i>
                            </div>
                            <input type="text"
                                   class="form-control"
                                   name="description"
                                   id="field_description"
                                   v-model="cashTransaction.description"
                                   placeholder="description"
                                   required ng-minlength="2"/>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-lg-3">
                    <div class="form-group">
                        <div class="input-group">
                            <div class="input-group-addon btn btn-default" uib-tooltip="transactionDate">
                                <i ng-click="$ctrl.openCalendar('transactionDate')"
                                   class="glyphicon glyphicon-calendar"></i>
                            </div>
                            <input id="field_transactionDate"
                                   type="text"
                                   class="form-control"
                                   name="transactionDate"
                                   datetime-picker="yyyy-MM-dd"
                                   v-model="cashTransaction.transactionDate"
                                   is-open="$ctrl.datePickerOpenStatus.transactionDate"
                                   placeholder="transactionDate"
                                   required/>
                        </div>
                    </div>
                </div>
                <div class="col-lg-3">
                    <div class="form-group">
                        <div class="input-group">
                            <div class="input-group-addon" uib-tooltip="type">
                                <i class="fa fa-tag"></i>
                            </div>
                            <selectize v-model="cashTransaction.type"
                                       :settings="getSelectizeTransactionTypeConfig">
                            </selectize>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row">

                <div class="col-lg-6">
                    <div class="form-group">
                        <div class="input-group">
                            <div class="input-group-addon" uib-tooltip="amount">
                                <i class="glyphicon glyphicon-euro"></i>
                            </div>
                            <div class="row no-gutters">
                                <div class="col-lg-8">
                                    <input type="number"
                                           class="form-control"
                                           name="amount"
                                           id="field_amount"
                                           v-model="cashTransaction.amount"
                                           placeholder="amount"
                                           required/>
                                </div>
                                <div class="col-lg-4">
                                    <selectize v-model="cashTransaction.currencyId"
                                               :settings="getSelectizeCurrencyConfig">
                                    </selectize>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-lg-2">
                    <div class="form-group">
                        <button type="button"
                                class="btn btn-primary"
                                name="multicurrency"
                                id="field_multicurrency"
                                @click="toggleMulticurrency"
                                placeholder="multicurrency"
                                uib-btn-checkbox><i class="fa fa-fw fa-check-square-o"
                                                    v-if="cashTransaction.multicurrency"/> <i
                                class="fa fa-fw fa-square-o" v-if="!cashTransaction.multicurrency"/>Multicurrency
                        </button>
                    </div>
                </div>
            </div>

            <div class="row" v-if="cashTransaction.multicurrency">

                <div class="col-lg-6">
                    <div class="form-group">
                        <div class="input-group">
                            <div class="input-group-addon" uib-tooltip="convertedAmount">
                                <i class="glyphicon glyphicon-euro"></i>
                            </div>
                            <div class="row no-gutters">
                                <div class="col-lg-8">
                                    <input type="number"
                                           class="form-control"
                                           name="convertedAmount"
                                           id="field_convertedAmount"
                                           v-model="cashTransaction.convertedAmount"
                                           placeholder="convertedAmount"/>
                                </div>
                                <div class="col-lg-4">
                                    <selectize v-model="cashTransaction.convertedCurrencyId"
                                               :settings="getSelectizeCurrencyConfig">
                                    </selectize>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-lg-6">
                    <div class="form-group">
                        <div class="input-group">
                            <div class="input-group-addon" uib-tooltip="exchangeAccount">
                                <i class="glyphicon glyphicon-modal-window"></i>
                            </div>
                            <selectize v-model="cashTransaction.exchangeAccountId"
                                       :settings="getSelectizeExchangeAccountConfig">
                            </selectize>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-lg-6">
                    <div class="form-group">
                        <div class="input-group">
                            <div class="input-group-addon" uib-tooltip="outAccount">
                                <i class="glyphicon glyphicon-log-out"></i>
                            </div>
                            <selectize v-model="cashTransaction.outAccountId"
                                       :settings="getSelectizeOutAccountConfig">
                            </selectize>
                        </div>
                    </div>
                </div>
                <div class="col-lg-6">
                    <div class="form-group">
                        <div class="input-group">
                            <div class="input-group-addon" uib-tooltip="inAccount">
                                <i class="glyphicon glyphicon-log-in"></i>
                            </div>
                            <selectize v-model="cashTransaction.inAccountId"
                                       :settings="getSelectizeInAccountConfig">
                            </selectize>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    </div>
</template>

<script>
    import Selectize from 'vue2-selectize'

    export default {
        name: 'CashTransactionEditVue',
        components: {
            Selectize
        },
        props: {
            cashTransaction: Object,
            cashAccounts: Array,
            cashCurrencies: Array,
            isImportPanel: Boolean
        },
        data() {
            return {
                isMounted: false,
                cashTransactionTypes: [{value: 'CREDIT', text: 'Generic credit'},
                    {value: 'DEBIT', text: 'Generic debit'},
                    {value: 'INT', text: 'Interest earned'},
                    {value: 'DIV', text: 'Dividend'},
                    {value: 'FEE', text: 'Bank fee'},
                    {value: 'SRVCHG', text: 'Service charge'},
                    {value: 'DEP', text: 'Deposit'},
                    {value: 'ATM', text: 'ATM transaction'},
                    {value: 'POS', text: 'Point of sale'},
                    {value: 'XFER', text: 'Transfer'},
                    {value: 'CHECK', text: 'Check'},
                    {value: 'PAYMENT', text: 'Electronic payment'},
                    {value: 'CASH', text: 'Cash'},
                    {value: 'DIRECTDEP', text: 'Direct deposit'},
                    {value: 'DIRECTDEBIT', text: 'Merchant-initiated debit'},
                    {value: 'REPEATPMT', text: 'Repeating payment'},
                    {value: 'OTHER', text: 'Other'}]
            }
        },
        methods: {
            toggleMulticurrency: function () {
                this.$set(this.cashTransaction, 'multicurrency', !this.cashTransaction.multicurrency);
            },
            toggleToImport: function () {
                this.$set(this.cashTransaction, 'toImport', !this.cashTransaction.toImport);
            }
        },
        computed: {
            getSelectizeTransactionTypeConfig: function () {
                return {
                    maxItems: 1,
                    placeholder: 'type',
                    valueField: 'value',
                    labelField: 'text',
                    options: this.cashTransactionTypes
                };
            },
            getSelectizeCurrencyConfig: function () {
                return {
                    maxItems: 1,
                    placeholder: 'currency',
                    valueField: 'id',
                    labelField: 'code',
                    options: this.cashCurrencies
                };
            },
            getSelectizeOutAccountConfig: function () {
                return {
                    maxItems: 1,
                    valueField: 'id',
                    labelField: 'name',
                    searchField: ['name'],
                    optgroupField: 'type',
                    optgroups: [{value: 'ASSET'},
                        {value: 'LIABILITY'},
                        {value: 'EQUITY'},
                        {value: 'INCOME'},
                        {value: 'EXPENSE'}],
                    optgroupLabelField: 'value',
                    placeholder: 'outAccount',
                    options: this.cashAccounts

                };
            },
            getSelectizeInAccountConfig: function () {
                return {
                    maxItems: 1,
                    valueField: 'id',
                    labelField: 'name',
                    searchField: ['name'],
                    optgroupField: 'type',
                    optgroups: [{value: 'ASSET'},
                        {value: 'LIABILITY'},
                        {value: 'EQUITY'},
                        {value: 'INCOME'},
                        {value: 'EXPENSE'}],
                    optgroupLabelField: 'value',
                    placeholder: 'inAccount',
                    options: this.cashAccounts
                };
            },
            getSelectizeExchangeAccountConfig: function () {
                return {
                    maxItems: 1,
                    valueField: 'id',
                    labelField: 'name',
                    searchField: ['name'],
                    optgroupField: 'type',
                    optgroups: [{value: 'ASSET'},
                        {value: 'LIABILITY'},
                        {value: 'EQUITY'},
                        {value: 'INCOME'},
                        {value: 'EXPENSE'}],
                    optgroupLabelField: 'value',
                    placeholder: 'exchangeAccount',
                    options: this.cashAccounts
                };
            }
        },
        mounted: function(){
            this.isMounted = true;
        }
    }
</script>
