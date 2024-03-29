/*
 * (C) Copyright 2022 Nuxeo (http://nuxeo.com/) and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     Michael Vachette
 */

package org.nuxeo.labs.video.ext.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.work.api.WorkManager;
import org.nuxeo.ecm.platform.video.VideoDocument;
import org.nuxeo.ecm.platform.video.VideoHelper;
import org.nuxeo.labs.video.ext.work.ExtendedVideoInfoWork;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.model.DefaultComponent;

public class VideoInfoServiceImpl extends DefaultComponent implements VideoInfoService {

    private static final Logger log = LogManager.getLogger(VideoInfoServiceImpl.class);

    @Override
    public void clearVideoInfo(DocumentModel docModel) {
        VideoHelper.updateVideoInfo(docModel, null);
    }

    @Override
    public void updateVideoInfo(DocumentModel docModel) {
        VideoDocument videoDocument = docModel.getAdapter(VideoDocument.class, true);
        VideoHelper.updateVideoInfo(docModel, videoDocument.getVideo().getBlob());
    }

    @Override
    public void scheduleVideoInfoWork(DocumentModel doc) {
        WorkManager workManager = Framework.getService(WorkManager.class);
        ExtendedVideoInfoWork work = new ExtendedVideoInfoWork(doc.getRepositoryName(), doc.getId());
        log.debug("Scheduling work: video info of document {}.", doc);
        workManager.schedule(work, true);
    }

}
