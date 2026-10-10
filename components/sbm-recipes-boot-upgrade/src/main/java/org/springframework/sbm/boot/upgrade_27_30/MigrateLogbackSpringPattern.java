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

import org.jetbrains.annotations.NotNull;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.TreeVisitor;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.xml.XmlIsoVisitor;
import org.openrewrite.xml.tree.Xml;

import java.util.regex.Pattern;

/**
 * Migrates logback 1.2 pattern abbreviation syntax in {@code logback*.xml} files to the syntax
 * required by logback 1.4 / Spring Boot 3.0.
 * <p>
 * logback 1.2 accepted class-abbreviation conversion parts like {@code %C{1.}} (a trailing dot
 * meaning the optional package prefix is empty). logback 1.4 rejects that syntax at runtime with
 * {@code NumberFormatException}, which fails the application context under Spring Boot 3.0.
 */
public class MigrateLogbackSpringPattern extends Recipe {

    private static final Pattern ABBREVIATED_NUMERIC_ARG = Pattern.compile("\\{(\\d+)\\.}");

    @Override
    public @NotNull String getDisplayName() {
        return "Migrate logback 1.2 pattern abbreviations to logback 1.4";
    }

    @Override
    public @NotNull String getDescription() {
        return "Replace logback 1.2 abbreviation arguments such as %C{1.} with the syntax " +
                "accepted by logback 1.4, e.g. %C{1}, as required by Spring Boot 3.0.";
    }

    @Override
    public @Nullable TreeVisitor<?, ExecutionContext> getApplicableTest() {
        return null;
    }

    @Override
    protected @NotNull XmlIsoVisitor<ExecutionContext> getVisitor() {
        return new XmlIsoVisitor<>() {
            @Override
            public Xml.CharData visitCharData(Xml.CharData charData, ExecutionContext ctx) {
                Xml.CharData cd = super.visitCharData(charData, ctx);
                String text = cd.getText();
                if (ABBREVIATED_NUMERIC_ARG.matcher(text).find()) {
                    cd = cd.withText(ABBREVIATED_NUMERIC_ARG.matcher(text).replaceAll("{$1}"));
                }
                return cd;
            }
        };
    }
}