package uz.mydonation.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.mydonation.domain.model.FullStatisticRes;
import uz.mydonation.domain.model.PagedRes;
import uz.mydonation.domain.model.StatisticRes;
import uz.mydonation.domain.projection.DonationInfo;
import uz.mydonation.domain.request.DonationReq;
import uz.mydonation.service.donation.DonationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/donation")
public class DonationController {
    private final DonationService donationService;

    @PostMapping("/{streamerId}")
    public ResponseEntity<String> donate(@PathVariable Long streamerId,
                                         @RequestBody @Valid DonationReq donationReq) {
        String response = donationService.donate(donationReq, streamerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{streamerId}")
    public PagedRes<DonationInfo> getDonationsOfStreamer(
            @PathVariable Long streamerId,
            @RequestParam int days,
            @RequestParam int page,
            @RequestParam int size) {
        return new PagedRes<>(donationService.getDonationsOfStreamer(streamerId, page, size, days));
    }

    @GetMapping
    public PagedRes<DonationInfo> getAllDonations(
            @Valid @RequestParam @Min(1) int days,
            @RequestParam int page,
            @RequestParam int size) {
        return new PagedRes<>(donationService.getDonations(page, size, days));
    }

    @GetMapping("/statistics")
    public List<StatisticRes> getAllStatistics(@RequestParam int days) {
        return donationService.getStatisticsForAdmin(days);
    }

    @GetMapping("/statistics/{streamerId}")
    public List<StatisticRes> getStatisticsOfStreamer(
            @PathVariable Long streamerId,
            @RequestParam int days) {
        return donationService.getStatisticsForStreamer(streamerId, days);
    }

    @GetMapping("/info/{streamerId}")
    public FullStatisticRes getFullStatistic(
            @PathVariable(required = false) Long streamerId,
            @RequestParam int days) {
        return donationService.getFullStatistic(streamerId, days);
    }
}
