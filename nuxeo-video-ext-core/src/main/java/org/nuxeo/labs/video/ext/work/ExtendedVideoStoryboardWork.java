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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventService;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;
import org.nuxeo.ecm.platform.picture.api.adapters.MultiviewPictureAdapter;
import org.nuxeo.ecm.platform.video.service.VideoStoryboardWork;
import org.nuxeo.labs.video.ext.adapter.StoryboardAdapter;
import org.nuxeo.labs.video.ext.service.VideoStoryBoardService;
import org.nuxeo.runtime.api.Framework;

public class ExtendedVideoStoryboardWork extends VideoStoryboardWork {

    public static final String VIDEO_STORYBOARD_DONE_EVENT = "videoStoryboardDone";

    private static final Logger log = LogManager.getLogger(ExtendedVideoStoryboardWork.class);

    public ExtendedVideoStoryboardWork(String repositoryName, String docId) {
        super(repositoryName, docId);
    }

    @Override
    public void work() {
        super.work();
        fireVideoStoryboardDoneEvent();
    }

    @Override
    protected boolean updateStoryboard(DocumentModel doc, Blob blob) {
        var storyboard = doc.getAdapter(StoryboardAdapter.class);
        if (storyboard != null && !storyboard.isEmpty()) {
            return false;
        }
        log.debug(String.format("Updating storyboard of Video document %s.", doc));
        VideoStoryBoardService videoStoryBoardService = Framework.getService(VideoStoryBoardService.class);
        videoStoryBoardService.updateStoryboard(doc);
        log.debug(String.format("End updating storyboard of Video document %s.", doc));
        return true;
    }

    @Override
    protected boolean updatePreviews(DocumentModel doc, Blob blob) {
        var previews = new MultiviewPictureAdapter(doc);
        if (previews.getViews().length > 0) {
            return false;
        }
        log.debug(String.format("Updating previews of Video document %s.", doc));
        VideoStoryBoardService videoStoryBoardService = Framework.getService(VideoStoryBoardService.class);
        videoStoryBoardService.updatePreviews(doc);
        log.debug(String.format("End updating previews of Video document %s.", doc));
        return true;
    }

    protected void fireVideoStoryboardDoneEvent() {
        DocumentModel doc = session.getDocument(new IdRef(docId));
        DocumentEventContext ctx = new DocumentEventContext(session, session.getPrincipal(), doc);
        Event event = ctx.newEvent(VIDEO_STORYBOARD_DONE_EVENT);
        Framework.getService(EventService.class).fireEvent(event);
    }

}
