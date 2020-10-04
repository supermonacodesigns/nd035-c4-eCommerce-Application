package com.example.demo.controllers;

import com.example.demo.TestUtils;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;

    private UserRepository userRepo = mock(UserRepository.class);
    private CartRepository cartRepo = mock(CartRepository.class);
    private ItemRepository itemRepo = mock(ItemRepository.class);

    @Before
    public void setUp() {

        cartController = new CartController();

        TestUtils.injectObjects(cartController, "userRepository", userRepo);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepo);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepo);

        Cart cart = new Cart();

        User user = new User();
        user.setId(1L);
        user.setUsername("Dave");
        user.setPassword("Password");
        user.setCart(cart);

        Item item = new Item();
        item.setId(1L);
        item.setName("Round widget");
        item.setDescription("A widget that is round");
        item.setPrice(BigDecimal.valueOf(2.99));

        when(userRepo.findByUsername("Dave")).thenReturn(user);
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));

        when(itemRepo.findById(1L)).thenReturn(Optional.of(item));
    }

    @Test
    public void addToCartHappyPath() throws Exception {

        ModifyCartRequest m = new ModifyCartRequest();
        m.setItemId(1L);
        m.setUsername("Dave");
        m.setQuantity(1);

        final ResponseEntity<Cart> response = cartController.addTocart(m);

        assertNotNull(response);
        assertEquals(200,response.getStatusCodeValue());
    }

    @Test
    public void addToCartUserNotFound() throws Exception {

        ModifyCartRequest m = new ModifyCartRequest();
        m.setItemId(1L);
        m.setUsername("Spart");
        m.setQuantity(1);

        final ResponseEntity<Cart> response = cartController.addTocart(m);

        assertNotNull(response);
        assertEquals(404,response.getStatusCodeValue());
    }

    @Test
    public void addToCartItemNotFound() throws Exception {

        ModifyCartRequest m = new ModifyCartRequest();
        m.setItemId(12L);
        m.setUsername("Dave");
        m.setQuantity(1);

        final ResponseEntity<Cart> response = cartController.addTocart(m);

        assertNotNull(response);
        assertEquals(404,response.getStatusCodeValue());
    }

    @Test
    public void removeFromCartHappyPath() throws Exception {

        ModifyCartRequest m = new ModifyCartRequest();
        m.setItemId(1L);
        m.setUsername("Dave");
        m.setQuantity(2);

        ResponseEntity<Cart> response = cartController.addTocart(m);
        assertNotNull(response);
        assertEquals(200,response.getStatusCodeValue());

        m = new ModifyCartRequest();
        m.setItemId(1L);
        m.setUsername("Dave");
        m.setQuantity(1);

        response = cartController.removeFromcart(m);

        assertNotNull(response);
        assertEquals(200,response.getStatusCodeValue());

        Cart c = response.getBody();
        assertEquals(BigDecimal.valueOf(2.99),c.getTotal());
    }

    @Test
    public void removeFromCartUserNotFound() throws Exception {

        ModifyCartRequest m = new ModifyCartRequest();
        m.setItemId(1L);
        m.setUsername("Dave");
        m.setQuantity(2);

        ResponseEntity<Cart> response = cartController.addTocart(m);
        assertNotNull(response);
        assertEquals(200,response.getStatusCodeValue());

        m = new ModifyCartRequest();
        m.setItemId(1L);
        m.setUsername("Spart");
        m.setQuantity(1);

        response = cartController.removeFromcart(m);

        assertNotNull(response);
        assertEquals(404,response.getStatusCodeValue());
    }

    @Test
    public void removeFromCartItemNotFound() throws Exception {

        ModifyCartRequest m = new ModifyCartRequest();
        m.setItemId(1L);
        m.setUsername("Dave");
        m.setQuantity(2);

        ResponseEntity<Cart> response = cartController.addTocart(m);
        assertNotNull(response);
        assertEquals(200,response.getStatusCodeValue());

        m = new ModifyCartRequest();
        m.setItemId(12L);
        m.setUsername("Dave");
        m.setQuantity(1);

        response = cartController.removeFromcart(m);

        assertNotNull(response);
        assertEquals(404,response.getStatusCodeValue());
    }


}
