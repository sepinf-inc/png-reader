/*
 * Copyright (c) 2000, 2010, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package com.sun.imageio.plugins.png2;

import java.io.IOException;
import java.util.Locale;
import java.util.Iterator;
import javax.imageio.ImageReader;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.spi.ServiceRegistry;
import javax.imageio.metadata.IIOMetadataFormat;
import javax.imageio.metadata.IIOMetadataFormatImpl;
import javax.imageio.stream.ImageInputStream;

public class PNGImageReaderSpi extends ImageReaderSpi {

    private static final String vendorName = "Oracle Corporation";

    private static final String version = "1.0";

    private static final String[] names = { "png", "PNG" };

    private static final String[] suffixes = { "png" };

    private static final String[] MIMETypes = { "image/png", "image/x-png" };

    private static final String readerClassName = PNGImageReader.class.getName();

    private static final String[] writerSpiNames = null;

    public PNGImageReaderSpi() {
        super(vendorName,
              version,
              names,
              suffixes,
              MIMETypes,
              readerClassName,
              new Class<?>[] { ImageInputStream.class },
              writerSpiNames,
              false,
              null, null,
              null, null,
              true,
              PNGMetadata.nativeMetadataFormatName,
              PNGMetadataFormat.class.getName(),
              null, null
              );
    }

    public String getDescription(Locale locale) {
        return "Standard PNG image reader";
    }

    public boolean canDecodeInput(Object input) throws IOException {
        if (!(input instanceof ImageInputStream)) {
            return false;
        }

        ImageInputStream stream = (ImageInputStream)input;
        byte[] b = new byte[8];
        stream.mark();
        stream.readFully(b);
        stream.reset();

        return (b[0] == (byte)137 &&
                b[1] == (byte)80 &&
                b[2] == (byte)78 &&
                b[3] == (byte)71 &&
                b[4] == (byte)13 &&
                b[5] == (byte)10 &&
                b[6] == (byte)26 &&
                b[7] == (byte)10);
    }

    public ImageReader createReaderInstance(Object extension) {
        return new PNGImageReader(this);
    }

    @Override
    public void onRegistration(final ServiceRegistry registry, final Class<?> category) {
        Iterator<ImageReaderSpi> iterator = registry.getServiceProviders(ImageReaderSpi.class, false);
        while (iterator.hasNext()) {
            ImageReaderSpi spi = iterator.next();
            if (spi != this) {
                for (String ext : spi.getFileSuffixes()) {
                    if ("png".equals(ext)) {
                        registry.setOrdering(ImageReaderSpi.class, this, spi);
                        break;
                    }
                }
            }
        }
    }
}
