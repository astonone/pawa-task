import { Module } from 'vuex'
import { userApi } from '@/plugins/axios'

export interface UserInfo {
    id: number
    username: string
    fullName: string
}

export interface AuthState {
    token: string | null
    userInfo: UserInfo | null
}

const authModule: Module<AuthState, any> = {
    namespaced: true,
    state: () => ({
        token: localStorage.getItem('token'),
        userInfo: null
    }),
    mutations: {
        setToken(state, token: string) {
            state.token = token
            localStorage.setItem('token', token)
        },
        clearToken(state) {
            state.token = null
            localStorage.removeItem('token')
        },
        setUserInfo(state, user: UserInfo) {
            state.userInfo = user
        }
    },
    actions: {
        async login({ commit, dispatch }, payload: { username: string; password: string }) {
            const response = await userApi.post('/auth/login', payload)
            commit('setToken', response.data.token)
            await dispatch('fetchUserInfo')
        },
        logout({ commit }) {
            commit('clearToken')
        },
        async fetchUserInfo({ commit }) {
            const response = await userApi.get('/users/me')
            commit('setUserInfo', response.data)
        }
    },
    getters: {
        isAuthenticated(state): boolean {
            return !!state.token
        },
        token(state): string | null {
            return state.token
        }
    }
}

export default authModule