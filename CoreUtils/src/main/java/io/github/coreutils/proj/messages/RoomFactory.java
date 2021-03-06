package io.github.coreutils.proj.messages;

public final class RoomFactory {

    private RoomFactory() {
        // don't delete
    }

    public static RoomData makeDisconnectRoom(PlayerData playerData) {
        RoomData data = new RoomData();
        data.addPlayer(playerData);
        data.setRequestType(RoomData.RequestType.DISCONNECT);
        return data;
    }

    public static RoomData makeDeniedRoom() {
        RoomData data = new RoomData();
        data.setRequestType(RoomData.RequestType.DENIED);
        return data;
    }

    public static RoomData makeCreateRoom(String roomTitle, PlayerData player) {
        RoomData data = new RoomData();
        data.setTitle(roomTitle);
        data.addPlayer(player);
        data.setRequestType(RoomData.RequestType.NORMAL);
        return data;
    }
}
