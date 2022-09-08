package org.nuxeo.labs.video.ext.listener;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.event.EventService;
import org.nuxeo.ecm.core.event.impl.EventListenerDescriptor;
import org.nuxeo.ecm.platform.test.PlatformFeature;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import javax.inject.Inject;

@RunWith(FeaturesRunner.class)
@Features({ PlatformFeature.class })
@Deploy({ "nuxeo-video-ext-core", "org.nuxeo.ecm.platform.tag", "org.nuxeo.ecm.platform.video" })
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
