package name.lattuada.trading.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
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

        // Set up mock behavior for case: security ID found
        when(securityRepository.findById(mockSecurity.getId())).thenReturn(Optional.of(mockSecurity));

        ResponseEntity<SecurityDTO> responseEntity = securityController.getSecurityById(mockSecurity.getId());

        // Check that the status code is 200 (OK)
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        // Checkt that correct security is part of the response body
        assertEquals(mockSecurity.getId(), responseEntity.getBody().getId());
    }

    @Test
    public void testGetSecurityByIdNotFound() {
        // Mock behavior for security is not found
        when(securityRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // Call getSecurityById() with a random ID (not existing)
        ResponseEntity<SecurityDTO> responseEntity = securityController.getSecurityById(UUID.randomUUID());

        // Check that the status code is 404 (NOT_FOUND)
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testAddSecuritySuccess() {
        // Create a mock SecurityDTO
        SecurityDTO mockSecurityDTO = new SecurityDTO();
        mockSecurityDTO.setId(UUID.randomUUID());
        mockSecurityDTO.setName("Test Security");

        // Mock the securityRepository.save() method
        SecurityEntity mockCreatedSecurityEntity = new SecurityEntity();
        mockCreatedSecurityEntity.setId(mockSecurityDTO.getId());
        mockCreatedSecurityEntity.setName(mockSecurityDTO.getName());
        when(securityRepository.save(any(SecurityEntity.class))).thenReturn(mockCreatedSecurityEntity);

        ResponseEntity<SecurityDTO> responseEntity = securityController.addSecurity(mockSecurityDTO);

        // Check that the response status code is 201 (CREATED)
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        
        // Check that the correct SecurityDTO (by ID and name) is part of the response body
        SecurityDTO responseBody = responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(mockSecurityDTO.getId(), responseBody.getId());
        assertEquals(mockSecurityDTO.getName(), responseBody.getName());
    
    }

}