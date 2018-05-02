package logic;

import JsonMessage.JSONException;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import HttpRequest.*;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.concurrent.TimeoutException;


//class for sending a certain message through RabbitMQ

public class Sender {

    // 1=createUuidRecord
    // no UUID given so ask for a new one, message will contain the json string containing the UUID
    public static String createUuidRecord(String messageType, int Entity_sourceId, Helper.EntityType Entity_type, Helper.SourceType Source_type) throws IOException, TimeoutException, JAXBException {

        // NIEUW OBJECT ZONDER UUID (POST)

        String xmlTotalMessage = "";
        String message = "";

        //set xml header description for message here with timestamp
        String description = "Standard description for '"+messageType+"' set in sendMessage1()";

        String newUUID = "";

        message = Helper.httpPostCreateUuidRecord(Entity_sourceId, Entity_type, Source_type);

        //System.out.println("Post returned msg:\n" + message);

        //cut away the outer [ and ] from the retrieved json string
        message = message.substring(1, message.length() - 1);

        System.out.println("\nMessage From UUID server: " + message);

        // try to parse retrieved object to our own UUID_insertUuidRecord class
        // (since this actually the same as our createUuidRecord response)
        UUID_insertUuidRecord obj = null;
        try {
            Gson gson = new Gson();
            UUID_insertUuidRecord firstTest = gson.fromJson(message, UUID_insertUuidRecord.class);

            newUUID = firstTest.getUUID();

        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println(e);
            System.out.println(newUUID);
        }

        return newUUID;
        //Process xml

        //System.out.println("Generated XML: " + xmlTotalMessage);

        //Send message

        //return sendMessage(xmlTotalMessage);

    }


    // 2=insertUuidRecord
    // for registering an object to the UUID manager to let it know your local id of it
    public static String insertUuidRecord(String messageType, int Entity_sourceId, Helper.EntityType Entity_type, Helper.SourceType Source_type, String UUID) throws IOException, TimeoutException, JAXBException {

        // NIEUW OBJECT MET UUID (POST)

        String xmlTotalMessage = "";
        String message = "";
        String description = "Standard description set in sendMessage2()";

        //String newUUID = "";

        message = Helper.httpPostInsertUuidRecord(UUID, Entity_sourceId, Entity_type, Source_type);

        if (message == "") {

            return "Entity with UUID: " + UUID + " should be updated!";

        } else {

            return "Something could have gone wrong updating entity with UUID: " + UUID + "...\nError: " + message;
        }
        /*
        This message is only to let UUID know you got the object locally, no need to send this to the exchange...

        // process UUID from message

        //Process xml

        xmlTotalMessage = logic.Helper.getOurXml(messageType, description, Source_type, userUUID, sessionUUID);
        System.out.println("Generated XML: " + xmlTotalMessage);

        //Send message

        return sendMessage(xmlTotalMessage);
        */
    }


    // 3=updateUuidRecord
    // for letting the UUID manager know you changed something in your own database so this Source_type should be yours
    public static int updateUuidRecordVersion(String messageType, Helper.SourceType Source_type, String UUID) throws IOException, TimeoutException, JAXBException {

        // BESTAAND OBJECT AANPASSEN (PUT)

        String message = "";
        int newEntity_version = 1;

        message = Helper.httpPutUpdateUuidRecordVersion(UUID, Source_type);


        // process UUID from message
        //cut away the outer [ and ] from the retrieved json string
        message = message.substring(1, message.length() - 1);

        System.out.println("\nMessage From UUID server: " + message + " .\n");

        UUID_updateUuidRecordVersionResponse obj = null;
        try {
            Gson gson = new Gson();
            obj = gson.fromJson(message, UUID_updateUuidRecordVersionResponse.class);

            newEntity_version = obj.getEntity_version();

            //System.out.println("newEntity_versionFROMJSON: "+newEntity_version);
        } catch (Exception e) {
            System.out.println(obj.getEntity_version());
            System.out.println(e);

        }

        return newEntity_version;


    }


