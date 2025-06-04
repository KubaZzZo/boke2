// API请求工具函数
const api = {
    // 发送GET请求
    get: async (url) => {
        console.log('发送GET请求:', url);
        try {
            const response = await fetch(url, {
                method: 'GET',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                }
            });
            
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            
            const data = await response.json();
            console.log('请求响应:', data);
            return data;
        } catch (error) {
            console.error('请求失败:', error);
            throw error;
        }
    },

    // 发送POST请求
    post: async (url, body) => {
        console.log('发送POST请求:', url, body);
        try {
            const response = await fetch(url, {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(body)
            });
            
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            
            const data = await response.json();
            console.log('请求响应:', data);
            return data;
        } catch (error) {
            console.error('请求失败:', error);
            throw error;
        }
    },

    // 发送PUT请求
    put: async (url, body) => {
        console.log('发送PUT请求:', url, body);
        try {
            const response = await fetch(url, {
                method: 'PUT',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(body)
            });
            
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            
            const data = await response.json();
            console.log('请求响应:', data);
            return data;
        } catch (error) {
            console.error('请求失败:', error);
            throw error;
        }
    },

    // 发送DELETE请求
    delete: async (url) => {
        console.log('发送DELETE请求:', url);
        try {
            const response = await fetch(url, {
                method: 'DELETE',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                }
            });
            
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            
            const data = await response.json();
            console.log('请求响应:', data);
            return data;
        } catch (error) {
            console.error('请求失败:', error);
            throw error;
        }
    }
};
