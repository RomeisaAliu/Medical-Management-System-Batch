package com.example.medicalBatch.config;

import com.example.medicalBatch.listener.CustomJobExecutionListener;
import com.example.medicalBatch.listener.CustomStepExecutionListener;
import com.example.medicalBatch.listener.ItemReaderListener;
import com.example.medicalmanagement.model.User;
import com.example.medicalmanagement.repository.UserRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(basePackages = "com.example.medicalmanagement.repository")
@ComponentScan(basePackages = "com.example.medicalmanagement")
public class UsersBatchConfig {
   private final UserRepository userRepository;
   private final CustomJobExecutionListener customJobExecutionListener;
   private final CustomStepExecutionListener customStepExecutionListener;
   private final ItemReaderListener itemReaderListener;
   @Autowired
    public UsersBatchConfig(UserRepository userRepository, CustomJobExecutionListener customJobExecutionListener, CustomStepExecutionListener customStepExecutionListener, ItemReaderListener itemReaderListener) {
        this.userRepository = userRepository;
        this.customJobExecutionListener = customJobExecutionListener;
        this.customStepExecutionListener = customStepExecutionListener;
        this.itemReaderListener = itemReaderListener;
   }

    @Value("${file.path}")
    private String filePath;


    @Bean
    public FlatFileItemReader<User> reader() {
        FlatFileItemReader<User> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource(filePath));
        reader.setName("Reader");
        reader.setLinesToSkip(1);
        reader.setLineMapper(lineMapper());
        return reader;
    }

    private LineMapper<User> lineMapper() {
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
    public UserProcessor processor() {
        return new UserProcessor();
    }

    @Bean
    public RepositoryItemWriter<User> writer() {
        RepositoryItemWriter<User> writer = new RepositoryItemWriter<>();
        writer.setRepository(userRepository);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("csv-step2",jobRepository).
                <User,User>chunk(10,transactionManager)
                .reader(reader()).listener(itemReaderListener)
                .processor(new
                        UserProcessor())
                .writer(writer())
                .listener(customStepExecutionListener)
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Job runJob(JobRepository jobRepository,PlatformTransactionManager transactionManager) {
        return new JobBuilder("importUsers",jobRepository).listener(customJobExecutionListener)
                .flow(step1(jobRepository,transactionManager)).end().build();

    }


    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        asyncTaskExecutor.setConcurrencyLimit(10);
        return asyncTaskExecutor;
    }

}