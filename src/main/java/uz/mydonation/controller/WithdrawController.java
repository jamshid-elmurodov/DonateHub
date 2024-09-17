package uz.mydonation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import uz.mydonation.domain.enums.WithdrawStatus;
import uz.mydonation.domain.model.PagedRes;
import uz.mydonation.domain.projection.WithdrawInfo;
import uz.mydonation.service.withdraw.WithdrawService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/withdraw")
public class WithdrawController {
    private final WithdrawService withdrawService;

    /**
     * userlar uchun pul chiqarish uchun so'rov yaratadi
     */
    @PostMapping("/{streamerId}")
    public void create(@PathVariable Long streamerId, @RequestParam Integer amount, @RequestParam String cardNumber){
        withdrawService.create(streamerId, amount, cardNumber);
    }

    /**
     * admin uchun withdraw statusini o'zgartirish uchun
     */
    @PutMapping("/{withdrawId}")
    public void setStatus(@PathVariable Long withdrawId, @RequestParam WithdrawStatus status){
        withdrawService.setStatus(withdrawId, status);
    }

    /**
     * admin uchun withdraw larni statusi bo'yicha qaytaradi
     */
    @GetMapping
    public PagedRes<WithdrawInfo> getWithdrawsByStatus(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam int days,
            @RequestParam WithdrawStatus status){
        return new PagedRes<>(withdrawService.getWithdrawsByStatus(page, size, days, status));
    }

    /**
     * streamer uchun withdraw larini status bo'yicha qaytaradi
     */
    @GetMapping("/{streamerId}")
    public PagedRes<WithdrawInfo> getWithdrawsOfStreamerByStatus(
            @PathVariable Long streamerId,
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam int days,
            @RequestParam WithdrawStatus status){
        return new PagedRes<>(withdrawService.getWithdrawsOfStreamerByStatus(streamerId, page, size, days, status));
    }
}
