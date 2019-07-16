package org.sonar.samples.java.checks;

import org.sonar.check.Rule;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.semantic.Symbol;
import org.sonar.plugins.java.api.tree.BaseTreeVisitor;
import org.sonar.plugins.java.api.tree.MemberSelectExpressionTree;
import org.sonar.plugins.java.api.tree.MethodInvocationTree;
import org.sonar.plugins.java.api.tree.Tree.Kind;

/**
 * <p/>
 * <p/>
 * 功能描述
 * <p/>
 * 规则：从集合或者map取值后 要做非空判断 如 list.get(0).toString() 存在空指针风险
 * <p/>
 * 
 * @author zhangzhengp
 *
 */
@Rule(key = "CollectionAndMapNullValue")
public class CollectionAndMapNullValueRule extends BaseTreeVisitor implements JavaFileScanner {
	private JavaFileScannerContext context;

	@Override
	public void scanFile(JavaFileScannerContext context) {
		this.context = context;
		scan(context.getTree());
	}

	@Override
	public void visitMethodInvocation(MethodInvocationTree tree) {
		Symbol symbol = tree.symbol();
		if (symbol.isMethodSymbol()) {
			String methodName = symbol.name();
			if ("toString".equals(methodName) || "equals".equals(methodName)) {
				if (tree.methodSelect().is(Kind.MEMBER_SELECT)) {
					MemberSelectExpressionTree expressionTree = (MemberSelectExpressionTree) tree.methodSelect();
					if (expressionTree.expression().is(Kind.METHOD_INVOCATION)) {
						MethodInvocationTree beforeTree = (MethodInvocationTree) expressionTree.expression();
						Symbol beforeSymbol = beforeTree.symbol();
						if (beforeSymbol.isMethodSymbol()) {
							String beforeMethodName = beforeSymbol.name();
							String beforeOwner = beforeSymbol.owner().name().toLowerCase();
							if ("get".equals(beforeMethodName)
									&& (beforeOwner.endsWith("map") || beforeOwner.endsWith("list"))) {
								context.reportIssue(this, tree, "空指针隐患");
							}
						}
					}
				}

			}
		}
	}
}
