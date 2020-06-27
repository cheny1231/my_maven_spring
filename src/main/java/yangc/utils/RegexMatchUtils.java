package yangc.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexMatchUtils {
	public static String matchModifier(String expression) throws Exception {
		String[] split = validateExpression(expression);
		return split[0];
	}

	public static String matchClassName(String expression) throws Exception {
		return match(expression, "(.+?)(?=\\..+\\()");
	}

	public static String matchMethodParam(String expression) throws Exception {
		return match(expression, "(?<=\\()(.*?)(?=\\))");
	}

	public static String matchMethodName(String expression) throws Exception {
		return match(expression, "[^\\.]+?(?=\\()");
	}

	private static String[] validateExpression(String expression) throws Exception {
		Pattern compile = Pattern.compile("(?<=\\().+?\\)");
		Matcher matcher = compile.matcher(expression);
		if (!matcher.find()) {
			throw new Exception("Wrong expression");
		}
		String[] split = matcher.group().split(" ");
		if (split.length <= 1) {
			throw new Exception("Wrong expression");
		}
		return split;
	}

	private static String match(String expression, String s) throws Exception {
		String[] split = validateExpression(expression);
		String pathName = split[1];
		Pattern compile = Pattern.compile(s);
		Matcher matcher = compile.matcher(pathName);
		StringBuilder sb = new StringBuilder();
		while (matcher.find()) {
			sb.append(matcher.group());
		}
		return sb.toString();
	}

	public static void main(String[] args) throws Exception {
		String s = matchClassName("execution(* aop.pointcut.impl.RegexExpressionPointCutResolver.test(..))");
		System.out.println(s);
	}
}
