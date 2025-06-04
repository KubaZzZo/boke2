document.addEventListener('DOMContentLoaded', function() {
    // 导航菜单激活状态
    activateCurrentNavItem();
    
    // 初始化动画效果
    initAnimations();
    
    // 初始化博客列表（模拟数据）
    if (document.querySelector('.blog-grid')) {
        initBlogList();
    }
    
    // 初始化编辑器（如果在发布或编辑页面）
    if (document.getElementById('blog-content-editor')) {
        initEditor();
    }
    
    // 表单验证
    initFormValidation();
});

// 激活当前页面对应的导航菜单项
function activateCurrentNavItem() {
    const currentPath = window.location.pathname;
    const navLinks = document.querySelectorAll('.nav-menu a');
    
    navLinks.forEach(link => {
        if (link.getAttribute('href') === currentPath) {
            link.classList.add('active');
        }
    });
    
    // 如果是首页，默认激活首页链接
    if (currentPath === '/' || currentPath === '/index.html') {
        const homeLink = document.querySelector('.nav-menu a[href="/"]') || 
                         document.querySelector('.nav-menu a[href="/index.html"]');
        if (homeLink) {
            homeLink.classList.add('active');
        }
    }
}

// 初始化动画效果
function initAnimations() {
    const elements = document.querySelectorAll('.blog-card, .sidebar, .blog-detail');
    
    // 添加淡入动画类
    elements.forEach((element, index) => {
        // 错开动画时间，创造级联效果
        setTimeout(() => {
            element.classList.add('fade-in');
        }, index * 100);
    });
}

// 初始化博客列表（使用模拟数据）
function initBlogList() {
    const blogGrid = document.querySelector('.blog-grid');
    // 通过接口获取数据
    fetch('/api/posts/page?page=1&size=6')
        .then(res => res.json())
        .then(res => {
            if (res.code === 0 && res.data && res.data.content) {
                blogGrid.innerHTML = '';
                res.data.content.forEach(post => {
                    const blogCard = createBlogCard({
                        id: post.id,
                        title: post.title,
                        excerpt: post.excerpt || '',
                        image: post.featuredImageUrl || 'img/blog-default.jpg',
                        date: post.publishedAt ? post.publishedAt.substring(0, 10) : '',
                        author: post.authorName || '',
                        category: post.categoryName || ''
                    });
                    blogGrid.appendChild(blogCard);
                });
            } else {
                blogGrid.innerHTML = '<div>暂无数据</div>';
            }
        })
        .catch(() => {
            blogGrid.innerHTML = '<div>加载失败</div>';
        });
    
    // 添加点击事件，跳转到博客详情页
    const blogCards = document.querySelectorAll('.blog-card');
    blogCards.forEach(card => {
        card.addEventListener('click', function() {
            const blogId = this.getAttribute('data-id');
            window.location.href = `/blog-detail.html?id=${blogId}`;
        });
    });
}

// 创建博客卡片元素
function createBlogCard(post) {
    const card = document.createElement('div');
    card.className = 'blog-card';
    card.setAttribute('data-id', post.id);
    
    card.innerHTML = `
        <div class="blog-image">
            <img src="${post.image}" alt="${post.title}">
        </div>
        <div class="blog-content">
            <h3 class="blog-title">${post.title}</h3>
            <p class="blog-excerpt">${post.excerpt}</p>
            <div class="blog-meta">
                <span>${post.date}</span>
                <span>${post.category}</span>
            </div>
        </div>
    `;
    
    return card;
}

// 初始化编辑器（简单实现，实际项目可能需要集成第三方编辑器）
function initEditor() {
    const editor = document.getElementById('blog-content-editor');
    
    // 添加简单的工具栏
    const toolbarItems = ['h1', 'h2', 'h3', 'bold', 'italic', 'link', 'image', 'list'];
    const toolbar = document.createElement('div');
    toolbar.className = 'editor-toolbar';
    
    toolbarItems.forEach(item => {
        const button = document.createElement('button');
        button.type = 'button';
        button.className = `toolbar-btn toolbar-${item}`;
        button.textContent = item.charAt(0).toUpperCase() + item.slice(1);
        button.addEventListener('click', () => handleEditorAction(item, editor));
        toolbar.appendChild(button);
    });
    
    editor.parentNode.insertBefore(toolbar, editor);
}

// 处理编辑器操作
function handleEditorAction(action, editor) {
    // 简单实现，实际项目可能需要更复杂的逻辑
    switch(action) {
        case 'h1':
            insertText(editor, '# ', '');
            break;
        case 'h2':
            insertText(editor, '## ', '');
            break;
        case 'h3':
            insertText(editor, '### ', '');
            break;
        case 'bold':
            insertText(editor, '**', '**');
            break;
        case 'italic':
            insertText(editor, '*', '*');
            break;
        case 'link':
            insertText(editor, '[链接文本](', ')');
            break;
        case 'image':
            insertText(editor, '![图片描述](', ')');
            break;
        case 'list':
            insertText(editor, '- ', '');
            break;
    }
}

