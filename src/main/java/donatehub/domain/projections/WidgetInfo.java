package donatehub.domain.projections;

import donatehub.domain.entities.WidgetEntity;

/**
 * Projection for {@link WidgetEntity}
 */
public interface WidgetInfo {
    Long getId();

    String getVideoUrl();

    String getAudioUrl();

    Integer getTime();

    Float getMinDonateAmount();
}