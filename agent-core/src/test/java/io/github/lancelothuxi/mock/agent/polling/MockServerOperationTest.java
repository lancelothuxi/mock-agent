package io.github.lancelothuxi.mock.agent.polling;

import io.github.lancelothuxi.mock.agent.config.MockConfig;
import io.github.lancelothuxi.mock.agent.config.registry.MockConfigRegistry;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lancelottestng
 * @version 1.0
 * @date 2023/7/24 下午7:25
 */
public class MockServerOperationTest {

    @org.testng.annotations.Test
    public void testRegisterAndGet4Dubbo() {

        System.setProperty("mock.agent.log.enabled", "true");

        MockConfig mockConfig = new MockConfig("a1", "b1", "", "");

        MockConfigRegistry.add(new MockConfig("a", "b", "", ""));
        MockConfigRegistry.add(mockConfig);

        MockConfig queryResult = MockConfigRegistry.getMockConfig(mockConfig);

        Assert.assertNotNull(queryResult);

        MockConfig notExist = new MockConfig("a1", "zz", "", "");

        MockConfig queryResultnotExist = MockConfigRegistry.getMockConfig(notExist);

        Assert.assertNull(queryResultnotExist);

        List<MockConfig> list = new ArrayList<>();
        list.add(new MockConfig("a", "b", "", ""));
        list.add(new MockConfig("a3", "b3", "", ""));
    }

    @org.testng.annotations.Test
    public void testRegisterAndGet4DubboRest() {
    }

    @org.testng.annotations.Test
    public void testRegisterAndGet4Feign() {
    }
}
