package com.xxx.myspringboot.dto.input;

import lombok.Data;

@Data
public class ContractExportInput extends BaseFormExportOption {

    private static final long serialVersionUID = 1L;

    private Boolean showPrice;
}
