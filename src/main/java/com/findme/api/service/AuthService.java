package com.findme.api.service;

import com.findme.api.exception.CustomUnauthorizedException;
import com.findme.api.mapper.UserMapper;
import com.findme.api.model.AuthRequest;
import com.findme.api.model.AuthResponse;
import com.findme.api.model.Role;
import com.findme.api.model.User;
import com.findme.api.model.dto.UserDTO;
import com.findme.api.repository.UserRepository;
import com.mailgun.api.v3.MailgunMessagesApi;
import com.mailgun.client.MailgunClient;
import com.mailgun.model.message.Message;
import com.mailgun.model.message.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthService {
	UserRepository userRepository;

	EmailTemplates emailTemplates = new EmailTemplates();
	
	UserMapper userMapper = new UserMapper();

	@Autowired
	private Environment environment;
	
	UserService userService;
	
	BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
	
	@Autowired
	JwtService jwtService;
	
	@Autowired
	public AuthService(UserRepository userRepository) {
		this.userRepository = userRepository;
		this.userService = new UserService(userRepository);
	}
	
	public User register(AuthRequest authRequest) throws CustomUnauthorizedException {
		if (userRepository.existsByUsername(authRequest.getUsername())) {
			throw new CustomUnauthorizedException("Username already exists");
		} else if (userRepository.existsByEmail(authRequest.getEmail())) {
			throw new CustomUnauthorizedException("Email already exists");
		}
		UserDTO userDTO = new UserDTO();
		userDTO.setUsername(authRequest.getUsername());
		userDTO.setEmail(authRequest.getEmail());
		userDTO.setPassword(bCryptPasswordEncoder.encode(authRequest.getPassword()));
		
		return userService.createUser(userDTO);
	}

	public MessageResponse sendConfMail(User user) {
		MailgunMessagesApi mailgunMessagesApi = MailgunClient.config(environment.getProperty("mailgun.api.key"))
				.createApi(MailgunMessagesApi.class);
		Message message = emailTemplates.mailConfMessage(user, environment);
		return mailgunMessagesApi.sendMessage(environment.getProperty("mailgun.domain"), message);
	}

	public User generateNewCode(String email) throws Exception {
		int leftLimit;
		int rightLimit;
		double nb;
		int targetStringLength = 26;
		Random random = new Random();
		StringBuilder buffer = new StringBuilder(targetStringLength);
		for (int i = 0; i < targetStringLength; i++) {
			nb = Math.random();
			if (nb<0.4) {
				leftLimit = 97; // letter 'a'
				rightLimit = 122; // letter 'z'
			} else if (nb<0.6) {
				leftLimit = 65; // letter 'A'
				rightLimit = 90; // letter 'Z'
			} else {
				leftLimit = 48; // letter '0'
				rightLimit = 57; // letter '9'
			}
			int randomLimitedInt = leftLimit + (int)
					(random.nextFloat() * (rightLimit - leftLimit + 1));
			buffer.append((char) randomLimitedInt);
		}
		String confirmationCode = buffer.toString();
		List<User> userList = userRepository.findAllByEmail(email);
		if(userList.get(0) == null) {
			throw new Exception("No user");
		}

		User user = userList.get(0);
		if(!user.getRoles().contains(Role.UNVERIFIED)) {
			throw new Exception("Already verified");
		}
		user.setConfirmationCode(confirmationCode);
		userRepository.save(user);

		sendConfMail(user);
		return user;
	}

	public boolean checkMail(String id, String code) throws Exception {
		boolean check;
		Optional<User> user = userRepository.findById(id);
		if(user.isEmpty()) {
			throw new Exception("No user");
		}
		if(!user.get().getRoles().contains(Role.UNVERIFIED)) {
			throw new Exception("Already verified");
		}
		System.out.println(user.get());
		System.out.println(user.get().getConfirmationCode());
		System.out.println(code);
        check = Objects.equals(code, user.get().getConfirmationCode());

		List<Role> roles=new ArrayList<Role>();
		for (Role entry: user.get().getRoles())
		{
			// filter values that start with `B`
			if (!entry.equals(Role.UNVERIFIED)) {
				roles.add(entry);
			}
		}
		if(check) {
			user.get().setRoles(roles);
		}
		userRepository.save(user.get());
		return check;
	}
	
	public AuthResponse authResponse(User user, String username) {
		user.setRefreshToken(jwtService.createRefreshToken());
		userRepository.save(user);
		AuthResponse authResponse = new AuthResponse();
		authResponse.setJwtToken(jwtService.generateToken(username));
		authResponse.setRefreshToken(user.getRefreshToken());
		return authResponse;
	}
}
