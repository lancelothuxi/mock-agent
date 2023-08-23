package io.github.lancelothuxi.mock.agent.functions.impl;


import io.github.lancelothuxi.mock.agent.functions.AbstractFunction;
import io.github.lancelothuxi.mock.agent.functions.CompoundVariable;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.regex.Pattern;

/**
 * @author lancelot
 */
public class TimeFunction extends AbstractFunction {

    private static final String KEY = "__time";

    private static final Pattern DIVISOR_PATTERN = Pattern.compile("/\\d+");

    private String format = "";

    @Override
    public String execute(Object... args) {
        String datetime = "";
        if (format.isEmpty()) {
            datetime = Long.toString(System.currentTimeMillis());
        }
        if (DIVISOR_PATTERN.matcher(format).matches()) {
            long div = Long.parseLong(format.substring(1));
            datetime = Long.toString(System.currentTimeMillis() / div);
        } else {
            DateTimeFormatter df = DateTimeFormatter
                    .ofPattern(format)
                    .withZone(ZoneId.systemDefault());
            datetime = df.format(Instant.now());
        }
        return datetime;
    }

    @Override
    public void setParameters(Collection<CompoundVariable> parameters) {
        // TODO 参数个数校验

        Object[] values = parameters.toArray();
        int count = values.length;

        if (count > 0) {
            format = ((CompoundVariable) values[0]).execute();
        }
    }


    @Override
    public String getReferenceKey() {
        return KEY;
    }
}