// 在编辑器中插入文本
function insertText(editor, before, after) {
    const start = editor.selectionStart;
    const end = editor.selectionEnd;
    const selectedText = editor.value.substring(start, end);
    const newText = before + selectedText + after;
    
    editor.value = editor.value.substring(0, start) + newText + editor.value.substring(end);
    editor.focus();
    editor.selectionStart = start + before.length;
    editor.selectionEnd = start + before.length + selectedText.length;
}

// 表单验证
function initFormValidation() {
    const forms = document.querySelectorAll('form');
    
    forms.forEach(form => {
        form.addEventListener('submit', function(event) {
            let isValid = true;
            
            // 获取所有必填字段
            const requiredFields = form.querySelectorAll('[required]');
            
            // 检查每个必填字段是否有值
            requiredFields.forEach(field => {
                if (!field.value.trim()) {
                    isValid = false;
                    showFieldError(field, '此字段不能为空');
                } else {
                    clearFieldError(field);
                }
            });
            
            // 如果表单无效，阻止提交
            if (!isValid) {
                event.preventDefault();
            }
        });
    });
}

// 显示字段错误信息
function showFieldError(field, message) {
    // 清除可能存在的错误信息
    clearFieldError(field);
    
    // 创建错误消息元素
    const errorElement = document.createElement('div');
    errorElement.className = 'field-error';
    errorElement.textContent = message;
    
    // 添加错误样式到字段
    field.classList.add('field-invalid');
    
    // 将错误消息添加到字段后面
    field.parentNode.appendChild(errorElement);
}

// 清除字段错误信息
function clearFieldError(field) {
    // 移除字段的错误样式
    field.classList.remove('field-invalid');
    
    // 移除可能存在的错误消息
    const errorElement = field.parentNode.querySelector('.field-error');
    if (errorElement) {
        errorElement.remove();
    }
}

// 显示提示消息
function showAlert(message, type = 'info') {
    const alertContainer = document.getElementById('alert-container') || createAlertContainer();
    
    const alert = document.createElement('div');
    alert.className = `alert alert-${type}`;
    alert.textContent = message;
    
    // 添加关闭按钮
    const closeBtn = document.createElement('span');
    closeBtn.className = 'alert-close';
    closeBtn.innerHTML = '&times;';
    closeBtn.addEventListener('click', () => alert.remove());
    alert.appendChild(closeBtn);
    
    alertContainer.appendChild(alert);
    
    // 5秒后自动关闭
    setTimeout(() => {
        if (alert.parentNode) {
            alert.remove();
        }
    }, 5000);
}

// 创建提示消息容器
function createAlertContainer() {
    const container = document.createElement('div');
    container.id = 'alert-container';
    document.body.appendChild(container);
    return container;
}

// 处理分页
function handlePagination() {
    const paginationLinks = document.querySelectorAll('.pagination a');
    
    paginationLinks.forEach(link => {
        link.addEventListener('click', function(event) {
            event.preventDefault();
            
            // 移除所有活动状态
            paginationLinks.forEach(l => l.classList.remove('active'));
            
            // 添加活动状态到当前链接
            this.classList.add('active');
            
            // 获取页码
            const page = this.getAttribute('data-page');
            
            // 这里可以添加加载对应页面数据的逻辑
            // loadPageData(page);
            
            // 模拟加载效果
            const blogGrid = document.querySelector('.blog-grid');
            blogGrid.innerHTML = '<div class="loader-container"><div class="loader"></div></div>';
            
            setTimeout(() => {
                // 重新初始化博客列表（实际项目中应该是加载新数据）
                initBlogList();
            }, 500);
        });
    });
}

// 模拟API调用（实际项目中应该是真实的API调用）
function fetchBlogPosts(page = 1, limit = 6) {
    // 这里应该是实际的API调用
    // return fetch(`/api/blogs?page=${page}&limit=${limit}`).then(res => res.json());
    
    // 模拟API响应
    return new Promise(resolve => {
        setTimeout(() => {
            resolve({
                posts: [], // 这里应该是实际的博客数据
                total: 0,  // 总博客数
                page: page,
                totalPages: 0
            });
        }, 500);
    });
}

// 处理博客搜索
function handleSearch() {
    const searchForm = document.getElementById('search-form');
    
    if (searchForm) {
        searchForm.addEventListener('submit', function(event) {
            event.preventDefault();
            
            const searchInput = this.querySelector('input[type="search"]');
            const searchTerm = searchInput.value.trim();
            
            if (searchTerm) {
                // 重定向到搜索结果页面
                window.location.href = `/search-results.html?q=${encodeURIComponent(searchTerm)}`;
            }
        });
    }
}

// 初始化搜索结果页面
function initSearchResults() {
    const urlParams = new URLSearchParams(window.location.search);
    const searchTerm = urlParams.get('q');
    
    if (searchTerm) {
        const searchTermDisplay = document.getElementById('search-term');
        if (searchTermDisplay) {
            searchTermDisplay.textContent = searchTerm;
        }
        
        // 这里应该是实际的搜索API调用
        // 模拟搜索结果
        const blogGrid = document.querySelector('.blog-grid');
        if (blogGrid) {
            blogGrid.innerHTML = '<div class="loader-container"><div class="loader"></div></div>';
            
            setTimeout(() => {
                // 重新初始化博客列表（实际项目中应该是加载搜索结果）
                initBlogList();
            }, 500);
        }
    }
}