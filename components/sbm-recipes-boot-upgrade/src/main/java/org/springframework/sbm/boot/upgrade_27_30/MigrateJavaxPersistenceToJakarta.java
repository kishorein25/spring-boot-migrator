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

/**
 * Migrates {@code javax.persistence} references to {@code jakarta.persistence}.
 * <p>
 * This is a classpath-independent complement to the Jakarta EE 9 recipe
 * {@code org.openrewrite.java.migrate.jakarta.JavaxMigrationToJakarta} which only rewrites types
 * that are resolvable on the compile classpath.
 */
public class MigrateJavaxPersistenceToJakarta extends MigratePackageReferences {

    public MigrateJavaxPersistenceToJakarta() {
        super("javax.persistence", "jakarta.persistence");
    }
}