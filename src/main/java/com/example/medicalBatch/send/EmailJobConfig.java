package com.example.medicalBatch.send;


import com.example.medicalmanagement.model.User;
import com.example.medicalmanagement.model.UserRole;
import com.example.medicalmanagement.repository.AppointmentRepository;
import com.example.medicalmanagement.repository.UserRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(basePackages = "com.example.medicalmanagement.repository")
@ComponentScan(basePackages = "com.example.medicalmanagement")
public class EmailJobConfig {
    @Autowired
   private final UserRepository userRepository;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private final AppointmentRepository appointmentRepository;

    public EmailJobConfig(UserRepository userRepository, AppointmentRepository appointmentRepository) {
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        return mailSender;
    }



    @Bean
    public ItemReader<User> reader() {
        Sort sort = Sort.by(Sort.Direction.ASC, "fullName");
        return (ItemReader<User>) userRepository.findByRolesUserRole(UserRole.DOCTOR, sort);
    }

    @Bean
    public ItemProcessor<User, SimpleMailMessage> processor() {
        return user -> {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(user.getEmail());
            message.setSubject("Test Email");
            message.setText("This is a test email sent using Spring Batch and Spring Email.");
            return message;
        };
    }

    @Bean
    public ItemWriter<SimpleMailMessage> writer() {
        return items -> {
            for (SimpleMailMessage message : items) {
                mailSender.send(message);
                System.out.println("Sent email to: " + message.getTo()[0]);
            }
        };
    }

    @Bean
    public Step step1(JobRepository jobRepository,PlatformTransactionManager platformTransactionManager) {
        return new
                StepBuilder("send-email",jobRepository)
                .<User, SimpleMailMessage>chunk(10,platformTransactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public Job sendEmailJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("sendingEmail",jobRepository)
                .flow(step1(jobRepository,transactionManager)).end().build();

    }

}