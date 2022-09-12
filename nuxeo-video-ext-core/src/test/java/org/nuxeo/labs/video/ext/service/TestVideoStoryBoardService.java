package org.nuxeo.labs.video.ext.service;

import static org.junit.Assert.assertNotNull;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.platform.test.PlatformFeature;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

@RunWith(FeaturesRunner.class)
@Features({ PlatformFeature.class })
@Deploy("nuxeo-video-ext-core")
public class TestVideoStoryBoardService {

    @Inject
    protected VideoStoryBoardService videoStoryBoardService;

    @Test
    public void testService() {
        assertNotNull(videoStoryBoardService);
    }
}
