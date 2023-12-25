export const api = {
	producer: process.env.VUE_APP_PRODUCER + '/producer',
	consumer: process.env.VUE_APP_CONSUMER + '/consumer'
}

export const requests = {
	initTopics: api.producer + '/topics'
}
