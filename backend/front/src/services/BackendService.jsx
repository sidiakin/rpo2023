import axios from 'axios'

const API_URL = 'http://localhost:8081/api/v1'
const AUTH_URL = 'http://localhost:8081/auth'

class BackendService {

    login(login, password) {
        return axios.post(`${AUTH_URL}/login`, {login, password})
    }

}

export default new BackendService()