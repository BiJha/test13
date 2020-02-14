package com.example.demo;

import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {
    private CartController cartController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp()
    {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void addToCart()
    {
        ModifyCartRequest req = new ModifyCartRequest();
        req.setItemId(0);
        req.setUsername("user");
        req.setQuantity(2);

        User user = new User();
        user.setId(0);
        user.setUsername("user");
        user.setPassword("userPassword");

        Cart cart = new Cart();

        user.setCart(cart);

        Item item = new Item();
        item.setId(0L);
        item.setName("Round Widget");
        item.setDescription("A widget that is round");
        item.setPrice(BigDecimal.valueOf(2.99));

        Optional<Item> itemOpt = Optional.of(item);

        when(userRepository.findByUsername("user")).thenReturn(user);

        when(itemRepository.findById(0L)).thenReturn(itemOpt);

        ResponseEntity<Cart> response = cartController.addTocart(req);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().getItems().size());
    }

    @Test
    public void addToCartItemNotFound()
    {
        ModifyCartRequest req = new ModifyCartRequest();
        req.setItemId(0);
        req.setUsername("user");
        req.setQuantity(2);

        User user = new User();
        user.setId(0);
        user.setUsername("user");
        user.setPassword("userPassword");

        Cart cart = new Cart();

        user.setCart(cart);

        when(userRepository.findByUsername("user")).thenReturn(user);

        ResponseEntity<Cart> response = cartController.addTocart(req);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void addToCartUserNotFound()
    {
        ModifyCartRequest req = new ModifyCartRequest();
        req.setItemId(0);
        req.setUsername("user");
        req.setQuantity(2);

        User user  = null;

        Item item = new Item();
        item.setId(0L);
        item.setName("Round Widget");
        item.setDescription("A widget that is round");
        item.setPrice(BigDecimal.valueOf(2.99));

        Optional<Item> itemOpt = Optional.of(item);

        when(userRepository.findByUsername("user")).thenReturn(user);

        when(itemRepository.findById(0L)).thenReturn(itemOpt);

        ResponseEntity<Cart> response = cartController.addTocart(req);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void removeFromCart()
    {
        ModifyCartRequest req = new ModifyCartRequest();
        req.setItemId(0);
        req.setUsername("user");
        req.setQuantity(2);

        User user = new User();
        user.setId(0);
        user.setUsername("user");
        user.setPassword("userPassword");

        Cart cart = new Cart();
        cart.setId(0L);
        cart.setUser(user);
        List<Item> items = cart.getItems();
        if(items == null)
        {
            items = new ArrayList<Item>();
        }

        user.setCart(cart);

        Item item = new Item();
        item.setId(0L);
        item.setName("Round Widget");
        item.setDescription("A widget that is round");
        item.setPrice(BigDecimal.valueOf(2.99));

        items.add(item);
        items.add(item);
        items.add(item);
        items.add(item);
        items.add(item);
        cart.setItems(items);
        cart.setTotal(BigDecimal.valueOf(2.99*5));

        Optional<Item> itemOpt = Optional.of(item);

        when(userRepository.findByUsername("user")).thenReturn(user);

        when(itemRepository.findById(0L)).thenReturn(itemOpt);

        ResponseEntity<Cart> response = cartController.removeFromcart(req);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(3, response.getBody().getItems().size());
    }

    @Test
    public void removeFromCartItemNotFound()
    {
        ModifyCartRequest req = new ModifyCartRequest();
        req.setItemId(0);
        req.setUsername("user");
        req.setQuantity(2);

        User user = new User();
        user.setId(0);
        user.setUsername("user");
        user.setPassword("userPassword");

        Cart cart = new Cart();
        cart.setId(0L);
        cart.setUser(user);
        List<Item> items = cart.getItems();
        if(items == null)
        {
            items = new ArrayList<Item>();
        }

        user.setCart(cart);

        Item item = null;

        items.add(item);
        items.add(item);
        items.add(item);
        items.add(item);
        items.add(item);
        cart.setItems(items);
        cart.setTotal(BigDecimal.valueOf(2.99*5));

        Optional<Item> itemOpt = Optional.ofNullable(item);

        when(userRepository.findByUsername("user")).thenReturn(user);

        when(itemRepository.findById(0L)).thenReturn(itemOpt);

        ResponseEntity<Cart> response = cartController.removeFromcart(req);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void removeFromCartUserNotFound()
    {
        ModifyCartRequest req = new ModifyCartRequest();
        req.setItemId(0);
        req.setUsername("user");
        req.setQuantity(2);

        User user = null;

        Item item = new Item();
        item.setId(0L);
        item.setName("Round Widget");
        item.setDescription("A widget that is round");
        item.setPrice(BigDecimal.valueOf(2.99));

        Optional<Item> itemOpt = Optional.of(item);

        when(userRepository.findByUsername("user")).thenReturn(user);

        when(itemRepository.findById(0L)).thenReturn(itemOpt);

        ResponseEntity<Cart> response = cartController.removeFromcart(req);

        assertEquals(404, response.getStatusCodeValue());
    }
}
