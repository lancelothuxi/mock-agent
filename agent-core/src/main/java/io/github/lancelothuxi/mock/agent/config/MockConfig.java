package io.github.lancelothuxi.mock.agent.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lancelot
 */
@Data
@NoArgsConstructor
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
    private Integer serverSideMock = 0;

    //jsonpath ognl
    private Integer expressionType;

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
}
