/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements. See the NOTICE
 * file distributed with this work for additional information regarding copyright ownership. The ASF licenses this file
 * to You under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 */

package io.github.lancelothuxi.mock.agent.core;

import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lancelot
 */
public class PluginFinder {

    private List<Transformer> transformers = new ArrayList<>();

    /**
     * @param transformers
     */
    public PluginFinder(List<Transformer> transformers) {
        this.transformers = transformers;
    }

    public Transformer find(TypeDescription typeDescription) {
        for (Transformer transformer : transformers) {
            if(transformer.classMatcher().matches(typeDescription)){
                return transformer;
            }
        }
        return null;
    }

    public ElementMatcher<? super TypeDescription> buildMatch() {
        List<ElementMatcher<? super TypeDescription>> elementMatchers = new ArrayList<>();
        for (Transformer transformer : transformers) {
            ElementMatcher<? super TypeDescription> elementMatcher = transformer.classMatcher();
            elementMatchers.add(elementMatcher);
        }
        return new AnyMatcher<>(elementMatchers);
    }

}
