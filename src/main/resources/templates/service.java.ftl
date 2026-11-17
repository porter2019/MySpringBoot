package ${package.Service};

import com.xxx.myspringboot.dto.input.${entity}PageInput;
import ${package.Entity}.${entity};
import com.baomidou.mybatisplus.spring.service.IService;
import com.xxx.myspringboot.common.PageResult;

/**
* ${table.comment!} 服务类
*/
public interface ${table.serviceName} extends IService<${entity}> {

    /**
    * 获取分页列表
    *
    * @param input input
    * @return 分页对象
    */
    PageResult<${entity}> getPageList(${entity}PageInput input);

    /**
    * 生成下一个编号
    *
    * @param prefix 编号前缀 HT
    */
    String generateNextCode(String prefix);

    /**
    * 添加
    *
    * @param entity 实体对象
    */
    void add(${entity} entity);

    /**
    * 修改
    *
    * @param entity 实体对象
    */
    void edit(${entity} entity);

}