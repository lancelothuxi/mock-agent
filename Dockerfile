FROM busybox
RUN mkdir -p /agent
COPY target/mock-agent.jar /agent/dubbo_mock_agent.jar
WORKDIR /agent
