package com.paymybuddy.paymybuddy.service;

import com.paymybuddy.paymybuddy.dto.UserRegistrationDTO;
import com.paymybuddy.paymybuddy.exception.DataAlreadyRegisteredException;
import com.paymybuddy.paymybuddy.exception.ResourceNotFoundException;
import com.paymybuddy.paymybuddy.model.BuddyAccount;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserService implements IUserService {

    private UserRepository userRepository;

    private IBuddyAccountService buddyAccountService;

    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(final UserRepository userRepository, final IBuddyAccountService buddyAccountService,
                       final BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.buddyAccountService = buddyAccountService;
        this.passwordEncoder = passwordEncoder;
    }


    public UserRegistrationDTO registerUser(UserRegistrationDTO userDTO) {
        User userFound = userRepository.findByEmail(userDTO.getEmail());

        if (userFound != null) {
            throw new DataAlreadyRegisteredException("User is already registered");
        }

        User user = new User(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail(),
                passwordEncoder.encode(userDTO.getPassword()), userDTO.getPhone());

        User userSaved = userRepository.save(user);

        BuddyAccount buddyAccount = new BuddyAccount(userSaved, BigDecimal.ZERO);
        buddyAccountService.saveBuddyAccount(buddyAccount);

        return new UserRegistrationDTO(userSaved.getFirstName(), userSaved.getLastName(), userSaved.getEmail(),
                userSaved.getPassword(), userSaved.getPhone());
    }

    public void login(String userName, String password) {
        UserDetails user = loadUserByUsername(userName);

        boolean passwordsMatch = passwordEncoder.matches(password, user.getPassword());

        if (!passwordsMatch) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    public User getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("User not registered");
        }

        return user;
    }

    public void addConnection(String myEmail, String buddyEmail) {
        User owner = userRepository.findByEmail(myEmail);
        User buddy = userRepository.findByEmail(buddyEmail);

        if (buddy == null) {
            throw new UsernameNotFoundException("No buddy registered with this email");
        }
        owner.getContacts().add(buddy);

        userRepository.save(owner);
    }

    public User getConnection(String myEmail, String buddyEmail) {
        User owner = userRepository.findByEmail(myEmail);
        Collection<User> contacts = owner.getContacts();

        for (User contact : contacts) {
            if (contact.getEmail().equals(buddyEmail)) {
                return contact;
            }
        }

        throw new ResourceNotFoundException("Buddy not found in contacts. Please checks entered email or add this user to contacts");
    }

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email);
        if(user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("USER"));
        grantedAuthorities.add(new SimpleGrantedAuthority("ADMIN"));

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                grantedAuthorities);
    }
}
