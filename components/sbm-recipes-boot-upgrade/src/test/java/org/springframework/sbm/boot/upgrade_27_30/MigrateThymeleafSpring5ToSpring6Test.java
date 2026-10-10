/*
 * Copyright 2021 - 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.sbm.boot.upgrade_27_30;

import org.junit.jupiter.api.Test;
import org.openrewrite.Result;

import java.util.List;

public class MigrateThymeleafSpring5ToSpring6Test {

    private final JavaTestHelper javaTestHelper = new JavaTestHelper();

    @Test
    void migratesThymeleafSpring5ToSpring6WithoutTypeAttribution() {
        String before = """
                package org.fabianlee.springmicrowithactuator.actuator;

                import org.thymeleaf.context.Context;
                import org.thymeleaf.spring5.SpringTemplateEngine;

                @Component
                public class CustomPrometheusEndpoint {

                    protected SpringTemplateEngine mMessageTemplateEngine;

                    public String render() {
                        return mMessageTemplateEngine.process("text/prometheus-custom.txt", new Context());
                    }
                }
                """;

        String after = """
                package org.fabianlee.springmicrowithactuator.actuator;

                import org.thymeleaf.context.Context;
                import org.thymeleaf.spring6.SpringTemplateEngine;

                @Component
                public class CustomPrometheusEndpoint {

                    protected SpringTemplateEngine mMessageTemplateEngine;

                    public String render() {
                        return mMessageTemplateEngine.process("text/prometheus-custom.txt", new Context());
                    }
                }
                """;

        List<Result> result = javaTestHelper.runRecipe(
                new MigrateThymeleafSpring5ToSpring6(),
                List.of(),
                before
        );

        javaTestHelper.assertResult(result, after);
    }
}