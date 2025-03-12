-- ----------------------------
-- 1、用户表
-- ----------------------------
CREATE TABLE sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(30) NOT NULL COMMENT '用户账号',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    nickname VARCHAR(30) NOT NULL COMMENT '用户昵称',
    avatar VARCHAR(255) COMMENT '头像地址',
    email VARCHAR(50) COMMENT '用户邮箱',
    phone VARCHAR(11) COMMENT '手机号码',
    bio VARCHAR(255) COMMENT '个人简介',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '帐号状态（0-停用，1-正常）',
    dept_id BIGINT COMMENT '部门ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除（0-未删除，1-已删除）',
    UNIQUE KEY uk_username (username),
    INDEX idx_status (status),
    INDEX idx_dept_id (dept_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';

-- ----------------------------
-- 2、角色表
-- ----------------------------
CREATE TABLE sys_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '角色ID',
    name VARCHAR(30) NOT NULL COMMENT '角色名称',
    code VARCHAR(100) NOT NULL COMMENT '角色权限字符串',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '角色状态（0-停用，1-正常）',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除（0-未删除，1-已删除）',
    UNIQUE KEY uk_code (code),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色信息表';

-- ----------------------------
-- 3、菜单权限表
-- ----------------------------
CREATE TABLE sys_menu (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '菜单ID',
    name VARCHAR(50) NOT NULL COMMENT '菜单名称',
    parent_id BIGINT DEFAULT 0 COMMENT '父菜单ID',
    path VARCHAR(200) COMMENT '路由地址',
    component VARCHAR(255) COMMENT '组件路径',
    redirect VARCHAR(255) COMMENT '重定向地址',
    icon VARCHAR(100) COMMENT '菜单图标',
    title VARCHAR(50) COMMENT '菜单标题',
    hidden TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否隐藏（0-显示，1-隐藏）',
    keep_alive TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否缓存（0-不缓存，1-缓存）',
    type VARCHAR(10) NOT NULL COMMENT '菜单类型（M-目录，C-菜单，F-按钮）',
    permission VARCHAR(100) COMMENT '权限标识',
    order_num INT DEFAULT 0 COMMENT '显示顺序',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '菜单状态（0-停用，1-正常）',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除（0-未删除，1-已删除）',
    INDEX idx_parent_id (parent_id),
    INDEX idx_order_num (order_num)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单权限表';

-- ----------------------------
-- 4、部门表
-- ----------------------------
CREATE TABLE sys_dept (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '部门ID',
    name VARCHAR(30) NOT NULL COMMENT '部门名称',
    code VARCHAR(50) NOT NULL COMMENT '部门编码',
    parent_id BIGINT DEFAULT 0 COMMENT '父部门ID',
    path VARCHAR(255) COMMENT '祖级列表',
    leader VARCHAR(20) COMMENT '负责人',
    phone VARCHAR(11) COMMENT '联系电话',
    email VARCHAR(50) COMMENT '邮箱',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '部门状态（0-停用，1-正常）',
    order_num INT DEFAULT 0 COMMENT '显示顺序',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除（0-未删除，1-已删除）',
    UNIQUE KEY uk_code (code),
    INDEX idx_parent_id (parent_id),
    INDEX idx_order_num (order_num)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- ----------------------------
-- 5、用户和角色关联表
-- ----------------------------
CREATE TABLE sys_user_role (
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    PRIMARY KEY (user_id, role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户和角色关联表';

-- ----------------------------
-- 6、角色和菜单关联表
-- ----------------------------
CREATE TABLE sys_role_menu (
    role_id BIGINT NOT NULL COMMENT '角色ID',
    menu_id BIGINT NOT NULL COMMENT '菜单ID',
    PRIMARY KEY (role_id, menu_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色和菜单关联表';

-- ----------------------------
-- 7、通知公告表
-- ----------------------------
CREATE TABLE sys_notice (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '公告ID',
    title VARCHAR(100) NOT NULL COMMENT '公告标题',
    content TEXT COMMENT '公告内容',
    type CHAR(1) NOT NULL COMMENT '公告类型（1-通知，2-公告，3-警告）',
    status CHAR(1) NOT NULL DEFAULT '0' COMMENT '公告状态（0-草稿，1-已发布，2-已撤回）',
    publish_time DATETIME COMMENT '发布时间',
    expire_time DATETIME COMMENT '过期时间',
    is_top TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否置顶（0-否，1-是）',
    read_count INT NOT NULL DEFAULT 0 COMMENT '阅读次数',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除（0-未删除，1-已删除）',
    INDEX idx_type (type),
    INDEX idx_status (status),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知公告表';

-- ----------------------------
-- 8、消息通知表
-- ----------------------------
CREATE TABLE sys_notification (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '通知ID',
    title VARCHAR(100) NOT NULL COMMENT '通知标题',
    content TEXT COMMENT '通知内容',
    type VARCHAR(20) NOT NULL COMMENT '通知类型（info-信息，success-成功，warning-警告，error-错误）',
    is_read TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否已读（0-未读，1-已读）',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    link VARCHAR(255) COMMENT '关联链接',
    sender VARCHAR(64) COMMENT '发送者',
    sender_avatar VARCHAR(255) COMMENT '发送者头像',
    priority VARCHAR(10) COMMENT '优先级（low-低，medium-中，high-高）',
    category VARCHAR(50) COMMENT '通知分类',
    expire_time DATETIME COMMENT '过期时间',
    user_id BIGINT NOT NULL COMMENT '接收用户ID',
    deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除（0-未删除，1-已删除）',
    INDEX idx_user_id (user_id),
    INDEX idx_is_read (is_read),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息通知表';

-- ----------------------------
-- 9、字典类型表
-- ----------------------------
CREATE TABLE sys_dict_type (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '字典类型ID',
    name VARCHAR(100) NOT NULL COMMENT '字典名称',
    type VARCHAR(100) NOT NULL COMMENT '字典类型',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态（0-停用，1-正常）',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除（0-未删除，1-已删除）',
    UNIQUE KEY uk_type (type),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典类型表';

-- ----------------------------
-- 10、字典数据表
-- ----------------------------
CREATE TABLE sys_dict_data (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '字典数据ID',
    dict_type_id BIGINT NOT NULL COMMENT '字典类型ID',
    dict_type VARCHAR(100) NOT NULL COMMENT '字典类型',
    label VARCHAR(100) NOT NULL COMMENT '字典标签',
    value VARCHAR(100) NOT NULL COMMENT '字典值',
    css_class VARCHAR(100) COMMENT '样式属性',
    list_class VARCHAR(100) COMMENT '表格回显样式',
    is_default TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否默认（0-否，1-是）',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态（0-停用，1-正常）',
    order_num INT NOT NULL DEFAULT 0 COMMENT '排序号',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除（0-未删除，1-已删除）',
    INDEX idx_dict_type (dict_type),
    INDEX idx_status (status),
    INDEX idx_order_num (order_num)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典数据表'; 