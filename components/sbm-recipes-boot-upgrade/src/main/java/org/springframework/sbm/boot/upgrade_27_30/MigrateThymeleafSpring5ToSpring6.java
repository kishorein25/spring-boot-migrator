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
 * Migrates {@code org.thymeleaf.spring5} references to {@code org.thymeleaf.spring6}.
 * <p>
 * Spring Boot 3 / Spring Framework 6 integrate with Thymeleaf 3 using the {@code spring6} Spring
 * integration package instead of the {@code spring5} package used by Spring Boot 2.7.
 */
public class MigrateThymeleafSpring5ToSpring6 extends MigratePackageReferences {

    public MigrateThymeleafSpring5ToSpring6() {
        super("org.thymeleaf.spring5", "org.thymeleaf.spring6");
    }
}