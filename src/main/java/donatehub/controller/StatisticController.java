package donatehub.controller;

import donatehub.service.donation.DonationService;
import donatehub.service.user.UserService;
import donatehub.service.withdraw.WithdrawService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/statistic")
@RequiredArgsConstructor
public class StatisticController {
    private final UserService userService;
    private final DonationService donationService;
    private final WithdrawService withdrawService;


}