    //4=updateUuidRecordOnChanged
    // for changing the Entity_version of a certain record // doesn't seem to work yet
    public static String updateUuidRecordVersionB(String messageType, Helper.SourceType Source_type, String UUID, int Entity_version) throws IOException, TimeoutException, JAXBException {


        String xmlTotalMessage = "";
        String message = "";

        String description = "Standard description set in sendMessage4()";

        message = Helper.httpPutUpdateUuidRecordVersionB(UUID, Entity_version, Source_type);

        if (message == "") {
            //Process xml

            xmlTotalMessage = Helper.getOurXmlMessage(messageType, description, Source_type, UUID);
            //System.out.println("Generated XML: " + xmlTotalMessage);

            //Send message

            return sendMessage(xmlTotalMessage);

            //return "Entity with UUID: "+UUID+" should be updated!";

        } else {

            return "Something could have gone wrong updating entity with UUID: " + UUID + "...\n -*- Error starts here: -*-\n" + message + "\n -*- Error ends here -*-";
        }

    }

    //4=updateUuidRecordOnChanged
    // for changing the Entity_version of a certain record // doesn't seem to work yet
    public static String sendReservationMessage(String messageType, Helper.SourceType Source_type, String userUUID, String sessionUUID) throws IOException, TimeoutException, JAXBException {


        String xmlTotalMessage = "";
        String message = "";

        String description = "Standard Reservation description set in sendReservationMessage()";

        //message = Helper.httpPutUpdateUuidRecordVersionB(UUID, Entity_version, Source_type);

        //Process xml

        xmlTotalMessage = Helper.getXmlForReservation(messageType, description, Source_type, userUUID, sessionUUID);
        //System.out.println("Generated XML in send reservation message: " + xmlTotalMessage);

        //Send message

        return sendMessage(xmlTotalMessage);


    }


    //all messages come here to effectively send their message to our RabbitMQ
    public static String sendMessage(String xmlMessage) throws IOException, TimeoutException, JAXBException {

        //setup RabbitMQ connection, publish message to queue/exchange

        ConnectionFactory factory = new ConnectionFactory();

        String username = "Frontend";
        String password = "frontend";
        String virtualHost = "/";
        //String hostName="0:0:0:0:0:ffff:a03:3226";

        factory.setUsername(username);
        factory.setPassword(password);
        factory.setVirtualHost(virtualHost);
        factory.setHost(Helper.HOST_NAME_LINK);
        factory.setPort(Helper.PORT_NUMBER);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //publish to exchange
        try {
            channel.basicPublish(Helper.EXCHANGE_NAME, "", null, xmlMessage.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println(" [.x.] Sending to exchange:   '" + Helper.EXCHANGE_NAME + "'@ '" + Helper.getCurrentDateTimeStamp() + "'");

        //publish to channel
/*

        try {
            channel.basicPublish("", Helper.TASK_QUEUE_NAME, null, xmlMessage.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
*/

        channel.close();
        connection.close();

        return " [.x.] Sending to exchange:   '" + Helper.EXCHANGE_NAME + "' @ '" + Helper.getCurrentDateTimeStamp() + "' message with '" + xmlMessage.length()+"' characters.";

        //return " [x] Sending to queue:   '" + Helper.TASK_QUEUE_NAME + "' message with length: '" + xmlMessage.length() + "'";

        //channel.exchangeDeclare(EXCHANGE_NAME, "fanout"); // other options: direct, topic, headers and fanout
        //channel.queueDeclare(TASK_QUEUE_NAME, false, false, false, null);
        //channel.queueBind(TASK_QUEUE_NAME, EXCHANGE_NAME, "");

    }

}
/*
//old

    private static String joinStrings(String[] strings, String delimiter) {
        int length = strings.length;

        if (length == 0) return "";

        StringBuilder words = new StringBuilder(strings[0]);

        for (int i = 1; i < length; i++) {
            words.append(delimiter).append(strings[i]);
        }
        return words.toString();
    }
 */