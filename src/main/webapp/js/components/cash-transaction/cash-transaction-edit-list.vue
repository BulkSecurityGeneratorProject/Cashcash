<template>
    <div>
        <CashTransactionEditVue v-for="cashTransaction in cashTransactions"
                                :key="cashTransaction.id"
                                :cash-transaction="cashTransaction"
                                :cash-accounts="cashAccounts"
                                :cash-currencies="cashCurrencies"
                                :is-import-panel="true"/>
    </div>
</template>

<script>
    import CashTransactionEditVue from './cash-transaction-edit-panel.vue'

    export default {
        name: 'CashTransactionEditListVue',
        components: {
            CashTransactionEditVue
        },
        props: {
            cashTransactions: Array,
            cashAccounts: Array,
            cashCurrencies: Array,
            isImportPanel: Boolean
        }
    }
</script>
