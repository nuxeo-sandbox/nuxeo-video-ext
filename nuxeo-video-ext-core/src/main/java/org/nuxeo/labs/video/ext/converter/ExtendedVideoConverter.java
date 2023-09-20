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

package org.nuxeo.labs.video.ext.converter;

import java.io.Serializable;
import java.util.Map;

import org.nuxeo.ecm.core.api.blobholder.BlobHolder;
import org.nuxeo.ecm.platform.video.VideoInfo;
import org.nuxeo.ecm.platform.video.convert.VideoConversionConverter;

public class ExtendedVideoConverter extends VideoConversionConverter {

    @Override
    protected Map<String, String> getCmdStringParameters(BlobHolder blobHolder, Map<String, Serializable> parameters) {
        Map<String, String> cmdStringParams = super.getCmdStringParameters(blobHolder,parameters);
        setVideoTargetDimensions(cmdStringParams, parameters);
        return cmdStringParams;
    }

    public void setVideoTargetDimensions(Map<String, String> cmdStringParams, Map<String, Serializable> parameters) {
        VideoInfo videoInfo = (VideoInfo) parameters.get("videoInfo");
        if (videoInfo == null) {
            return;
        }

        long width = videoInfo.getWidth();
        long height = videoInfo.getHeight();

        long shortSideDimension = (Long) parameters.get("height");

        //make sure the dimension is a multiple of 2
        shortSideDimension = shortSideDimension + (shortSideDimension % 2);

        long newHeight;
        long newWidth;

        //landscape
        if (width >= height) {
            newHeight = shortSideDimension;
            newWidth = crossMultiply(width,height,newHeight);
        } else {
        //portrait
            newWidth = shortSideDimension;
            newHeight = crossMultiply(height,width,newWidth);
        }

        cmdStringParams.put("width", String.valueOf(newWidth));
        cmdStringParams.put("height", String.valueOf(newHeight));
    }

    public static long crossMultiply(long value, long otherDimensionValue, long otherDimensionNewValue) {
        long result = (otherDimensionNewValue * value) / otherDimensionValue;
        //make sure result is a multiple of 2
        result = result + (result % 2);
        return result;
    }

}
