package io.github.lancelothuxi.mock.agent.feign;

import io.github.lancelothuxi.mock.agent.config.MockConfig;
import feign.Headers;
import feign.RequestLine;

/**
 * @author lancelot
 * @version 1.0
 * @date 2023/6/14 下午8:13
 */

public interface CommonMockFeignClient {

    @RequestLine("POST /mock/feign/config/queryMockConfigData")
    @Headers("Content-Type: application/json")
    MockFeignResponse doMock(MockConfig mockConfig);
}
