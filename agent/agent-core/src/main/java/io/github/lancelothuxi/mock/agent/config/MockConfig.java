package io.github.lancelothuxi.mock.agent.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lancelot
 */
public class MockConfig implements Serializable {

    private static final long serialVersionUID = 3146855948097315719L;
    /**
     *
     */
    private Long id;
    /**
     * 接口
     */
    private String interfaceName;
    /**
     * 方法
     */
    private String methodName;
    /**
     * 分组
     */
    private String groupName;
    /**
     * 版本
     */
    private String version;
    /**
     * mock数据
     */
    private String data;
    /**
     * 是否启用
     */
    private Integer enabled;

    private Date updateTime;

    /**
     * 类型 duboo/dubborest/feign
     */
    private String type;

    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * 是否是服务端mock
     */
    private Integer serverSideMock = 1;

    /**
     * mock数据列表
     */
    private List<MockData> mockDataList = new ArrayList<>();


    public MockConfig(String interfaceName, String methodName, String groupName, String version) {
        this.interfaceName = interfaceName;
        this.methodName = methodName;
        this.groupName = groupName;
        this.version = version;
    }

    public MockConfig() {
    }

    public Integer getServerSideMock() {
        return serverSideMock;
    }

    public void setServerSideMock(Integer serverSideMock) {
        this.serverSideMock = serverSideMock;
    }

    public boolean mockFromServer() {
        return serverSideMock != null && serverSideMock == 1;
    }

    public List<MockData> getMockDataList() {
        return mockDataList;
    }

    public void setMockDataList(List<MockData> mockDataList) {
        this.mockDataList = mockDataList;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

}
