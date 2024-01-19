package io.github.lancelothuxi.mock.agent.examples.feign;

import feign.Headers;
import feign.RequestLine;

public interface HelloFeignService  {
    @RequestLine("POST /mock/feign/config/queryMockConfigData")
    public String hello();
}
