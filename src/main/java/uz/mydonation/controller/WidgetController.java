package uz.mydonation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.mydonation.service.widget.WidgetService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/widget")
public class WidgetController {
    private final WidgetService widgetService;

    @PutMapping("/{streamerId}")
    public void update(@PathVariable Long streamerId,
                @RequestParam MultipartFile videoFile,
                @RequestParam MultipartFile audioFile){
        widgetService.update(streamerId, videoFile, audioFile);
    }
}
