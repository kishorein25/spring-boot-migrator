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

import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.TreeVisitor;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.tree.Expression;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.TypeTree;

/**
 * Renames references from one package to another strictly on the source text.
 * <p>
 * Unlike {@link org.openrewrite.java.ChangePackage} this recipe does not require the referenced
 * classes to be resolvable on the compile classpath. {@code ChangePackage} only rewrites a source
 * file when {@link org.openrewrite.java.search.UsesType} can attribute the referenced types, so it
 * silently does nothing once a migration already swapped the classpath (for example after
 * {@code javax.persistence-api} was replaced by {@code jakarta.persistence-api}) while the source
 * still references the old package.
 * <p>
 * The rename is deliberately scoped to fully-qualified references whose package prefix exactly
 * matches {@link #oldPackageName}, so no unrelated strings are touched.
 */
public abstract class MigratePackageReferences extends Recipe {

    protected final String oldPackageName;
    protected final String newPackageName;

    public MigratePackageReferences(String oldPackageName, String newPackageName) {
        this.oldPackageName = oldPackageName;
        this.newPackageName = newPackageName;
    }

    @Override
    public @NotNull String getDisplayName() {
        return "Migrate package references from " + oldPackageName + " to " + newPackageName;
    }

    @Override
    public @NotNull String getDescription() {
        return "Migrate references from " + oldPackageName + " to " + newPackageName + " " +
                "independent of compile classpath type attribution.";
    }

    @Override
    public @Nullable TreeVisitor<?, ExecutionContext> getApplicableTest() {
        return null;
    }

    @Override
    protected @NotNull JavaIsoVisitor<ExecutionContext> getVisitor() {
        return new JavaIsoVisitor<>() {
            @Override
            public J.FieldAccess visitFieldAccess(J.FieldAccess fieldAccess, ExecutionContext ctx) {
                J.FieldAccess f = super.visitFieldAccess(fieldAccess, ctx);

                if (f.isFullyQualifiedClassReference(oldPackageName)) {
                    Expression replacement = TypeTree.build(newPackageName).withPrefix(f.getPrefix());
                    return (J.FieldAccess) replacement;
                }

                return f;
            }
        };
    }
}