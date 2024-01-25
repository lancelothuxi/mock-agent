package io.github.lancelothuxi.mock.agent.config;

import lombok.Data;

import java.io.Serializable;

/**
 * mock配置关联响应数据对象 mock_data
 *
 * @author ruoyi
 * @since  2023-05-10
 */
@Data
public class MockData implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * mock响应数据值
     */
    private String data;

    /**
     * mock规则配置表的id
     */
    private String mockConfigId;

    /**
     * mock请求参数匹配规则
     */
    private String expression;

    /**
     * 服务名
     */
    private String interfaceName;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 超时时间
     */
    private Integer timeout = 0;

    /**
     * 根据jsonpath eval后的实际值
     */
    private String expectedValue;

    /**
     * 是否启用
     */
    private Long enabled;

}
