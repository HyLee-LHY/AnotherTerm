/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2005-2018 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright 
     notice, this list of conditions and the following disclaimer in 
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package com.jcraft.jsch;

final class RequestSubsystem extends Request {
    private String subsystem = null;

    public void request(final Session session, final Channel channel, final String subsystem,
                        final boolean want_reply)
            throws Exception {
        setReply(want_reply);
        this.subsystem = subsystem;
        this.request(session, channel);
    }

    @Override
    public void request(final Session session, final Channel channel) throws Exception {
        assert subsystem != null;

        super.request(session, channel);

        final Buffer buf = new Buffer();
        final Packet packet = new Packet(buf);

        packet.reset();
        buf.putByte((byte) Session.SSH_MSG_CHANNEL_REQUEST);
        buf.putInt(channel.getRecipient());
        buf.putString(Util.str2byte("subsystem"));
        buf.putByte((byte) (waitForReply() ? 1 : 0));
        buf.putString(Util.str2byte(subsystem));
        write(packet);
    }
}
