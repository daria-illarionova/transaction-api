/* Creating the Notification abstraction. 

Using an interface because we don't want our business layer to know: "I am using Amazon SNS."

It should know: "I need to send a notification."

*/

package com.daria.transactionapi.service;

public interface NotificationService {

    void sendNotification(String subject, String message);
}
