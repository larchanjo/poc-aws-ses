package com.example.aws.ses;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import java.util.Objects;

public class Application {

  static final String SUBJECT = "Amazon SES test (AWS SDK for Java)";

  static final String HTMLBODY = "<h1>Amazon SES test (AWS SDK for Java)</h1>"
      + "<p>This email was sent with <a href='https://aws.amazon.com/ses/'>"
      + "Amazon SES</a> using the <a href='https://aws.amazon.com/sdk-for-java/'>"
      + "AWS SDK for Java</a>";

  static final String TEXTBODY = "This email was sent through Amazon SES "
      + "using the AWS SDK for Java.";

  public static void main(String[] args) {
    String from = System.getenv("FROM_EMAIL");
    if (Objects.isNull(from)) {
      throw new RuntimeException("Missing FROM_EMAIL environment variable");
    }

    String to = System.getenv("TO_EMAIL");
    if (Objects.isNull(to)) {
      throw new RuntimeException("Missing TO_EMAIL environment variable");
    }

    try {
      AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder
          .standard()
          .withRegion(Regions.US_WEST_2)
          .withCredentials(new EnvironmentVariableCredentialsProvider())
          .build();

      Destination destination = new Destination()
          .withToAddresses(to);

      Body body = new Body()
          .withHtml(new Content().withCharset("UTF-8").withData(HTMLBODY))
          .withText(new Content().withCharset("UTF-8").withData(TEXTBODY));

      Message message = new Message()
          .withBody(body)
          .withSubject(new Content().withCharset("UTF-8").withData(SUBJECT));

      SendEmailRequest request = new SendEmailRequest()
          .withDestination(destination)
          .withMessage(message)
          .withSource(from);

      SendEmailResult response = client.sendEmail(request);
      System.out.println(String.format("Email=[%s] sent from=[%s] to=[%s]", response, from, to));
    } catch (Exception ex) {
      System.out.println("The email was not sent. Error message: " + ex.getMessage());
    }
  }

}