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

import static org.nuxeo.ecm.platform.video.convert.Constants.POSITION_PARAMETER;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.blobholder.BlobHolder;
import org.nuxeo.ecm.core.api.blobholder.SimpleBlobHolder;
import org.nuxeo.ecm.core.convert.api.ConversionException;
import org.nuxeo.ecm.core.convert.api.ConversionService;
import org.nuxeo.ecm.core.work.api.WorkManager;
import org.nuxeo.ecm.platform.video.VideoHelper;
import org.nuxeo.labs.video.ext.adapter.StoryboardAdapter;
import org.nuxeo.labs.video.ext.api.Frame;
import org.nuxeo.labs.video.ext.work.ExtendedVideoStoryboardWork;
import org.nuxeo.runtime.api.Framework;

public class VideoStoryBoardServiceImpl implements VideoStoryBoardService {

    private static final Logger log = LogManager.getLogger(VideoStoryBoardServiceImpl.class);

    public static final String SCREENSHOT_CONVERTER_NAME = "video-screenshot";

    @Override
    public void updateStoryboard(DocumentModel docModel, Blob video) {
        this.updateStoryboard(docModel, video, new long[] {});
    }

    public void updateStoryboard(DocumentModel docModel, Blob video, long[] timecodes) {
        if (timecodes.length < 1) {
            VideoHelper.updateStoryboard(docModel, video);
        } else {
            computeStoryBoard(docModel, video, timecodes);
        }
    }

    @Override
    public void clearStoryboard(DocumentModel docModel) {
        VideoHelper.updateStoryboard(docModel, null);
    }

    @Override
    public void updatePreviews(DocumentModel docModel, Blob video) throws IOException {
        VideoHelper.updatePreviews(docModel, video);
    }

    @Override
    public void clearPreviews(DocumentModel docModel) throws IOException {
        VideoHelper.updatePreviews(docModel, null);
    }

    public void scheduleVideoStoryboardWork(DocumentModel doc) {
        WorkManager workManager = Framework.getService(WorkManager.class);
        ExtendedVideoStoryboardWork work = new ExtendedVideoStoryboardWork(doc.getRepositoryName(), doc.getId());
        log.debug("Scheduling work: video storyboard of document {}.", doc);
        workManager.schedule(work, true);
    }

    public void computeStoryBoard(DocumentModel docModel, Blob video, long[] timecodes) {
        StoryboardAdapter storyboard = docModel.getAdapter(StoryboardAdapter.class);
        for (long timecode : timecodes) {
            try {
                Map<String, Serializable> parameters = new HashMap<>();
                double timecodeInSecond = timecode/1000.0f;
                parameters.put(POSITION_PARAMETER, String.format("%.3f", timecodeInSecond));
                BlobHolder result = Framework.getService(ConversionService.class)
                                             .convert(SCREENSHOT_CONVERTER_NAME, new SimpleBlobHolder(video),
                                                     parameters);
                Frame frame = new Frame(result.getBlob(), timecodeInSecond, null);
                storyboard.addFrame(frame);
            } catch (ConversionException e) {
                // this can happen when if the codec is not supported or not
                // readable by ffmpeg and is recoverable by using a dummy preview
                log.error(String.format("could not extract story board for document '%s' with video file '%s': %s",
                        docModel.getTitle(), video, e.getMessage()));
            }
        }
    }

}
