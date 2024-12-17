package org.nuxeo.labs.video.ext.listener;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.nuxeo.labs.video.ext.listener.TriggerVideoConversionListener.NO_DOWNSCALING_VIDEO_CONVERSION_AVAILABLE;
import static org.nuxeo.labs.video.ext.work.ExtendedVideoInfoWork.VIDEO_INFO_DONE_EVENT;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import jakarta.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventService;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;
import org.nuxeo.ecm.core.event.impl.EventListenerDescriptor;
import org.nuxeo.ecm.core.event.test.CapturingEventListener;
import org.nuxeo.labs.video.ext.utils.VideoExtFeature;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

@RunWith(FeaturesRunner.class)
@Features(VideoExtFeature.class)
public class TestTriggerVideoConversionListener {

    protected final List<String> events = Arrays.asList("videoInfoDone");

    @Inject
    CoreSession session;
    @Inject
    VideoExtFeature videoExtFeature;

    @Inject
    protected EventService s;

    @Test
    public void listenerRegistration() {
        EventListenerDescriptor listener = s.getEventListener("triggerVideoConversionlistener");
        assertNotNull(listener);
        assertTrue(events.stream().allMatch(listener::acceptEvent));
        Assert.assertEquals(listener.asEventListener().getClass().getName(),
                TriggerVideoConversionListener.class.getName());
    }


    @Test
    public void shouldSendNoDownscalingConversionAvailable() {
        Framework.getProperties().put("nuxeo.video.conversion.allow.upscaling","false");
        DocumentModel doc = videoExtFeature.getEmptyVideoDocument(session);
        doc.setPropertyValue("vid:info", (Serializable) videoExtFeature.getVideoInfo().toMap());
        try (CapturingEventListener listener = new CapturingEventListener(NO_DOWNSCALING_VIDEO_CONVERSION_AVAILABLE)) {
            DocumentEventContext dec = new DocumentEventContext(session, session.getPrincipal(), doc);
            Event e = dec.newEvent(VIDEO_INFO_DONE_EVENT);
            Framework.getService(EventService.class).fireEvent(e);
            assertEquals(1, listener.getCapturedEventCount(NO_DOWNSCALING_VIDEO_CONVERSION_AVAILABLE));
        }
    }

}
