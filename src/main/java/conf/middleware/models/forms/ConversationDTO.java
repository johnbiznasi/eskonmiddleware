package conf.middleware.models.forms;

import org.jboss.resteasy.reactive.RestForm;

public class ConversationDTO {
    @RestForm
    private  int  senderid;
    @RestForm
    private  String  encryptedMessage;
}
