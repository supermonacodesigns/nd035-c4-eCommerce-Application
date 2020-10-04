package com.example.demo.controllers;

import com.example.demo.TestUtils;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;

    private OrderRepository orderRepo = mock(OrderRepository.class);
    private UserRepository userRepo = mock(UserRepository.class);

    @Before
    public void setUp() {

        orderController = new OrderController();

        TestUtils.injectObjects(orderController, "orderRepository", orderRepo);
        TestUtils.injectObjects(orderController, "userRepository", userRepo);

        User user = new User();
        user.setUsername("Dave");
        user.setId(1);
        user.setPassword("password");

        Item item = new Item();
        item.setId(2L);
        item.setName("Square Widget");
        item.setDescription("A widget that is square");
        item.setPrice(BigDecimal.valueOf(1.99));
        List<Item> items = new ArrayList<>();
        items.add(item);

        UserOrder userOrder = new UserOrder();
        userOrder.setUser(user);
        userOrder.setId(1L);

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        cart.setItems(items);
        cart.setTotal(item.getPrice());

        user.setCart(cart);

        when(userRepo.findByUsername("Dave")).thenReturn(user);

    }

    @Test
    public void submitOrderHappyPath() throws Exception {

        final ResponseEntity<UserOrder> response = orderController.submit("Dave");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void submitOrderUserNotFound() throws Exception {

        final ResponseEntity<UserOrder> response = orderController.submit("Spart");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void getOrdersForUserHappyPath() throws Exception {

        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("Dave");

        assertNotNull(response);
        assertEquals(200,response.getStatusCodeValue());
    }

    @Test
    public void getOrdersForUserNotFound() throws Exception {

        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("Spart");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
}
