package name.lattuada.trading.tests;
 
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import name.lattuada.trading.controller.UserController;
import name.lattuada.trading.model.dto.UserDTO;
import name.lattuada.trading.model.entities.UserEntity;
import name.lattuada.trading.repository.IUserRepository;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerUnit {

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private UserController userController;

    private UserDTO validUser;
    private UserDTO invalidUser;

    @Before
    public void setUp() {
        // Mock a valid user
        validUser = new UserDTO();
        validUser.setUsername("validUser");
        validUser.setPassword("validPassword");

        // Moch an invalid user (username is missing)
        invalidUser = new UserDTO();
        invalidUser.setPassword("invalidPassword");
    }

    @Test
    public void testAddUserWithValidInput() {
        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> {
            UserEntity userEntity = invocation.getArgument(0);
            userEntity.setId(UUID.randomUUID()); // UUID is automatically generated in the request
            return userEntity;
        });

        String hashedPassword = DigestUtils.sha256Hex(validUser.getPassword());

        ResponseEntity<UserDTO> responseEntity = userController.addUser(validUser);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(hashedPassword, responseEntity.getBody().getPassword());
    }

    @Test
    public void testAddUserWithInvalidInput() {
        ResponseEntity<UserDTO> responseEntity = userController.addUser(invalidUser);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }
}