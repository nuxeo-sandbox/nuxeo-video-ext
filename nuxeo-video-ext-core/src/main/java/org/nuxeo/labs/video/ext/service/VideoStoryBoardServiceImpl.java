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

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.work.api.WorkManager;
import org.nuxeo.ecm.platform.video.VideoHelper;
import org.nuxeo.labs.video.ext.work.ExtendedVideoStoryboardWork;
import org.nuxeo.runtime.api.Framework;

public class VideoStoryBoardServiceImpl implements VideoStoryBoardService {

    private static final Logger log = LogManager.getLogger(VideoStoryBoardServiceImpl.class);

    public void updateStoryboard(DocumentModel docModel, Blob video, long[] timecodes) {
        VideoHelper.updateStoryboard(docModel,video);
    }

    @Override
    public void updatePreviews(DocumentModel docModel, Blob video) throws IOException {
        VideoHelper.updatePreviews(docModel,video);
    }

    public void scheduleVideoStoryboardWork(DocumentModel doc) {
        WorkManager workManager = Framework.getService(WorkManager.class);
        ExtendedVideoStoryboardWork work = new ExtendedVideoStoryboardWork(doc.getRepositoryName(), doc.getId());
        log.debug("Scheduling work: video storyboard of document {}.", doc);
        workManager.schedule(work, true);
    }

}
