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
package org.sonar.samples.java.checks;

import org.sonar.check.Rule;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.tree.Arguments;
import org.sonar.plugins.java.api.tree.BaseTreeVisitor;
import org.sonar.plugins.java.api.tree.BinaryExpressionTree;
import org.sonar.plugins.java.api.tree.ConditionalExpressionTree;
import org.sonar.plugins.java.api.tree.MemberSelectExpressionTree;
import org.sonar.plugins.java.api.tree.MethodInvocationTree;
import org.sonar.plugins.java.api.tree.Tree.Kind;

@Rule(key = "DontRepeatedValueThree")
public class DontRepeatedValueThreeRule extends BaseTreeVisitor implements JavaFileScanner {

	private JavaFileScannerContext context;

	@Override
	public void scanFile(JavaFileScannerContext context) {
		this.context = context;

		// The call to the scan method on the root of the tree triggers the
		// visit of the AST by this visitor
		scan(context.getTree());

		// For debugging purpose, you can print out the entire AST of the
		// analyzed file
		// System.out.println(PrinterVisitor.print(context.getTree()));
	}

	/**
	 * Overriding the visitor method to implement the logic of the rule.
	 * 
	 * @param tree
	 *            AST of the visited method.
	 */

	/**
	 * @author wxb
	 * @version 1
	 * @date 2017-6-26
	 * @description This scanning plug-in can greatly improve code
	 *              specifications
	 */

	@Override
	public void visitConditionalExpression(ConditionalExpressionTree tree) {
		String getControlValueString1 = "";
		String getControlValueString2 = "";
		String str1 = "";
		String str2 = "";
		try {
		if (tree.condition().is(Kind.EQUAL_TO)) {
			BinaryExpressionTree binaryExpressionTree = (BinaryExpressionTree) tree.condition();
			if (binaryExpressionTree.leftOperand().is(Kind.METHOD_INVOCATION)) {
				MethodInvocationTree methodInvocationTree = (MethodInvocationTree) binaryExpressionTree.leftOperand();
				MemberSelectExpressionTree memberSelectExpressionTree = (MemberSelectExpressionTree) methodInvocationTree
						.methodSelect();
				getControlValueString1 = memberSelectExpressionTree.lastToken().text();
				Arguments argumentListTree = (Arguments) methodInvocationTree.arguments();
				if (argumentListTree.size() > 0) {
					str1 = argumentListTree.get(0).firstToken().text();
				}
			}
		}
		if (tree.falseExpression().is(Kind.METHOD_INVOCATION)) {
			MethodInvocationTree binaryTree = (MethodInvocationTree) tree.falseExpression();
			if (binaryTree.methodSelect().is(Kind.MEMBER_SELECT)) {
				MemberSelectExpressionTree memberSelectExpressionTree = (MemberSelectExpressionTree) binaryTree
						.methodSelect();
				getControlValueString2 = memberSelectExpressionTree.lastToken().text();
			}
			if (binaryTree.arguments().is(Kind.ARGUMENTS)) {
				Arguments argumentListTree = (Arguments) binaryTree.arguments();
				if (argumentListTree.size() > 0) {
					str2 = argumentListTree.get(0).firstToken().text();
				}
			}
		}
		if (getControlValueString1 != "" && str1 != "") {
			if(getControlValueString1.equals("getControlValueString")){
				if ((getControlValueString1.equals(getControlValueString2)) && (str1.equals(str2))) {
					context.reportIssue(this, tree, "重复取值3");
				}
			}

		}
		super.visitConditionalExpression(tree);
		}
		catch(Exception e){
			e.printStackTrace();    					 
			}
	}
}
