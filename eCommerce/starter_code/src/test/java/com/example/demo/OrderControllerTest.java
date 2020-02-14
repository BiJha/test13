package com.example.demo;

import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    private OrderController orderController;

    private UserRepository userRepository = mock(UserRepository.class);

    private OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void setup() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void getOrdersForUser() {
        User user = new User();
        user.setId(0L);
        user.setUsername("user");
        user.setPassword("userPassword");

        Cart cart = new Cart();
        cart.setId(0L);
        cart.setUser(user);

        Item item = new Item();
        item.setId(0L);
        item.setName("Round Widget");
        item.setDescription("A widget that is round");
        item.setPrice(BigDecimal.valueOf(2.99));

        List<Item> items = Arrays.asList(item, item, item);
        cart.setItems(items);
        cart.setTotal(BigDecimal.valueOf(2.99 * 3));

        user.setCart(cart);

        when(userRepository.findByUsername("user")).thenReturn(user);

        UserOrder userOrder = UserOrder.createFromCart(cart);

        List<UserOrder> userOrders = Arrays.asList(userOrder);

        when(orderRepository.findByUser(user)).thenReturn(userOrders);

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("user");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(3, response.getBody().get(0).getItems().size());
        assertEquals("user", response.getBody().get(0).getUser().getUsername());
        assertEquals(cart.getTotal(), response.getBody().get(0).getTotal());

    }

    @Test
    public void getOrdersForUserNotFound() {
        User user = null;

        Cart cart = new Cart();
        cart.setId(0L);
        cart.setUser(user);

        Item item = new Item();
        item.setId(0L);
        item.setName("Round Widget");
        item.setDescription("A widget that is round");
        item.setPrice(BigDecimal.valueOf(2.99));

        List<Item> items = Arrays.asList(item, item, item);
        cart.setItems(items);
        cart.setTotal(BigDecimal.valueOf(2.99 * 3));

        when(userRepository.findByUsername("user")).thenReturn(user);

        UserOrder userOrder = UserOrder.createFromCart(cart);

        List<UserOrder> userOrders = Arrays.asList(userOrder);

        when(orderRepository.findByUser(user)).thenReturn(userOrders);

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("user");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

    }


    @Test
    public void submitOrder() {
        User user = new User();
        user.setId(0L);
        user.setUsername("user");
        user.setPassword("userPassword");

        Cart cart = new Cart();
        cart.setId(0L);
        cart.setUser(user);

        Item item = new Item();
        item.setId(0L);
        item.setName("Round Widget");
        item.setDescription("A widget that is round");
        item.setPrice(BigDecimal.valueOf(2.99));

        List<Item> items = Arrays.asList(item, item, item);
        cart.setItems(items);
        cart.setTotal(BigDecimal.valueOf(2.99 * 3));

        user.setCart(cart);

        when(userRepository.findByUsername("user")).thenReturn(user);

        ResponseEntity<UserOrder> response = orderController.submit("user");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(3, response.getBody().getItems().size());
        assertEquals("user", response.getBody().getUser().getUsername());
        assertEquals(cart.getTotal(), response.getBody().getTotal());

    }


    @Test
    public void submitOrderUserNotFound() {
        User user = null;

        when(userRepository.findByUsername("user")).thenReturn(user);

        ResponseEntity<UserOrder> response = orderController.submit("user");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

    }


}
