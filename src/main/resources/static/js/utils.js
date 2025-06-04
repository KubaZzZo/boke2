// 日期格式化
function formatDate(dateStr) {
    if (!dateStr) return '';
    const date = new Date(dateStr);
    const pad = (num) => String(num).padStart(2, '0');
    
    const year = date.getFullYear();
    const month = pad(date.getMonth() + 1);
    const day = pad(date.getDate());
    const hours = pad(date.getHours());
    const minutes = pad(date.getMinutes());
    const seconds = pad(date.getSeconds());
    
    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}

// HTML转义
function escapeHtml(str) {
    if (!str) return '';
    const div = document.createElement('div');
    div.innerText = str;
    return div.innerHTML;
}

// API请求工具
const api = {
    // 基础URL
    baseUrl: '',
    
    // 通用请求方法
    async request(url, options = {}) {
        const defaultOptions = {
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            credentials: 'include'  // 添加这行以支持跨域请求携带cookie
        };
        
        // 合并请求头
        const headers = { ...defaultOptions.headers, ...options.headers };
        const finalOptions = { ...defaultOptions, ...options, headers };
        
        try {
            const fullUrl = url.startsWith('http') ? url : this.baseUrl + url;
            console.log(`[API Request] ${options.method || 'GET'} ${fullUrl}`, options.body || '');
            
            const response = await fetch(fullUrl, finalOptions);
            console.log(`[API Response Status] ${response.status}`);
            
            if (!response.ok) {
                const errorData = await response.json().catch(() => ({}));
                throw new Error(errorData.message || `HTTP error! status: ${response.status}`);
            }
            
            const data = await response.json();
            console.log('[API Response Data]', data);
            
            // 检查API响应格式
            if (typeof data === 'object' && 'code' in data) {
                if (data.code !== 0) {
                    throw new Error(data.message || '请求失败');
                }
                return data;
            }
            
            return { code: 0, data, message: 'success' };
        } catch (error) {
            console.error('[API Error]', error);
            throw error;
        }
    },
    
    // GET请求
    async get(url, params = {}) {
        const queryString = Object.entries(params)
            .filter(([_, value]) => value != null)
            .map(([key, value]) => `${encodeURIComponent(key)}=${encodeURIComponent(value)}`)
            .join('&');
        
        const fullUrl = queryString ? `${url}${url.includes('?') ? '&' : '?'}${queryString}` : url;
        return this.request(fullUrl, { method: 'GET' });
    },
    
    // POST请求
    async post(url, data) {
        return this.request(url, {
            method: 'POST',
            body: JSON.stringify(data)
        });
    },
    
    // PUT请求
    async put(url, data) {
        return this.request(url, {
            method: 'PUT',
            body: JSON.stringify(data)
        });
    },
    
    // DELETE请求
    async delete(url) {
        return this.request(url, { method: 'DELETE' });
    }
};
