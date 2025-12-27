/**
 * 将时间戳转换成日期
 * @param timestamp 时间戳
 * @returns {string} xx:xx:xx
 */
export const formatTime = (timestamp: number): string => {
    const date = new Date(timestamp)
    return `${date.getHours()}:${date.getMinutes()}:${date.getSeconds()}`
}

/**
 * 将时间戳转换成日期
 * @param timestamp 时间戳
 * @returns {string} xxxx年xx月xx日
 */
export const formatDate = (timestamp: number): string => {
    const date = new Date(timestamp)
    return `${date.getFullYear()}年${date.getMonth() + 1}月${date.getDate()}日`
}

/**
 * 将时间戳转换成日期
 * @param timestamp 时间戳
 * @returns {string} xxxxn年xx月xx日 xx:xx:xx
 */
export const formatDateTime = (timestamp: number): string => {
    const date = new Date(timestamp)
    return `${date.getFullYear()}年${date.getMonth() + 1}月${date.getDate()}日 ${date.getHours()}:${date.getMinutes()}:${date.getSeconds()}`
}