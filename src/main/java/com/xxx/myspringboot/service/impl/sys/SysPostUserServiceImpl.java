package com.xxx.myspringboot.service.impl.sys;

import com.xxx.myspringboot.entity.sys.SysPostUser;
import com.xxx.myspringboot.mapper.sys.SysPostUserMapper;
import com.xxx.myspringboot.service.sys.ISysPostUserService;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 岗位下的用户 服务实现类
 */
@Service
public class SysPostUserServiceImpl extends ServiceImpl<SysPostUserMapper, SysPostUser> implements ISysPostUserService {

}
