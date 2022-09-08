package org.nuxeo.labs.video.ext.listener;

import static org.nuxeo.ecm.platform.video.VideoConstants.TRANSCODED_VIDEOS_PROPERTY;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.video.listener.VideoChangedListener;
import org.nuxeo.labs.video.ext.service.ExtendedVideoService;
import org.nuxeo.runtime.api.Framework;

import java.io.IOException;

public class ExtendedVideoChangedListener extends VideoChangedListener {

    private static final Logger log = LogManager.getLogger(ExtendedVideoChangedListener.class);

    protected void resetProperties(DocumentModel doc) throws IOException {
        ExtendedVideoService extendedVideoService = Framework.getService(ExtendedVideoService.class);
        log.debug("Resetting video info, storyboard, previews and conversions of document {}", doc);
        extendedVideoService.updateVideoInfo(doc, null);
        extendedVideoService.updateStoryboard(doc, null);
        extendedVideoService.updatePreviews(doc, null);
        doc.setPropertyValue(TRANSCODED_VIDEOS_PROPERTY, null);
    }

    @Override
    protected void scheduleAsyncProcessing(DocumentModel doc) {
        ExtendedVideoService extendedVideoService = Framework.getService(ExtendedVideoService.class);
        extendedVideoService.scheduleVideoInfoWork(doc);
    }

}
