import org.codehaus.jackson.map.ObjectMapper;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VaccinationAvailability {
    public static void main(String[] args) throws Exception {

        JobDetail job1 = JobBuilder.newJob(AvailableVaccinationCentresJob.class)
                .withIdentity("AvailableVaccinationCentresJob", "group1").build();

        Trigger trigger1 = TriggerBuilder.newTrigger()
                .withIdentity("cronTrigger1", "group1")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0/15 * * * ?"))
                .build();

        Scheduler scheduler1 = new StdSchedulerFactory().getScheduler();
        scheduler1.start();
        scheduler1.scheduleJob(job1, trigger1);


    }
}
