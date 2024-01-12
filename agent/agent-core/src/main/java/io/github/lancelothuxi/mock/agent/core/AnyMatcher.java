package io.github.lancelothuxi.mock.agent.core;

import net.bytebuddy.description.NamedElement;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.List;

/**
 * @author lancelot
 * @version 1.0
 * @date 2023/8/24 下午3:14
 */
public class AnyMatcher<T extends NamedElement> extends ElementMatcher.Junction.AbstractBase<T> {

    private List<ElementMatcher<? super TypeDescription>> elementMatchers;

    public AnyMatcher(List<ElementMatcher<? super TypeDescription>> elementMatchers) {
        this.elementMatchers = elementMatchers;
    }

    @Override
    public boolean matches(T target) {
        if (elementMatchers == null || elementMatchers.size() == 0) {
            return false;
        }

        for (ElementMatcher elementMatcher : elementMatchers) {
            if (elementMatcher.matches(target)) {
                return true;
            }
        }
        return false;
    }
}
