package com.neu.boke2.controller;

import com.neu.boke2.common.ApiResponse;
import com.neu.boke2.entity.Setting;
import com.neu.boke2.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settings")
public class SettingController {
    @Autowired
    private SettingService settingService;

    @GetMapping
    public ApiResponse<Setting> getSetting() {
        Setting setting = settingService.getSetting();
        return ApiResponse.success(setting);
    }

    @PostMapping
    public ApiResponse<Setting> saveSetting(@RequestBody Setting setting) {
        Setting saved = settingService.saveSetting(setting);
        return ApiResponse.success(saved);
    }
} 