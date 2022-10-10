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

import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.DocumentModel;

public interface VideoStoryBoardService {

    /**
     * Update the storyboard on the input document
     * @param docModel A document
     */
    void updateStoryboard(DocumentModel docModel);

    /**
     * Update the storyboard on the input document using an array of frame time codes
     * @param docModel A document
     * @param timecodeInSeconds the time codes of frames to use to build the storyboard
     */
    void updateStoryboard(DocumentModel docModel, double[] timecodeInSeconds);

    /**
     * Remove the storyboard on the input document
     * @param docModel A document
     */
    void clearStoryboard(DocumentModel docModel);

    /**
     * Update the playback preview and thumbnail on the input document
     * @param docModel A document
     */
    void updatePreviews(DocumentModel docModel);

    /**
     *  Update the playback preview and thumbnail on the input document using the frame corresponding to the input time code
     * @param docModel A document
     * @param timecodeInSecond A frame timecode in seconds
     */
    void updatePreviews(DocumentModel docModel, double timecodeInSecond);

    /**
     * Clears the playback preview and thumbnail on the input document
     * @param docModel A document
     */
    void clearPreviews(DocumentModel docModel);

    /**
     * Queue a worker to update the video storyboard, playback preview and thumbnail of the input document
     * @param doc
     */
    void scheduleVideoStoryboardWork(DocumentModel doc);

    /**
     *
     * @param video a video Blob
     * @param timecode A frame timecode in seconds
     * @return a screenshot image blob
     */
    Blob screenshot(Blob video, double timecode);

}
