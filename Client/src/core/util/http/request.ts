import axios, {
    type AxiosInstance,
    type AxiosRequestConfig,
    type AxiosResponse,
    type InternalAxiosRequestConfig,
    type AxiosError,
    type AxiosBasicCredentials,
} from 'axios'

// 定义通用响应结构
// 所有方法永远返回此结构
// 例如get<T>('/api/users/current') 中的T 表明data的类型
export interface HttpResponse<T = any> {
    code: number
    data: T
    message: string
}
// 定义请求配置
export interface RequestConfig extends AxiosRequestConfig {
    withToken?: boolean,
}

class HttpRequest {
    private instance: AxiosInstance
    constructor() {
        // 创建 axios 实例
        this.instance = axios.create({
            baseURL: import.meta.env.VITE_APP_BASE_API || '',
            timeout: 10000,
            headers: {
                'Content-Type': 'application/json;charset=UTF-8'
            },
        })

        // 设置拦截器
        this.setupInterceptors();
    }

    // 设置拦截器
    private setupInterceptors(): void {
        // 请求拦截器
        this.instance.interceptors.request.use(
            (config: InternalAxiosRequestConfig) => {
                // 处理自定义配置
                const customConfig = config as RequestConfig

                // 添加认证 token
                if (customConfig.withToken !== false) {
                    const token = localStorage.getItem('token')
                    // token 不存在或者为空时
                    if (!token || token === '') {
                        localStorage.removeItem('token')
                        return Promise.reject(new Error('请先登录!'))
                    }
                    config.headers.Authorization = `Bearer ${localStorage.getItem('token')}`
                }


                // 可在这里添加其他业务逻辑，比如显示 loading 等
                return config
            },
            (error: AxiosError) => {
                console.error('请求错误:', error)
                return Promise.reject(error)
            }
        )

        // 响应拦截器
        this.instance.interceptors.response.use(
            (response: AxiosResponse<HttpResponse>) => {
                const headers = response.headers
                // 处理响应头
                // 处理Authorization
                if (headers.authorization) {
                    localStorage.removeItem('token')

                    // 以`Bearer {jwt}` 保存
                    const token = headers.authorization.startsWith('Bearer ') ?
                        headers.authorization :
                        `Bearer ${headers.authorization}`
                    localStorage.setItem('token', token)
                }
                const { data, message, code } = response.data
                // 根据返回码进行不同处理
                if (code === 200) {
                    // 直接返回响应对象，让axios正常处理
                    return response;
                } else {
                    console.error('业务错误:', message)
                    return Promise.reject(new Error(message || 'Error'))
                }
            },
            (error: AxiosError) => {
                console.error('响应错误:', error)
                // 可以在这里统一处理 HTTP 状态码
                return Promise.reject(error)
            }
        )
    }

    // 设置默认请求头
    public setHeader(key: string, value: string): void {
        this.instance.defaults.headers.common[key] = value
    }

    // 设置 Content-Type
    public setContentType(type: string): void {
        this.instance.defaults.headers.post['Content-Type'] = type
        this.instance.defaults.headers.put['Content-Type'] = type
    }

    // GET 请求
    public async get<T = any>(url: string, config?: RequestConfig): Promise<HttpResponse<T>> {
        const response = await this.instance.get(url, config)
        return response.data
    }

    // POST 请求
    public async post<T = any>(url: string, data?: any, config?: RequestConfig): Promise<HttpResponse<T>> {
        const response = await this.instance.post(url, data, config)
        return response.data
    }

    // PUT 请求
    public async put<T = any>(url: string, data?: any, config?: RequestConfig): Promise<HttpResponse<T>> {
        const response = await this.instance.put(url, data, config)
        return response.data
    }

    // DELETE 请求
    public async delete<T = any>(url: string, config?: RequestConfig): Promise<HttpResponse<T>> {
        const response = await this.instance.delete(url, config)
        return response.data
    }

    // PATCH 请求
    public async patch<T = any>(url: string, data?: any, config?: RequestConfig): Promise<HttpResponse<T>> {
        const response = await this.instance.patch(url, data, config)
        return response.data
    }
}

// 默认导出实例
export default new HttpRequest()

// 模块化导出各个方法
const http = new HttpRequest()
export const get = http.get.bind(http)
export const post = http.post.bind(http)
export const put = http.put.bind(http)
export const del = http.delete.bind(http)
export const patch = http.patch.bind(http)
export const setHeader = http.setHeader.bind(http)
export const setContentType = http.setContentType.bind(http)