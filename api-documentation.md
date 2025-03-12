# Vue Naive Admin API 接口文档

本文档详细描述了Vue Naive Admin项目中所有API接口的使用方法、参数和返回值。

## 目录

- [用户管理](#用户管理)
- [认证管理](#认证管理)
- [角色管理](#角色管理)
- [权限管理](#权限管理)
- [部门管理](#部门管理)
- [菜单管理](#菜单管理)
- [公告管理](#公告管理)
- [消息通知](#消息通知)
- [字典管理](#字典管理)

## 用户管理

用户管理相关的API接口，包括用户的增删改查、登录、获取用户信息等功能。

### 数据结构

#### User

```typescript
interface User {
  id: number;
  username: string;
  nickname: string;
  avatar: string;
  role: string;
  email?: string;
  phone?: string;
  bio?: string;
  status?: boolean;
  createTime?: string;
}
```

#### UserListResult

```typescript
interface UserListResult {
  list: User[];
  total: number;
}
```

#### UserListParams

```typescript
interface UserListParams {
  page?: number;
  pageSize?: number;
  keyword?: string;
}
```

#### LoginParams

```typescript
interface LoginParams {
  username: string;
  password: string;
}
```

#### UserInfo

```typescript
interface UserInfo {
  id: number;
  username: string;
  nickname: string;
  avatar: string;
  roles: string[];
}
```

### 接口列表

#### 获取用户列表

```typescript
function getUserList(params: UserListParams): Promise<UserListResult>
```

- 请求方式：GET
- 请求路径：/user/list
- 请求参数：
  - page：页码
  - pageSize：每页数量
  - keyword：搜索关键词
- 返回数据：用户列表和总数

#### 创建用户

```typescript
function createUser(data: Omit<User, 'id'>): Promise<any>
```

- 请求方式：POST
- 请求路径：/user/create
- 请求参数：用户信息（不包含id）
- 返回数据：创建结果

#### 更新用户

```typescript
function updateUser(id: number, data: Partial<User>): Promise<any>
```

- 请求方式：PUT
- 请求路径：/user/{id}
- 请求参数：
  - id：用户ID
  - data：需要更新的用户信息
- 返回数据：更新结果

#### 删除用户

```typescript
function deleteUser(id: number): Promise<any>
```

- 请求方式：DELETE
- 请求路径：/user/{id}
- 请求参数：用户ID
- 返回数据：删除结果

#### 用户登录

```typescript
function login(data: LoginParams): Promise<any>
```

- 请求方式：POST
- 请求路径：/auth/login
- 请求参数：
  - username：用户名
  - password：密码
- 返回数据：登录结果，包含token和用户信息

#### 获取用户信息

```typescript
function getUserInfo(): Promise<UserInfo>
```

- 请求方式：GET
- 请求路径：/user/info
- 请求参数：无
- 返回数据：当前登录用户的信息

#### 用户登出

```typescript
function logout(): Promise<any>
```

- 请求方式：POST
- 请求路径：/auth/logout
- 请求参数：无
- 返回数据：登出结果

#### 更新用户信息

```typescript
function updateUserInfo(data: Partial<User>): Promise<any>
```

- 请求方式：PUT
- 请求路径：/user/update
- 请求参数：需要更新的用户信息
- 返回数据：更新结果

#### 修改密码

```typescript
function updatePassword(data: { oldPassword: string; newPassword: string }): Promise<any>
```

- 请求方式：PUT
- 请求路径：/user/password
- 请求参数：
  - oldPassword：旧密码
  - newPassword：新密码
- 返回数据：修改结果

#### 上传头像

```typescript
function uploadAvatar(file: File): Promise<any>
```

- 请求方式：POST
- 请求路径：/user/avatar
- 请求参数：文件对象
- 请求头：Content-Type: multipart/form-data
- 返回数据：上传结果

## 认证管理

认证管理相关的API接口，包括登录、登出等功能。

### 数据结构

#### LoginParams

```typescript
interface LoginParams {
  username: string;
  password: string;
}
```

#### LoginResult

```typescript
interface LoginResult {
  token: string;
  userInfo: {
    id: number;
    username: string;
    avatar: string;
    role: string;
  }
}
```

### 接口列表

#### 用户登录

```typescript
function login(data: LoginParams): Promise<LoginResult>
```

- 请求方式：POST
- 请求路径：/auth/login
- 请求参数：
  - username：用户名
  - password：密码
- 返回数据：登录结果，包含token和用户信息

#### 用户登出

```typescript
function logout(): Promise<any>
```

- 请求方式：POST
- 请求路径：/auth/logout
- 请求参数：无
- 返回数据：登出结果

## 角色管理

角色管理相关的API接口，包括角色的增删改查等功能。

### 数据结构

#### Role

```typescript
interface Role {
  id: number;
  name: string;
  code: string;
  status: boolean;
  remark: string;
  createTime: string;
}
```

#### RoleListResult

```typescript
interface RoleListResult {
  list: Role[];
  total: number;
}
```

#### RoleListParams

```typescript
interface RoleListParams {
  page?: number;
  pageSize?: number;
  keyword?: string;
}
```

#### SimpleRole

```typescript
interface SimpleRole {
  id: number;
  name: string;
  code: string;
}
```

### 接口列表

#### 获取角色列表

```typescript
function getRoleList(params: RoleListParams): Promise<RoleListResult>
```

- 请求方式：GET
- 请求路径：/role/list
- 请求参数：
  - page：页码
  - pageSize：每页数量
  - keyword：搜索关键词
- 返回数据：角色列表和总数

#### 获取所有角色

```typescript
function getAllRoles(): Promise<SimpleRole[]>
```

- 请求方式：GET
- 请求路径：/role/all
- 请求参数：无
- 返回数据：所有角色的简要信息

#### 创建角色

```typescript
function createRole(data: Omit<Role, 'id'>): Promise<any>
```

- 请求方式：POST
- 请求路径：/role/create
- 请求参数：角色信息（不包含id）
- 返回数据：创建结果

#### 更新角色

```typescript
function updateRole(id: number, data: Partial<Role>): Promise<any>
```

- 请求方式：PUT
- 请求路径：/role/{id}
- 请求参数：
  - id：角色ID
  - data：需要更新的角色信息
- 返回数据：更新结果

#### 删除角色

```typescript
function deleteRole(id: number): Promise<any>
```

- 请求方式：DELETE
- 请求路径：/role/{id}
- 请求参数：角色ID
- 返回数据：删除结果

## 权限管理

权限管理相关的API接口，包括权限的增删改查等功能。

### 数据结构

#### Permission

```typescript
interface Permission {
  id: number;
  name: string;
  code: string;
  type: string;
  status: boolean;
  remark: string;
  createTime: string;
}
```

#### PermissionListResult

```typescript
interface PermissionListResult {
  list: Permission[];
  total: number;
}
```

#### PermissionListParams

```typescript
interface PermissionListParams {
  page?: number;
  pageSize?: number;
  keyword?: string;
}
```

#### MenuPermission

```typescript
interface MenuPermission {
  id: number;
  name: string;
  code: string;
  type: string;
  children?: MenuPermission[];
}
```

### 接口列表

#### 获取权限列表

```typescript
function getPermissionList(params: PermissionListParams): Promise<PermissionListResult>
```

- 请求方式：GET
- 请求路径：/permission/list
- 请求参数：
  - page：页码
  - pageSize：每页数量
  - keyword：搜索关键词
- 返回数据：权限列表和总数

#### 获取所有权限

```typescript
function getAllPermissions(): Promise<MenuPermission[]>
```

- 请求方式：GET
- 请求路径：/permission/all
- 请求参数：无
- 返回数据：所有权限的树形结构

#### 创建权限

```typescript
function createPermission(data: Omit<Permission, 'id'>): Promise<any>
```

- 请求方式：POST
- 请求路径：/permission/create
- 请求参数：权限信息（不包含id）
- 返回数据：创建结果

#### 更新权限

```typescript
function updatePermission(id: number, data: Partial<Permission>): Promise<any>
```

- 请求方式：PUT
- 请求路径：/permission/{id}
- 请求参数：
  - id：权限ID
  - data：需要更新的权限信息
- 返回数据：更新结果

#### 删除权限

```typescript
function deletePermission(id: number): Promise<any>
```

- 请求方式：DELETE
- 请求路径：/permission/{id}
- 请求参数：权限ID
- 返回数据：删除结果

## 部门管理

部门管理相关的API接口，包括部门的增删改查等功能。

### 数据结构

#### Department

```typescript
interface Department {
  id: number;
  name: string;         // 部门名称
  code: string;         // 部门编码
  parentId: number;     // 上级部门ID
  path: string;         // 部门层级路径
  leader: string;       // 部门负责人
  phone: string;        // 联系电话
  email: string;        // 邮箱
  status: boolean;      // 状态（启用/禁用）
  orderNum: number;     // 排序号
  createTime: string;   // 创建时间
  updateTime: string;   // 更新时间
  children?: Department[]; // 子部门
}
```

#### DepartmentQuery

```typescript
interface DepartmentQuery {
  keyword?: string;
  status?: boolean;
}
```

### 接口列表

#### 获取部门树形列表

```typescript
function getDepartmentTree(params?: DepartmentQuery): Promise<Department[]>
```

- 请求方式：GET
- 请求路径：/system/department/tree
- 请求参数：
  - keyword：搜索关键词
  - status：状态
- 返回数据：部门树形结构

#### 获取部门列表

```typescript
function getDepartmentList(params?: DepartmentQuery): Promise<Department[]>
```

- 请求方式：GET
- 请求路径：/system/department/list
- 请求参数：
  - keyword：搜索关键词
  - status：状态
- 返回数据：部门列表

#### 获取部门详情

```typescript
function getDepartmentDetail(id: number): Promise<Department>
```

- 请求方式：GET
- 请求路径：/system/department/{id}
- 请求参数：部门ID
- 返回数据：部门详情

#### 创建部门

```typescript
function createDepartment(data: Partial<Department>): Promise<any>
```

- 请求方式：POST
- 请求路径：/system/department
- 请求参数：部门信息
- 返回数据：创建结果

#### 更新部门

```typescript
function updateDepartment(id: number, data: Partial<Department>): Promise<any>
```

- 请求方式：PUT
- 请求路径：/system/department/{id}
- 请求参数：
  - id：部门ID
  - data：需要更新的部门信息
- 返回数据：更新结果

#### 删除部门

```typescript
function deleteDepartment(id: number): Promise<any>
```

- 请求方式：DELETE
- 请求路径：/system/department/{id}
- 请求参数：部门ID
- 返回数据：删除结果

## 菜单管理

菜单管理相关的API接口，包括菜单的增删改查等功能。

### 数据结构

#### Menu

```typescript
interface Menu {
  id: number;
  name: string;         // 菜单名称
  parentId: number;     // 上级菜单ID
  path: string;         // 路由路径
  component: string;    // 组件路径
  redirect: string;     // 重定向地址
  icon: string;         // 菜单图标
  title: string;        // 菜单标题
  hidden: boolean;      // 是否隐藏
  keepAlive: boolean;   // 是否缓存
  type: string;         // 菜单类型（目录、菜单、按钮）
  permission: string;   // 权限标识
  orderNum: number;     // 排序号
  status: boolean;      // 状态（启用/禁用）
  createTime: string;   // 创建时间
  updateTime: string;   // 更新时间
  children?: Menu[];    // 子菜单
}
```

#### MenuQuery

```typescript
interface MenuQuery {
  keyword?: string;
  status?: boolean;
  type?: string;
}
```

### 接口列表

#### 获取菜单树形列表

```typescript
function getMenuTree(params?: MenuQuery): Promise<Menu[]>
```

- 请求方式：GET
- 请求路径：/system/menu/tree
- 请求参数：
  - keyword：搜索关键词
  - status：状态
  - type：菜单类型
- 返回数据：菜单树形结构

#### 获取菜单列表

```typescript
function getMenuList(params?: MenuQuery): Promise<Menu[]>
```

- 请求方式：GET
- 请求路径：/system/menu/list
- 请求参数：
  - keyword：搜索关键词
  - status：状态
  - type：菜单类型
- 返回数据：菜单列表

#### 获取菜单详情

```typescript
function getMenuDetail(id: number): Promise<Menu>
```

- 请求方式：GET
- 请求路径：/system/menu/{id}
- 请求参数：菜单ID
- 返回数据：菜单详情

#### 创建菜单

```typescript
function createMenu(data: Partial<Menu>): Promise<any>
```

- 请求方式：POST
- 请求路径：/system/menu
- 请求参数：菜单信息
- 返回数据：创建结果

#### 更新菜单

```typescript
function updateMenu(id: number, data: Partial<Menu>): Promise<any>
```

- 请求方式：PUT
- 请求路径：/system/menu/{id}
- 请求参数：
  - id：菜单ID
  - data：需要更新的菜单信息
- 返回数据：更新结果

#### 删除菜单

```typescript
function deleteMenu(id: number): Promise<any>
```

- 请求方式：DELETE
- 请求路径：/system/menu/{id}
- 请求参数：菜单ID
- 返回数据：删除结果

#### 获取图标列表

```typescript
function getIconList(): Promise<string[]>
```

- 请求方式：GET
- 请求路径：/system/menu/icons
- 请求参数：无
- 返回数据：图标列表

## 公告管理

公告管理相关的API接口，包括公告的增删改查、发布、撤回等功能。

### 数据结构

#### Notice

```typescript
interface Notice {
  id: number;
  title: string;        // 公告标题
  content: string;      // 公告内容
  type: string;         // 公告类型（通知、公告、警告）
  status: string;       // 公告状态（草稿、已发布、已撤回）
  publishTime: string;  // 发布时间
  expireTime: string;   // 过期时间
  isTop: boolean;       // 是否置顶
  readCount: number;    // 阅读次数
  createBy: string;     // 创建者
  createTime: string;   // 创建时间
  updateTime: string;   // 更新时间
}
```

#### NoticeQuery

```typescript
interface NoticeQuery {
  keyword?: string;
  type?: string;
  status?: string;
  page?: number;
  pageSize?: number;
}
```

### 接口列表

#### 获取公告列表

```typescript
function getNoticeList(params?: NoticeQuery): Promise<{ list: Notice[]; total: number }>
```

- 请求方式：GET
- 请求路径：/system/notice/list
- 请求参数：
  - keyword：搜索关键词
  - type：公告类型
  - status：公告状态
  - page：页码
  - pageSize：每页数量
- 返回数据：公告列表和总数

#### 获取公告详情

```typescript
function getNoticeDetail(id: number): Promise<Notice>
```

- 请求方式：GET
- 请求路径：/system/notice/{id}
- 请求参数：公告ID
- 返回数据：公告详情

#### 创建公告

```typescript
function createNotice(data: Partial<Notice>): Promise<any>
```

- 请求方式：POST
- 请求路径：/system/notice
- 请求参数：公告信息
- 返回数据：创建结果

#### 更新公告

```typescript
function updateNotice(id: number, data: Partial<Notice>): Promise<any>
```

- 请求方式：PUT
- 请求路径：/system/notice/{id}
- 请求参数：
  - id：公告ID
  - data：需要更新的公告信息
- 返回数据：更新结果

#### 删除公告

```typescript
function deleteNotice(id: number): Promise<any>
```

- 请求方式：DELETE
- 请求路径：/system/notice/{id}
- 请求参数：公告ID
- 返回数据：删除结果

#### 发布公告

```typescript
function publishNotice(id: number): Promise<any>
```

- 请求方式：PUT
- 请求路径：/system/notice/publish/{id}
- 请求参数：公告ID
- 返回数据：发布结果

#### 撤回公告

```typescript
function revokeNotice(id: number): Promise<any>
```

- 请求方式：PUT
- 请求路径：/system/notice/revoke/{id}
- 请求参数：公告ID
- 返回数据：撤回结果

## 消息通知

消息通知相关的API接口，包括通知的查询、标记已读、删除等功能。

### 数据结构

#### Notification

```typescript
interface Notification {
  id: string;
  title: string;
  content: string;
  type: 'info' | 'success' | 'warning' | 'error';
  isRead: boolean;
  createTime: string;
  link?: string;
  sender?: string;
  senderAvatar?: string;
  priority?: 'low' | 'medium' | 'high';
  category?: string;
  expireTime?: string;
  action?: {
    text: string;
    link: string;
  }
}
```

#### NotificationListResult

```typescript
interface NotificationListResult {
  list: Notification[];
  total: number;
}
```

#### NotificationListParams

```typescript
interface NotificationListParams {
  page?: number;
  pageSize?: number;
  isRead?: boolean;
  category?: string;
  type?: string;
  priority?: string;
  startTime?: number;
  endTime?: number;
}
```

### 接口列表

#### 获取通知列表

```typescript
function getNotificationList(params: NotificationListParams): Promise<NotificationListResult>
```

- 请求方式：GET
- 请求路径：/notification/list
- 请求参数：
  - page：页码
  - pageSize：每页数量
  - isRead：是否已读
  - category：分类
  - type：类型
  - priority：优先级
  - startTime：开始时间
  - endTime：结束时间
- 返回数据：通知列表和总数

#### 获取未读通知数量

```typescript
function getUnreadCount(): Promise<number>
```

- 请求方式：GET
- 请求路径：/notification/unread/count
- 请求参数：无
- 返回数据：未读通知数量

#### 标记通知为已读

```typescript
function markAsRead(id: string): Promise<any>
```

- 请求方式：PUT
- 请求路径：/notification/{id}/read
- 请求参数：通知ID
- 返回数据：标记结果

#### 标记所有通知为已读

```typescript
function markAllAsRead(): Promise<any>
```

- 请求方式：PUT
- 请求路径：/notification/read/all
- 请求参数：无
- 返回数据：标记结果

#### 删除通知

```typescript
function deleteNotification(id: string): Promise<any>
```

- 请求方式：DELETE
- 请求路径：/notification/{id}
- 请求参数：通知ID
- 返回数据：删除结果

## 字典管理

字典管理相关的API接口，包括字典类型和字典数据的增删改查等功能。

### 数据结构

#### DictType

```typescript
interface DictType {
  id: number;
  name: string;         // 字典名称
  type: string;         // 字典类型
  status: boolean;      // 状态（启用/禁用）
  remark: string;       // 备注
  createTime: string;   // 创建时间
  updateTime: string;   // 更新时间
}
```

#### DictData

```typescript
interface DictData {
  id: number;
  dictTypeId: number;   // 字典类型ID
  dictType: string;     // 字典类型
  label: string;        // 字典标签
  value: string;        // 字典值
  cssClass: string;     // 样式属性
  listClass: string;    // 表格回显样式
  isDefault: boolean;   // 是否默认
  status: boolean;      // 状态（启用/禁用）
  orderNum: number;     // 排序号
  remark: string;       // 备注
  createTime: string;   // 创建时间
  updateTime: string;   // 更新时间
}
```

#### DictQuery

```typescript
interface DictQuery {
  keyword?: string;
  status?: boolean;
  page?: number;
  pageSize?: number;
}
```

#### DictDataQuery

```typescript
interface DictDataQuery extends DictQuery {
  dictType: string;
}
```

### 接口列表

#### 获取字典类型列表

```typescript
function getDictTypeList(params?: DictQuery): Promise<{ list: DictType[]; total: number }>
```

- 请求方式：GET
- 请求路径：/system/dict/type/list
- 请求参数：
  - keyword：搜索关键词
  - status：状态
  - page：页码
  - pageSize：每页数量
- 返回数据：字典类型列表和总数

#### 获取字典类型详情

```typescript
function getDictTypeDetail(id: number): Promise<DictType>
```

- 请求方式：GET
- 请求路径：/system/dict/type/{id}
- 请求参数：字典类型ID
- 返回数据：字典类型详情

#### 创建字典类型

```typescript
function createDictType(data: Partial<DictType>): Promise<any>
```

- 请求方式：POST
- 请求路径：/system/dict/type
- 请求参数：字典类型信息
- 返回数据：创建结果

#### 更新字典类型

```typescript
function updateDictType(id: number, data: Partial<DictType>): Promise<any>
```

- 请求方式：PUT
- 请求路径：/system/dict/type/{id}
- 请求参数：
  - id：字典类型ID
  - data：需要更新的字典类型信息
- 返回数据：更新结果

#### 删除字典类型

```typescript
function deleteDictType(id: number): Promise<any>
```

- 请求方式：DELETE
- 请求路径：/system/dict/type/{id}
- 请求参数：字典类型ID
- 返回数据：删除结果

#### 获取字典数据列表

```typescript
function getDictDataList(params: DictDataQuery): Promise<{ list: DictData[]; total: number }>
```

- 请求方式：GET
- 请求路径：/system/dict/data/list
- 请求参数：
  - dictType：字典类型
  - keyword：搜索关键词
  - status：状态
  - page：页码
  - pageSize：每页数量
- 返回数据：字典数据列表和总数

#### 获取字典数据详情

```typescript
function getDictDataDetail(id: number): Promise<DictData>
```

- 请求方式：GET
- 请求路径：/system/dict/data/{id}
- 请求参数：字典数据ID
- 返回数据：字典数据详情

#### 创建字典数据

```typescript
function createDictData(data: Partial<DictData>): Promise<any>
```

- 请求方式：POST
- 请求路径：/system/dict/data
- 请求参数：字典数据信息
- 返回数据：创建结果

#### 更新字典数据

```typescript
function updateDictData(id: number, data: Partial<DictData>): Promise<any>
```

- 请求方式：PUT
- 请求路径：/system/dict/data/{id}
- 请求参数：
  - id：字典数据ID
  - data：需要更新的字典数据信息
- 返回数据：更新结果

#### 删除字典数据

```typescript
function deleteDictData(id: number): Promise<any>
```

- 请求方式：DELETE
- 请求路径：/system/dict/data/{id}
- 请求参数：字典数据ID
- 返回数据：删除结果 