package com.qx;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author qiux
 * @Date 2023/3/19
 * @since
 */
@Data
@Builder
public class SseRespInfoBO implements Serializable {

    private String id;

    private String data;

    private String event;

}
