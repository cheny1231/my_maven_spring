package yangc.test;

import yangc.context.annotation.Component;

@Component
public class User {
	private String name;
	private String password;
	private int age;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public void init() {
		System.out.println("Initing user...");
	}

	public void destroy() {
		System.out.println("Destroying user...");
	}

	public String sayHello() {
		System.out.println("Hello");
		return "Hello!";
	}
}
