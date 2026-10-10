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
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.Result;
import org.openrewrite.xml.XmlParser;
import org.openrewrite.xml.tree.Xml;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MigrateLogbackSpringPatternTest {

    @Test
    void migratesLogbackOneTwoAbbreviations() {
        String before = """
                <configuration>
                    <appender name="Console" class="ch.qos.logback.core.ConsoleAppender">
                        <layout class="ch.qos.logback.classic.PatternLayout">
                            <Pattern>%black(%d{ISO8601}) %highlight(%-5level) [%blue(%t)] %yellow(%C{1.}): %msg%n%throwable</Pattern>
                        </layout>
                    </appender>
                    <appender name="RollingFile" class="ch.qos.logback.core.rolling.RollingFileAppender">
                        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
                            <Pattern>%d %p %C{1.} [%t] %m%n</Pattern>
                        </encoder>
                    </appender>
                </configuration>
                """;

        String after = """
                <configuration>
                    <appender name="Console" class="ch.qos.logback.core.ConsoleAppender">
                        <layout class="ch.qos.logback.classic.PatternLayout">
                            <Pattern>%black(%d{ISO8601}) %highlight(%-5level) [%blue(%t)] %yellow(%C{1}): %msg%n%throwable</Pattern>
                        </layout>
                    </appender>
                    <appender name="RollingFile" class="ch.qos.logback.core.rolling.RollingFileAppender">
                        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
                            <Pattern>%d %p %C{1} [%t] %m%n</Pattern>
                        </encoder>
                    </appender>
                </configuration>
                """;

        List<Xml.Document> documents = new XmlParser().parse(before);
        List<Result> result = new MigrateLogbackSpringPattern().run(documents,
                new InMemoryExecutionContext(Throwable::printStackTrace)).getResults();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAfter().printAll()).isEqualTo(after);
    }
}