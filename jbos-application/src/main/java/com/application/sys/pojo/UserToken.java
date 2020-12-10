package com.application.sys.pojo;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * UserToken
 * @author youfu.wang
 */
@Setter
@Getter
public class UserToken implements Serializable {

    private String userId;

    private String token;

    private Date expireTime;

    private Date updateTime;
}
