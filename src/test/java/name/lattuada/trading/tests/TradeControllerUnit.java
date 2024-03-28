package name.lattuada.trading.tests;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import name.lattuada.trading.controller.TradeController;
import name.lattuada.trading.model.dto.TradeDTO;
import name.lattuada.trading.model.entities.TradeEntity;
import name.lattuada.trading.repository.ITradeRepository;

@RunWith(MockitoJUnitRunner.class)
public class TradeControllerUnit {

    @Mock
    private ITradeRepository tradeRepository;

    @InjectMocks
    private TradeController tradeController;

    private List<TradeEntity> no_trades;
    private List<TradeEntity> two_trades = new ArrayList<>();

    @Before
    public void setUp() {
        // Set up no trades available scenario
        no_trades = new ArrayList<>();

        // Set up two valid trades
        two_trades = new ArrayList<>();
        TradeEntity trade1 = new TradeEntity();
        two_trades.add(trade1);
        TradeEntity trade2 = new TradeEntity();
        two_trades.add(trade2);
    }

    @Test
    public void testGetTradesNoContent() {
        // Mock tradeRepository given the empty list of trades
        when(tradeRepository.findAll()).thenReturn(no_trades);

        ResponseEntity<List<TradeDTO>> responseEntity = tradeController.getTrades();

        // Check that the status code is 204 (NO_CONTENT)
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    public void testGetTradeByIdNotFound() {
        // Mock tradeRepository.findById()
        when(tradeRepository.findById(any())).thenReturn(Optional.empty());

        // Call getTradeById() with a random UUID
        ResponseEntity<TradeDTO> responseEntity = tradeController.getTradeById(UUID.randomUUID());

        // Check that the status code is 404 (NOT_FOUND)
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testGetTradeByBuyAndSellOrderIdNotFound() {
        // Mock tradeRepository.findByOrderBuyIdAndOrderSellId()
        when(tradeRepository.findByOrderBuyIdAndOrderSellId(any(), any())).thenReturn(new ArrayList<>());

        // Call getTradeByBuyAndSellOrderId() method with random (non-existing) UUIDs
        ResponseEntity<TradeDTO> responseEntity = tradeController.getTradeByBuyAndSellOrderId(UUID.randomUUID(), UUID.randomUUID());

        // Check that the status code is 404 (NOT_FOUND)
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testGetTradesSuccess() {
        // Mock tradeRepository to give two_trades
        when(tradeRepository.findAll()).thenReturn(two_trades);

        ResponseEntity<List<TradeDTO>> responseEntity = tradeController.getTrades();

        // Check that status code is 200 (OK)
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        // Check that the body contains the expected number of trades
        assertEquals(2, responseEntity.getBody().size());
    }

    @Test
    public void getTradeByBuyAndSellOrderIdSuccess() {
        // Create a mock TradeEntity with a buy ID and a sell ID
        UUID orderBuyId = UUID.randomUUID();
        UUID orderSellId = UUID.randomUUID();
        UUID tradeId = UUID.randomUUID();

        TradeEntity tradeEntity = new TradeEntity();
        tradeEntity.setId(tradeId);
        
        when(tradeRepository.findByOrderBuyIdAndOrderSellId(orderBuyId, orderSellId)).thenReturn(List.of(tradeEntity));
        ResponseEntity<TradeDTO> responseEntity = tradeController.getTradeByBuyAndSellOrderId(orderBuyId, orderSellId);
      
        // Check that the status code is 200 (OK)
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        // Check that the body contains the correct trade (by ID)
        assertEquals(tradeId, responseEntity.getBody().getId());
    }

}

