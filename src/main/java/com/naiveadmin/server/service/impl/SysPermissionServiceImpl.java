package com.naiveadmin.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.naiveadmin.server.common.Result;
import com.naiveadmin.server.entity.SysPermission;
import com.naiveadmin.server.mapper.SysPermissionMapper;
import com.naiveadmin.server.service.SysPermissionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements SysPermissionService {

    @Override
    public Result getList(String keyword, Boolean status, Integer page, Integer pageSize) {
        LambdaQueryWrapper<SysPermission> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(keyword)) {
            wrapper.like(SysPermission::getName, keyword)
                    .or()
                    .like(SysPermission::getCode, keyword);
        }
        if (status != null) {
            wrapper.eq(SysPermission::getStatus, status);
        }
        wrapper.orderByDesc(SysPermission::getCreateTime);

        Page<SysPermission> pageResult = page(new Page<>(page, pageSize), wrapper);
        return Result.success(pageResult);
    }

    @Override
    public Result getAllPermissions() {
        List<SysPermission> permissions = list(new LambdaQueryWrapper<SysPermission>()
                .eq(SysPermission::getStatus, true)
                .select(SysPermission::getId, SysPermission::getName, SysPermission::getCode));
        return Result.success(permissions);
    }

    @Override
    public Result getDetail(Long id) {
        SysPermission permission = getById(id);
        if (permission == null) {
            return Result.error("权限不存在");
        }
        return Result.success(permission);
    }

    @Override
    public Result create(SysPermission permission) {
        if (checkPermissionExists(permission)) {
            return Result.error("权限编码已存在");
        }
        permission.setCreateTime(LocalDateTime.now());
        permission.setUpdateTime(LocalDateTime.now());
        save(permission);
        return Result.success();
    }

    @Override
    public Result update(SysPermission permission) {
        SysPermission existingPermission = getById(permission.getId());
        if (existingPermission == null) {
            return Result.error("权限不存在");
        }
        if (!existingPermission.getCode().equals(permission.getCode()) && checkPermissionExists(permission)) {
            return Result.error("权限编码已存在");
        }
        permission.setUpdateTime(LocalDateTime.now());
        updateById(permission);
        return Result.success();
    }

    @Override
    public Result delete(Long id) {
        if (!removeById(id)) {
            return Result.error("删除失败");
        }
        return Result.success();
    }

    private boolean checkPermissionExists(SysPermission permission) {
        return count(new LambdaQueryWrapper<SysPermission>()
                .eq(SysPermission::getCode, permission.getCode())
                .ne(permission.getId() != null, SysPermission::getId, permission.getId())) > 0;
    }
} 