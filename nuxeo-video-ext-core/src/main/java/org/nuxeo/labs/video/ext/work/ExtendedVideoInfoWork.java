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

package org.nuxeo.labs.video.ext.work;

import static org.nuxeo.ecm.core.api.CoreSession.ALLOW_VERSION_WRITE;
import static org.nuxeo.ecm.core.api.versioning.VersioningService.DISABLE_AUTO_CHECKOUT;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.blobholder.BlobHolder;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventService;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;
import org.nuxeo.ecm.platform.video.VideoDocument;
import org.nuxeo.ecm.platform.video.VideoHelper;
import org.nuxeo.ecm.platform.video.service.VideoInfoWork;
import org.nuxeo.labs.video.ext.service.VideoInfoService;
import org.nuxeo.runtime.api.Framework;

public class ExtendedVideoInfoWork extends VideoInfoWork {

    public static final String VIDEO_INFO_DONE_EVENT = "videoInfoDone";

    private static final Logger log = LogManager.getLogger(ExtendedVideoInfoWork.class);

    public ExtendedVideoInfoWork(String repositoryName, String docId) {
        super(repositoryName, docId);
    }

    @Override
    public void work() {
        setStatus("Updating video info");
        setProgress(Progress.PROGRESS_INDETERMINATE);
        VideoHelper.newTransaction();
        openSystemSession();

        // get video blob and update video info
        DocumentModel doc = session.getDocument(new IdRef(docId));
        updateVideoInfo(doc);
        fireVideoInfoDoneEvent(doc);

        setStatus("Done");
    }

    protected void fireVideoInfoDoneEvent(DocumentModel doc) {
        DocumentEventContext ctx = new DocumentEventContext(session, session.getPrincipal(), doc);
        Event event = ctx.newEvent(VIDEO_INFO_DONE_EVENT);
        Framework.getService(EventService.class).fireEvent(event);
    }

    @Override
    protected void updateVideoInfo(DocumentModel doc) {
        VideoDocument videoDocument = doc.getAdapter(VideoDocument.class);
        if (videoDocument.getVideo().getWidth() != 0 && videoDocument.getVideo().getHeight() != 0) {
            // assume the video info is already computed
            return;
        }

        BlobHolder blobHolder = doc.getAdapter(BlobHolder.class);
        Blob video = blobHolder.getBlob();
        log.debug(String.format("Updating video info of document %s.", doc));
        VideoInfoService videoInfoService = Framework.getService(VideoInfoService.class);
        videoInfoService.updateVideoInfo(doc,video);
        log.debug(String.format("End updating video info of document %s.", doc));

        // save document
        if (doc.isVersion()) {
            doc.putContextData(ALLOW_VERSION_WRITE, Boolean.TRUE);
        }
        doc.putContextData(DISABLE_AUTO_CHECKOUT, Boolean.TRUE);
        session.saveDocument(doc);
    }


}
