package com.ltx.saleassistant.controller;

import com.alibaba.excel.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ltx.saleassistant.mapper.JiaJuMapper;
import com.ltx.saleassistant.domain.jiajuEntity.JiaJu;
import com.ltx.saleassistant.enums.Result;
import com.ltx.saleassistant.service.JiaJuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController

@CrossOrigin(origins = "*")
@RequestMapping("/jiaju")
public class JiajuController {
    @Value("${file.upload_dir}")
    private String upload_dir;
    @Value("${file.upload_url}")
    private String upload_url;
    @Autowired
    private JiaJuService jiaJuService;

    @Autowired
    JiaJuMapper jiaJuMapper;

    @PostMapping("/imgUpload")
    public Result upload(MultipartFile file) throws IOException {
        String uuid = UUID.randomUUID().toString();
        File file1 = new File(upload_dir + uuid + ".jpg");
        file.transferTo(file1);
        String pic_path = upload_url + uuid + ".jpg";
        System.out.println(pic_path);
        return Result.success(pic_path);
    }

    @PostMapping("/addNewJiaju")
    public Result getFormByMap(@Validated JiaJu jiaju, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error("500", String.valueOf(bindingResult.getFieldErrors().get(0)));
        }
        String randomId = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        jiaju.setJiajuId(randomId);
        try {
            jiaJuService.insert(jiaju);
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return Result.success();

    }

    @PostMapping("deletById")
    public Result deletById(JiaJu jiaju) {
        try {
            String img_name = jiaju.getImgUrl().substring(jiaju.getImgUrl().length() - 36);
//
            File file = new File(img_name + ".jpg");
            if (file.isFile() && file.exists()) {
                file.delete();
            }
            jiaJuMapper.deleteById(jiaju.getJiajuId());
        } catch (Exception e) {
            return Result.error("500", "删除错误！");
        }
        return Result.success();
    }
    @PostMapping("updateJiaJu")
    public Result updateJiaJu(JiaJu jiaju) {
        int updatedLines = 0;
        try {
            updatedLines = jiaJuMapper.updateById(jiaju);
        }catch (Exception e){
            return Result.error("500", e.getMessage());
        }
        return Result.success(updatedLines);
    }
    @GetMapping("getTotal")
    public Result getTotal() {
        Long count = jiaJuMapper.selectCount();
        return Result.success(count);
    }
    @GetMapping("selectJiaJuById")
    public Result selectJiaJuById(String jiajuId) {
        JiaJu one_jiaJu = jiaJuMapper.selectById(jiajuId);
        return Result.success(one_jiaJu);
    }

    @GetMapping("getPage")
    public Result getPage(Integer cur, Integer size) {
        QueryWrapper<JiaJu> qw = new QueryWrapper<>();
        IPage<JiaJu> page = new Page<>();
        page.setCurrent(cur);
        page.setSize(size);

        IPage<JiaJu> result = jiaJuMapper.selectPage(page, qw);
        List<JiaJu> jiaJuList = result.getRecords();
        System.out.println(jiaJuList.size());
        return Result.success(jiaJuList);
    }

    @GetMapping("searchPage")
    public Result searchPage(Integer cur, Integer size, String searchStr) {
        QueryWrapper<JiaJu> qw = new QueryWrapper<>();
        IPage<JiaJu> page = new Page<>();
        page.setCurrent(cur);
        page.setSize(size);
        if (StringUtils.isNotBlank(searchStr)) {
            qw.like("name", searchStr).or()
                    .like("brand", searchStr).or()
                    .like("type", searchStr).or()
                    .like("cailiao", searchStr).or()
                    .like("color", searchStr);
        }
        IPage<JiaJu> result = jiaJuMapper.selectPage(page,qw);
        List<JiaJu> jiaJuList = result.getRecords();
        return Result.success(jiaJuList);
    }

}
