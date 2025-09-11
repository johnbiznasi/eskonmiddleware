package conf.middleware.services;

import conf.middleware.models.EmailNotification;
import io.quarkus.logging.Log;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import io.quarkus.mailer.reactive.ReactiveMailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class EmailService {

    @Inject
    ReactiveMailer reactiveMailer;

    @Incoming("signup-emails")
    public void signup(EmailNotification email) {
        // process your price.
        Log.info("The signup kafka topic has been called");
        reactiveMailer.send(
                Mail.withText(email.getReceiver(),
                        email.getSubject(),
                        email.getMessage()
                )
        ).subscribe().with(
                success -> System.out.println("Mail sent"),
                Throwable::printStackTrace
        );
    }

    @Incoming("password-emails")

    public void passwordReset(EmailNotification email) {
        // process your price.
        reactiveMailer.send(
                Mail.withText(email.getReceiver(),
                        email.getSubject(),
                        email.getMessage()
                )
        ).subscribe().with(
                success -> System.out.println("Mail sent"),
                Throwable::printStackTrace
        );;
    }

}
