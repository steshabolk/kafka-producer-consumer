import { createStore } from 'vuex'

import statistic from '@/store/statistic'

const store = {
	modules: {
		statistic
	}
}

export default createStore(store)
