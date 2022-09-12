package org.nuxeo.labs.video.ext.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.work.api.WorkManager;
import org.nuxeo.ecm.platform.video.VideoHelper;
import org.nuxeo.labs.video.ext.work.ExtendedVideoInfoWork;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.model.DefaultComponent;

public class VideoInfoServiceImpl extends DefaultComponent implements VideoInfoService {

    private static final Logger log = LogManager.getLogger(VideoInfoServiceImpl.class);

    @Override
    public void updateVideoInfo(DocumentModel docModel, Blob video) {
        VideoHelper.updateVideoInfo(docModel,video);
    }

    @Override
    public void scheduleVideoInfoWork(DocumentModel doc) {
        WorkManager workManager = Framework.getService(WorkManager.class);
        ExtendedVideoInfoWork work = new ExtendedVideoInfoWork(doc.getRepositoryName(), doc.getId());
        log.debug("Scheduling work: video info of document {}.", doc);
        workManager.schedule(work, true);
    }

}
