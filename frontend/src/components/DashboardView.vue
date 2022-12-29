<template>

    <v-data-table
        :headers="headers"
        :items="dashboard"
        :items-per-page="5"
        class="elevation-1"
    ></v-data-table>

</template>

<script>
    const axios = require('axios').default;

    export default {
        name: 'DashboardView',
        props: {
            value: Object,
            editMode: Boolean,
            isNew: Boolean
        },
        data: () => ({
            headers: [
                { text: "movieId", value: "movieId" },
                { text: "title", value: "title" },
                { text: "reviewCnt", value: "reviewCnt" },
                { text: "seatCnt", value: "seatCnt" },
                { text: "reservationCnt", value: "reservationCnt" },
                { text: "reservId", value: "reservId" },
                { text: "reservStatus", value: "reservStatus" },
                { text: "paymentId", value: "paymentId" },
                { text: "paymentStatus", value: "paymentStatus" },
            ],
            dashboard : [],
        }),
          async created() {
            var temp = await axios.get(axios.fixUrl('/dashboards'))

            temp.data._embedded.dashboards.map(obj => obj.id=obj._links.self.href.split("/")[obj._links.self.href.split("/").length - 1])

            this.dashboard = temp.data._embedded.dashboards;
        },
        methods: {
        }
    }
</script>

