package name.lattuada.trading.tests;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import name.lattuada.trading.controller.OrderController;
import name.lattuada.trading.model.dto.OrderDTO;
import name.lattuada.trading.model.entities.OrderEntity;
import name.lattuada.trading.repository.IOrderRepository;

@RunWith(MockitoJUnitRunner.class)
public class OrderControllerUnit {

    @Mock
    private IOrderRepository orderRepository;

    @InjectMocks
    private OrderController orderController;

    @Before
    public void setUp() {
        // Set up mock behavior for orderRepository
        List<OrderEntity> orders = new ArrayList<>();;
        when(orderRepository.findAll()).thenReturn(orders);
    }

    @Test
    public void testGetOrdersNoContent() {
        // Call the getOrders() method
        ResponseEntity<List<OrderDTO>> responseEntity = orderController.getOrders();

        // Verify that the response status code is 204 (NO_CONTENT)
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    public void testGetOrdersServerError() {
        // Set up mock behavior to throw an exception when orderRepository.findAll() is called
        when(orderRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        // Call the getOrders() method
        ResponseEntity<List<OrderDTO>> responseEntity = orderController.getOrders();

        // Verify that the response status code is 500 (INTERNAL_SERVER_ERROR)
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals(null, responseEntity.getBody());
    }

    @Test
    public void testAddOrderNonExistingUser() {
        // Create a mock order DTO with a non-existing user ID
        OrderDTO mockOrderDTO = new OrderDTO();
        mockOrderDTO.setUserId(UUID.randomUUID()); // Assuming this ID doesn't exist

        // Set up mock behavior to throw an exception when trying to save the order
        when(orderRepository.save(any(OrderEntity.class)))
                .thenThrow(new RuntimeException("User not found")); // Or any appropriate exception

        // Call the addOrder() method with the mock order DTO
        ResponseEntity<OrderDTO> responseEntity = orderController.addOrder(mockOrderDTO);

        // Verify that the response status code is 500 (INTERNAL_SERVER_ERROR)
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals(null, responseEntity.getBody());
    }
}
