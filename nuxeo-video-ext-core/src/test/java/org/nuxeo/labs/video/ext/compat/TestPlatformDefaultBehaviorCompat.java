package org.nuxeo.labs.video.ext.compat;

import static org.nuxeo.ecm.platform.video.VideoHelper.DEFAULT_NUMBER_OF_THUMBNAILS;

import java.io.Serializable;
import java.util.Collection;

import jakarta.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.picture.api.adapters.MultiviewPictureAdapter;
import org.nuxeo.ecm.platform.video.TranscodedVideo;
import org.nuxeo.ecm.platform.video.VideoDocument;
import org.nuxeo.ecm.platform.video.VideoInfo;
import org.nuxeo.labs.video.ext.adapter.StoryboardAdapter;
import org.nuxeo.labs.video.ext.utils.VideoExtFeature;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.TransactionalFeature;

@RunWith(FeaturesRunner.class)
@Features(VideoExtFeature.class)
@Deploy({ "nuxeo-video-ext-core:reenable-video-changed-listener-contrib.xml",
        "nuxeo-video-ext-core:dummy-video-converter-contrib.xml" })
public class TestPlatformDefaultBehaviorCompat {

    @Inject
    protected TransactionalFeature transactionalFeature;
    @Inject
    CoreSession session;
    @Inject
    VideoExtFeature videoExtFeature;

    @Test
    public void testDefaultConfig() {
        DocumentModel doc = session.createDocumentModel("/", "Video", "Video");
        Blob blob = videoExtFeature.getVideoBlob();
        doc.setPropertyValue("file:content", (Serializable) blob);
        session.createDocument(doc);

        transactionalFeature.nextTransaction();

        // reload document
        doc = session.getDocument(doc.getRef());
        VideoDocument videoDocument = doc.getAdapter(VideoDocument.class, true);

        //check video metadata
        VideoInfo videoInfo = videoDocument.getVideo().getVideoInfo();
        Assert.assertNotNull(videoInfo);
        Assert.assertTrue(videoInfo.getDuration() > 0);

        //check conversions
        Collection<TranscodedVideo> transcodedVideos = videoDocument.getTranscodedVideos();
        Assert.assertTrue(transcodedVideos.size() > 0);

        //check preview
        MultiviewPictureAdapter adapter = new MultiviewPictureAdapter(doc);
        Assert.assertEquals(2, adapter.getViews().length);

        //check storyboard
        StoryboardAdapter storyboardAdapter = doc.getAdapter(StoryboardAdapter.class);
        Assert.assertEquals(DEFAULT_NUMBER_OF_THUMBNAILS,storyboardAdapter.size());
    }

}
