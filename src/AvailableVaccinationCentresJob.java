import org.codehaus.jackson.map.ObjectMapper;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AvailableVaccinationCentresJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusMonths(1);

        long numOfDays = ChronoUnit.DAYS.between(startDate, endDate);

        List<LocalDate> listOfDates1 = Stream.iterate(startDate, date -> date.plusDays(1))
                .limit(numOfDays)
                .collect(Collectors.toList());


        System.out.println(listOfDates1);//650, 651,

        TreeMap<String, List<String>> availableHospitals = new TreeMap<>();
        try {
            for (LocalDate localDate :
                    listOfDates1) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyy");
                DateTimeFormatter dateFormatMonth = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
                String strDate = formatter.format(localDate);
                URL url = new URL("https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByDistrict?district_id=650&date=" + strDate);
                System.out.println(url);
                List<String> availableHospitalsPErDay = new ArrayList<>();

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();


                connection.setRequestProperty("accept", "application/json");


                InputStream responseStream = connection.getInputStream();

                ObjectMapper mapper = new ObjectMapper();

                ObjectMapper objectMapper = new ObjectMapper();

                    Sessions sessions = objectMapper.readValue(responseStream, Sessions.class);
                    if (sessions.getSessions() != null) {
                        for (Session session : sessions.getSessions()) {
                            if (session.getMin_age_limit() >= 18 && session.getMin_age_limit() < 45 && session.getAvailable_capacity() > 0) {
                                availableHospitalsPErDay.add("Fee Type : "+session.getFee_type()+"; name: " + session.getName() + "; district: " + session.getDistrict_name() + "; state: " + session.getState_name());
                            }

                        }
                    }
                    if (!availableHospitalsPErDay.isEmpty())
                        availableHospitals.put(dateFormatMonth.format(localDate), availableHospitalsPErDay);

            }

            StringBuilder stringBuilder= new StringBuilder("");

            for (String key : availableHospitals.keySet()) {
                stringBuilder.append("<p>");
                stringBuilder.append(key);
                stringBuilder.append("  |  ");
                List<String> hospitals=availableHospitals.get(key);
                for (String hospital:hospitals) {
                    stringBuilder.append("<p>");
                    stringBuilder.append(hospital);
                    stringBuilder.append("<br />");
                    stringBuilder.append("</p>");

                }
                stringBuilder.append("<br />");
                stringBuilder.append("</p>");
                System.out.println(key+" "+availableHospitals.get(key));
            }
            if(!availableHospitals.keySet().isEmpty())
            SendEmail.sendEmail(stringBuilder.toString());

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }
}
