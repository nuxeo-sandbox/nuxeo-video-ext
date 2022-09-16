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

package org.nuxeo.labs.video.ext.adapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.collections.map.HashedMap;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.video.VideoConstants;
import org.nuxeo.labs.video.ext.api.Frame;
import org.nuxeo.labs.video.ext.api.Storyboard;

public class StoryboardAdapter implements Storyboard {

    private final DocumentModel doc;

    private final SortedSet<Frame> frames = new TreeSet<>(Comparator.comparingDouble(Frame::getTimeInSeconds));

    public StoryboardAdapter(DocumentModel doc) {
        this.doc = doc;
        List<Map<String, Serializable>> property = (List<Map<String, Serializable>>) doc.getPropertyValue(
                VideoConstants.STORYBOARD_PROPERTY);
        if (property != null) {
            for (Map<String, Serializable> item : property) {
                Frame frame = new Frame();
                frame.setBlob((Blob) item.get("content"));
                frame.setTimeInSeconds((Double) item.get("timecode"));
                frame.setComment((String) item.get("comment"));
                frames.add(frame);
            }
        }
    }

    @Override
    public void addFrame(Frame frame) {
        frames.add(frame);
        updateDoc();
    }

    @Override
    public void addAllFrames(List<Frame> frames) {
        this.frames.addAll(frames);
        updateDoc();
    }

    @Override
    public int size() {
        return frames.size();
    }

    @Override
    public List<Frame> getFrames() {
        return new ArrayList<>(frames);
    }

    protected void updateDoc() {
        List<Map<String, Serializable>> property = new ArrayList<>();
        for (Frame frame : frames) {
            Map<String, Serializable> item = new HashedMap();
            item.put("content", (Serializable) frame.getBlob());
            item.put("timecode", frame.getTimeInSeconds());
            item.put("comment", frame.getComment());
            property.add(item);
        }
        doc.setPropertyValue(VideoConstants.STORYBOARD_PROPERTY, (Serializable) property);
    }

}