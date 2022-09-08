package org.nuxeo.labs.video.ext.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.work.api.WorkManager;
import org.nuxeo.ecm.platform.video.VideoHelper;
import org.nuxeo.labs.video.ext.work.ExtendedVideoInfoWork;
import org.nuxeo.labs.video.ext.work.ExtendedVideoStoryboardWork;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.model.DefaultComponent;

import java.io.IOException;

public class ExtendedVideoServiceImpl extends DefaultComponent implements ExtendedVideoService {

    private static final Logger log = LogManager.getLogger(ExtendedVideoServiceImpl.class);

    @Override
    public void updateVideoInfo(DocumentModel docModel, Blob video) {
        VideoHelper.updateVideoInfo(docModel,video);
    }

    @Override
    public void updateStoryboard(DocumentModel docModel, Blob video) {
        VideoHelper.updateStoryboard(docModel,video);
    }

    @Override
    public void updatePreviews(DocumentModel docModel, Blob video) throws IOException {
        VideoHelper.updatePreviews(docModel,video);
    }

    @Override
    public void scheduleVideoInfoWork(DocumentModel doc) {
        WorkManager workManager = Framework.getService(WorkManager.class);
        ExtendedVideoInfoWork work = new ExtendedVideoInfoWork(doc.getRepositoryName(), doc.getId());
        log.debug("Scheduling work: video info of document {}.", doc);
        workManager.schedule(work, true);
    }

    public void scheduleVideoStoryboardWork(DocumentModel doc) {
        WorkManager workManager = Framework.getService(WorkManager.class);
        ExtendedVideoStoryboardWork work = new ExtendedVideoStoryboardWork(doc.getRepositoryName(), doc.getId());
        log.debug("Scheduling work: video storyboard of document {}.", doc);
        workManager.schedule(work, true);
    }

}
