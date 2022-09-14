package org.nuxeo.labs.video.ext.listener;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.event.EventService;
import org.nuxeo.ecm.core.event.impl.EventListenerDescriptor;
import org.nuxeo.labs.video.ext.utils.VideoExtFeature;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

@RunWith(FeaturesRunner.class)
@Features(VideoExtFeature.class)
public class TestExtendedVideoChangedListener {

    protected final List<String> events = Arrays.asList("documentCreated", "beforeDocumentModification");

    @Inject
    protected EventService s;

    @Test
    public void listenerRegistration() {
        EventListenerDescriptor listener = s.getEventListener("videoChangedListener");
        assertNotNull(listener);
        assertTrue(events.stream().allMatch(listener::acceptEvent));
        Assert.assertEquals(listener.asEventListener().getClass().getName(),ExtendedVideoChangedListener.class.getName());
    }
}
