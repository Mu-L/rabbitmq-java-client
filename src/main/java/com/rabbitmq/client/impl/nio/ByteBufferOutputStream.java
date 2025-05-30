// Copyright (c) 2007-2025 Broadcom. All Rights Reserved. The term "Broadcom" refers to Broadcom Inc. and/or its subsidiaries.
//
// This software, the RabbitMQ Java client library, is triple-licensed under the
// Mozilla Public License 2.0 ("MPL"), the GNU General Public License version 2
// ("GPL") and the Apache License version 2 ("ASL"). For the MPL, please see
// LICENSE-MPL-RabbitMQ. For the GPL, please see LICENSE-GPL2.  For the ASL,
// please see LICENSE-APACHE2.
//
// This software is distributed on an "AS IS" basis, WITHOUT WARRANTY OF ANY KIND,
// either express or implied. See the LICENSE file for specific language governing
// rights and limitations of this software.
//
// If you have any questions regarding licensing, please contact us at
// info@rabbitmq.com.

package com.rabbitmq.client.impl.nio;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

/**
 * Bridge between the byte buffer and stream worlds.
 */
public class ByteBufferOutputStream extends OutputStream {

    private final WritableByteChannel channel;

    private final ByteBuffer buffer;

    public ByteBufferOutputStream(WritableByteChannel channel, ByteBuffer buffer) {
        this.buffer = buffer;
        this.channel = channel;
    }

    @Override
    public void write(int b) throws IOException {
        if(!buffer.hasRemaining()) {
            drain(channel, buffer);
        }
        buffer.put((byte) b);
    }

    @Override
    public void flush() throws IOException {
        drain(channel, buffer);
    }

    public static void drain(WritableByteChannel channel, ByteBuffer buffer) throws IOException {
        buffer.flip();
        while(buffer.hasRemaining() && channel.write(buffer) != -1);
        buffer.clear();
    }

}
