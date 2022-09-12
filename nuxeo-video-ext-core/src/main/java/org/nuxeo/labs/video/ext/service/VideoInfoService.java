package org.nuxeo.labs.video.ext.service;

import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.DocumentModel;

public interface VideoInfoService {

    public void updateVideoInfo(DocumentModel docModel, Blob video);

    public void scheduleVideoInfoWork(DocumentModel doc);

}
