<template>
	<div class="wrapper">
		<div class="wrapper-content">
			<div class="container" style="margin: 2rem auto">
				<div class="col">
					<div class="text-purple-pink upper">producer</div>
					<div class="text-purple-blue upper">consumer</div>
				</div>
				<div v-if="isTopicsInit">
					<div class="col">
						<div class="text-purple-pink">{{ producer.total }}</div>
						<div class="text-purple-blue">{{ consumer.total }}</div>
					</div>
					<div v-for="(keys, topic) of producer.counter" :key="topic">
						<p class="text-yellow-red">topic : {{ topic }}</p>
						<div v-for="(count, key) of keys" :key="key">
							<p class="text-pink-red">key : {{ key }}</p>
							<div class="col">
								<p class="text-purple-pink">{{ count }}</p>
								<p class="text-purple-blue">{{ consumer.counter[topic][key] }}</p>
							</div>
						</div>
					</div>
				</div>
				<div v-else>
					<div class="text-pink-red">topics loading...</div>
					<div v-if="errMsg" class="text-yellow-red">{{ errMsg }}</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
import { mapActions, mapGetters } from 'vuex'

export default {
	name: 'App',
	components: {},
	data() {
		return {
			poolingInterval: 2500
		}
	},
	methods: {
		poolingCounters() {
			setInterval(() => {
				this.requestProducer()
				this.requestConsumer()
			}, this.poolingInterval)
		},
		...mapActions('statistic', ['requestInitTopics', 'requestProducer', 'requestConsumer', 'setErr'])
	},
	computed: {
		...mapGetters('statistic', { consumer: 'getConsumer', producer: 'getProducer', errMsg: 'getErr', isTopicsInit: 'isTopicsInit' })
	},
	watch: {
		isTopicsInit: {
			handler(curr, old) {
				if (curr === true) {
					this.poolingCounters()
				}
			}
		}
	},
	beforeMount() {
		this.setErr('')
		this.requestInitTopics()
	}
}
</script>
