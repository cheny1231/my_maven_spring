package yangc.test;

import yangc.context.annotation.Autowired;
import yangc.mvc.annotation.Controller;
import yangc.mvc.annotation.RequestMapping;

@Controller
public class UserController {
	@Autowired
	private UserInterface user;

	@RequestMapping(path = "/user")
	public String get() {
//		user.setAge(12);
//		user.setName("abc");
//		System.out.println("name: " + user.getName() + ", age: " + user.getAge());
		System.out.println("hello!");
		return user.sayHello();
	}
}
