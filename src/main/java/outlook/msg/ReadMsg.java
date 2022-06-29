package outlook.msg;

import com.auxilii.msgparser.Message;
import com.auxilii.msgparser.MsgParser;
import com.auxilii.msgparser.attachment.Attachment;

import java.util.Iterator;
import java.util.List;


public class ReadMsg {
    public static void main(String[] args) {
        try {
            MsgParser msgp = new MsgParser();
            Message msg = msgp.parseMsg("C:\\Users\\mgmet\\Desktop\\MICROPROCESSORS.msg");
            String from_email = msg.getFromEmail();
            String from_name = msg.getFromName();
            String subject = msg.getSubject();
            String body = msg.getBodyText();
            String to_list = msg.getDisplayTo();
            String cc_list = msg.getDisplayCc();
            String bcc_list = msg.getDisplayBcc();


            String[] splitWords = body.split("\\s+");

            for (String a : splitWords) {
                System.out.println(a);
            }


            List list = msg.getAttachments();
            System.out.println("Attachments -" + list.size());
            Iterator it_list = list.iterator();
            Attachment attachemetn = null;
            while (it_list.hasNext()) {
                attachemetn = (Attachment) it_list.next();
                System.out.println(attachemetn);
            }

            System.out.println("-----");
            System.out.println("from_email " + from_email);
            System.out.println("from_name " + from_name);
            System.out.println("to_list " + to_list);
            System.out.println("cc_list " + cc_list);
            System.out.println("bcc_list " + bcc_list);
            System.out.println("subject " + subject);
            System.out.println("body " + body);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}