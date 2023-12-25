import axios from 'axios'

const instance = axios.create({
	headers: {
		Accept: 'application/json',
		'Content-Type': 'application/json; charset=UTF-8'
	},
	timeout: 2 * 60 * 1000
})

instance.interceptors.request.use(
	config => {
		// console.log(config)
		return config
	},
	error => {
		return Promise.reject(error)
	}
)

instance.interceptors.response.use(
	response => {
		// console.log(response)
		return response
	},
	error => {
		// console.log(error.response)
		return Promise.reject(error)
	}
)

export default instance
