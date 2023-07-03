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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.blobholder.BlobHolder;
import org.nuxeo.ecm.core.api.blobholder.SimpleBlobHolder;
import org.nuxeo.ecm.core.convert.api.ConversionException;
import org.nuxeo.ecm.core.convert.api.ConversionService;
import org.nuxeo.ecm.core.work.api.WorkManager;
import org.nuxeo.ecm.platform.mimetype.interfaces.MimetypeRegistry;
import org.nuxeo.ecm.platform.picture.api.adapters.AbstractPictureAdapter;
import org.nuxeo.ecm.platform.video.VideoDocument;
import org.nuxeo.ecm.platform.video.VideoHelper;
import org.nuxeo.labs.video.ext.adapter.StoryboardAdapter;
import org.nuxeo.labs.video.ext.api.Frame;
import org.nuxeo.labs.video.ext.work.ExtendedVideoStoryboardWork;
import org.nuxeo.runtime.api.Framework;

public class VideoStoryBoardServiceImpl implements VideoStoryBoardService {

    public static final String SCREENSHOT_CONVERTER_NAME = "video-screenshot";
    private static final Logger log = LogManager.getLogger(VideoStoryBoardServiceImpl.class);

    @Override
    public void updateStoryboard(DocumentModel docModel) {
        this.updateStoryboard(docModel, new double[] {});
    }

    public void updateStoryboard(DocumentModel docModel, double[] timecodeInSeconds) {
        VideoDocument videoDocument = docModel.getAdapter(VideoDocument.class);
        if (timecodeInSeconds.length < 1) {
            VideoHelper.updateStoryboard(docModel, videoDocument.getVideo().getBlob());
        } else {
            computeStoryBoard(docModel, timecodeInSeconds);
        }
    }

    @Override
    public void clearStoryboard(DocumentModel docModel) {
        VideoHelper.updateStoryboard(docModel, null);
    }

    @Override
    public void updatePreviews(DocumentModel docModel) {
        VideoDocument videoDocument = docModel.getAdapter(VideoDocument.class);
        try {
            VideoHelper.updatePreviews(docModel, videoDocument.getVideo().getBlob());
        } catch (IOException e) {
            throw new NuxeoException(e);
        }
    }

    @Override
    public void updatePreviews(DocumentModel docModel, double timecodeInSecond) {
        VideoDocument videoDocument = docModel.getAdapter(VideoDocument.class);
        double roundedTimecode = Precision.round(timecodeInSecond, 3);
        try {
            List<Map<String, Object>> views = new ArrayList<>();
            Map<String, Object> thumbnailView = new LinkedHashMap<>();
            thumbnailView.put("title", "Small");
            thumbnailView.put("maxsize", (long) AbstractPictureAdapter.SMALL_SIZE);
            views.add(thumbnailView);
            Map<String, Object> staticPlayerView = new HashMap<>();
            staticPlayerView.put("title", "StaticPlayerView");
            staticPlayerView.put("maxsize", (long) AbstractPictureAdapter.MEDIUM_SIZE);
            views.add(staticPlayerView);
            VideoHelper.updatePreviews(docModel, videoDocument.getVideo().getBlob(), roundedTimecode, views);
        } catch (IOException e) {
            throw new NuxeoException(e);
        }
    }

    @Override
    public void clearPreviews(DocumentModel docModel) {
        try {
            VideoHelper.updatePreviews(docModel, null);
        } catch (IOException e) {
            throw new NuxeoException(e);
        }
    }

    @Override
    public void scheduleVideoStoryboardWork(DocumentModel doc) {
        WorkManager workManager = Framework.getService(WorkManager.class);
        ExtendedVideoStoryboardWork work = new ExtendedVideoStoryboardWork(doc.getRepositoryName(), doc.getId());
        log.debug("Scheduling work: video storyboard of document {}.", doc);
        workManager.schedule(work, true);
    }

    /**
     * Generate and save the storyboard on the input document using an array of frame time codes
     * @param docModel A document
     * @param timecodesInSeconds the time codes of frames to use to build the storyboard
     */
    public void computeStoryBoard(DocumentModel docModel, double[] timecodesInSeconds) {
        StoryboardAdapter storyboard = docModel.getAdapter(StoryboardAdapter.class);
        VideoDocument videoDocument = docModel.getAdapter(VideoDocument.class);
        Blob video = videoDocument.getVideo().getBlob();
        for (double timecode : timecodesInSeconds) {
            try {
                double roundedTimecode = Precision.round(timecode, 3);
                Blob screenshot = screenshot(video,roundedTimecode);
                Frame frame = new Frame(screenshot, roundedTimecode, null);
                storyboard.addFrame(frame);
            } catch (ConversionException e) {
                // this can happen when if the codec is not supported or not
                // readable by ffmpeg and is recoverable by using a dummy preview
                log.error(String.format("could not extract story board for document '%s' with video file '%s': %s",
                        docModel.getTitle(), video, e.getMessage()));
            }
        }
    }

    public Blob screenshot(Blob video, double timecode) {
        String name = FilenameUtils.getBaseName(video.getFilename());

        Map<String, Serializable> parameters = new HashMap<>();
        parameters.put(POSITION_PARAMETER, String.format("%.3f", timecode));

        BlobHolder result = Framework.getService(ConversionService.class)
                .convert(SCREENSHOT_CONVERTER_NAME, new SimpleBlobHolder(video),
                        parameters);

        Blob blob = result.getBlob();

        MimetypeRegistry registry = Framework.getService(MimetypeRegistry.class);
        String ext = registry.getExtensionsFromMimetypeName(blob.getMimeType()).get(0);
        blob.setFilename(String.format("%s_frame_%.3f.%s",name,timecode,ext));

        return result.getBlob();
    }

}
