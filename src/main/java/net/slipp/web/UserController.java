package net.slipp.web;

import java.util.ArrayList;
import java.util.List;

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
	private List<User> users = new ArrayList<User>();
	
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
			System.out.println("Login Failue!");
			return "redirect:/users/loginForm";
		}
		
		if (!user.matchPassword(password)) {
			System.out.println("login Failue!");
			return "redirect:/users/loginForm";
		}
		
		session.setAttribute("user", user);
		
		return "redirect:/";
	}
	
	@GetMapping("/form")
	public String form() {
		return "/user/form";
	}
	
	@PostMapping("")
	public String create(User user) {
		System.out.println(user.toString());
		userRepository.save(user);
		
		return "redirect:/users";
	}
	
	@GetMapping("")
	public String list(Model model) {
		model.addAttribute("users", userRepository.findAll());
		
		return "/user/list";
	}
	
	@GetMapping("/{id}/form")
	public String updateForm(@PathVariable Long id, Model model) {
		model.addAttribute("user", userRepository.findById(id).get());
		return "/user/updateForm";
	}

	@PutMapping("/{id}")
	public String update(@PathVariable Long id, User newUser) {
		User user = userRepository.findById(id).get();
		user.update(newUser);
		userRepository.save(user);
		return "redirect:/users";
	}
}
