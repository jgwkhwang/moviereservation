
import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router);


import ReservationManager from "./components/listers/ReservationCards"
import ReservationDetail from "./components/listers/ReservationDetail"

import PaymentManager from "./components/listers/PaymentCards"
import PaymentDetail from "./components/listers/PaymentDetail"

import ReviewManager from "./components/listers/ReviewCards"
import ReviewDetail from "./components/listers/ReviewDetail"


import DashboardView from "./components/DashboardView"
import DashboardViewDetail from "./components/DashboardViewDetail"
import ScheduleManager from "./components/listers/ScheduleCards"
import ScheduleDetail from "./components/listers/ScheduleDetail"


export default new Router({
    // mode: 'history',
    base: process.env.BASE_URL,
    routes: [
            {
                path: '/reservations',
                name: 'ReservationManager',
                component: ReservationManager
            },
            {
                path: '/reservations/:id',
                name: 'ReservationDetail',
                component: ReservationDetail
            },

            {
                path: '/payments',
                name: 'PaymentManager',
                component: PaymentManager
            },
            {
                path: '/payments/:id',
                name: 'PaymentDetail',
                component: PaymentDetail
            },

            {
                path: '/reviews',
                name: 'ReviewManager',
                component: ReviewManager
            },
            {
                path: '/reviews/:id',
                name: 'ReviewDetail',
                component: ReviewDetail
            },


            {
                path: '/dashboards',
                name: 'DashboardView',
                component: DashboardView
            },
            {
                path: '/dashboards/:id',
                name: 'DashboardViewDetail',
                component: DashboardViewDetail
            },
            {
                path: '/schedules',
                name: 'ScheduleManager',
                component: ScheduleManager
            },
            {
                path: '/schedules/:id',
                name: 'ScheduleDetail',
                component: ScheduleDetail
            },



    ]
})
