package org.nuxeo.labs.video.ext.service;

import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.DocumentModel;

import java.io.IOException;

public interface ExtendedVideoService {

    public void updateVideoInfo(DocumentModel docModel, Blob video);

    public void updateStoryboard(DocumentModel docModel, Blob video);

    public void updatePreviews(DocumentModel docModel, Blob video) throws IOException;

    public void scheduleVideoInfoWork(DocumentModel doc);

    public void scheduleVideoStoryboardWork(DocumentModel doc);

}
