package org.nuxeo.labs.video.ext.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.nuxeo.labs.video.ext.work.ExtendedVideoInfoWork.VIDEO_INFO_DONE_EVENT;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.test.CapturingEventListener;
import org.nuxeo.ecm.platform.video.VideoDocument;
import org.nuxeo.ecm.platform.video.VideoInfo;
import org.nuxeo.labs.video.ext.utils.VideoExtFeature;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.TransactionalFeature;

import java.io.Serializable;

@RunWith(FeaturesRunner.class)
@Features(VideoExtFeature.class)
public class TestVideoInfoService {

    @Inject
    CoreSession session;

    @Inject
    protected TransactionalFeature transactionalFeature;

    @Inject
    VideoExtFeature videoExtFeature;

    @Inject
    protected VideoInfoService videoInfoService;

    @Test
    public void testService() {
        assertNotNull(videoInfoService);
    }

    @Test
    public void testComputeVideoInfo() {
        DocumentModel doc = videoExtFeature.getVideoDocument(session);

        videoInfoService.updateVideoInfo(doc);

        VideoDocument videoDocument = doc.getAdapter(VideoDocument.class,true);
        VideoInfo videoInfo = videoDocument.getVideo().getVideoInfo();
        Assert.assertNotNull(videoInfo);
        Assert.assertEquals(121.65,videoInfo.getDuration(),0.1d);

    }

    @Test
    @Deploy("nuxeo-video-ext-core:disable-video-info-listener-contrib.xml")
    public void testComputeVideoInfoAsync() {
        DocumentModel doc = videoExtFeature.getEmptyVideoDocument(session);
        doc.setPropertyValue("file:content", (Serializable) videoExtFeature.getVideoBlob());
        session.saveDocument(doc);

        try (CapturingEventListener listener = new CapturingEventListener(VIDEO_INFO_DONE_EVENT)) {
            videoInfoService.scheduleVideoInfoWork(doc);
            transactionalFeature.nextTransaction();

            assertEquals(1, listener.getCapturedEventCount(VIDEO_INFO_DONE_EVENT));

            doc = session.getDocument(doc.getRef());
            VideoDocument videoDocument = doc.getAdapter(VideoDocument.class);
            VideoInfo videoInfo = videoDocument.getVideo().getVideoInfo();
            Assert.assertNotNull(videoInfo);
            Assert.assertEquals(121.65,videoInfo.getDuration(),0.1d);
        }
    }
}
