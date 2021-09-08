package com.xl.learn.common.msg;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author: xl
 * @date: 2021/8/5
 **/
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendMsgReqVO implements Serializable {

    private String msg;

    private Long to;

    private Long from;

    private Integer type;
}
