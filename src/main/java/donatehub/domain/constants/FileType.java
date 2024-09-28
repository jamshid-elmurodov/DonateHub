package donatehub.domain.constants;

import lombok.Getter;

import java.util.Set;

@Getter
public enum FileType {
    VIDEO(Set.of("mp4", "mov")),
    AUDIO(Set.of("mp3", "mpeg", "wav")),
    IMAGE(Set.of("png", "jpeg", "jpg", "svg"));

    private final Set<String> types;

    FileType(Set<String> types) {
        this.types = types;
    }
}
