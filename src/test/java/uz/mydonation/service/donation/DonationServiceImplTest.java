package uz.mydonation.service.donation;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import uz.mydonation.repo.UserRepository;
import uz.mydonation.service.user.UserService;

import static org.junit.jupiter.api.Assertions.*;

class DonationServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

//    @Mock
//    private DonationServiceImpl donationService = new DonationServiceImpl(userRepository, userService, );

    @Test
    void donate() {

    }
}