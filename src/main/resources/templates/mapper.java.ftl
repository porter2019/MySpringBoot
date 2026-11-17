package ${package.Mapper};

import ${package.Entity}.${entity};
import com.xxx.myspringboot.mapper.common.MyPageQueryMapper;

/**
* ${table.comment!} Mapper 接口
*/
@Mapper
public interface ${table.mapperName} extends MyPageQueryMapper<${entity}> {

}