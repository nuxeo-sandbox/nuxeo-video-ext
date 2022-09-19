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

import org.nuxeo.ecm.core.api.DocumentModel;

public interface VideoInfoService {

    /**
     * Clears the video metadata from the document model
     * @param docModel A document
     */
    void clearVideoInfo(DocumentModel docModel);

    /**
     * Update the video metadata on the input document model
     * @param docModel A document
     */
    void updateVideoInfo(DocumentModel docModel);

    /**
     * Queue a worker to update the video metadata on the input document
     * @param doc A document
     */
    void scheduleVideoInfoWork(DocumentModel doc);

}
