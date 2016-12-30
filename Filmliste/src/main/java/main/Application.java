package main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	/*
	 * https://aws.amazon.com/de/blogs/devops/deploying-a-spring-boot-application-on-aws-using-aws-elastic-beanstalk/
	 * https://spring.io/guides/gs/uploading-files/
	 * https://www.techcoil.com/blog/how-to-upload-a-file-via-a-http-multipart-request-in-java-without-using-any-external-libraries/
	 * http://stackoverflow.com/questions/2469451/upload-files-from-java-client-to-a-http-server
	 */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
