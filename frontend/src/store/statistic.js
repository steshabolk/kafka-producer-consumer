import instance from '@/store/axiosInstance'
import { api, requests } from '@/_config'
import { UNKNOWN_ERROR } from '@/services/errorMessages'
import { counter } from '@/services/requestBody'

export default {
	namespaced: true,
	state: {
		producer: { ...counter },
		consumer: { ...counter },
		err: ''
	},
	actions: {
		async requestProducer({ commit, state }) {
			await instance
				.get(api.producer)
				.then(response => {
					commit('setProducer', response.data)
				})
				.catch(error => {
					commit('setErr', UNKNOWN_ERROR)
				})
		},
		async requestConsumer({ commit, state }) {
			await instance
				.get(api.consumer)
				.then(response => {
					commit('setConsumer', response.data)
				})
				.catch(error => {
					commit('setErr', UNKNOWN_ERROR)
				})
		},
		async requestInitTopics({ commit, state }) {
			await instance
				.get(requests.initTopics)
				.then(response => {
					commit('setInitTopics', response.data)
				})
				.catch(error => {
					commit('setErr', UNKNOWN_ERROR)
				})
		},
		async setErr({ commit }, payload) {
			commit('setErr', payload)
		}
	},
	getters: {
		getErr(state) {
			return state.err
		},
		getProducer(state) {
			return state.producer
		},
		getConsumer(state) {
			return state.consumer
		},
		isTopicsInit(state) {
			return Object.keys(state.producer.counter).length > 0
		}
	},
	mutations: {
		setErr(state, payload) {
			state.err = payload
		},
		setInitTopics(state, payload) {
			state.producer.updatedAt = payload.updatedAt
			state.producer.total = payload.total
			state.producer.counter = { ...payload.counter }
			state.consumer.updatedAt = payload.updatedAt
			state.consumer.total = payload.total
			state.consumer.counter = { ...payload.counter }
		},
		setProducer(state, payload) {
			if (state.producer.updatedAt < payload.updatedAt) {
				state.producer.updatedAt = payload.updatedAt
				state.producer.total = payload.total
				for (const [key, val] of Object.entries(payload.counter)) {
					state.producer.counter[key] = val
				}
			}
		},
		setConsumer(state, payload) {
			if (state.consumer.updatedAt < payload.updatedAt) {
				state.consumer.updatedAt = payload.updatedAt
				state.consumer.total = payload.total
				for (const [key, val] of Object.entries(payload.counter)) {
					state.consumer.counter[key] = val
				}
			}
		}
	}
}
