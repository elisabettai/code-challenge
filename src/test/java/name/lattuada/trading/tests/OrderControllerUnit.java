package name.lattuada.trading.tests;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

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
        // Set up mock behavior for orderRepository.findAll()
        List<OrderEntity> orders = new ArrayList<>();
        // Add some orders to the list (or leave it empty)
        // For this test, let's assume there are no orders available
        // orders.add(new OrderEntity());
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
}
