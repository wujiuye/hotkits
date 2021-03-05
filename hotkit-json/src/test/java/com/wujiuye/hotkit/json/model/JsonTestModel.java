package com.wujiuye.hotkit.json.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author wujiuye 2020/06/22
 */
@NoArgsConstructor
@Data
@ToString
@Accessors(chain = true)
public class JsonTestModel {

    private String date;
    private Boolean flag;
    private Integer id;
    private String content;
    private Double dv;

}

