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
package org.sonar.samples.java.checks.galaxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.semantic.Symbol;
import org.sonar.plugins.java.api.semantic.SymbolMetadata;
import org.sonar.plugins.java.api.semantic.Type;
import org.sonar.plugins.java.api.tree.BaseTreeVisitor;
import org.sonar.plugins.java.api.tree.VariableTree;

import java.util.List;

/**
 * <p/>Name: GalaxySpringBeanAvoidNonFinalVariablesRule
 * <p/>
 * <p/>功能描述
 * <p/>规则：新核心应用不允许在服务Bean中声明非Final成员变量
 * <p/>
 *
 * @author hanleia
 * Create 2019/06/27 12:45
 * Copyright (c) DigitalChina All Rights Reserved | http://www.dcits.com
 *
 * 变更日期        变更人       变更简述
 *———————————————————————————————————
 *
 */
@Rule(key = "GalaxySpringBeanAvoidNonFinalVariables")
public class GalaxySpringBeanAvoidNonFinalVariablesRule extends BaseTreeVisitor implements JavaFileScanner {

  private JavaFileScannerContext context;

  @Override
  public void scanFile(JavaFileScannerContext context) {
    this.context = context;

    scan(context.getTree());
  }

  /**
   * 新核心应用不允许在服务Bean中声明非Final成员变量检查
   *
   * <p/>功能详述
   * <p/>1.检查Spring服务Bean下非自动注入的非final成员变量
   * <p/>2.日志类与java.lang.ThreadLocal类不处理
   * <p/>3.新核心指标类与工厂类不检查
   *
   * @param tree 成员变量代码树
   *
   * Create At 2019/06/27 12:45 By hanleia
   */
  @Override
  public void visitVariable(VariableTree tree) {
    Symbol  variableSymbol = tree.symbol();
    // final 变量不处理
    if(variableSymbol.isFinal()){
      return;
    }
    //特殊类型声明变量、非spring托管类下的自动注入变量、指标类/工厂类实现等不处理
    if(ignoreType(variableSymbol)||ignoreVariable(variableSymbol)||ignoreImpls(variableSymbol)){
      return;
    }
    context.reportIssue(this, tree, "新核心应用不允许在服务Bean中声明非Final成员变量！");
  }


  /**
   * 是否为不处理的成员变量声明
   *
   * @param variableSymbol 成员变量描述符
   * @return true/false
   *
   * Create At 2019/06/27 14:02 By hanleia
   */
  private boolean ignoreType(Symbol  variableSymbol){
    // 是否无需处理类型
    // 是否本地线程类，是否日志类
    if(variableSymbol.type().fullyQualifiedName() != null ) {
      return "java.lang.ThreadLocal".equals(variableSymbol.type().fullyQualifiedName())
              || "org.slf4j.Logger".equals(variableSymbol.type().fullyQualifiedName());
    }
    return false;
  }


  /**
   * 是否为非Spring托管类下非自动注入成员变量
   *
   * @param variableSymbol 成员变量描述符
   * @return true/false
   *
   * Create At 2019/06/27 14:02 By hanleia
   */
  private boolean ignoreVariable(Symbol  variableSymbol){
    Symbol classSymbol =variableSymbol.owner();
    SymbolMetadata parentClassOwner =  classSymbol.metadata();
    // 是否spring托管类
    boolean isSpringContext = parentClassOwner.isAnnotatedWith("org.springframework.stereotype.Controller")
            ||parentClassOwner.isAnnotatedWith("org.springframework.stereotype.Repository")
            ||parentClassOwner.isAnnotatedWith("org.springframework.stereotype.Component")
            ||parentClassOwner.isAnnotatedWith("org.springframework.stereotype.Service");
    // 是否自动注入属性
    boolean isSpringVar = variableSymbol.metadata().isAnnotatedWith("javax.annotation.Resource")
            ||variableSymbol.metadata().isAnnotatedWith("org.springframework.beans.factory.annotation.Autowired");
    //非spring托管类或自动注入属性不处理
    //final属性变量不处理
    if(!isSpringContext || isSpringVar) {
      return true;
    }
    return false;
  }

  /**
   * 是否为无需处理的接口实现类
   * 1.指标类忽略 5.30版本改造内容
   * 2.以Factory结尾并实现ApplicationContextAware和ApplicationListener的工厂类
   * @param variableSymbol 成员变量描述符
   * @return true/false
   *
   * Create At 2019/06/27 14:06 By hanleia
   */
  private boolean ignoreImpls(Symbol  variableSymbol){
    Symbol classSymbol =variableSymbol.owner();
    boolean isIgoreImpls = false;
    List<Type> interfaces= classSymbol.enclosingClass().interfaces();
    if(interfaces!= null && !interfaces.isEmpty()){
      for (Type anInterface : interfaces) {
        //指标类忽略 5.30版本改造内容
        if("com.dcits.ensemble.product.api.application.IProductPar".equals(anInterface.fullyQualifiedName())){
          isIgoreImpls = true;
          break;
        }
        //工厂类忽略
        if(classSymbol.name().endsWith("Factory")&&(
                "org.springframework.context.ApplicationContextAware".equals(anInterface.fullyQualifiedName())
                        || "org.springframework.context.ApplicationListener".equals(anInterface.fullyQualifiedName()))){
          isIgoreImpls = true;
          break;
        }
      }
    }
    return isIgoreImpls;
  }

}
