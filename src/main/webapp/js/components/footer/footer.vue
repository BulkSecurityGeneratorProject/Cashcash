<template>
    <div class="footer">
        <div class="col-lg-12">
            <p class="text-center">Copyright © Samuel GAGNEPAIN {{currentYear}}</p>
            <p class="text-center"><span>v{{version}}</span></p>
        </div>
    </div>
</template>

<script>
    export default {
        name: 'FooterVue',
        props: {
            version: String
        },
        data() {
            return {
                currentYear: new Date().getFullYear()
            }
        }
    }
</script>
