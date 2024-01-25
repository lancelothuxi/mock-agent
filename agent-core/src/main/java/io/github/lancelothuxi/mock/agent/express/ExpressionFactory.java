package io.github.lancelothuxi.mock.agent.express;

import io.github.lancelothuxi.mock.agent.express.jsonpath.JsonpathExpression;
import io.github.lancelothuxi.mock.agent.express.ognl.OgnlExpression;

import static io.github.lancelothuxi.mock.agent.express.ExpressionEnum.JSON_PATH;
import static io.github.lancelothuxi.mock.agent.express.ExpressionEnum.OGNL;

public class ExpressionFactory {

    public static Expression getExpression(int expressionType,Object[] args){
        if(expressionType == JSON_PATH.getCode()){
            return new JsonpathExpression(args);
        }

        if(expressionType == OGNL.getCode()){
            return new OgnlExpression(args);
        }

        throw new RuntimeException("invalid expression type");
    }
}
