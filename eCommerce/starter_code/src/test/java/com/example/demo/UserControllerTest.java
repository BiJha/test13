package com.example.demo;

import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void createUser() throws Exception {
        when(encoder.encode("userPassword")).thenReturn("Hashed");
        CreateUserRequest req = new CreateUserRequest();
        req.setUsername("user");
        req.setPassword("userPassword");
        req.setConfirmPassword("userPassword");

        final ResponseEntity<User> response = userController.createUser(req);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();

        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("user", user.getUsername());
        assertEquals("Hashed", user.getPassword());

    }

    @Test
    public void findByUsername() {
        String username = "user";

        Cart cart = new Cart();

        User user = new User();
        user.setId(0);
        user.setUsername(username);
        user.setPassword("Hashed");
        user.setCart(cart);

        when(userRepository.findByUsername("user")).thenReturn(user);

        ResponseEntity<User> response = userController.findByUserName(username);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        user = response.getBody();
        Assert.assertEquals("user", user.getUsername());
        assertEquals(0, user.getId());
    }

    @Test
    public void passwordNotmatch() {
        when(encoder.encode("userPassword")).thenReturn("Hashed");

        CreateUserRequest req = new CreateUserRequest();
        req.setUsername("user2");
        req.setPassword("userPassword");
        req.setConfirmPassword("UserPassworD");

        final ResponseEntity<User> response = userController.createUser(req);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }


}
