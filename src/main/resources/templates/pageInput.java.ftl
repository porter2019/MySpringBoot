package ${dtoPackage};

import com.xxx.myspringboot.dto.BasePageQueryModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.io.Serializable;

/**
* ${table.comment!} 分页所需参数
*/
@Getter
@Setter
@ToString
public class ${entity}PageInput extends BasePageQueryModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private Integer flag;

    private String flagIn;

}