package com.xxx.myspringboot;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xxx.myspringboot.entity.SysUser;
import com.xxx.myspringboot.service.ISysUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MySpringBootApplicationTests {

	@Autowired
	private ISysUserService sysUserService;

	@Test
	void contextLoads() {
		//验证上下文加载成功
	}


	@Test
	void testUserService(){
		var query=new LambdaQueryWrapper<SysUser>();
		query.eq(SysUser::getUserName,"admin");
		var user = sysUserService.getOne(query);

		Assert.isNull(user);
	}


}
