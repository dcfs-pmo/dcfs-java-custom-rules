/*
 * SonarQube Java Custom Rules Example
 * Copyright (C) 2016-2016 SonarSource SA
 * mailto:contact AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.samples.java;

import java.util.List;

import org.sonar.plugins.java.api.JavaCheck;

import org.sonar.samples.java.checks.DontRepeatedValueOneRule;
import org.sonar.samples.java.checks.DontRepeatedValueThreeRule;
import org.sonar.samples.java.checks.DontRepeatedValueTwoRule;
import org.sonar.samples.java.checks.DontSurplusJudgeRule;
import org.sonar.samples.java.checks.DontUseCacheRule;
import org.sonar.samples.java.checks.DontUseErrorObjectRule;
import org.sonar.samples.java.checks.DontUseErrorValueRule;
import org.sonar.samples.java.checks.DontUseTotalDataRule;
import org.sonar.samples.java.checks.GalaxyUndocumentedApiRule;
import org.sonar.samples.java.checks.CollectionAndMapNullValueRule;
import org.sonar.samples.java.checks.DcitsUndocumentedApiContainAuthorRule;
import org.sonar.samples.java.checks.GalaxyUndocumentedApiContainAuthorRule;

import com.google.common.collect.ImmutableList;
import org.sonar.samples.java.checks.galaxy.GalaxySpringBeanAvoidNonFinalVariablesRule;

public final class RulesList {

  private RulesList() {
  }

  public static List<Class> getChecks() {
    return ImmutableList.<Class>builder().addAll(getJavaChecks()).addAll(getJavaTestChecks()).build();
  }

  public static List<Class<? extends JavaCheck>> getJavaChecks() {
    return ImmutableList.<Class<? extends JavaCheck>>builder()
      .add(DontUseErrorObjectRule.class)
      .add(DontUseErrorValueRule.class)
      .add(DontUseTotalDataRule.class)
      .add(DontUseCacheRule.class)
      .add(DontSurplusJudgeRule.class)
      .add(DontRepeatedValueOneRule.class)
      .add(DontRepeatedValueTwoRule.class)
      .add(DontRepeatedValueThreeRule.class)
      .add(GalaxyUndocumentedApiRule.class)
      .add(DcitsUndocumentedApiContainAuthorRule.class)
      .add(GalaxyUndocumentedApiContainAuthorRule.class)
      .add(GalaxySpringBeanAvoidNonFinalVariablesRule.class)
      .add(CollectionAndMapNullValueRule.class)
      .build();
  }

  public static List<Class<? extends JavaCheck>> getJavaTestChecks() {
    return ImmutableList.<Class<? extends JavaCheck>>builder()
      .build();
  }
}
