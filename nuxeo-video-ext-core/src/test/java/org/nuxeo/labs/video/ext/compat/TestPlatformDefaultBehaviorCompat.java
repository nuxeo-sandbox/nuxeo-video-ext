package org.nuxeo.labs.video.ext.compat;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.video.TranscodedVideo;
import org.nuxeo.ecm.platform.video.VideoDocument;
import org.nuxeo.ecm.platform.video.VideoInfo;
import org.nuxeo.labs.video.ext.utils.VideoExtFeature;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.TransactionalFeature;

@RunWith(FeaturesRunner.class)
@Features(VideoExtFeature.class)
@Deploy({
        "nuxeo-video-ext-core:reenable-video-changed-listener-contrib.xml",
        "nuxeo-video-ext-core:dummy-video-converter-contrib.xml"
})
public class TestPlatformDefaultBehaviorCompat {

    @Inject
    CoreSession session;

    @Inject
    protected TransactionalFeature transactionalFeature;

    @Inject
    VideoExtFeature videoExtFeature;

    @Test
    public void testDefaultConfig() {
        DocumentModel doc = session.createDocumentModel("/", "Video", "Video");
        Blob blob = videoExtFeature.getVideoBlob();
        doc.setPropertyValue("file:content", (Serializable) blob);
        session.createDocument(doc);

        transactionalFeature.nextTransaction();

        //reload document
        doc = session.getDocument(doc.getRef());
        VideoDocument videoDocument = doc.getAdapter(VideoDocument.class,true);

        Collection<TranscodedVideo> transcodedVideos = videoDocument.getTranscodedVideos();
        Assert.assertTrue(transcodedVideos.size()>0);

        VideoInfo videoInfo = videoDocument.getVideo().getVideoInfo();
        Assert.assertNotNull(videoInfo);
        Assert.assertTrue(videoInfo.getDuration()>0);

        List<Map<String, Serializable>> views = (List<Map<String, Serializable>>) doc.getPropertyValue("picture:views");
        Assert.assertEquals(2,views.size());

    }

}
