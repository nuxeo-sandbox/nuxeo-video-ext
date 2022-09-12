package org.nuxeo.labs.video.ext.listener;

import static org.nuxeo.ecm.platform.video.VideoConstants.TRANSCODED_VIDEOS_PROPERTY;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.video.listener.VideoChangedListener;
import org.nuxeo.labs.video.ext.service.VideoInfoService;
import org.nuxeo.labs.video.ext.service.VideoStoryBoardService;
import org.nuxeo.runtime.api.Framework;

public class ExtendedVideoChangedListener extends VideoChangedListener {

    private static final Logger log = LogManager.getLogger(ExtendedVideoChangedListener.class);

    protected void resetProperties(DocumentModel doc) throws IOException {
        log.debug("Resetting video info, storyboard, previews and conversions of document {}", doc);
        VideoInfoService videoInfoService = Framework.getService(VideoInfoService.class);
        videoInfoService.updateVideoInfo(doc, null);
        VideoStoryBoardService videoStoryBoardService = Framework.getService(VideoStoryBoardService.class);
        videoStoryBoardService.updateStoryboard(doc, null);
        videoStoryBoardService.updatePreviews(doc, null);
        doc.setPropertyValue(TRANSCODED_VIDEOS_PROPERTY, null);
    }

    @Override
    protected void scheduleAsyncProcessing(DocumentModel doc) {
        VideoInfoService videoInfoService = Framework.getService(VideoInfoService.class);
        videoInfoService.scheduleVideoInfoWork(doc);
    }

}
