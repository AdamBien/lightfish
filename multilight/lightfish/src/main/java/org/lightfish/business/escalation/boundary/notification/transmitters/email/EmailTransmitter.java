package org.lightfish.business.escalation.boundary.notification.transmitters.email;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.lightfish.business.escalation.boundary.notification.transmitter.Transmitter;
import org.lightfish.business.escalation.boundary.notification.transmitter.TransmitterType;
import org.lightfish.business.escalation.entity.Escalation;

/**
 *
 * @author rveldpau
 */
@TransmitterType("email")
public class EmailTransmitter implements Transmitter<EmailTransmitterConfiguration> {

    @Inject
    Logger LOG;

    @Override
    public String getId() {
        return "email";
    }

    @Override
    public String getName() {
        return "E-Mail";
    }

    @Override
    public boolean isSystem() {
        return false;
    }

    @Override
    public void send(EmailTransmitterConfiguration configuration, Escalation escalation) {
        Properties props = createProperties(configuration);

        Authenticator authenticator = createAuthenticator(configuration);
        Session session = Session.getInstance(props, authenticator);

        MimeMessage message = new MimeMessage(session);
        try {
            message.setRecipients(Message.RecipientType.TO, getToAddresses(configuration));
            message.setFrom(new InternetAddress(configuration.getFrom()));
            message.setSubject("Escalation Notification: " + escalation.getChannel());
            message.setSentDate(new Date());

            Multipart multipart = new MimeMultipart("alternative");

            MimeBodyPart textPart = new MimeBodyPart();
            String textContent = escalation.getBasicMessage();
            textPart.setText(textContent);

            MimeBodyPart htmlPart = new MimeBodyPart();
            String htmlContent = applyTemplate(escalation);
            htmlPart.setContent(htmlContent, "text/html");

            multipart.addBodyPart(textPart);
            multipart.addBodyPart(htmlPart);
            message.setContent(multipart);

            Transport.send(message);

        } catch (MessagingException ex) {
            LOG.log(Level.SEVERE, "Failed to send e-mail because " + ex.toString(), ex);
        }


    }

    private Properties createProperties(final EmailTransmitterConfiguration config) {
        Properties props = new Properties();

        props.put("mail.smtp.host", config.getHost());
        props.put("mail.smtp.port", config.getPort());
        props.put("mail.smtp.auth", config.isAuthorizationRequired());

        switch (config.getProtocol()) {
            case "SMTPS":
                props.put("mail.smtp.ssl.enable", true);
                break;
            case "TLS":
                props.put("mail.smtp.starttls.enable", true);
                break;
        }

        return props;
    }

    private Authenticator createAuthenticator(final EmailTransmitterConfiguration config) {
        Authenticator authenticator = null;
        if (config.isAuthorizationRequired()) {

            authenticator = new Authenticator() {
                private PasswordAuthentication pa = new PasswordAuthentication(config.getUserName(), config.getPassword());

                @Override
                public PasswordAuthentication getPasswordAuthentication() {
                    return pa;
                }
            };
        }
        return authenticator;
    }

    private InternetAddress[] getToAddresses(EmailTransmitterConfiguration config) {
        String[] splitEmails = config.getTo().split("[;,]");
        List<InternetAddress> addresses = new ArrayList<>(splitEmails.length);
        for (String email : splitEmails) {
            if (email.trim().isEmpty()) {
                continue;
            }
            try {
                addresses.add(new InternetAddress(email));
            } catch (AddressException ex) {
                LOG.log(Level.WARNING, email + " does not appear to be a valid e-mail address.", ex);
            }
        }
        InternetAddress[] addressArray = new InternetAddress[addresses.size()];
        return addresses.toArray(addressArray);
    }
    private static String emailTemplate = null;

    private String applyTemplate(Escalation escalation) {
        return getEmailTemplate().replaceAll("MESSAGE", escalation.getRichMessage());
    }

    private String getEmailTemplate() {
        if (emailTemplate == null) {
            InputStream templateStream = EmailTransmitter.class.getResourceAsStream("mailTemplate.html");
            try {
                emailTemplate = new Scanner(templateStream).useDelimiter("\\A").next();
            } catch (java.util.NoSuchElementException e) {
                emailTemplate = "";
            }
        }
        return emailTemplate;
    }
}
