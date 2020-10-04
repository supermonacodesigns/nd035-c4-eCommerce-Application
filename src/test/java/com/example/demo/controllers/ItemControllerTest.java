package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;

    private ItemRepository itemRepo = mock(ItemRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController();

        TestUtils.injectObjects(itemController, "itemRepository",itemRepo);

        Item item = new Item();
        item.setId(1L);
        item.setName("Round Widget");
        item.setDescription("A widget that is round");
        item.setPrice(BigDecimal.valueOf(2.99));

        when(itemRepo.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepo.findByName("Round Widget")).thenReturn(Collections.singletonList(item));
        when(itemRepo.findAll()).thenReturn(Collections.singletonList(item));
    }

    @Test
    public void getItemByIdHappyPath() throws Exception {

        final ResponseEntity<Item> response = itemController.getItemById(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Item item = response.getBody();
        assertEquals("Round Widget",item.getName());
    }

    @Test
    public void getItemByIdNotFound() throws Exception {

        final ResponseEntity<Item> response = itemController.getItemById(12L);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void getItemsHappyPath() throws Exception {

        final ResponseEntity<List<Item>> response = itemController.getItems();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> allItems = response.getBody();
        assertEquals(1, allItems.size());
    }

    @Test
    public void getItemsByNameHappyPath() throws Exception {

        final ResponseEntity<List<Item>> response = itemController.getItemsByName("Round Widget");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Item firstItem = response.getBody().get(0);
        assertEquals("Round Widget", firstItem.getName());
    }

    @Test
    public void getItemsByNameNotFound() throws Exception {

        final ResponseEntity<List<Item>> response = itemController.getItemsByName("Square Widget");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

}
