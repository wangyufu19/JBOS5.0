package com.application.common;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * UserObject
 * @author youfu.wang
 * @date 2019-10-28
 */
@Setter
@Getter
public class UserObject implements Serializable{
    private String userId;
    private String loginName;
    private String username;
    private String userStatus;
    private String orgId;
    private String depId;

}
