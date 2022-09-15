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
package org.nuxeo.labs.video.ext.api;

import org.nuxeo.ecm.core.api.Blob;

public class Frame {

    protected Blob blob;
    protected double timeInSeconds;
    protected String comment;

    public Frame() {}

    public Frame(Blob blob, double timeInSeconds, String comment) {
        this.blob = blob;
        this.timeInSeconds = timeInSeconds;
        this.comment = comment;
    }


    public Blob getBlob() {
        return blob;
    }


    public void setBlob(Blob blob) {
        this.blob = blob;
    }


    public double getTimeInSeconds() {
        return timeInSeconds;
    }


    public void setTimeInSeconds(double time) {
        this.timeInSeconds = time;
    }


    public String getComment() {
        return comment;
    }


    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "FrameImpl{" +
                "blob=" + blob +
                ", timeInSeconds=" + timeInSeconds +
                ", comment='" + comment + '\'' +
                '}';
    }
}
