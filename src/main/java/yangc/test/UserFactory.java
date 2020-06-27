package yangc.test;

public class UserFactory {
	public Object createMethod() {
		return new User();
	}

	public static Object staticCreateMethod() {
		return new User();
	}
}
