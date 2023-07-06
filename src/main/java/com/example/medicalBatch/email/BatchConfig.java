package com.example.medicalBatch.email;

import com.example.medicalBatch.item.AppointmentDoctorProcessor;
import com.example.medicalBatch.item.EmailItemWriter;
import com.example.medicalmanagement.model.User;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
@EnableJpaRepositories(basePackages = "com.example.medicalmanagement.repository")
@ComponentScan(basePackages = "com.example.medicalmanagement")
public class BatchConfig {



    private final JavaMailSender mailSender;

    private final DataSource dataSource;


    @Autowired
    public BatchConfig( JavaMailSender mailSender, DataSource dataSource){
        this.mailSender = mailSender;
        this.dataSource = dataSource;

    }

    @Bean
    public Step emailStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("emailStep",jobRepository)
                .<User, MimeMessagePreparator>chunk(10,transactionManager)
                 .processor(new AppointmentDoctorProcessor())
                .build();
    }

    @Bean
    public Job emailJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("emailJob")
                .start(emailStep( jobRepository,  transactionManager))
                .build();
    }

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("romeisaaliu1@gmail.com");
        mailSender.setPassword("mbquecrtbcitgpxc");
        return mailSender;
    }
    @Bean
    public ItemReader<User> doctorReader() {
        JpaPagingItemReader<User> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(entityManagerFactory().getObject());
        reader.setQueryString("SELECT u FROM User u");
        return reader;
    }

    private LineMapper<User> lineMapper() {
        return getUserLineMapper();

    }

    public static LineMapper<User> getUserLineMapper() {
        DefaultLineMapper<User> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("id", "fullName", "phoneNumber");

        BeanWrapperFieldSetMapper<User> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(User.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }

    @Bean
    public ItemProcessor<User, MimeMessagePreparator> doctorProcessor() {
        return new AppointmentDoctorProcessor();
    }

    @Bean
    public ItemWriter<MimeMessagePreparator> emailWriter() {
        return new EmailItemWriter(mailSender);
    }


    @Bean
    public Job appointmentNotificationJob(Step appointmentNotificationStep) {
        return new JobBuilder("appointmentNotificationJob")
                .incrementer(new RunIdIncrementer())
                .start(appointmentNotificationStep)
                .build();
    }
    @Bean
    public Step appointmentNotificationStep(ItemReader<User> doctorReader,
                                            ItemProcessor<User, MimeMessagePreparator> doctorProcessor,
                                            ItemWriter<MimeMessagePreparator> emailWriter) {
        return new StepBuilder("appointmentNotificationStep")
                .<User, MimeMessagePreparator>chunk(10)
                .reader(doctorReader)
                .processor(doctorProcessor)
                .writer(emailWriter)
                .build();
    }
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.example.medicalmanagement.model");
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        return em;
    }

    @Bean
    public Step step(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        return new StepBuilder("step1",jobRepository).<User, MimeMessagePreparator>chunk(10,transactionManager)
                .reader(doctorReader())
                .writer(emailWriter()).taskExecutor(taskExecutor()).build();
    }

    @Bean
    public Job runJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("sendingEmail",jobRepository)
                .flow(step(jobRepository,transactionManager)).end().build();

    }


    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        asyncTaskExecutor.setConcurrencyLimit(10);
        return asyncTaskExecutor;
    }

}
