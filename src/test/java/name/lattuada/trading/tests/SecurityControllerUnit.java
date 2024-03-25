package name.lattuada.trading.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import name.lattuada.trading.controller.SecurityController;
import name.lattuada.trading.model.dto.SecurityDTO;
import name.lattuada.trading.model.entities.SecurityEntity;
import name.lattuada.trading.repository.ISecurityRepository;

import java.util.Optional;
import java.util.UUID;

@RunWith(MockitoJUnitRunner.class)
public class SecurityControllerUnit {

        @Mock
    private ISecurityRepository securityRepository;

    @InjectMocks
    private SecurityController securityController;

    private SecurityEntity mockSecurity;

    @Test
    public void testGetSecurityByIdSuccess() {
        // Create a mock SecurityEntity
        mockSecurity = new SecurityEntity();
        mockSecurity.setId(UUID.randomUUID());
        mockSecurity.setName("Mock Security");

        // Set up mock behavior for security ID found
        when(securityRepository.findById(mockSecurity.getId())).thenReturn(Optional.of(mockSecurity));

        // Call the getSecurityById() method with the ID of the mock security
        ResponseEntity<SecurityDTO> responseEntity = securityController.getSecurityById(mockSecurity.getId());

        // Verify that the response status code is 200 (OK)
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        // Verify that the response body contains the correct security
        assertEquals(mockSecurity.getId(), responseEntity.getBody().getId());
    }

    @Test
    public void testGetSecurityByIdNotFound() {
        // Set up mock behavior for security is not found
        when(securityRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // Call the getSecurityById() method with a random ID (not existing)
        ResponseEntity<SecurityDTO> responseEntity = securityController.getSecurityById(UUID.randomUUID());

        // Verify that the response status code is 404 (NOT_FOUND)
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testAddSecuritySuccess() {
        // Create a mock SecurityDTO
        SecurityDTO mockSecurityDTO = new SecurityDTO();
        mockSecurityDTO.setId(UUID.randomUUID());
        mockSecurityDTO.setName("Test Security");

        // Mock behavior for the securityRepository.save() method
        SecurityEntity mockCreatedSecurityEntity = new SecurityEntity();
        mockCreatedSecurityEntity.setId(mockSecurityDTO.getId());
        mockCreatedSecurityEntity.setName(mockSecurityDTO.getName());
        when(securityRepository.save(any(SecurityEntity.class))).thenReturn(mockCreatedSecurityEntity);

        // Call the addSecurity() method with the mock SecurityDTO
        ResponseEntity<SecurityDTO> responseEntity = securityController.addSecurity(mockSecurityDTO);

        // Verify that the response status code is 201 (CREATED)
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        
        // Verify that the response body contains the correct SecurityDTO
        SecurityDTO responseBody = responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(mockSecurityDTO.getId(), responseBody.getId());
        assertEquals(mockSecurityDTO.getName(), responseBody.getName());
    
    }

}