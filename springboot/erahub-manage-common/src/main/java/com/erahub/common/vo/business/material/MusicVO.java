package com.erahub.common.vo.business.material;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Author lipeng
 * @Date 2020/3/14 19:28
 * @Version 1.0
 **/
@Data
public class MusicVO {
    @JsonProperty(value = "title")
    private String name;
    @JsonProperty(value = "src")
    private String url;
    private String artist;
    @JsonProperty(value = "pic")
    private String cover;
    private String lrc;
}
