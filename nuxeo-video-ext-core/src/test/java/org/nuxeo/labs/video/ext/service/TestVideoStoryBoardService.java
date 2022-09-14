package org.nuxeo.labs.video.ext.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.nuxeo.ecm.platform.video.VideoHelper.DEFAULT_NUMBER_OF_THUMBNAILS;
import static org.nuxeo.labs.video.ext.work.ExtendedVideoStoryboardWork.VIDEO_STORYBOARD_DONE_EVENT;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.test.CapturingEventListener;
import org.nuxeo.labs.video.ext.adapter.StoryboardAdapter;
import org.nuxeo.labs.video.ext.api.Storyboard;
import org.nuxeo.labs.video.ext.utils.VideoExtFeature;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.TransactionalFeature;

@RunWith(FeaturesRunner.class)
@Features(VideoExtFeature.class)
public class TestVideoStoryBoardService {

    @Inject
    CoreSession session;

    @Inject
    VideoExtFeature videoExtFeature;

    @Inject
    protected TransactionalFeature transactionalFeature;

    @Inject
    protected VideoStoryBoardService videoStoryBoardService;

    @Test
    public void testService() {
        assertNotNull(videoStoryBoardService);
    }

    @Test
    public void testComputeDefaultStoryboard() {
        DocumentModel doc = videoExtFeature.getVideoDocument(session);
        Blob video = videoExtFeature.getVideoBlob();
        videoStoryBoardService.updateStoryboard(doc, video);
        Storyboard storyboard = doc.getAdapter(StoryboardAdapter.class);
        Assert.assertEquals(DEFAULT_NUMBER_OF_THUMBNAILS, storyboard.size());
    }

    @Test
    public void testComputeCustomStoryboard() {
        DocumentModel doc = videoExtFeature.getVideoDocument(session);
        Blob video = videoExtFeature.getVideoBlob();
        videoStoryBoardService.updateStoryboard(doc, video, new long[] { 5000, 10000, 60000 });
        Storyboard storyboard = doc.getAdapter(StoryboardAdapter.class);
        Assert.assertEquals(3, storyboard.size());
    }

    @Test
    public void testComputeStoryboardAsync() {
        DocumentModel doc = videoExtFeature.getVideoDocument(session);

        try (CapturingEventListener listener = new CapturingEventListener(VIDEO_STORYBOARD_DONE_EVENT)) {
            videoStoryBoardService.scheduleVideoStoryboardWork(doc);
            transactionalFeature.nextTransaction();

            assertEquals(1, listener.getCapturedEventCount(VIDEO_STORYBOARD_DONE_EVENT));

            Storyboard storyboard = doc.getAdapter(StoryboardAdapter.class);
            Assert.assertEquals(DEFAULT_NUMBER_OF_THUMBNAILS, storyboard.size());
        }
    }

}
