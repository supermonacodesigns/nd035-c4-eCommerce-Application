package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepo = mock(UserRepository.class);
    private CartRepository cartRepo = mock(CartRepository.class);
    private BCryptPasswordEncoder bCryptEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {

        userController = new UserController();

        TestUtils.injectObjects(userController, "userRepository", userRepo);
        TestUtils.injectObjects(userController, "cartRepository", cartRepo);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptEncoder);

    }

    @Test
    public void createUserHappyPath() throws Exception {

        // example of stubbing
        when(bCryptEncoder.encode("testPassword")).thenReturn("thisIsHashed");

        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");

        final ResponseEntity<User> response = userController.createUser(r);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("test", u.getUsername());
        assertEquals("thisIsHashed", u.getPassword());

    }

    @Test
    public void createUserSadPathPasswordTooShort() throws Exception {

        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("pass");
        r.setPassword("pass");

        final ResponseEntity<User> response = userController.createUser(r);

        assertNotNull(response);
        assertEquals(400,response.getStatusCodeValue());

    }

    @Test
    public void createUserSadPathPasswordTypo() throws Exception {

        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("password");
        r.setConfirmPassword("password123");

        final ResponseEntity<User> response = userController.createUser(r);

        assertNotNull(response);
        assertEquals(400,response.getStatusCodeValue());
    }

    @Test
    public void findUserByUsername() throws Exception {

        User user = new User();
        Cart cart = new Cart();

        user.setId(0);
        user.setUsername("test");
        user.setPassword("password");
        user.setCart(cart);

        when(userRepo.findByUsername("test")).thenReturn(user);
        when(userRepo.findById(0L)).thenReturn(Optional.of(user));

        final ResponseEntity<User> response = userController.findByUserName("test");
        assertNotNull(response);
        assertEquals(200,response.getStatusCodeValue());

        User u = response.getBody();

        assertEquals("test",u.getUsername());
    }

    @Test
    public void findUserByUsernameNotFound() throws Exception {

        User user = new User();
        Cart cart = new Cart();

        user.setId(0);
        user.setUsername("test");
        user.setPassword("password");
        user.setCart(cart);

        when(userRepo.findByUsername("test")).thenReturn(user);
        when(userRepo.findById(0L)).thenReturn(Optional.of(user));

        final ResponseEntity<User> response = userController.findByUserName("Dave");
        assertNotNull(response);
        assertEquals(404,response.getStatusCodeValue());
    }


    @Test
    public void findUserById() {

        User user = new User();
        Cart cart = new Cart();

        user.setId(0);
        user.setUsername("test");
        user.setPassword("password");
        user.setCart(cart);

        when(userRepo.findByUsername("test")).thenReturn(user);
        when(userRepo.findById(0L)).thenReturn(Optional.of(user));

        final ResponseEntity<User> response = userController.findById(0L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();

        assertEquals(0,u.getId());
    }


}
