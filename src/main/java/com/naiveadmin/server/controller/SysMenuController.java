package com.naiveadmin.server.controller;

import com.naiveadmin.server.common.Result;
import com.naiveadmin.server.entity.SysMenu;
import com.naiveadmin.server.service.ISysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/menu")
public class SysMenuController {

    @Autowired
    private ISysMenuService menuService;

    @GetMapping("/tree")
    public Result getMenuTree(@RequestParam(required = false) String keyword,
                            @RequestParam(required = false) Boolean status,
                            @RequestParam(required = false) String type) {
        return menuService.getDetail(null);
    }

    @GetMapping("/list")
    public Result getMenuList(@RequestParam(required = false) String keyword,
                            @RequestParam(required = false) Boolean status,
                            @RequestParam(required = false) String type) {
        return menuService.getDetail(null);
    }

    @GetMapping("/{id}")
    public Result getDetail(@PathVariable Long id) {
        return menuService.getDetail(id);
    }

    @PostMapping
    public Result create(@RequestBody SysMenu menu) {
        return menuService.create(menu);
    }

    @PutMapping("/{id}")
    public Result update(@PathVariable Long id, @RequestBody SysMenu menu) {
        menu.setId(id);
        return menuService.update(menu);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        return menuService.delete(id);
    }

    @GetMapping("/icons")
    public Result getIconList() {
        return Result.success(menuService.getIconList());
    }
} 