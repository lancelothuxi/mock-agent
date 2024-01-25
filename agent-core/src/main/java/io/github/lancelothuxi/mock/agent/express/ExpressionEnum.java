package io.github.lancelothuxi.mock.agent.express;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum  ExpressionEnum {

    OGNL(1),JSON_PATH(2);

    @Getter
    private int code;

}
