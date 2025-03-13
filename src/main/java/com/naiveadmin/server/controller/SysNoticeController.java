package com.naiveadmin.server.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.naiveadmin.server.common.Result;
import com.naiveadmin.server.entity.SysNotice;
import com.naiveadmin.server.service.ISysNoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 通知公告Controller
 */
@Tag(name = "通知公告管理")
@RestController
@RequestMapping("/system/notice")
@RequiredArgsConstructor
public class SysNoticeController {

    private final ISysNoticeService noticeService;

    @Operation(summary = "获取公告列表")
    @GetMapping("/list")
    public Result<IPage<SysNotice>> list(Page<SysNotice> page,
                                   @RequestParam(required = false) String keyword,
                                   @RequestParam(required = false) String type,
                                   @RequestParam(required = false) String status,
                                   @RequestParam(required = false) LocalDateTime startTime,
                                   @RequestParam(required = false) LocalDateTime endTime) {
        return Result.success(noticeService.getNoticePage(page, keyword, type, status, startTime, endTime));
    }

    @Operation(summary = "获取有效公告列表")
    @GetMapping("/valid")
    public Result<List<SysNotice>> validList() {
        List<SysNotice> list = noticeService.getValidNoticeList();
        return Result.success(list);
    }

    @Operation(summary = "获取公告详情")
    @GetMapping("/{id}")
    public Result<SysNotice> getInfo(@PathVariable Long id) {
        return Result.success(noticeService.getNoticeById(id));
    }

    @Operation(summary = "新增公告")
    @PostMapping
    public Result<Void> add(@RequestBody SysNotice notice) {
        noticeService.createNotice(notice);
        return Result.success();
    }

    @Operation(summary = "修改公告")
    @PutMapping
    public Result<Void> edit(@RequestBody SysNotice notice) {
        noticeService.updateNotice(notice);
        return Result.success();
    }

    @Operation(summary = "删除公告")
    @DeleteMapping("/{id}")
    public Result<Void> remove(@PathVariable Long id) {
        noticeService.deleteNotice(id);
        return Result.success();
    }

    @Operation(summary = "发布公告")
    @PutMapping("/publish/{id}")
    public Result<Void> publish(@PathVariable Long id) {
        noticeService.publishNotice(id);
        return Result.success();
    }

    @Operation(summary = "撤回公告")
    @PutMapping("/revoke/{id}")
    public Result<Void> revoke(@PathVariable Long id) {
        noticeService.revokeNotice(id);
        return Result.success();
    }

    @Operation(summary = "阅读公告")
    @PutMapping("/read/{id}")
    public Result<Void> read(@PathVariable Long id) {
        noticeService.readNotice(id);
        return Result.success();
    }
} 