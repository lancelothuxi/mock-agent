package io.github.lancelothuxi.mock.agent.openfeign;

import feign.Headers;
import feign.RequestLine;
import io.github.lancelothuxi.mock.agent.config.MockConfig;

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
