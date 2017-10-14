<template>
    <div class="panel panel-default">
        <div class="panel-body">

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
                                       :settings="selectizeTransactionTypeConfig">
                                <option v-for="cashTransactionType in cashTransactionTypes"
                                        :value="cashTransactionType.type">{{cashTransactionType.text}}
                                </option>
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
                                               :settings="selectizeCurrencyConfig">
                                        <option v-for="cashCurrency in cashCurrencies"
                                                :value="cashCurrency.id">{{cashCurrency.code}}
                                        </option>
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
                                               :settings="selectizeCurrencyConfig">
                                        <option v-for="cashCurrency in cashCurrencies"
                                                :value="cashCurrency.id">{{cashCurrency.code}}
                                        </option>
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
                                       :settings="selectizeExchangeAccountConfig">
                                <option v-for="cashAccount in cashAccounts"
                                        :value="cashAccount.id">{{cashAccount.name}}
                                </option>
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
                                       :settings="selectizeOutAccountConfig">
                                <option v-for="cashAccount in cashAccounts"
                                        :value="cashAccount.id">{{cashAccount.name}}
                                </option>
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
                                       :settings="selectizeInAccountConfig">
                                <option v-for="cashAccount in cashAccounts"
                                        :value="cashAccount.id">{{cashAccount.name}}
                                </option>
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
                selectizeTransactionTypeConfig: {
                    maxItems: 1,
                    placeholder: 'type'
                },
                cashTransactionTypes: [{type: 'CREDIT', text: 'Generic credit'},
                    {type: 'DEBIT', text: 'Generic debit'},
                    {type: 'INT', text: 'Interest earned'},
                    {type: 'DIV', text: 'Dividend'},
                    {type: 'FEE', text: 'Bank fee'},
                    {type: 'SRVCHG', text: 'Service charge'},
                    {type: 'DEP', text: 'Deposit'},
                    {type: 'ATM', text: 'ATM transaction'},
                    {type: 'POS', text: 'Point of sale'},
                    {type: 'XFER', text: 'Transfer'},
                    {type: 'CHECK', text: 'Check'},
                    {type: 'PAYMENT', text: 'Electronic payment'},
                    {type: 'CASH', text: 'Cash'},
                    {type: 'DIRECTDEP', text: 'Direct deposit'},
                    {type: 'DIRECTDEBIT', text: 'Merchant-initiated debit'},
                    {type: 'REPEATPMT', text: 'Repeating payment'},
                    {type: 'OTHER', text: 'Other'}],
                selectizeCurrencyConfig: {
                    maxItems: 1,
                    placeholder: 'currency'
                },
                selectizeOutAccountConfig: {
                    maxItems: 1,
                    optgroupField: 'type',
                    optgroups: [{value: 'ASSET'},
                        {value: 'LIABILITY'},
                        {value: 'EQUITY'},
                        {value: 'INCOME'},
                        {value: 'EXPENSE'}],
                    optgroupLabelField: 'value',
                    placeholder: 'outAccount'
                },
                selectizeInAccountConfig: {
                    maxItems: 1,
                    optgroupField: 'type',
                    optgroups: [{value: 'ASSET'},
                        {value: 'LIABILITY'},
                        {value: 'EQUITY'},
                        {value: 'INCOME'},
                        {value: 'EXPENSE'}],
                    optgroupLabelField: 'value',
                    placeholder: 'inAccount'
                },
                selectizeExchangeAccountConfig: {
                    maxItems: 1,
                    optgroupField: 'type',
                    optgroups: [{value: 'ASSET'},
                        {value: 'LIABILITY'},
                        {value: 'EQUITY'},
                        {value: 'INCOME'},
                        {value: 'EXPENSE'}],
                    optgroupLabelField: 'value',
                    placeholder: 'exchangeAccount'
                }
            }
        },
        methods: {
            toggleMulticurrency: function () {
                this.$set(this.cashTransaction, 'multicurrency', !this.cashTransaction.multicurrency);
            },
            toggleToImport: function () {
                this.$set(this.cashTransaction, 'toImport', !this.cashTransaction.toImport);
            }
        }
    }
</script>
