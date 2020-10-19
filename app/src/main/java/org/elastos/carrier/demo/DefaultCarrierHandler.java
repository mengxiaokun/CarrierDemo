package org.elastos.carrier.demo;

import org.elastos.carrier.AbstractCarrierHandler;
import org.elastos.carrier.Carrier;
import org.elastos.carrier.ConnectionStatus;
import org.elastos.carrier.FriendInfo;
import org.elastos.carrier.UserInfo;

import java.util.Date;
import java.util.List;

public class DefaultCarrierHandler extends AbstractCarrierHandler {
    @Override
    public void onConnection(Carrier carrier, ConnectionStatus status) {
        Logger.info("Carrier connection status: " + status);

        if(status == ConnectionStatus.Connected) {
            String msg = "Friend List:";
            List<FriendInfo> friendList = CarrierHelper.getFriendList();
            if(friendList != null) {
                for(FriendInfo info: friendList) {
                    msg += "\n  " + info.getUserId();
                }
            }
            Logger.info(msg);
        }
    }

    @Override
    public void onFriendRequest(Carrier carrier, String userId, UserInfo info, String hello) {
        Logger.info("Carrier received friend request. peer UserId: " + userId);
        CarrierHelper.acceptFriend(userId, hello);
    }

    @Override
    public void onFriendAdded(Carrier carrier, FriendInfo info) {
        Logger.info("Carrier friend added. peer UserId: " + info.getUserId());
    }

    @Override
    public void onFriendConnection(Carrier carrier, String friendId, ConnectionStatus status) {
        Logger.info("Carrier friend connect. peer UserId: " + friendId + " status:" + status);
        if(status == ConnectionStatus.Connected) {
            CarrierHelper.setPeerUserId(friendId);
        } else {
            CarrierHelper.setPeerUserId(null);
        }
    }

    @Override
    public void onFriendMessage(Carrier carrier, String from, byte[] message,
                                Date timestamp, boolean isOffline) {
        Logger.info("Carrier receiver message from UserId: " + from
                + "\nmessage: " + new String(message));
        StringBuilder sb = new StringBuilder(message.length * 2);
        for(byte b: message)
            sb.append(String.format("%02x", b));
        Logger.info("ReceivedMessage: \n" + sb.toString());

    }
}

