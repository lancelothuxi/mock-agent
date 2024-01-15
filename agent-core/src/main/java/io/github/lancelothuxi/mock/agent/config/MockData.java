package io.github.lancelothuxi.mock.agent.config;

/**
 * mock配置关联响应数据对象 mock_data
 *
 * @author ruoyi
 * @date 2023-05-10
 */
public class MockData {
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
    private String mockReqParams;

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

    public Long getEnabled() {
        return enabled;
    }

    public void setEnabled(Long enabled) {
        this.enabled = enabled;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getExpectedValue() {
        return expectedValue;
    }

    public void setExpectedValue(String expectedValue) {
        this.expectedValue = expectedValue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMockConfigId() {
        return mockConfigId;
    }

    public void setMockConfigId(String mockConfigId) {
        this.mockConfigId = mockConfigId;
    }

    public String getMockReqParams() {
        return mockReqParams;
    }

    public void setMockReqParams(String mockReqParams) {
        this.mockReqParams = mockReqParams;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }
}
