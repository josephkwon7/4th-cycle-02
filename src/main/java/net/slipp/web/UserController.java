package net.slipp.web;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.slipp.domain.User;
import net.slipp.domain.UserRepository;

@Controller
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/loginForm")
	public String loginForm() {
		return "/user/login";
	}
	
	@PostMapping("/login") 
	public String login(String userId, String password, HttpSession session) {
		User user = userRepository.findByUserId(userId);
		if (user == null) {
			System.out.println("Login Failure!");
			return "redirect:/users/loginForm";
		}
		if (!password.equals(user.getPassword())) {
			System.out.println("Login Failure!");
			return "redirect:/users/loginForm";
		}
		
		session.setAttribute("sessionedUser", user);
		System.out.println("Login Success!");
		
		return "redirect:/";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("sessionedUser");
		System.out.println("Logged out!");
		return "redirect:/";
	}
	
	@GetMapping("/form")
	public String form() {
		return "/user/form";
	}
	
	@PostMapping("")
	public String create(User user) {
		System.out.println("User : " + user);
		userRepository.save(user);
		return "redirect:/users";
	}
	
	@GetMapping("")
	public String list(Model model) {
		model.addAttribute("users", userRepository.findAll());
		return "/user/list";
	}

	@GetMapping("/{id}/form")
	public String updateForm(@PathVariable Long id, Model model, HttpSession session) {
		Object tmpUser = session.getAttribute("sessionedUser");
		if (tmpUser == null) {
			System.out.println("로그인한 사용자만 수정할 수 있습니다.");
			return "redirect:/users/loginForm";
		}
		User sessionedUser = (User)tmpUser;
		
		if (!id.equals(sessionedUser.getId())) {
			throw new IllegalStateException("You can not update the another user.");
		}
		
		User user = userRepository.getOne(id);
		model.addAttribute("user", user);
		return "/user/updateForm";
	}
	
	@PutMapping("/{id}")
	public String update(@PathVariable Long id, User updatedUser, HttpSession session) {
		Object tmpUser = session.getAttribute("sessionedUser");
		if (tmpUser == null) {
			System.out.println("로그인한 사용자만 수정할 수 있습니다.");
			return "redirect:/users/loginForm";
		}
		User sessionedUser = (User)tmpUser;
		
		if (!id.equals(sessionedUser.getId())) {
			throw new IllegalStateException("You can not update the another user.");
		}
		
		User user = userRepository.getOne(id);
		user.update(updatedUser);
		userRepository.save(user);	
		return "redirect:/users";
	}
	
}
