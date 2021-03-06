package io.github.gameengine.proj;


import io.github.API.ISubscribeCallback;
import io.github.API.MessagingAPI;
import io.github.API.messagedata.MsgResultAPI;
import io.github.API.messagedata.MsgStatus;
import io.github.API.utils.GsonWrapper;
import io.github.coreutils.proj.messages.Channels;
import io.github.coreutils.proj.messages.LoginData;
import io.github.coreutils.proj.messages.LoginResponseData;
import io.github.coreutils.proj.messages.OnlineState;

/**
 * Callback class for the authorization service. This class will get the message from the API and make corresponding
 * method calls with the database through the server
 * @author Utsav Parajuli
 */
public class AuthorizationCallback implements ISubscribeCallback{

    @Override
    public void status(MessagingAPI mApi, MsgStatus status) {

    }


    /**
     * In this method we will get the what channel the API wants to go to. After we get the message sent by the client
     * API and make a LoginData variable that will contain the full scale data of the message sent by client. We
     * check if the message was sent to the authorization channel or the login validation and check with the database
     * through the {@link DBManager}. We then send back the appropriate message back to the client.
     * @param mApi : messaging API
     * @param message: result
     * @author Utsav Parajuli
     */
    @Override
    public void resolved(MessagingAPI mApi, MsgResultAPI message) {
        if (message.getChannel().equals(Channels.AUTHOR_VALIDATE.toString()) ||
                message.getChannel().equals(Channels.AUTHOR_CREATE.toString())) {
            LoginData data = GsonWrapper.fromJson(message.getMessage(), LoginData.class);

            try {
                //Checking if the message was sent to the validate channel
                if (message.getChannel().equals(Channels.AUTHOR_VALIDATE.toString())) {
                    if (DBManager.getInstance().verifyLogin(data)) {        //checking if login is correct
                        //if the login was successful checking if the account is deleted or not
                        if (DBManager.getInstance().getIsDeleted(data)) {
                            mApi.publish()
                                    .message(new LoginResponseData(data, true, "Validate", "true"))
                                    .channel(Channels.PRIVATE + message.getPublisherUuid())
                                    .execute();
                        } else {                                            //account not deleted case
                            mApi.publish()
                                    .message(new LoginResponseData(data, true, "Validate", "false"))
                                    .channel(Channels.PRIVATE + message.getPublisherUuid())
                                    .execute();
                            // updates online list in lobby
                            mApi.publish()
                                    .message(new OnlineState(data.getUsername(), true))
                                    .channel(Channels.ONLINE_STATE.toString())
                                    .execute();
                        }
                    } else {                                                //login unsuccessful
                        mApi.publish()
                                .message(new LoginResponseData(data, false, "Validate", "false"))
                                .channel(Channels.PRIVATE + message.getPublisherUuid())
                                .execute();
                    }
                } else if (message.getChannel().equals(Channels.AUTHOR_CREATE.toString())) {    //Checking if create account
                    if (DBManager.getInstance().createAccount(data)) {      //account created successfully
                            mApi.publish()
                                    .message(new LoginResponseData(data, true, "Create", "false"))
                                    .channel(Channels.PRIVATE + message.getPublisherUuid())
                                    .execute();
                            // updates online list in lobby
                            mApi.publish()
                                .message(new OnlineState(data.getUsername(), false))
                                .channel(Channels.ONLINE_STATE.toString())
                                .execute();
                        } else {                                            //account already exists
                            mApi.publish()
                                    .message(new LoginResponseData(data, false, "Create", "false"))
                                    .channel(Channels.PRIVATE + message.getPublisherUuid())
                                    .execute();
                        }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void rejected(Exception e) {

    }
}
