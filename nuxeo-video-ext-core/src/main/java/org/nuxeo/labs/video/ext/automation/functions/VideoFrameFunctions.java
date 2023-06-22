/*
 * (C) Copyright 2023 Nuxeo (http://nuxeo.com/) and others.
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

package org.nuxeo.labs.video.ext.automation.functions;

import org.nuxeo.ecm.automation.context.ContextHelper;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.blobholder.BlobHolder;
import org.nuxeo.ecm.core.api.blobholder.SimpleBlobHolder;
import org.nuxeo.ecm.core.convert.api.ConversionService;
import org.nuxeo.runtime.api.Framework;

import java.io.IOException;
import java.util.HashMap;

public class VideoFrameFunctions implements ContextHelper {

    public static final String IMAGE_MEAN_GRAY_CONVERTER = "imageMeanGrey";

    public VideoFrameFunctions() {}

    public boolean isBlackFrame(Blob blob) throws IOException {
        return this.isBlackFrame(blob, 10);
    }

    public boolean isBlackFrame(Blob blob, double threshold) throws IOException {
        BlobHolder holder = Framework.getService(ConversionService.class)
                .convert(IMAGE_MEAN_GRAY_CONVERTER, new SimpleBlobHolder(blob), new HashMap<>());
        Blob info = holder.getBlob();
        return info != null && (Double.parseDouble(info.getString()) < threshold);
    }

}
