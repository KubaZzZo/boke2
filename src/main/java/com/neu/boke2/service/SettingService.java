package com.neu.boke2.service;

import com.neu.boke2.entity.Setting;
import com.neu.boke2.repository.SettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class SettingService {
    @Autowired
    private SettingRepository settingRepository;

    public Setting getSetting() {
        return settingRepository.findAll().stream().findFirst().orElse(null);
    }

    public Setting saveSetting(Setting setting) {
        Setting old = getSetting();
        if (old != null) {
            setting.setId(old.getId());
        }
        return settingRepository.save(setting);
    }
} 