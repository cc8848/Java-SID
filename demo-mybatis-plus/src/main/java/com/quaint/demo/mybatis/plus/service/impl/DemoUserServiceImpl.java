package com.quaint.demo.mybatis.plus.service.impl;

import com.quaint.demo.mybatis.plus.mapper.DemoUserMapper;
import com.quaint.demo.mybatis.plus.dto.DemoUserDTO;
import com.quaint.demo.mybatis.plus.po.DemoUserPO;
import com.quaint.demo.mybatis.plus.service.DemoUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author qi cong
 * @date 2019-12-25 16:32
 */
@Service
@Slf4j
public class DemoUserServiceImpl implements DemoUserService {

    @Autowired
    DemoUserMapper demoUserMapper;

    @Override
    public DemoUserDTO getUserInfoById(Integer id) {
        log.info("getUserInfoById param is [{}]",id);
        DemoUserPO po = demoUserMapper.selectById(id);
        if (po != null){
            DemoUserDTO dto = new DemoUserDTO();
            BeanUtils.copyProperties(po,dto);
            return dto;
        }
        return null;
    }

    @Override
    public DemoUserDTO addUserInfo(DemoUserDTO reqDto) {

        if (Objects.isNull(reqDto)){
            return null;
        }

        DemoUserPO po = new DemoUserPO();
        BeanUtils.copyProperties(reqDto,po);
        demoUserMapper.insert(po);

        DemoUserDTO respDto = new DemoUserDTO();
        BeanUtils.copyProperties(po,respDto);
        return respDto;
    }

    @Override
    public boolean delUserInfo(Integer id){
        // PO valid字段上增加 @TableLogic 注解后 会 执行update 方法进行逻辑删除
        return demoUserMapper.deleteById(id)>0;
    }

}
