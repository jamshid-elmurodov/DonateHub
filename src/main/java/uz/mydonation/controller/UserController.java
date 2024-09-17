package uz.mydonation.controller;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import uz.mydonation.domain.model.PagedRes;
import uz.mydonation.domain.projection.UserInfo;
import uz.mydonation.domain.request.UpdateUserReq;
import uz.mydonation.service.user.UserService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/user-info/{userId}")
    public UserInfo getUserInfo(@PathVariable Long userId){
        return userService.getById(userId);
    }

    @Hidden
    @GetMapping("/s/{api}")
    public ModelAndView showDonationPage(@PathVariable UUID api) {
        ModelAndView modelAndView = new ModelAndView("donation");
        modelAndView.addObject("api", api);
        return modelAndView;
    }

    @GetMapping("/{channelName}")
    public UserInfo getStreamerByChannelName(@PathVariable String channelName){
        return userService.findByChannelName(channelName);
    }

    @GetMapping("/verified")
    public PagedRes<UserInfo> getApprovedUsers(@RequestParam int page, @RequestParam int size){
        return new PagedRes<>(userService.getUsersByEnableState(true, page, size));
    }

    @GetMapping("/not-verified")
    public PagedRes<UserInfo> getNotApproved(@RequestParam int page, @RequestParam int size){
        return new PagedRes<>(userService.getUsersByEnableState(false, page, size));
    }

    @GetMapping("/search")
    public PagedRes<UserInfo> search(@RequestParam String text, @RequestParam int page, @RequestParam int size){
        return new PagedRes<>(userService.searchUsers(text, page, size));
    }

    @PutMapping("/{userId}")
    public void update(@PathVariable Long userId,
                       @RequestPart UpdateUserReq updateReq,
                       @RequestPart("profileImg") MultipartFile profileImg,
                       @RequestPart("bannerImg") MultipartFile bannerImg) {
        userService.update(userId, updateReq, profileImg, bannerImg);
    }

    @PutMapping("/enable/{streamerId}")
    public void enable(@PathVariable Long streamerId){
        userService.enable(streamerId);
    }

    @PutMapping("/disable/{streamerId}")
    public void disable(@PathVariable Long streamerId){
        userService.disable(streamerId);
    }
}
